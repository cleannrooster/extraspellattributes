package com.cleannrooster.extraspellattributes.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "server_v5")
public class ServerConfig  implements ConfigData {
    public ServerConfig(){}
    @Comment("Reabsorption regeneration factor (proportion of 25% of maximum per second) - Default: 1.0")
    public float factor = 1.0F;
    @Comment("Reabsorption regeneration delay in seconds - Default: 4.0")
    public float delay = 4.0F;
    @Comment("Reabsorption custom visuals (Default: true)")
    public boolean visuals = true;
    @Comment("Registration of turtle bracer")
    public boolean turtle_bracer = true;
    @Comment("Mod of turtle bracer (Evasion Rating points; 15 is one guaranteed Glancing Blow)")
    public float turtle_bracer_mod = 5F;

    @Comment("Registration of turtle girdle")
    public boolean turtle_girdle = true;

    @Comment("Mod of turtle girdle (Fortitude points; each is +10% saturation healing)")
    public float turtle_girdle_mod = 4F;
    @Comment("Registration of arcane bracer")
    public boolean arcane_bracer = true;
    @Comment("Mod of arcane bracer")
    public float arcane_bracer_mod = 0.25F;
    @Comment("Registration of nonbeliever amulet")
    public boolean nonbeliever = true;

    @Comment("Mod of nonbeliever amulet (spell suppression chance)")
    public float nonbeliever_mod = 0.4F;
    @Comment("Registration of defiance ring")
    public boolean defiance_ring = true;

    @Comment("Mod of defiance ring")
    public float defiance_ring_mod = 0.15F;
    @Comment("Registration of undying soul")
    public boolean undying_soul = true;

    @Comment("Mod of undying soul")
    public float undying_soul_mod = 0.3F;
    @Comment("Mod of gold absorption ring")
    public float gold_absorption_ring_mod = 4;
    @Comment("Mod of netherite absorption ring")
    public float netherite_absorption_ring_mod = 6;
    @Comment("Mod of gold necklace")
    public float gold_absorption_necklace_mod = 0.25F;
    @Comment("Mod of netherite necklace")
    public float netherite_absorption_necklace_mod = 0.5F;

    // Armor Conversion. All values below are full-SET totals, split evenly across the four pieces.
    // Each family's tier 3 total is budgeted to roughly double effective HP under ideal conditions.
    @Comment("Armor Conversion: grant Reabsorption, Evasion Rating or Fortitude to armor declared in the mage_armor, evasion_armor and fortitude_armor tier tags. Turning this off leaves the tagged armor as ordinary armor; already-worn pieces keep their bonus until re-equipped")
    public boolean armor_conversion = true;

    // Reabsorption is a flat regenerating pool on top of health, so a tier 3 set matching the
    // player's own 20 maximum health is what doubles effective HP.
    @Comment("Armor Conversion: Reabsorption granted by a full set of tier 1 mage armor")
    public float mage_armor_tier_1 = 8F;
    @Comment("Armor Conversion: Reabsorption granted by a full set of tier 2 mage armor")
    public float mage_armor_tier_2 = 14F;
    @Comment("Armor Conversion: Reabsorption granted by a full set of tier 3 mage armor. 20 points matches a player's maximum health, doubling effective HP")
    public float mage_armor_tier_3 = 20F;

    // 15 Evasion Rating is one guaranteed Glancing Blow (x0.65). 24 is 1.6 procs, whose expected
    // multiplier 0.65 * (0.4 + 0.6 * 0.65) = 0.514 is very close to halving damage taken.
    @Comment("Armor Conversion: Evasion Rating granted by a full set of tier 1 evasion armor")
    public float evasion_armor_tier_1 = 8F;
    @Comment("Armor Conversion: Evasion Rating granted by a full set of tier 2 evasion armor")
    public float evasion_armor_tier_2 = 16F;
    @Comment("Armor Conversion: Evasion Rating granted by a full set of tier 3 evasion armor. 24 rating is 1.6 Glancing Blow procs, roughly doubling effective HP")
    public float evasion_armor_tier_3 = 24F;

    // Fortitude scales saturation healing by 1 + 0.1 per point, and sustainable incoming DPS is
    // healing divided by the damage that gets through, so the armor's own reduction cancels out:
    // 10 points doubles the healing and therefore doubles the DPS needed to whittle the wearer down.
    @Comment("Armor Conversion: Fortitude granted by a full set of tier 1 fortitude armor (each point is +10% saturation healing)")
    public float fortitude_armor_tier_1 = 4F;
    @Comment("Armor Conversion: Fortitude granted by a full set of tier 2 fortitude armor (each point is +10% saturation healing)")
    public float fortitude_armor_tier_2 = 7F;
    @Comment("Armor Conversion: Fortitude granted by a full set of tier 3 fortitude armor. 10 points doubles saturation healing, so the wearer needs twice the incoming DPS to be whittled down")
    public float fortitude_armor_tier_3 = 10F;
}
