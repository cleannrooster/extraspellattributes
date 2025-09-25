package com.cleannrooster.extraspellattributes.items;

import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class JewelryFactory {
    public static Function<JewelryFactory.ItemArgs, Item> factory = (args) -> {
        Item.Settings settings = args.settings;
        if (args.attributes != null) {
            settings.attributeModifiers(args.attributes);
        }

        return new VanillaJewelryItem(settings, args.lore);
    };

    public JewelryFactory() {
    }

    public static Function<JewelryFactory.ItemArgs, Item> getFactory() {
        return factory;
    }

    public static record ItemArgs(Item.Settings settings, @Nullable AttributeModifiersComponent attributes, @Nullable String lore, @Nullable String slot) {
        public ItemArgs(Item.Settings settings, @Nullable AttributeModifiersComponent attributes, @Nullable String lore, @Nullable String slot) {
            this.settings = settings;
            this.attributes = attributes;
            this.lore = lore;
            this.slot = slot;
        }

        public Item.Settings settings() {
            return this.settings;
        }

        public @Nullable AttributeModifiersComponent attributes() {
            return this.attributes;
        }

        public @Nullable String lore() {
            return this.lore;
        }

        public @Nullable String slot() {
            return this.slot;
        }
    }
}
