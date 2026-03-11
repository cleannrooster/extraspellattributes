package com.extraspellattributes;

import com.extraspellattributes.api.SneakAttackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;

import static com.extraspellattributes.ReabsorptionInit.MOD_ID;

public class CustomImpacts {
    private static SpellHandlers.CustomImpact impact = new SpellHandlers.CustomImpact() {
        @Override
        public SpellHandlers.ImpactResult onSpellImpact(RegistryEntry<Spell> registryEntry, SpellPower.Result result, LivingEntity livingEntity, @Nullable Entity entity, SpellHelper.ImpactContext impactContext) {
            if(!(entity instanceof SneakAttackable attackable)) return new SpellHandlers.ImpactResult(false,false);
            boolean bool = attackable.processSneakAttack(livingEntity, result.school());
            SpellHandlers.ImpactResult impactResult =  new SpellHandlers.ImpactResult(bool,bool) ;
            return impactResult ;
        }
    };
    public static void register(){
        SpellHandlers.registerCustomImpact(Identifier.of(MOD_ID,"try_sneak"),impact);
    }
}
