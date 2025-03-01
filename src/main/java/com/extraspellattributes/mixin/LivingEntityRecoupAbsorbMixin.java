package com.extraspellattributes.mixin;

import com.extraspellattributes.Calculations;
import com.extraspellattributes.api.RecoupInstances;
import com.extraspellattributes.api.Sign;
import com.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.extraspellattributes.ReabsorptionInit.*;

@Mixin( LivingEntity.class)
public abstract class LivingEntityRecoupAbsorbMixin {
	@Shadow(prefix="fooRPG$")
	protected abstract float fooRPG$applyArmorToDamage(DamageSource source, float amount) ;

	@Inject(at = @At("HEAD"), method = "damage", cancellable = true)
	private void damageHeadRecoupAbsorb( DamageSource source, float amount, CallbackInfoReturnable<Boolean> info){
		LivingEntity living = (LivingEntity) (Object) this;
		if( living instanceof RecoupLivingEntityInterface recoupLivingEntityInterface && living instanceof PlayerEntity player && Calculations.recoup_reabsorb(player) > 1F){
			recoupLivingEntityInterface.addRecoupAbsorption(new RecoupInstances.RecoupInstanceAbsorption(player, 80, (amount-(double)DamageUtil.getDamageLeft(player,(float)((amount)),source,player.getArmor(),(float)player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)))*(-1+Calculations.recoup_reabsorb(player))));
		}
	}
}