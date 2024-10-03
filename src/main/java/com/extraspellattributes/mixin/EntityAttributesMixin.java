package com.extraspellattributes.mixin;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.extraspellattributes.ReabsorptionInit.*;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void static_tail_Cleann(CallbackInfo ci) {
        WARDING = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "reabsorption"), new ClampedEntityAttribute("attribute.name.extraspellattributes.reabsorption", 0,0,9999));
        CONVERTFROMFIRE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "convertfromfire"),new ClampedEntityAttribute("attribute.name.extraspellattributes.convertfromfire", 100,100,9999));
        CONVERTFROMFROST = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "convertfromfrost"),new ClampedEntityAttribute("attribute.name.extraspellattributes.convertfromfrost", 100,100,9999));
        CONVERTFROMARCANE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "convertfromarcane"),new ClampedEntityAttribute("attribute.name.extraspellattributes.convertfromarcane", 100,100,9999));
        CONVERTTOFIRE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "converttofire"),new  ClampedEntityAttribute("attribute.name.extraspellattributes.converttofire", 100,100,9999));
        CONVERTTOFROST = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "converttofrost"),new ClampedEntityAttribute("attribute.name.extraspellattributes.converttofrost", 100,100,9999));
        CONVERTTOARCANE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "converttoarcane"), new ClampedEntityAttribute("attribute.name.extraspellattributes.converttoarcane", 100,100,9999));
        CONVERTTOHEAL = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "converttoheal"), new ClampedEntityAttribute("attribute.name.extraspellattributes.converttoheal", 100,100,9999));
        GLANCINGBLOW = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "glancingblow"), new ClampedEntityAttribute("attribute.name.extraspellattributes.glancingblow", 100,100,200));
        SPELLSUPPRESS = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "spellsuppression"), new ClampedEntityAttribute("attribute.name.extraspellattributes.spellsuppression", 100,100,200));
        ACRO = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"serenity"), new ClampedEntityAttribute("attribute.name.extraspellattributes.serenity", 100,100,175));
        DEFIANCE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"defiance"),new ClampedEntityAttribute("attribute.name.extraspellattributes.defi", 0,0,999));
        RECOUP = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"recoup"),new ClampedEntityAttribute("attribute.name.extraspellattributes.determination", 100,100,9999));

    }
}