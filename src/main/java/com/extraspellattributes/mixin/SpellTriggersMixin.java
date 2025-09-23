package com.extraspellattributes.mixin;

import com.extraspellattributes.ReabsorptionInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellTriggers;
import net.spell_engine.utils.WorldScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpellTriggers.class)
public class SpellTriggersMixin {
    @Inject(method = "onSpellImpactSpecific", at = @At("HEAD"))
    private static void onSpellImpactSpecificReab(PlayerEntity player, Entity target, RegistryEntry<Spell> spell, Spell.Impact impact, boolean critical, Spell.Trigger.Stage stage, CallbackInfo ci) {
        if(critical){
            if(player.hasStatusEffect(ReabsorptionInit.INEVITABILITYEFFECT)) {

                ((WorldScheduler) player.getWorld()).schedule(1, () -> {
                    if (player.hasStatusEffect(ReabsorptionInit.INEVITABILITYEFFECT)) {
                        player.removeStatusEffect(ReabsorptionInit.INEVITABILITYEFFECT);
                    }

                });
            }
        }
    }
}