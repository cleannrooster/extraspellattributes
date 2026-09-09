package com.cleannrooster.extraspellattributes.mixin;

import com.cleannrooster.extraspellattributes.Calculations;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    /* ordinal 0 is the saturation fast-regen heal; ordinal 1 is the plain hunger-bar regen,
     * which Fortitude deliberately does not touch. */
    @WrapOperation(method = "update", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;heal(F)V", ordinal = 0))
    private void esa$scaleSaturationHealing(PlayerEntity player, float amount, Operation<Void> original) {
        original.call(player, (float) (amount * Calculations.fortitude(player)));
    }
}
