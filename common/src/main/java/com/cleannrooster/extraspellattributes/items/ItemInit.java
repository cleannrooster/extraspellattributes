package com.cleannrooster.extraspellattributes.items;

import com.cleannrooster.extraspellattributes.ReabsorptionInit;
import com.cleannrooster.extraspellattributes.config.ItemConfig;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jewelry.JewelryMod;
import net.jewelry.compat.JewelryAccessoriesItem;
import net.jewelry.items.Group;
import net.jewelry.items.JewelryItems;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.cleannrooster.extraspellattributes.ReabsorptionInit.MOD_ID;

import static com.cleannrooster.extraspellattributes.DynamicAttribute.*;

public class ItemInit {

    public static ItemGroup extraspellattributes;
    public static Item GOLDQUARTZRING;
    public static Item NETHERITEDIAMOND;
    public static Item GOLDQUARTZAMULET;
    public static Item NETHERITEDIAMONDAMULET;
    public static RegistryKey<ItemGroup> KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),Identifier.of(MOD_ID,"generic"));
    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID,name),item);
    }
    private static void addItemToGroup(Item item){
        ItemGroupEvents.modifyEntriesEvent(KEY).register((content) -> {
            content.add(item);
        });
    }
    private static final Identifier modifierId;
    private static ItemGroup GROUP;
    
    static{
        modifierId = Identifier.of("jewelry", "equipment_bonus");

    }
    public static Entry arcane_bracer;
    public static Entry martialbracer;
    public static Entry nonbelieversamulet;
    public static Entry defiancering;
    public static Entry undyingsoul;
    public static Entry turtlegirdle;
    public static Entry goldquartzamulet;
    public static Entry netheritediamondamulet;
    public static Entry goldquartzring ;
    public static Entry netheritediamondring;
    public static final ArrayList<Entry> all = new ArrayList();

    public static Entry add(Identifier id, ItemConfig.Item config) {
        return add(id, Rarity.COMMON, config, (String)null, false);
    }

    public static Entry add(Identifier id, Rarity rarity, ItemConfig.Item config) {
        return add(id, rarity, config, (String)null, false);
    }

    public static Entry add(Identifier id, Rarity rarity, ItemConfig.Item config, boolean fireproof) {
        return add(id, rarity, config, (String)null, fireproof);
    }

    public static Entry add(Identifier id, Rarity rarity, boolean addLore, ItemConfig.Item config) {
        return add(id, rarity, config, addLore ? "item." + id.getNamespace() + "." + id.getPath() + ".lore" : null, false);
    }

    public static Entry add(Identifier id, Rarity rarity, ItemConfig.Item config, String lore, boolean fireproof) {
        Entry entry = new Entry(id, rarity, config, lore, fireproof);
        all.add(entry);
        return entry;
    }
    public static final class Entry {
        private final Identifier id;
        private final Rarity rarity;
        private final ItemConfig.Item config;
        private final String lore;
        private boolean fireproof;
        int tier = 0;
        public Item item;

        public Entry(Identifier id, Rarity rarity, ItemConfig.Item config, String lore, boolean fireproof) {
            this.id = id;
            this.rarity = rarity;
            this.config = config;
            this.lore = lore;
            this.fireproof = fireproof;
        }

        public Identifier id() {
            return this.id;
        }

        public Rarity rarity() {
            return this.rarity;
        }

        public ItemConfig.Item config() {
            return this.config;
        }

        public String lore() {
            return this.lore;
        }

        public boolean fireproof() {
            return this.fireproof;
        }

        public Item create(Item.Settings settings, AttributeModifiersComponent attributes) {
            String slot = this.id.getPath().contains("ring") ? "ring" : (this.id.getPath().contains("necklace") ? "necklace" : null);
            this.item = (Item) JewelryFactory.getFactory().apply(new JewelryFactory.ItemArgs(settings, attributes, this.lore, slot));
            return this.item;
        }

        public Item item() {
            return this.item;
        }

        public Entry setTier(int tier) {
            this.tier = tier;
            this.fireproof = tier >= 3;
            return this;
        }

        public int tier() {
            return this.tier;
        }
    }
    public static void registerItems(ItemConfig allConfigs) {
        Iterator var1 = all.iterator();

        while(var1.hasNext()) {
            Entry entry = (Entry)var1.next();

                ItemConfig.Item itemConfig = entry.config;


            AttributeModifiersComponent.Builder attributes = AttributeModifiersComponent.builder();
            Iterator var5 = itemConfig.attributes.iterator();

            while(var5.hasNext()) {
                ItemConfig.AttributeModifier modifier = (ItemConfig.AttributeModifier)var5.next();
                Identifier id = Identifier.of(modifier.id);
                Optional<RegistryEntry.Reference<EntityAttribute>> attribute = Registries.ATTRIBUTE.getEntry(id);
                if (attribute.isPresent()) {
                    attributes.add((RegistryEntry)attribute.get(), new EntityAttributeModifier(id, (double)modifier.value, modifier.operation), AttributeModifierSlot.ANY);
                } else {
                    System.err.println("Failed to resolve EntityAttribute with id: " + modifier.id);
                }
            }

            Item.Settings settings = (new Item.Settings()).rarity(entry.rarity);
            if (entry.fireproof()) {
                settings.fireproof();
            }

            Item item = entry.create(settings.maxCount(1), attributes.build());
            Registry.register(Registries.ITEM, entry.id(), item);
        }

        ItemGroupEvents.modifyEntriesEvent(KEY).register((content) -> {
            Iterator var2 = all.iterator();

            while(var2.hasNext()) {
                Entry entry = (Entry)var2.next();
                content.add(entry.item());
            }

        });
    }
    public static void register() {

            netheritediamondring =  add(Identifier.of(MOD_ID, "netheritediamondring"), Rarity.RARE, new ItemConfig.Item(
                List.of(
                        new ItemConfig.AttributeModifier(WARDING.getIdAsString(), 6F, EntityAttributeModifier.Operation.ADD_VALUE)
                )
        )).setTier(1);
        goldquartzring =  add(Identifier.of(MOD_ID, "goldquartzring"), Rarity.UNCOMMON, new ItemConfig.Item(
                List.of(
                        new ItemConfig.AttributeModifier(WARDING.getIdAsString(), 4F, EntityAttributeModifier.Operation.ADD_VALUE)
                )
        )).setTier(1);
        netheritediamondamulet  = add(Identifier.of(MOD_ID, "netheritediamondamulet"), Rarity.RARE, new ItemConfig.Item(
                List.of(
                        new ItemConfig.AttributeModifier(WARDING.getIdAsString(), 0.5F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                )
        )).setTier(1);
        goldquartzamulet  = add(Identifier.of(MOD_ID, "goldquartzamulet"), Rarity.UNCOMMON, new ItemConfig.Item(
                List.of(
                        new ItemConfig.AttributeModifier(WARDING.getIdAsString(), 0.25F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                )
        )).setTier(1);
        turtlegirdle  = add(Identifier.of(MOD_ID, "turtlegirdle"), Rarity.RARE, new ItemConfig.Item(
                List.of(
                        new ItemConfig.AttributeModifier(DEFIANCE.getIdAsString(), 2F, EntityAttributeModifier.Operation.ADD_VALUE)
                )
        )).setTier(1);
        undyingsoul = add(Identifier.of(MOD_ID, "undyingsoul"), Rarity.RARE, new ItemConfig.Item(
                List.of(
                        new ItemConfig.AttributeModifier(RECOUP.getIdAsString(), 0.15F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                )
        )).setTier(1);

        defiancering = add(Identifier.of(MOD_ID, "defiancering"), Rarity.UNCOMMON, new ItemConfig.Item(
                List.of(
                        new ItemConfig.AttributeModifier(RECOUP.getIdAsString(), 0.15F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                )
        )).setTier(1);
        nonbelieversamulet  = add(Identifier.of(MOD_ID, "nonbelieversamulet"), Rarity.RARE, new ItemConfig.Item(
                List.of(
                        new ItemConfig.AttributeModifier(ACRO.getIdAsString(), 0.4F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                )
        )).setTier(1);
        martialbracer  = add(Identifier.of(MOD_ID, "martialbracer"), Rarity.UNCOMMON, new ItemConfig.Item(
                List.of(
                        new ItemConfig.AttributeModifier(GLANCINGBLOW.getIdAsString(), 0.3F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                )
        )).setTier(1);
        arcane_bracer  = add(Identifier.of(MOD_ID, "arcanebracer"), Rarity.UNCOMMON, new ItemConfig.Item(
                List.of(
                        new ItemConfig.AttributeModifier(SPELLSUPPRESS.getIdAsString(), 0.3F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                )
        )).setTier(1);
        GROUP = FabricItemGroup.builder().displayName(Text.translatable("itemGroup.extraspellattributes.general")).icon(() -> {
                    Item item = (Item) ((RegistryEntry.Reference) Registries.ITEM.getEntry(netheritediamondring.id()).get()).value();
                    return new ItemStack(item);
                }
        ).build();
        Registry.register(Registries.ITEM_GROUP, KEY, GROUP);

        registerItems(null);
    }
}
