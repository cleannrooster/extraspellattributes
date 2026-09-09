package com.cleannrooster.extraspellattributes;

import com.cleannrooster.extraspellattributes.api.Signed;
import com.cleannrooster.extraspellattributes.config.ServerConfig;
import com.cleannrooster.extraspellattributes.config.ServerConfigWrapper;
import com.cleannrooster.extraspellattributes.effects.Dissolution;
import com.cleannrooster.extraspellattributes.mixin.EntityAttributeInstanceInvoker;
import com.cleannrooster.extraspellattributes.trades.CustomTrades;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import static com.cleannrooster.extraspellattributes.DynamicAttribute.*;
import static com.cleannrooster.extraspellattributes.ExampleMod.MOD_ID;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
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
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_power.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReabsorptionInit {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("extraspellattributes");
	public static final String MOD_ID = "extraspellattributes";

	public static final RegistryKey<Enchantment> GUERILLA_ENCHANT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID,"guerilla"));
	public static final RegistryKey<Enchantment> MAGEBANE_ENCHANT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID,"magebane"));
	public static final RegistryKey<Enchantment> IMBALANCED_GUARD_ENCHANT = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID,"imbalanced_guard"));

	/** Armor-bypassing physical damage type used only by Sneak Attack's double-crit branch. */
	public static final RegistryKey<DamageType> SNEAK_ATTACK_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(MOD_ID,"sneak_attack"));







	public static void initEffects(){
		Effects.initEffects();
	}
	public static void initAttr() {

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
		((PHYSIQUE.value())).setTracked(true);
		((FINESSE.value())).setTracked(true);
		((ATTUNEMENT.value())).setTracked(true);
		((INEVITABILITY.value())).setTracked(true);

	}
		public static void onInitialize() {


		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		Effects.config = AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig().server;

			// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		SpellSchools.FROST.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			double add = 0;
			if(queryArgs.entity().getAttributes() != null){
				add = SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*(-1+Calculations.converttoFrost(queryArgs.entity()));
			}
			return add;});
		SpellSchools.FIRE.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			double add = 0;
			if(queryArgs.entity().getAttributes() != null){
				add = SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*(-1+Calculations.converttoFire(queryArgs.entity()));
			}
			return add;});
		SpellSchools.ARCANE.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			double add = 0;
			if(queryArgs.entity().getAttributes() != null){
				add = SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*(-1+Calculations.converttoArcane(queryArgs.entity()));
			}
			return add;});
		SpellSchools.HEALING.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD,queryArgs -> {
			double add = 0;
			if(queryArgs.entity().getAttributes() != null){
				add = SpellPower.getSpellPower(ExternalSpellSchools.PHYSICAL_MELEE,queryArgs.entity()).baseValue()*(-1+Calculations.converttoHeal(queryArgs.entity()));
			}
			return add;});
		SpellHandlers.registerCustomImpact(Identifier.of(MOD_ID, "sneak_attack"), SneakAttackHandler::onImpact);
		GuerillaHandler.register();
		LOGGER.info("Hello Fabric world!");

	}
	//Code Credit to Pufferfish


}