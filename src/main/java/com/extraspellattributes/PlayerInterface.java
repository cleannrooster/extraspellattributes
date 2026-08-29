package com.extraspellattributes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.spell.Spell;

import java.util.List;

public interface PlayerInterface {

    int getReabLasthurt();
    float getReabDamageAbsorbed();
    void resetReabDamageAbsorbed();
    void ReababsorbDamage(float i);
    boolean getReabsorbing();
    void setReabsorbing(boolean bool);
    void setReabLasthurt(int lasthurt);


}
