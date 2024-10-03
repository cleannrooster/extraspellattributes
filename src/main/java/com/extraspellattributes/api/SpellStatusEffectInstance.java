package com.extraspellattributes.api;

import com.mojang.logging.LogUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.api.spell.Spell;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;

public class SpellStatusEffectInstance extends StatusEffectInstance {
    private float spellPower = 0;
    private LivingEntity owner = null;
    private Spell spell;
    private SpellStatusEffect  spellEffect;
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final int INFINITE = -1;
    private int duration;
    private int amplifier;
    private boolean ambient;
    private boolean showParticles;
    private boolean showIcon;
    @Nullable
    private StatusEffectInstance hiddenEffect;
    public SpellStatusEffectInstance(RegistryEntry<StatusEffect> type, Spell spell, float spellPower, LivingEntity owner, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, @Nullable StatusEffectInstance hiddenEffect) {
        super(type,duration,amplifier,ambient,showParticles,showIcon,hiddenEffect);
        this.spellEffect = (SpellStatusEffect)type.value();
        this.spell = spell;
        this.spellPower = spellPower;
        this.owner = owner;
    }

    public float getSpellPower() {
        return spellPower;
    }
    @Nullable
    public void setOwner(LivingEntity owner) {
        this.owner =owner;
    }
    @Nullable

    public Spell getSpell() {
        return spell;
    }

    public LivingEntity getOwner() {
        return owner;
    }

    public SpellStatusEffect getSpellEffect() {
        return spellEffect;
    }

    private boolean isActive() {
        return this.getSpellEffect().canApplySpellEffect(this.getDuration(),this.getAmplifier());
    }

    @Override
    public boolean update(LivingEntity entity,Runnable overwriteCallback) {
        if (this.isActive()) {
            this.spellEffect.applySpellEffect(entity, this.owner, this.amplifier, this.spellPower,this.spell);
        }

        return super.update(entity,overwriteCallback);
    }
}
