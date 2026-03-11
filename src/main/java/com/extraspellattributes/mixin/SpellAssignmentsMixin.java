package com.extraspellattributes.mixin;

import com.extraspellattributes.api.WeaponSkills;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.internals.container.SpellAssignments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

import static com.extraspellattributes.ReabsorptionInit.MOD_ID;

@Mixin(SpellAssignments.class)
public class SpellAssignmentsMixin {
    @Shadow
    public static  Map<Identifier, SpellContainer> containers ;

    @Inject(method = "loadContainers", at = @At("TAIL"))

    private static void loadContainersVuln(ResourceManager resourceManager, CallbackInfo callbackInfo) {

    }
}
