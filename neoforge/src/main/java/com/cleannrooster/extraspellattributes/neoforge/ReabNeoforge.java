package com.cleannrooster.extraspellattributes.neoforge;

import com.cleannrooster.extraspellattributes.ReabsorptionInit;
import com.cleannrooster.extraspellattributes.items.ItemInit;
import com.cleannrooster.extraspellattributes.neoforge.compat.CompatFeatures;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import com.cleannrooster.extraspellattributes.ExampleMod;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.runes.neoforge.NeoForgeMod;

import static com.cleannrooster.extraspellattributes.ReabsorptionInit.initAttr;
import static com.cleannrooster.extraspellattributes.ReabsorptionInit.initEffects;

@Mod(ExampleMod.MOD_ID)
public final class ReabNeoforge {
    public ReabNeoforge(IEventBus modBus) {
        ReabsorptionInit.onInitialize();


        modBus.addListener(RegisterEvent.class, ReabNeoforge::register);

    }

    public static void register(RegisterEvent event) {


        event.register(RegistryKeys.ITEM, reg -> {
            CompatFeatures.init();

            ItemInit.register();

        });
        event.register(RegistryKeys.STATUS_EFFECT, reg ->{
            initEffects();

        });
        event.register(RegistryKeys.ATTRIBUTE, reg ->{
            initAttr();

        });


    }

}
