package com.cleannrooster.extraspellattributes.armor;

import com.cleannrooster.extraspellattributes.DynamicAttribute;
import com.cleannrooster.extraspellattributes.Effects;
import com.cleannrooster.extraspellattributes.config.ServerConfig;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.cleannrooster.extraspellattributes.DynamicAttribute.GLANCINGBLOW;
import static com.cleannrooster.extraspellattributes.DynamicAttribute.SPELLSUPPRESS;
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
 * <p>Config values are full-SET totals; each piece receives total * slotWeight, using vanilla's own
 * 15/40/30/15 split (diamond's 3/8/6/3). Balance is therefore reasoned about in set totals, and
 * mixed sets degrade sensibly.
 */
public final class ArmorConversion {
    private ArmorConversion() {}

    /** Fixed priority - first match wins, so an item in two families can never double up. */
    public enum Family { MAGE, EVASION, ANTIMAGE, FORTITUDE }

    public record Grant(Family family, int tier) {}

    private static final int MAX_TIER = 3;

    /** Vanilla's armor split, 3/8/6/3 out of diamond's 20. Doubles as the netherite reference. */
    private static final Map<EquipmentSlot, Double> SLOT_WEIGHTS = new EnumMap<>(EquipmentSlot.class);
    static {
        SLOT_WEIGHTS.put(EquipmentSlot.HEAD, 0.15);
        SLOT_WEIGHTS.put(EquipmentSlot.CHEST, 0.40);
        SLOT_WEIGHTS.put(EquipmentSlot.LEGS, 0.30);
        SLOT_WEIGHTS.put(EquipmentSlot.FEET, 0.15);
    }

    /**
     * One modifier id per slot. 1.21 keys attribute modifiers by Identifier rather than UUID, so
     * four pieces sharing an id would collapse to one - vanilla armor avoids this the same way,
     * with minecraft:armor.helmet and friends. Families can share the table because only one family
     * ever applies to a given item.
     */
    private static final Map<EquipmentSlot, Identifier> MODIFIER_IDS = new EnumMap<>(EquipmentSlot.class);
    static {
        for (EquipmentSlot slot : SLOT_WEIGHTS.keySet()) {
            MODIFIER_IDS.put(slot, Identifier.of(MOD_ID, "armor_conversion." + slot.getName()));
        }
    }

    private static final Map<Family, TagKey<Item>[]> TIER_TAGS = new EnumMap<>(Family.class);
    static {
        TIER_TAGS.put(Family.MAGE, tierTags("mage_armor"));
        TIER_TAGS.put(Family.EVASION, tierTags("evasion_armor"));
        TIER_TAGS.put(Family.ANTIMAGE, tierTags("antimage_armor"));
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
        Double weight = SLOT_WEIGHTS.get(slot);
        if (weight == null) {
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
        double amount = setTotal * weight;

        // Evasion is the payment for being lightly armored, so discount it by the armor the piece
        // already carries. Linear to zero at the netherite reference: reference * weight is exactly
        // netherite's own per-piece armor (3/8/6/3). Summed over an unclamped set this comes to
        // total * (1 - setArmor / reference), so set-total reasoning survives the per-piece split.
        if (grant.family() == Family.EVASION) {
            double reference = config.evasion_armor_reference * weight;
            if (reference <= 0) {
                return;
            }
            amount *= Math.max(0.0, 1.0 - baseArmor(stack, slot) / reference);
        }

        if (amount <= 0) {
            return;
        }

        EntityAttributeModifier modifier =
                new EntityAttributeModifier(MODIFIER_IDS.get(slot), amount, operation(grant.family()));
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
            case ANTIMAGE -> switch (grant.tier()) {
                case 1 -> config.antimage_armor_tier_1;
                case 2 -> config.antimage_armor_tier_2;
                default -> config.antimage_armor_tier_3;
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
            case EVASION -> GLANCINGBLOW;
            case ANTIMAGE -> SPELLSUPPRESS;
            case FORTITUDE -> DynamicAttribute.FORTITUDE;
        };
    }

    /**
     * Reabsorption and Fortitude are flat point pools, matching the warding enchantment and the
     * turtle girdle; glancing and suppression are chances, matching their enchantments.
     *
     * <p>For the two dynamic attributes the operations are numerically identical - they start from
     * 1 and read raw modifiers - and differ only in rendering, flat against percent. For Fortitude
     * the choice is load-bearing, not cosmetic: it is a clamped attribute read through
     * getAttributeValue with a base of 0, so ADD_MULTIPLIED_BASE would scale that zero and grant
     * nothing at all.
     */
    private static EntityAttributeModifier.Operation operation(Family family) {
        return switch (family) {
            case MAGE, FORTITUDE -> EntityAttributeModifier.Operation.ADD_VALUE;
            case EVASION, ANTIMAGE -> EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE;
        };
    }

    @Nullable
    private static EquipmentSlot slotOf(ItemStack stack) {
        Equipment equipment = Equipment.fromStack(stack);
        return equipment == null ? null : equipment.getSlotType();
    }

    /**
     * The armor points the piece itself declares. Read straight off the component rather than
     * through ItemStack#applyAttributeModifiers, which would recurse back into this class and would
     * also count armor granted by enchantments.
     */
    // The empty-component fallback mirrors ItemStack#applyAttributeModifiers exactly. Item's getter
    // is deprecated in favour of the component, but dropping the fallback would silently zero the
    // evasion discount for any item that supplies its modifiers by override rather than by setting.
    @SuppressWarnings("deprecation")
    private static double baseArmor(ItemStack stack, EquipmentSlot slot) {
        AttributeModifiersComponent component =
                stack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
        if (component.modifiers().isEmpty()) {
            component = stack.getItem().getAttributeModifiers();
        }
        double[] armor = {0};
        component.applyModifiers(slot, (attribute, modifier) -> {
            if (attribute.value() == EntityAttributes.GENERIC_ARMOR.value()
                    && modifier.operation() == EntityAttributeModifier.Operation.ADD_VALUE) {
                armor[0] += modifier.value();
            }
        });
        return armor[0];
    }
}
