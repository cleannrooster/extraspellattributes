package com.cleannrooster.extraspellattributes.mixin;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(EntityAttributeInstance.class)
public interface EntityAttributeInstanceInvoker {

    //Code Credit to Pufferfish
    @Invoker("getModifiersByOperation")
    Collection<EntityAttributeModifier> invokeGetModifiersByOperation(
            EntityAttributeModifier.Operation operation
    );
}