package com.cleannrooster.extraspellattributes.fabric;

import com.cleannrooster.extraspellattributes.ReabsorptionInit;
import com.cleannrooster.extraspellattributes.items.ItemInit;
import net.fabricmc.api.ModInitializer;

import com.cleannrooster.extraspellattributes.ExampleMod;

import static com.cleannrooster.extraspellattributes.ReabsorptionInit.initAttr;
import static com.cleannrooster.extraspellattributes.ReabsorptionInit.initEffects;

public final class ExtraSpellFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ReabsorptionInit.onInitialize();
        initAttr();
        initEffects();
        ItemInit.register();
        // Run our common setup.
        ExampleMod.init();
    }
}
