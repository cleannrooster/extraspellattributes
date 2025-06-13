package com.extraspellattributes.mixin;

import com.extraspellattributes.PlayerInterface;
import com.extraspellattributes.api.RecoupInstances;
import com.extraspellattributes.api.Sign;
import com.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.spell_engine.internals.WorldScheduler;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

import static com.extraspellattributes.ReabsorptionInit.*;

@Mixin(value = DamageTracker.class, priority = 9999)
public class LivingEntityRecoupMixin  {
	@Shadow
	private  LivingEntity entity;


	@Inject(at = @At("HEAD"), method = "onDamage", cancellable = true)
	public  void damageRecoup(DamageSource source, float amount, CallbackInfo info) {
		LivingEntity living = (LivingEntity) entity;
		if(living instanceof RecoupLivingEntityInterface recoupLivingEntityInterface && living instanceof PlayerEntity player ){
			recoupLivingEntityInterface.addRecoupHealth(new RecoupInstances.RecoupInstanceHealth(player, 80, amount*(-1+applyAttributeModifiers(1, Sign.POSITIVE.wrap(player.getAttributeInstance(RECOUP))))));
		}

	}
	@Inject(at = @At("HEAD"), method = "onDamage", cancellable = true)
	public  void damageDissolution(DamageSource source, float amount, CallbackInfo info) {
		LivingEntity living = (LivingEntity) entity;
		if (living.getAttributeValue(DISSOLUTION) > 0 && amount > 0 && living.getWorld() instanceof ServerWorld serverWorld) {
			int i = (int) Math.max(1,(living.getAttributeValue(DISSOLUTION) * Math.max(1,amount))) -1;
			int j;
			if (living.getStatusEffect(DISSOLUTIONEFFECT) != null) {
				j = living.getStatusEffect(DISSOLUTIONEFFECT).getAmplifier() + 1;
			} else {
				j = 0;
			}
			living.addStatusEffect(new StatusEffectInstance(DISSOLUTIONEFFECT, 80, i + j, false, false));
		}

	}

}