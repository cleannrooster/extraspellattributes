package com.cleannrooster.extraspellattributes;

import com.cleannrooster.extraspellattributes.api.EsaEffectTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.client.util.Color;
import net.spell_engine.compat.CriticalStrikeCompat;
import net.spell_engine.entity.DamageSourceExtension;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_engine.internals.SpellExecution;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * Custom Spell Engine impact for the Rogue "Sneak Attack" skill.
 *
 * Concealment guarantees a critical strike; an attack that ALSO rolls a natural critical
 * additionally ignores armor. The natural roll and the concealment guarantee are kept as
 * separate conditions so the armor-bypass decision can depend on the raw roll alone.
 * The handler owns the single damage instance (the spell uses DIRECT delivery, so no weapon
 * attack happens alongside it).
 */
public final class SneakAttackHandler {

    public static SpellHandlers.ImpactResult onImpact(RegistryEntry<Spell> spellEntry,
                                                      SpellPower.Result power,
                                                      LivingEntity caster,
                                                      @Nullable Entity target,
                                                      SpellExecution.ImpactContext context) {
        if (!(target instanceof LivingEntity living)) {
            return new SpellHandlers.ImpactResult(false, false);
        }

        var school = power.school();

        // Critical state — the natural roll and the concealment guarantee stay separate:
        var concealment = findConcealment(caster);
        boolean concealed = concealment != null;

        var vulnerability = SpellPower.getVulnerability(living, school);
        var rolled = power.random(vulnerability);
        boolean naturalCritical = rolled.isCritical();        // raw natural roll, never forced
        boolean finalCritical = naturalCritical || concealed; // concealment guarantees a crit
        boolean ignoreArmor = concealed && naturalCritical;   // "double crit" = crit damage + armor bypass

        // Crit multiplier is applied iff finalCritical, exactly once.
        double amount = rolled.amount();
        if (finalCritical && !naturalCritical) {
            // Concealment forced the crit although the natural roll failed: promote the
            // non-crit amount to a crit amount with a single multiplier (no double scaling).
            amount *= power.criticalDamage();
        }
        amount *= coefficientOf(spellEntry);
        amount *= context.total(spellEntry);
        if (context.isChanneled()) {
            amount *= SpellPower.getHaste(caster, school);
        }

        // Consume exactly one concealment effect, only now that we are committed to a valid target.
        // Consumed even if the damage below is later blocked/absorbed.
        if (concealed) {
            caster.removeStatusEffect(concealment.getEffectType());
        }

        // Exactly one physical-melee damage instance. Armor bypass only on the double-crit branch.
        caster.onAttacking(target);
        DamageSource damageSource = ignoreArmor
                ? armorBypassingSource(caster)
                : SpellDamageSource.create(school, caster);
        if (finalCritical) {
            CriticalStrikeCompat.setCriticalStrike(damageSource, (float) power.criticalDamage());
        }
        ((DamageSourceExtension) damageSource).setSpellIndirect(context.focusMode() != SpellTarget.FocusMode.DIRECT);
        living.damage(damageSource, (float) amount);

        // Mutually exclusive runtime feedback. This cannot live in the spell JSON because
        // the correct effect depends on concealment and the natural critical roll.
        if (caster.getWorld() instanceof ServerWorld serverWorld) {
            if (ignoreArmor) {
                playCritFeedback(serverWorld, living);
            } else if (concealed) {
                playStrongFeedback(serverWorld, living);
            } else {
                playWeakFeedback(serverWorld, living);
            }
        }

        return new SpellHandlers.ImpactResult(true, finalCritical);
    }

    private static void playWeakFeedback(ServerWorld world, LivingEntity target) {
        world.spawnParticles(ParticleTypes.SWEEP_ATTACK,
                target.getX(), target.getBodyY(0.5), target.getZ(), 2, 0.15, 0.2, 0.15, 0.0);
        world.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
                SoundCategory.PLAYERS, 0.65F, 1.0F);
    }

    private static void playStrongFeedback(ServerWorld world, LivingEntity target) {
        playConcealedCritFeedback(world, target, Color.RED);
    }

    private static void playCritFeedback(ServerWorld world, LivingEntity target) {
        playConcealedCritFeedback(world, target, Color.ELECTRIC);
    }

    private static void playConcealedCritFeedback(ServerWorld world, LivingEntity target, Color skullColor) {
        var blood = ParticleGroupBuilder.of(SpellEngineParticles.dripping_blood)
                .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(18).speed(0.12F, 0.35F));
        var skull = ParticleGroupBuilder.magic(SpellEngineParticles.magic_skull, ParticleGroup.Motion.BURST)
                .color(skullColor.toRGBA())
                .scale(1.15F)
                .batch(b -> b.shape(ParticleGroup.Shape.SPHERE).count(8).speed(0.1F, 0.3F));

        ParticleHelper.sendBatches(target, java.util.List.of(blood, skull));
        world.playSound(null, target.getBlockPos(), SpellEngineSounds.SIGNAL_SPELL_CRIT.soundEvent(),
                SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    @Nullable
    private static StatusEffectInstance findConcealment(LivingEntity caster) {
        return caster.getStatusEffects().stream()
                .filter(instance -> instance.getEffectType().isIn(EsaEffectTags.CONCEALMENT_EFFECTS))
                .min(Comparator.comparingInt(StatusEffectInstance::getDuration))
                .orElse(null);
    }

    private static float coefficientOf(RegistryEntry<Spell> spellEntry) {
        var impacts = spellEntry.value().impacts;
        if (!impacts.isEmpty()) {
            var action = impacts.get(0).action;
            if (action != null && action.damage != null) {
                return action.damage.spell_power_coefficient;
            }
        }
        return 1F;
    }

    private static DamageSource armorBypassingSource(LivingEntity caster) {
        Registry<DamageType> registry = ((DamageSourcesAccessor) caster.getDamageSources()).getRegistry();
        RegistryEntry<DamageType> type = registry.getEntry(ReabsorptionInit.SNEAK_ATTACK_DAMAGE).orElseThrow();
        return new DamageSource(type, caster);
    }

    private SneakAttackHandler() {
    }
}
