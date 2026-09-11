package com.cleannrooster.extraspellattributes.mixin;

import com.cleannrooster.extraspellattributes.armor.ArmorConversion;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

/**
 * Applies Armor Conversion by appending to the modifier stream rather than rewriting the
 * ATTRIBUTE_MODIFIERS component.
 *
 * <p>The component is the wrong place to put this: its defaults are baked at mod init, before
 * datapacks load, so a tag-driven value cannot be resolved there and would not survive a reload.
 * Both methods below resolve tags at query time instead.
 *
 * <p>1.21 splits the two consumers - LivingEntity drives applyAttributeModifiers(EquipmentSlot)
 * while the tooltip drives applyAttributeModifier(AttributeModifierSlot) - so both need the hook.
 * Injecting into the second means vanilla renders the tooltip line itself, under the right slot
 * heading and with the right formatting for the modifier operation.
 *
 * <p>NeoForge reroutes the first method through its own getAttributeModifiers() for
 * ItemAttributeModifierEvent, but keeps the signature and the trailing enchantment call, so this
 * single common mixin serves both loaders.
 */
@Mixin(ItemStack.class)
public class ItemStackArmorConversionMixin {

    @Inject(method = "applyAttributeModifiers", at = @At("TAIL"))
    private void esa$applyArmorConversion(
            EquipmentSlot slot,
            BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> consumer,
            CallbackInfo ci
    ) {
        ArmorConversion.apply((ItemStack) (Object) this, slot, consumer);
    }

    @Inject(method = "applyAttributeModifier", at = @At("TAIL"))
    private void esa$applyArmorConversionTooltip(
            AttributeModifierSlot slot,
            BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> consumer,
            CallbackInfo ci
    ) {
        ArmorConversion.apply((ItemStack) (Object) this, slot, consumer);
    }
}
