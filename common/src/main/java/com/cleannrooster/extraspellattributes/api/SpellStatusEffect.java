package com.cleannrooster.extraspellattributes.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.spell_engine.api.spell.Spell;
import org.jetbrains.annotations.Nullable;

public class SpellStatusEffect extends StatusEffect {
    private float spellPower = 0;
    private LivingEntity owner = null;
    private final Spell spell;
    public SpellStatusEffect(StatusEffectCategory category, int color, Spell spell) {
        super(category, color);
        this.spell = spell;
    }

    public float getSpellPower() {
        return spellPower;
    }

    @Nullable
    public LivingEntity getOwner() {
        return owner;
    }
    @Nullable
    public void setOwner(LivingEntity owner) {
        this.owner =owner;
    }
    @Nullable
    public Spell getSpell() {
        return spell;
    }
    public boolean canApplySpellEffect(int duration, int amplifier){
        return false;
    }
    public void applySpellEffect(LivingEntity entity, LivingEntity owner, int amplifier, float spellPower, Spell spell){

    }


}
