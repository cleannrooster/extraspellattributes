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
    /** Evasion Rating that buys one guaranteed Glancing Blow proc. */
    public static final double EVASION_RATING_PER_PROC = 15.0;

    public static double evasionRating(LivingEntity player){
        return player.getAttributeValue(EVASION_RATING);
    }

    /**
     * Multiplier applied to the procs the rating buys. Starts at 1 and reads raw modifiers, so a
     * +20% Evasion Chance source turns 15 rating into 1.2 procs rather than granting rating of its
     * own. Nothing grants it by default - it exists so effects can modulate evasion without
     * inflating the rating other sources are balanced against.
     */
    public static double evasionChance(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(EVASION_CHANCE)));
    }

    /** Fractional proc count: floor is guaranteed, the remainder rolls for one more. */
    public static double evasionProcs(LivingEntity player){
        return Math.max(0, evasionRating(player) / EVASION_RATING_PER_PROC * evasionChance(player));
    }
    public static double spellSuppress(LivingEntity player){
        return applyAttributeModifiers(1,Sign.POSITIVE.wrap(player.getAttributeInstance(SPELLSUPPRESS)));
    }
    public static double fortitude(LivingEntity player){
        return 1 + 0.1 * player.getAttributeValue(FORTITUDE);
    }
    public static double endurance(LivingEntity player){
        return 10/(10+(applyAttributeModifiers(5,Sign.POSITIVE.wrap(player.getAttributeInstance(ENDURANCE)))));

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
