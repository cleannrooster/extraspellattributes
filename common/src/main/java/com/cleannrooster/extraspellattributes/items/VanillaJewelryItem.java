package com.cleannrooster.extraspellattributes.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class VanillaJewelryItem extends Item {
    private final String lore;

    public VanillaJewelryItem(Item.Settings settings, String lore) {
        super(settings);
        this.lore = lore;
    }

    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if (this.lore != null && !this.lore.isEmpty()) {
            tooltip.add(Text.translatable(this.lore).formatted(new Formatting[]{Formatting.ITALIC, Formatting.GOLD}));
        }

    }
}
