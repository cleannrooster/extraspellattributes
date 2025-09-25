package com.cleannrooster.extraspellattributes.api;


import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ServiceLoader;

public interface Platform {
    Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst().orElseThrow();

    <T> RegistryEntry<T> registerReference(Registry<T> registry, Identifier id, T entry);

    <T> void registerAlias(Registry<T> registry, Identifier aliasId, Identifier id);
}