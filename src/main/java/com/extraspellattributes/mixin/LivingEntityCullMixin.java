package com.extraspellattributes.mixin;

import com.extraspellattributes.Calculations;
import com.extraspellattributes.api.RecoupInstances;
import com.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.extraspellattributes.ReabsorptionInit.CULL;
import static com.extraspellattributes.ReabsorptionInit.RECOUPABSORB;

@Mixin(value = LivingEntity.class, priority = 0)
public abstract class LivingEntityCullMixin {
	@Shadow(prefix="fooRPG$")
	protected abstract float fooRPG$applyArmorToDamage(DamageSource source, float amount) ;

	@Inject(at = @At("TAIL"), method = "damage", cancellable = true)
	private void damageHeadCull( DamageSource source, float amount, CallbackInfoReturnable<Boolean> info){
		LivingEntity living = (LivingEntity) (Object) this;
		Entity attacker = source.getAttacker();
		if(attacker instanceof LivingEntity livingAttacker ){

			if(Calculations.cull(livingAttacker)-1 > living.getHealth()/living.getMaxHealth()){
				living.damage(attacker.getDamageSources().create(DamageTypes.GENERIC_KILL,livingAttacker),99999);
			}


		}
		if(attacker != null) {
			if (Calculations.cull(living)-1 > living.getHealth() / living.getMaxHealth()) {
				living.damage(attacker.getDamageSources().create(DamageTypes.GENERIC_KILL, attacker), 99999);
			}
		}

	}
}