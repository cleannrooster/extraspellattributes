package com.cleannrooster.extraspellattributes.mixin;

import com.cleannrooster.extraspellattributes.api.RecoupInstances;
import com.cleannrooster.extraspellattributes.api.Sign;
import com.cleannrooster.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cleannrooster.extraspellattributes.DynamicAttribute.*;
import static com.cleannrooster.extraspellattributes.Effects.DISSOLUTIONEFFECT;

@Mixin(value = DamageTracker.class, priority = 9999)
public class LivingEntityRecoupMixin  {
	@Shadow
	private  LivingEntity entity;


	@Inject(at = @At("HEAD"), method = "onDamage", cancellable = true)
	public  void damageRecoup(DamageSource source, float amount, CallbackInfo info) {
		LivingEntity living = (LivingEntity) entity;
		if(living instanceof RecoupLivingEntityInterface recoupLivingEntityInterface && living instanceof PlayerEntity player ){
			recoupLivingEntityInterface.addRecoupHealth(new RecoupInstances.RecoupInstanceHealth(player, 80, amount*(-1+ applyAttributeModifiers(1, Sign.POSITIVE.wrap(player.getAttributeInstance(RECOUP))))));
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