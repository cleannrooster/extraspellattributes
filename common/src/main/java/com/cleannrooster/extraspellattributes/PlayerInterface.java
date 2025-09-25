package com.cleannrooster.extraspellattributes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

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
