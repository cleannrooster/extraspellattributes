package com.cleannrooster.extraspellattributes.armor;

import com.cleannrooster.extraspellattributes.DynamicAttribute;
import com.cleannrooster.extraspellattributes.Effects;
import com.cleannrooster.extraspellattributes.config.ServerConfig;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import static com.cleannrooster.extraspellattributes.DynamicAttribute.EVASION_RATING;
import static com.cleannrooster.extraspellattributes.DynamicAttribute.WARDING;
import static com.cleannrooster.extraspellattributes.ExampleMod.MOD_ID;

/**
 * Armor Conversion: tag-declared armor grants one defensive stat by material tier.
 *
 * <p>Tier is declared, never inferred - no generic signal on ArmorMaterial orders the vanilla
 * materials correctly (enchantability and durability misplace gold, toughness is 0 below diamond),
 * and modded RPG armors make inference worse. Items opt in through nested tags mirroring the
 * rpg_series:loot_tier convention: extraspellattributes:mage_armor/tier_1 and so on.
 *
 * <p>Config values are full-SET totals and every slot receives an equal quarter of the total. The
 * grant is a flat bonus: it is not scaled down by the armor points the piece already carries, and
 * it is not weighted by slot. A tier 3 set is budgeted to roughly double effective HP under ideal
 * conditions, whichever family it belongs to.
 */
public final class ArmorConversion {
    private ArmorConversion() {}

    /** Fixed priority - first match wins, so an item in two families can never double up. */
    public enum Family { MAGE, EVASION, FORTITUDE }

    public record Grant(Family family, int tier) {}

    private static final int MAX_TIER = 3;

    /** The four armor slots, each taking an equal share of the set total. */
    private static final Set<EquipmentSlot> ARMOR_SLOTS =
            EnumSet.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

    private static final double SLOT_SHARE = 1.0 / ARMOR_SLOTS.size();

    /**
     * One modifier id per slot. 1.21 keys attribute modifiers by Identifier rather than UUID, so
     * four pieces sharing an id would collapse to one - vanilla armor avoids this the same way,
     * with minecraft:armor.helmet and friends. Families can share the table because only one family
     * ever applies to a given item.
     */
    private static final Map<EquipmentSlot, Identifier> MODIFIER_IDS = new EnumMap<>(EquipmentSlot.class);
    static {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            MODIFIER_IDS.put(slot, Identifier.of(MOD_ID, "armor_conversion." + slot.getName()));
        }
    }

    private static final Map<Family, TagKey<Item>[]> TIER_TAGS = new EnumMap<>(Family.class);
    static {
        TIER_TAGS.put(Family.MAGE, tierTags("mage_armor"));
        TIER_TAGS.put(Family.EVASION, tierTags("evasion_armor"));
        TIER_TAGS.put(Family.FORTITUDE, tierTags("fortitude_armor"));
    }

    @SuppressWarnings("unchecked")
    private static TagKey<Item>[] tierTags(String family) {
        TagKey<Item>[] tags = new TagKey[MAX_TIER];
        for (int tier = 1; tier <= MAX_TIER; tier++) {
            tags[tier - 1] = TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, family + "/tier_" + tier));
        }
        return tags;
    }

    /**
     * Resolves the single family and tier an item belongs to. Families are checked in declaration
     * order; within a family the highest declared tier wins, so a datapack adding an item to a
     * higher tier upgrades it rather than being ignored.
     */
    @Nullable
    public static Grant resolve(ItemStack stack) {
        for (Family family : Family.values()) {
            TagKey<Item>[] tags = TIER_TAGS.get(family);
            for (int tier = MAX_TIER; tier >= 1; tier--) {
                if (stack.isIn(tags[tier - 1])) {
                    return new Grant(family, tier);
                }
            }
        }
        return null;
    }

    /**
     * Feeds this stack's converted stat into the consumer, if it declares one for this slot. Called
     * from both the gameplay and the tooltip path, so the tooltip line is rendered by vanilla with
     * the correct slot heading and flat/percent formatting.
     */
    public static void apply(
            ItemStack stack,
            EquipmentSlot slot,
            BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> consumer
    ) {
        if (!ARMOR_SLOTS.contains(slot)) {
            return;
        }
        // Checked before resolve() so a disabled feature costs nothing but the slot lookup, rather
        // than nine tag queries on every equipment recalculation and every tooltip frame.
        ServerConfig config = Effects.config;
        if (config == null || !config.armor_conversion) {
            return;
        }
        Grant grant = resolve(stack);
        if (grant == null) {
            return;
        }

        double setTotal = setTotal(config, grant);
        if (setTotal <= 0) {
            return;
        }

        EntityAttributeModifier modifier = new EntityAttributeModifier(
                MODIFIER_IDS.get(slot), setTotal * SLOT_SHARE, EntityAttributeModifier.Operation.ADD_VALUE);
        consumer.accept(attribute(grant.family()), modifier);
    }

    /** Same dispatch as above, but for the tooltip path, which addresses slots by name. */
    public static void apply(
            ItemStack stack,
            AttributeModifierSlot slot,
            BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> consumer
    ) {
        EquipmentSlot equipmentSlot = slotOf(stack);
        if (equipmentSlot != null && AttributeModifierSlot.forEquipmentSlot(equipmentSlot) == slot) {
            apply(stack, equipmentSlot, consumer);
        }
    }

    private static double setTotal(ServerConfig config, Grant grant) {
        return switch (grant.family()) {
            case MAGE -> switch (grant.tier()) {
                case 1 -> config.mage_armor_tier_1;
                case 2 -> config.mage_armor_tier_2;
                default -> config.mage_armor_tier_3;
            };
            case EVASION -> switch (grant.tier()) {
                case 1 -> config.evasion_armor_tier_1;
                case 2 -> config.evasion_armor_tier_2;
                default -> config.evasion_armor_tier_3;
            };
            case FORTITUDE -> switch (grant.tier()) {
                case 1 -> config.fortitude_armor_tier_1;
                case 2 -> config.fortitude_armor_tier_2;
                default -> config.fortitude_armor_tier_3;
            };
        };
    }

    private static RegistryEntry<EntityAttribute> attribute(Family family) {
        return switch (family) {
            case MAGE -> WARDING;
            case EVASION -> EVASION_RATING;
            case FORTITUDE -> DynamicAttribute.FORTITUDE;
        };
    }

    @Nullable
    private static EquipmentSlot slotOf(ItemStack stack) {
        Equipment equipment = Equipment.fromStack(stack);
        return equipment == null ? null : equipment.getSlotType();
    }
}
