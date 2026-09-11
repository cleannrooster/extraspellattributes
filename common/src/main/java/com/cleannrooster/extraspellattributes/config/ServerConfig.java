package com.cleannrooster.extraspellattributes.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "server_v4")
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
    @Comment("Mod of turtle bracer")
    public float turtle_bracer_mod = 0.3F;

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

    // Armor Conversion. All values below are full-SET totals, split across pieces by vanilla's own
    // 15/40/30/15 armor weighting, so they can be reasoned about as set bonuses.
    @Comment("Armor Conversion: grant Reabsorption, Glancing Blow or Spell Suppression to armor declared in the mage_armor, evasion_armor and antimage_armor tier tags. Turning this off leaves the tagged armor as ordinary armor; already-worn pieces keep their bonus until re-equipped")
    public boolean armor_conversion = true;
    @Comment("Armor Conversion: Reabsorption granted by a full set of tier 1 mage armor")
    public float mage_armor_tier_1 = 5F;
    @Comment("Armor Conversion: Reabsorption granted by a full set of tier 2 mage armor")
    public float mage_armor_tier_2 = 9F;
    @Comment("Armor Conversion: Reabsorption granted by a full set of tier 3 mage armor")
    public float mage_armor_tier_3 = 13F;

    @Comment("Armor Conversion: Glancing Blow chance granted by a full set of tier 1 evasion armor, before the armor discount")
    public float evasion_armor_tier_1 = 0.50F;
    @Comment("Armor Conversion: Glancing Blow chance granted by a full set of tier 2 evasion armor, before the armor discount")
    public float evasion_armor_tier_2 = 0.75F;
    @Comment("Armor Conversion: Glancing Blow chance granted by a full set of tier 3 evasion armor, before the armor discount. A typical 12-armor tier 3 set keeps half of this, landing near 60% of what Glancing V gives a full set")
    public float evasion_armor_tier_3 = 1.00F;
    @Comment("Armor Conversion: total armor points at which evasion armor grants nothing. A set is discounted linearly towards this. Netherite is 20; the default sits above it so that diamond-weight sets keep a residual share and the tier curve stays monotonic as sets gain armor")
    public float evasion_armor_reference = 24F;

    @Comment("Armor Conversion: Spell Suppression chance granted by a full set of tier 1 anti-mage armor")
    public float antimage_armor_tier_1 = 0.25F;
    @Comment("Armor Conversion: Spell Suppression chance granted by a full set of tier 2 anti-mage armor")
    public float antimage_armor_tier_2 = 0.40F;
    @Comment("Armor Conversion: Spell Suppression chance granted by a full set of tier 3 anti-mage armor")
    public float antimage_armor_tier_3 = 0.55F;

    @Comment("Armor Conversion: Fortitude granted by a full set of tier 1 fortitude armor (each point is +10% saturation healing)")
    public float fortitude_armor_tier_1 = 5F;
    @Comment("Armor Conversion: Fortitude granted by a full set of tier 2 fortitude armor (each point is +10% saturation healing)")
    public float fortitude_armor_tier_2 = 10F;
    @Comment("Armor Conversion: Fortitude granted by a full set of tier 3 fortitude armor (each point is +10% saturation healing)")
    public float fortitude_armor_tier_3 = 15F;
}
