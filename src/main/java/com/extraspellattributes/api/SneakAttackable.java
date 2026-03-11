package com.extraspellattributes.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.SpellSchool;

public interface SneakAttackable {
     boolean processSneakAttack(LivingEntity attacker, SpellSchool school);

}
