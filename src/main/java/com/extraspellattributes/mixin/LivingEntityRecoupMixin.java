package com.extraspellattributes.mixin;

import com.extraspellattributes.PlayerInterface;
import com.extraspellattributes.api.RecoupInstance;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
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
public class LivingEntityRecoupMixin {
	@Shadow
	private  LivingEntity entity;

	@Inject(at = @At("HEAD"), method = "onDamage", cancellable = true)
	public  void damageRecoup(DamageSource source, float amount, CallbackInfo info) {
		LivingEntity living = (LivingEntity) entity;
		if(living instanceof RecoupLivingEntityInterface recoupLivingEntityInterface && living instanceof PlayerEntity player && player.getAttributeValue(RECOUP) > 100){
			recoupLivingEntityInterface.addRecoup(new RecoupInstance(player, 80, amount*0.01*(player.getAttributeValue(RECOUP)-100)));
		}
	}

}