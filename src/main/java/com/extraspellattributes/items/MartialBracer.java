package com.extraspellattributes.items;

import com.extraspellattributes.ReabsorptionInit;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class MartialBracer extends TrinketItem {
    public float value = 0;
    public MartialBracer(Settings settings, float value) {
        super(settings);
        this.value = value;
    }



    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // +10% movement speed
        modifiers.put(ReabsorptionInit.GLANCINGBLOW, new EntityAttributeModifier(uuid, "extraspellattributes:extraspellattributes", value, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        // If the player has access to ring slots, this will give them an extra one
        return modifiers;
    }

}
