package com.extraspellattributes;


import net.minecraft.entity.attribute.EntityAttribute;

public class DynamicAttribute extends EntityAttribute {
    public DynamicAttribute(String translationKey) {
        super(translationKey, Double.NaN);
    }
}