package com.cleannrooster.extraspellattributes.AccessoriesCompat;

import com.cleannrooster.extraspellattributes.items.JewelryFactory;
import io.wispforest.accessories.api.components.AccessoriesDataComponents;
import io.wispforest.accessories.api.components.AccessoryItemAttributeModifiers;
import net.minecraft.sound.SoundEvents;

public class AccessoriesHelper {
    public static void registerFactory() {
        JewelryFactory.factory = args -> {
            var settings = args.settings();
            var attributes = args.attributes();
            var slot = args.slot() != null ? args.slot() : "ring"; // Use provided slot or default

            if (attributes != null) {
                var builder = AccessoryItemAttributeModifiers.builder();
                for (var bonus : attributes.modifiers()) {
                    builder = builder.addForSlot(bonus.attribute(), bonus.modifier(), slot, true);
                }
                settings = settings.component(AccessoriesDataComponents.ATTRIBUTES, builder.build());
            }

            return new JewelryAccessoriesItem(
                    settings,
                    args.lore(),
                    () -> SoundEvents.ITEM_ARMOR_EQUIP_CHAIN);
        };
    }
}