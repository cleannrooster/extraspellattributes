package com.extraspellattributes.mixin;


import com.extraspellattributes.PlayerInterface;
import com.extraspellattributes.ReabsorptionInit;
import com.extraspellattributes.api.RecoupInstance;
import com.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.max;
import static net.minecraft.util.math.MathHelper.sqrt;

@Mixin(PlayerEntity.class)
public class PlayerMixin implements PlayerInterface, RecoupLivingEntityInterface {
    public Entity lastReabAttacked;
    public int lastReabhurt = 0;
    public float damageReabAbsorbed;
    public boolean reabsorbing = false;
    public List<RecoupInstance> recoupInstances = new ArrayList<RecoupInstance>(List.of());

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


    @Override
    public List<RecoupInstance> getRecoups() {
        return this.recoupInstances;
    }


    @Override
    public void tickRecoups() {
        for(RecoupInstance instance : this.recoupInstances){
            instance.tick();
        }
        if(!this.recoupInstances.isEmpty()) {
            this.recoupInstances.removeIf(recoupInstance -> recoupInstance.remainingduration <= 0);
        }
    }

    @Override
    public void addRecoup(RecoupInstance instance) {
        this.recoupInstances.add(instance);
    }
}