package com.cleannrooster.extraspellattributes.mixin;


import com.cleannrooster.extraspellattributes.PlayerInterface;
import com.cleannrooster.extraspellattributes.api.RecoupInstances;
import com.cleannrooster.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

@Mixin(PlayerEntity.class)
public class PlayerMixin implements PlayerInterface, RecoupLivingEntityInterface {
    public Entity lastReabAttacked;
    public int lastReabhurt = 0;
    public float damageReabAbsorbed;
    public boolean reabsorbing = false;
    public List<RecoupInstances.RecoupInstanceHealth> recoupInstancesHealth = new ArrayList<RecoupInstances.RecoupInstanceHealth>(List.of());
    public List<RecoupInstances.RecoupInstanceAbsorption> recoupInstancesAbsorption = new ArrayList<RecoupInstances.RecoupInstanceAbsorption>(List.of());

    public int getReabLasthurt() {
        return lastReabhurt;
    }

    @Override
    public float getReabDamageAbsorbed() {
        return damageReabAbsorbed;
    }

    @Override
    public boolean getReabsorbing() {
        return reabsorbing;
    }

    @Override
    public void resetReabDamageAbsorbed() {
        damageReabAbsorbed = 0;
        this.lastReabhurt = ((PlayerEntity) (Object) this).age;
    }

    @Override
    public void ReababsorbDamage(float i) {
        damageReabAbsorbed = damageReabAbsorbed + i;
    }

    @Override
    public void setReabsorbing(boolean set) {
        reabsorbing = set;
    }

    public void setReabLasthurt(int lasthurt) {
        this.lastReabhurt = lasthurt;

    }



    @Inject(at = @At("HEAD"), method = "applyDamage", cancellable = true)
    protected void applyDamageMixinSpellblade(DamageSource source, float amount, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!player.isInvulnerableTo(source) && amount > 0) {
            this.setReabLasthurt(player.age);

            this.resetReabDamageAbsorbed();
        }

    }
    @Inject(at = @At("TAIL"), method = "applyDamage", cancellable = true)
    protected void applyDamageMixinSpellbladeTAIL(DamageSource source, float amount, CallbackInfo info) {

    }

    @Override
    public List<RecoupInstances.RecoupInstanceHealth> getRecoupsHealth() {
        return this.recoupInstancesHealth;
    }

    @Override
    public List<RecoupInstances.RecoupInstanceAbsorption> getRecoupsAbsorption() {
        return recoupInstancesAbsorption;
    }


    @Override
    public void tickRecoups() {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if(player.age % 10 == 0) {

            double toRecover = 0;
            for (RecoupInstances.RecoupInstanceHealth instance : this.recoupInstancesHealth) {
                if(instance.remainingduration > 0) {
                    double remainingPayments = Math.ceil(instance.remainingduration / 10.0);
                    double payment = instance.remainingvalue / remainingPayments;
                    toRecover += payment;
                    instance.remainingvalue -= payment;
                }
                instance.remainingduration -= 10;
            }
            player.heal(Math.max(0,(float) toRecover));
            double toRecoverAbs = 0;
            for (RecoupInstances.RecoupInstanceAbsorption instance : this.recoupInstancesAbsorption) {
                if(instance.remainingduration > 0) {
                    double remainingPayments = Math.ceil(instance.remainingduration / 10.0);
                    double payment = instance.remainingvalue / remainingPayments;
                    toRecoverAbs += payment;
                    instance.remainingvalue -= payment;
                }
                instance.remainingduration -= 10;
            }
            player.setAbsorptionAmount((float) (player.getAbsorptionAmount()+Math.max(0,toRecoverAbs)));
            if (!this.recoupInstancesHealth.isEmpty()) {
                this.recoupInstancesHealth.removeIf(recoupInstance -> recoupInstance.remainingduration <= 0);
            }
            if (!this.recoupInstancesAbsorption.isEmpty()) {
                this.recoupInstancesAbsorption.removeIf(recoupInstance -> recoupInstance.remainingduration <= 0);
            }

        }
    }

    @Override
    public void addRecoupHealth(RecoupInstances.RecoupInstanceHealth instance) {
        this.recoupInstancesHealth.add(instance);
    }

    @Override
    public void addRecoupAbsorption(RecoupInstances.RecoupInstanceAbsorption instance) {
        this.recoupInstancesAbsorption.add(instance);
    }
}