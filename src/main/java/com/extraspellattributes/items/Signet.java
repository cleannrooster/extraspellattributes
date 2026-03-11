package com.extraspellattributes.items;

import com.extraspellattributes.ReabsorptionInit;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class Signet extends TrinketItem {
    public float value = 0;
    public RegistryEntry<EntityAttribute> attribute;
    public Signet(Settings settings, float value, RegistryEntry<EntityAttribute> attribute) {
        super(settings);
        this.value = value;
        this.attribute = attribute;
    }



    public Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier>  getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, Identifier uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // +10% movement speed
        modifiers.put(ReabsorptionInit.VULNERABILITY, new EntityAttributeModifier(uuid,  0.1D, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        modifiers.put(attribute, new EntityAttributeModifier(uuid,  value, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        // If the player has access to ring slots, this will give them an extra one
        return modifiers;
    }

}
