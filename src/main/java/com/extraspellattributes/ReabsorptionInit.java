package com.extraspellattributes;

import com.extraspellattributes.config.ServerConfig;
import com.extraspellattributes.config.ServerConfigWrapper;
import com.extraspellattributes.items.ItemInit;
import com.extraspellattributes.trades.CustomTrades;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_power.api.*;
import net.spell_power.mixin.EntityAttributesMixin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReabsorptionInit implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("extraspellattributes");
	public static final String MOD_ID = "extraspellattributes";
	public static ServerConfig config;
	public static RegistryKey<Enchantment> DEFIANCEENCHANT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID,"defiance")) ;
	public static RegistryKey<Enchantment> WARDINGENCHANT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID,"warding")) ;
	public static RegistryKey<Enchantment> PRECOGNITIONENCHANT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID,"precognition")) ;
	public static RegistryKey<Enchantment> SUPPRESSINGENCHANT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID,"suppressing")) ;
	public static RegistryKey<Enchantment> SPELLBREAKINGENCHANT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID,"spellbreaking")) ;
	public static RegistryKey<Enchantment> BATTLEROUSE = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID,"battlerouse")) ;

	public static RegistryEntry<EntityAttribute> WARDING ;
	public static RegistryEntry<EntityAttribute> CONVERTFROMFIRE ;
	public static RegistryEntry<EntityAttribute> CONVERTFROMFROST;
	public static RegistryEntry<EntityAttribute> CONVERTFROMARCANE ;
	public static RegistryEntry<EntityAttribute> CONVERTTOFIRE ;
	public static RegistryEntry<EntityAttribute> CONVERTTOFROST ;
	public static RegistryEntry<EntityAttribute> CONVERTTOARCANE ;
	public static RegistryEntry<EntityAttribute> CONVERTTOHEAL ;
	public static RegistryEntry<EntityAttribute> GLANCINGBLOW;
	public static RegistryEntry<EntityAttribute> SPELLSUPPRESS;
	public static RegistryEntry<EntityAttribute> ACRO;
	public static RegistryEntry<EntityAttribute> DEFIANCE ;
	public static RegistryEntry<EntityAttribute> RECOUP;
	public static final GameRules.Key<GameRules.BooleanRule> CLASSIC_ENERGYSHIELD = GameRuleRegistry.register("classicEnergyShield", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
	static{

	}
	@Override
	public void onInitialize() {

		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		config = AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig().server;


		ItemInit.register();

		CustomTrades.registerCustomTrades();
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			// Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
			// We also check that the loot table ID is equal to the ID we want.
			if (source.isBuiltin() && LootTables.BASTION_TREASURE_CHEST.equals(key)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.NETHERITEDIAMOND));
				poolBuilder.rolls(BinomialLootNumberProvider.create(1,0.2F));
				tableBuilder.pool(poolBuilder);
				LootPool.Builder poolBuilder2 = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.NETHERITEDIAMONDAMULET));

				poolBuilder2.rolls(BinomialLootNumberProvider.create(1,0.2F));

				tableBuilder.pool(poolBuilder2);
			}
			if (source.isBuiltin() && LootTables.SIMPLE_DUNGEON_CHEST.equals(key)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.GOLDQUARTZRING));

				poolBuilder.rolls(BinomialLootNumberProvider.create(1,0.2F));
				tableBuilder.pool(poolBuilder);
				LootPool.Builder poolBuilder2 = LootPool.builder()
						.with(ItemEntry.builder(ItemInit.GOLDQUARTZAMULET));

				poolBuilder2.rolls(BinomialLootNumberProvider.create(1,0.2F));
				tableBuilder.pool(poolBuilder2);
			}
		});		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		((ClampedEntityAttribute)(CONVERTTOFROST.value())).setTracked(true);
		((ClampedEntityAttribute)(CONVERTTOFIRE.value())).setTracked(true);
		((ClampedEntityAttribute)(CONVERTTOARCANE.value())).setTracked(true);
		((ClampedEntityAttribute)(CONVERTTOHEAL.value())).setTracked(true);
		((ClampedEntityAttribute)(CONVERTFROMARCANE.value())).setTracked(true);
		((ClampedEntityAttribute)(CONVERTFROMFROST.value())).setTracked(true);
		((ClampedEntityAttribute)(CONVERTFROMFIRE.value())).setTracked(true);
		((ClampedEntityAttribute)(WARDING.value())).setTracked(true);
		SpellSchools.FROST.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*0.01*(queryArgs.entity().getAttributeValue(CONVERTTOFROST)-100);
		});
		SpellSchools.FIRE.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*0.01*(queryArgs.entity().getAttributeValue(CONVERTTOFIRE)-100);
		});
		SpellSchools.ARCANE.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*0.01*(queryArgs.entity().getAttributeValue(CONVERTTOARCANE)-100);
		});
		SpellSchools.HEALING.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*0.01*(queryArgs.entity().getAttributeValue(CONVERTTOHEAL)-100);
		});
		LOGGER.info("Hello Fabric world!");

	}
}