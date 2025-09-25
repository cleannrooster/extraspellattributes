package com.cleannrooster.extraspellattributes.neoforge;

import com.cleannrooster.extraspellattributes.ExampleMod;
import com.cleannrooster.extraspellattributes.ReabsorptionInit;
import com.cleannrooster.extraspellattributes.items.ItemInit;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import static com.cleannrooster.extraspellattributes.ExampleMod.MOD_ID;
import static com.cleannrooster.extraspellattributes.ReabsorptionInit.initAttr;
import static com.cleannrooster.extraspellattributes.ReabsorptionInit.initEffects;

@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public final class ReabNeoforgeClient {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            //initAttr();
        }

}
