package com.cleannrooster.extraspellattributes;

import com.cleannrooster.extraspellattributes.api.Sign;
import net.minecraft.entity.LivingEntity;

import static com.cleannrooster.extraspellattributes.DynamicAttribute.*;

public class Calculations {
    public static double converttoFrost(LivingEntity player){
        return applyAttributeModifiers(1, Sign.POSITIVE.wrap(player.getAttributeInstance(CONVERTTOFROST)));
    }
    public static double converttoFire(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(CONVERTTOFIRE)));

    }
    public static double converttoArcane(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(CONVERTTOARCANE)));
    }
    public static double converttoHeal(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(CONVERTTOHEAL)));
    }
    public static double glancingBlow(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(GLANCINGBLOW)));
    }
    public static double spellSuppress(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(SPELLSUPPRESS)));
    }
    public static double defiance(LivingEntity player){
        return player.getAttributeValue(DEFIANCE);
    }
    public static double fortitude(LivingEntity player){
        return applyAttributeModifiers(player.getMaxHealth()/5,Sign.POSITIVE.wrap(player.getAttributeInstance(FORTITUDE)));

    }
    public static double endurance(LivingEntity player){
        return 10/(10+(applyAttributeModifiers(5,Sign.POSITIVE.wrap(player.getAttributeInstance(ENDURANCE)))));

    }

    public static double spellbreak(LivingEntity player){

        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(ACRO)));
    }
    public static double recoup(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(RECOUP)));
    }
    public  static double recoup_reabsorb(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(RECOUPABSORB)));
    }
    public  static double reabsorbarmormax(LivingEntity player){
        return player.getAttributeValue(REABSORBARMORMAX);
    }
    public static double imbalanced(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(IMBALANCEDGUARD)));
    }
    public static double magebane(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(MAGEBANE)));
    }
    public static double blur(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(BLUR)));
    }
    public static double brittle(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(BRITTLE)));
    }
    public static double brittlenegative(LivingEntity player) {
        return applyAttributeModifiers(1, Sign.NEGATIVE.wrap(player.getAttributeInstance(BRITTLE)));
    }
        public static double cull(LivingEntity player){
            return applyAttributeModifiers(1, Sign.POSITIVE.wrap(player.getAttributeInstance(CULL)));
    }
}
