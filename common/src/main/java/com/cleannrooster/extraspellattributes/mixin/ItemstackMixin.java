package com.cleannrooster.extraspellattributes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemstackMixin {

    @ModifyReturnValue(at = @At("TAIL"), method = "getTooltip")
    public List<Text> getTooltipextraspellattributes(List<Text> tooltip,Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type) {
        if (tooltip.stream().anyMatch(text -> text.toString().contains("extraspellattributes.reabsorption"))) {
            tooltip.add(Text.translatable("desc.extraspellattributes.reabsorption").formatted(Formatting.GRAY));

        }
        return tooltip;

    }

}
