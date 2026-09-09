package com.cleannrooster.extraspellattributes.api;

import com.cleannrooster.extraspellattributes.ExampleMod;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * Registry tags for status (mob) effects used by this mod.
 */
public final class EsaEffectTags {
    /**
     * Effects that count as "concealment" for Sneak Attack. Other mods/datapacks can extend this tag;
     * the Sneak Attack handler must never hardcode individual effects.
     */
    public static final TagKey<StatusEffect> CONCEALMENT_EFFECTS =
            TagKey.of(RegistryKeys.STATUS_EFFECT, Identifier.of(ExampleMod.MOD_ID, "concealment_effects"));

    private EsaEffectTags() {
    }
}
