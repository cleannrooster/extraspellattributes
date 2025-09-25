package com.cleannrooster.extraspellattributes.config;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConfig {
    public Map<String, ItemConfig.Item> items = new HashMap();

    public ItemConfig() {
    }

    public static class Item {
        public List<AttributeModifier> attributes = List.of();

        Item() {
        }

        public Item(List<AttributeModifier> attributes) {
            this.attributes = attributes;
        }
    }

    public static class AttributeModifier {
        public String id;
        public float value;
        public EntityAttributeModifier.Operation operation;

        AttributeModifier() {
            this.id = "";
            this.value = 0.0F;
            this.operation = EntityAttributeModifier.Operation.ADD_VALUE;
        }

        public AttributeModifier(Identifier id, float value, EntityAttributeModifier.Operation operation) {
            this(id.toString(), value, operation);
        }

        public AttributeModifier(Identifier id, Bonus bonus) {
            this(id.toString(), bonus.value, bonus.operation);
        }

        public AttributeModifier(String id, Bonus bonus) {
            this(id, bonus.value, bonus.operation);
        }

        public AttributeModifier(String id, float value, EntityAttributeModifier.Operation operation) {
            this.id = "";
            this.value = 0.0F;
            this.operation = EntityAttributeModifier.Operation.ADD_VALUE;
            this.id = id;
            this.value = value;
            this.operation = operation;
        }
    }

    public static record Bonus(float value, EntityAttributeModifier.Operation operation) {
        public Bonus(float value, EntityAttributeModifier.Operation operation) {
            this.value = value;
            this.operation = operation;
        }

        public float value() {
            return this.value;
        }

        public EntityAttributeModifier.Operation operation() {
            return this.operation;
        }
    }
}
