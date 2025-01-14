package com.extraspellattributes;

import com.extraspellattributes.api.Sign;
import com.extraspellattributes.api.Signed;
import com.extraspellattributes.config.ServerConfig;
import com.extraspellattributes.config.ServerConfigWrapper;
import com.extraspellattributes.items.ItemInit;
import com.extraspellattributes.mixin.EntityAttributeInstanceInvoker;
import com.extraspellattributes.trades.CustomTrades;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_power.api.*;
import net.spell_power.mixin.EntityAttributesMixin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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
	public static RegistryEntry<EntityAttribute> RECOUPABSORB;
	public static RegistryEntry<EntityAttribute> REABSORBARMORMAX;

	public static RegistryEntry<EntityAttribute> IMBALANCEDGUARD;
	public static RegistryEntry<EntityAttribute> MAGEBANE;
	public static RegistryEntry<EntityAttribute> BLUR;
	public static RegistryEntry<EntityAttribute> BRITTLE;
	public static RegistryEntry<EntityAttribute> CULL;


	public static final GameRules.Key<GameRules.BooleanRule> CLASSIC_ENERGYSHIELD = GameRuleRegistry.register("classicEnergyShield", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
	static{

	}
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("extraSpellAttributes").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
				.executes((ctx) -> {

					PlayerEntity player = (((ServerCommandSource)ctx.getSource()).getPlayer());
					player.sendMessage(Text.of("converttofrost: "+String.valueOf(Calculations.converttoFrost(player))),false);
					player.sendMessage(Text.of("converttofire: "+String.valueOf(Calculations.converttoFire(player))),false);
					player.sendMessage(Text.of("converttoarcane: "+String.valueOf(Calculations.converttoArcane(player))),false);
					player.sendMessage(Text.of("converttoheal: "+String.valueOf(Calculations.converttoHeal(player))),false);
					player.sendMessage(Text.of("glancingblow: "+String.valueOf(Calculations.glancingBlow(player))),false);
					player.sendMessage(Text.of("spellsuppress: "+String.valueOf(Calculations.spellSuppress(player))),false);
					player.sendMessage(Text.of("defiance: "+String.valueOf(Calculations.defiance(player))),false);
					player.sendMessage(Text.of("spellbreak: "+String.valueOf(Calculations.spellbreak(player))),false);
					player.sendMessage(Text.of("recoup: "+String.valueOf(Calculations.recoup(player))),false);
					player.sendMessage(Text.of("recoup_reabsorb: "+String.valueOf(Calculations.recoup_reabsorb(player))),false);
					player.sendMessage(Text.of("reabsorbarmormax: "+String.valueOf(Calculations.reabsorbarmormax(player))),false);
					player.sendMessage(Text.of("imbalanced: "+String.valueOf(Calculations.imbalanced(player))),false);
					player.sendMessage(Text.of("magebane: "+String.valueOf(Calculations.magebane(player))),false);
					player.sendMessage(Text.of("blur: "+String.valueOf(Calculations.blur(player))),false);
					player.sendMessage(Text.of("brittle: "+String.valueOf(Calculations.brittle(player))),false);
					player.sendMessage(Text.of("cull: "+String.valueOf(Calculations.cull(player))),false);

					return 1;

				})));

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
		((CONVERTTOFROST.value())).setTracked(true);
		((CONVERTTOFIRE.value())).setTracked(true);
		((CONVERTTOARCANE.value())).setTracked(true);
		((CONVERTTOHEAL.value())).setTracked(true);
		((CONVERTFROMARCANE.value())).setTracked(true);
		((CONVERTFROMFROST.value())).setTracked(true);
		((CONVERTFROMFIRE.value())).setTracked(true);
		((WARDING.value())).setTracked(true);
		((RECOUP.value())).setTracked(true);

		((RECOUPABSORB.value())).setTracked(true);
		((REABSORBARMORMAX.value())).setTracked(true);
		((IMBALANCEDGUARD.value())).setTracked(true);
		((MAGEBANE.value())).setTracked(true);
		((BRITTLE.value())).setTracked(true);
		((BLUR.value())).setTracked(true);
		((CULL.value())).setTracked(true);

		SpellSchools.FROST.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*(-1+Calculations.converttoFrost(queryArgs.entity()));
		});
		SpellSchools.FIRE.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*(-1+Calculations.converttoFire(queryArgs.entity()));});
		SpellSchools.ARCANE.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*(-1+Calculations.converttoArcane(queryArgs.entity()));});
		SpellSchools.HEALING.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			return SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*(-1+Calculations.converttoHeal(queryArgs.entity()));});
		LOGGER.info("Hello Fabric world!");

	}
	//Code Credit to Pufferfish
	@SafeVarargs
	public static double applyAttributeModifiers(
			double initial,
			Signed<EntityAttributeInstance>... attributes
	) {
		for (var signedAttribute : attributes) {
			if (signedAttribute.value() == null) {
				continue;
			}
			for (var modifier : ((EntityAttributeInstanceInvoker) signedAttribute.value())
					.invokeGetModifiersByOperation(EntityAttributeModifier.Operation.ADD_VALUE)
			) {
				switch (signedAttribute.sign()) {
					case POSITIVE -> initial += modifier.value();
					case NEGATIVE -> initial -= modifier.value();
					default -> throw new IllegalStateException();
				}
			}
		}
		double result = initial;
		for (var signedAttribute : attributes) {
			if (signedAttribute.value() == null) {
				continue;
			}
			for (var modifier : ((EntityAttributeInstanceInvoker) signedAttribute.value())
					.invokeGetModifiersByOperation(EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
			) {
				switch (signedAttribute.sign()) {
					case POSITIVE -> result += initial * modifier.value();
					case NEGATIVE -> result -= initial * modifier.value();
					default -> throw new IllegalStateException();
				}
			}
		}
		for (var signedAttribute : attributes) {
			if (signedAttribute.value() == null) {
				continue;
			}
			for (var modifier : ((EntityAttributeInstanceInvoker) signedAttribute.value())
					.invokeGetModifiersByOperation(EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			) {
				switch (signedAttribute.sign()) {
					case POSITIVE -> result *= 1.0 + modifier.value();
					case NEGATIVE -> result *= 1.0 - modifier.value();
					default -> throw new IllegalStateException();
				}
			}
		}
		for (var signedAttribute : attributes) {
			if (signedAttribute.value() == null) {
				continue;
			}
			result = signedAttribute.value().getAttribute().value().clamp(result);
		}
		return result;
	}

}