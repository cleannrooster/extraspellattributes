package com.cleannrooster.extraspellattributes.mixin;

import com.cleannrooster.extraspellattributes.Calculations;
import com.cleannrooster.extraspellattributes.api.RecoupInstances;
import com.cleannrooster.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayDeque;
import java.util.Deque;

/** Measures physical mitigation from damage entry to the effective-health loss at return. */
@Mixin(LivingEntity.class)
public abstract class LivingEntityRecoupAbsorbMixin {
    @Shadow @Final
    private static TrackedData<Float> HEALTH;

    @Shadow
    protected float lastDamageTaken;

    @Unique
    private static final TagKey<net.minecraft.entity.damage.DamageType> ESA_MAGIC_DAMAGE =
            TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("c", "is_magic"));

    /* A stack is required because damage calls can nest through retaliation and other effects. */
    @Unique
    private final Deque<float[]> esa$recoupAbsorbSnapshots = new ArrayDeque<>();

    @Inject(method = "damage", at = @At("HEAD"))
    private void esa$captureDamageInput(DamageSource source, float amount,
                                        CallbackInfoReturnable<Boolean> cir) {
        LivingEntity living = (LivingEntity) (Object) this;
        float inputDamage = Math.max(0, amount);
        boolean shieldBlocked = inputDamage > 0 && living.blockedByShield(source);
        if (!shieldBlocked && living.timeUntilRegen > 10 && !source.isIn(DamageTypeTags.BYPASSES_COOLDOWN)) {
            inputDamage = Math.max(0, inputDamage - lastDamageTaken);
        }

        boolean absorbEligible = living instanceof PlayerEntity
                && !living.getWorld().isClient()
                && inputDamage > 0
                && !source.getTypeRegistryEntry().isIn(ESA_MAGIC_DAMAGE)
                && !living.isInvulnerableTo(source)
                && Calculations.recoup_reabsorb(living) > 1F;

        esa$recoupAbsorbSnapshots.push(new float[]{
                inputDamage,
                esa$health(living),
                living.getAbsorptionAmount(),
                absorbEligible ? 1F : 0F
        });
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void esa$recoupMitigatedPhysicalDamage(DamageSource source, float amount,
                                                    CallbackInfoReturnable<Boolean> cir) {
        if (esa$recoupAbsorbSnapshots.isEmpty()) {
            return;
        }

        float[] snapshot = esa$recoupAbsorbSnapshots.pop();
        LivingEntity living = (LivingEntity) (Object) this;
        if (!(living instanceof PlayerEntity player)
                || !(living instanceof RecoupLivingEntityInterface recoup)
                || living.getWorld().isClient()) {
            return;
        }

        float healthLoss = Math.max(0, snapshot[1] - esa$health(living));
        double lifeRecoupRate = Calculations.recoup(player) - 1F;
        if (healthLoss > 0 && lifeRecoupRate > 0) {
            recoup.addRecoupHealth(new RecoupInstances.RecoupInstanceHealth(
                    player, 80, healthLoss * lifeRecoupRate));
        }

        if (snapshot[3] == 0F) {
            return;
        }

        float absorptionLoss = Math.max(0, snapshot[2] - living.getAbsorptionAmount());
        float actualLoss = healthLoss + absorptionLoss;
        float effectiveHealth = snapshot[1] + snapshot[2];
        float damageThatCouldBeLost = Math.min(snapshot[0], effectiveHealth);
        float mitigated = Math.max(0, damageThatCouldBeLost - actualLoss);
        double absorbRecoupRate = Calculations.recoup_reabsorb(player) - 1F;

        if (mitigated > 0 && absorbRecoupRate > 0) {
            recoup.addRecoupAbsorption(new RecoupInstances.RecoupInstanceAbsorption(
                    player, 80, mitigated * absorbRecoupRate));
        }
    }

    @Unique
    private float esa$health(LivingEntity living) {
        return Math.max(0, living.getDataTracker().get(HEALTH));
    }
}
