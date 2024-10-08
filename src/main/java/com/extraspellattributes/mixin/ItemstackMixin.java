package com.extraspellattributes.mixin;

import com.extraspellattributes.ReabsorptionInit;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(ItemStack.class)
public class ItemstackMixin {
    @ModifyVariable()
    @ModifyReturnValue(at = @At("TAIL"), method = "getTooltip")
    public List<Text> getTooltipextraspellattributes(List<Text> tooltip, @Nullable PlayerEntity player, TooltipContext context) {
        if(tooltip.stream().anyMatch(text -> text.toString().contains("extraspellattributes.defi")) ||
                tooltip.stream().anyMatch(text -> text.toString().contains("extraspellattributes.glancingblow")) ||
                tooltip.stream().anyMatch(text -> text.toString().contains("extraspellattributes.serenity")) ||
                tooltip.stream().anyMatch(text -> text.toString().contains("extraspellattributes.spellsuppression")) ||
                tooltip.stream().anyMatch(text -> text.toString().contains("extraspellattributes.determination")) ||
                tooltip.stream().anyMatch(text -> text.toString().contains("extraspellattributes.reabsorption"))) {

            if (tooltip.stream().anyMatch(text -> text.toString().contains("extraspellattributes.reabsorption"))) {
                tooltip.add(Text.translatable("desc.extraspellattributes.reabsorption").formatted(Formatting.GRAY));

            }
        }
        return tooltip;

    }

}
