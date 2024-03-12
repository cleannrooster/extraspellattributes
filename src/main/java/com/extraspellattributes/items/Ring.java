package com.extraspellattributes.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.extraspellattributes.ReabsorptionInit;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.TrinketsMain;
import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class Ring extends TrinketItem {
    public float value = 0;
    public Ring(Settings settings,float value) {
        super(settings);
        this.value = value;
    }



    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // +10% movement speed
        modifiers.put(ReabsorptionInit.WARDING, new EntityAttributeModifier(uuid, "extraspellattributes:extraspellattributes", value, EntityAttributeModifier.Operation.ADDITION));
        // If the player has access to ring slots, this will give them an extra one
        return modifiers;
    }

}
