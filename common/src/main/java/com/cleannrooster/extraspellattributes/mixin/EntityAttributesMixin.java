package com.cleannrooster.extraspellattributes.mixin;

import com.cleannrooster.extraspellattributes.DynamicAttribute;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static com.cleannrooster.extraspellattributes.DynamicAttribute.*;
import static com.cleannrooster.extraspellattributes.ExampleMod.MOD_ID;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void static_tail_Cleann(CallbackInfo ci) {
        WARDING = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "reabsorption"), new ClampedEntityAttribute("attribute.name.extraspellattributes.reabsorption", 0,0,9999));
        CONVERTFROMFIRE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "convertfromfire"),new DynamicAttribute("attribute.name.extraspellattributes.convertfromfire"));
        CONVERTFROMFROST = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "convertfromfrost"),new DynamicAttribute("attribute.name.extraspellattributes.convertfromfrost"));
        CONVERTFROMARCANE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "convertfromarcane"),new DynamicAttribute("attribute.name.extraspellattributes.convertfromarcane"));
        CONVERTTOFIRE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "converttofire"),new DynamicAttribute("attribute.name.extraspellattributes.converttofire"));
        CONVERTTOFROST = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "converttofrost"),new DynamicAttribute("attribute.name.extraspellattributes.converttofrost"));
        CONVERTTOARCANE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "converttoarcane"), new DynamicAttribute("attribute.name.extraspellattributes.converttoarcane"));
        CONVERTTOHEAL = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "converttoheal"), new DynamicAttribute("attribute.name.extraspellattributes.converttoheal"));
        EVASION_RATING = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "evasion_rating"), new ClampedEntityAttribute("attribute.name.extraspellattributes.evasion_rating", 0, 0, 2048));
        EVASION_CHANCE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "evasion_chance"), new DynamicAttribute("attribute.name.extraspellattributes.evasion_chance"));
        SPELLSUPPRESS = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, "spellsuppression"), new DynamicAttribute(("attribute.name.extraspellattributes.spellsuppression")));
        ACRO = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"serenity"), new DynamicAttribute(("attribute.name.extraspellattributes.serenity")));
        DEFIANCE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"defiance"),new ClampedEntityAttribute("attribute.name.extraspellattributes.defi", 0,0,999));
        RECOUP = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"recoup"),new DynamicAttribute(("attribute.name.extraspellattributes.determination")));
        RECOUPABSORB = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"recoupabsorb"),new DynamicAttribute(("attribute.name.extraspellattributes.aureate")));
        REABSORBARMORMAX = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"reabsorbarmormax"),new ClampedEntityAttribute("attribute.name.extraspellattributes.reabsorbarmorcap", 0,0,9999));
        IMBALANCEDGUARD = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"imbalancedguard"), new DynamicAttribute(("attribute.name.extraspellattributes.imbalancedguard")));
        MAGEBANE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"magebane"),new ClampedEntityAttribute("attribute.name.extraspellattributes.magebane", 0,0,999));
        BLUR = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"blur"),new DynamicAttribute(("attribute.name.extraspellattributes.blur")));
        BRITTLE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"brittle"),new DynamicAttribute(("attribute.name.extraspellattributes.brittle")));
        CULL = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"cull"),new DynamicAttribute("attribute.name.extraspellattributes.cull"));
        PHYSIQUE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"physique"),new ClampedEntityAttribute("attribute.name.extraspellattributes.physique",0,-2048,2048));
        FINESSE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"finesse"),new ClampedEntityAttribute("attribute.name.extraspellattributes.finesse",0,-2048,2048));
        ATTUNEMENT = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"attunement"),new ClampedEntityAttribute("attribute.name.extraspellattributes.attunement",0,-2048,2048));
        FORTITUDE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"fortitude"),new ClampedEntityAttribute("attribute.name.extraspellattributes.fortitude",0,0,2048));
        ENDURANCE = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"endurance"),new DynamicAttribute("attribute.name.extraspellattributes.endurance"));
        DISSOLUTION = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"dissolution"),new ClampedEntityAttribute("attribute.name.extraspellattributes.dissolution",0,0,2048));
        INEVITABILITY = Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID,"inevitability"),new ClampedEntityAttribute("attribute.name.extraspellattributes.inevitability",0,0,2048));

    }
}