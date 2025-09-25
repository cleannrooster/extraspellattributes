package com.cleannrooster.extraspellattributes;

import com.cleannrooster.extraspellattributes.config.ServerConfig;
import com.cleannrooster.extraspellattributes.effects.Dissolution;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellPowerMechanics;

import static com.cleannrooster.extraspellattributes.ExampleMod.MOD_ID;

public class Effects {
    public static ServerConfig config;

    public static  RegistryEntry.Reference<StatusEffect> DISSOLUTIONEFFECT;
    public static  RegistryEntry.Reference<StatusEffect> INEVITABILITYEFFECT;
    public static void initEffects(){
        DISSOLUTIONEFFECT = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(MOD_ID,"dissolution"),new Dissolution(StatusEffectCategory.HARMFUL, 0xffff00)
                .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH,Identifier.of(MOD_ID,"dissolution"),-1, EntityAttributeModifier.Operation.ADD_VALUE));
        INEVITABILITYEFFECT = Registry.registerReference(Registries.STATUS_EFFECT,Identifier.of(MOD_ID,"inevitability"),new Dissolution(StatusEffectCategory.BENEFICIAL, 0xffff00)
                .addAttributeModifier(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry,Identifier.of(MOD_ID,"inevitability"),0.1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    }
}
