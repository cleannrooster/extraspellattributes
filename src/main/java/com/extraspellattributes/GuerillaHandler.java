package com.extraspellattributes;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKeys;

import java.util.Map;
import java.util.WeakHashMap;

/** Tracks short outgoing-damage bursts for the Guerilla enchantment. */
public final class GuerillaHandler {
    private static final int WINDOW_TICKS = 20;
    private static final int INVISIBILITY_TICKS = 4 * 20;
    private static final float CHANCE_PER_LEVEL = 0.08F;
    private static final double DAMAGE_MULTIPLIER = 1.5;

    private static final Map<LivingEntity, DamageWindow> DAMAGE_WINDOWS = new WeakHashMap<>();

    public static void register() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((target, source, baseDamageTaken, damageTaken, blocked) -> {
            if (blocked || damageTaken <= 0 || !(source.getAttacker() instanceof LivingEntity attacker)
                    || attacker == target) {
                return;
            }

            var enchantmentRegistry = attacker.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
            var guerilla = enchantmentRegistry.getEntry(ReabsorptionInit.GUERILLA_ENCHANT).orElse(null);
            if (guerilla == null) {
                return;
            }

            int level = EnchantmentHelper.getEquipmentLevel(guerilla, attacker);
            if (level <= 0 || attacker.hasStatusEffect(StatusEffects.INVISIBILITY)) {
                DAMAGE_WINDOWS.remove(attacker);
                return;
            }

            long now = attacker.getWorld().getTime();
            DamageWindow window = DAMAGE_WINDOWS.get(attacker);
            if (window == null || now - window.startedAt > WINDOW_TICKS) {
                window = new DamageWindow(now, 0);
            }

            window = new DamageWindow(window.startedAt, window.damage + damageTaken);
            double threshold = attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * DAMAGE_MULTIPLIER;
            if (window.damage >= threshold) {
                DAMAGE_WINDOWS.remove(attacker);
                float chance = Math.min(1.0F, CHANCE_PER_LEVEL * level);
                if (attacker.getRandom().nextFloat() < chance) {
                    attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, INVISIBILITY_TICKS));
                }
            } else {
                DAMAGE_WINDOWS.put(attacker, window);
            }
        });
    }

    private record DamageWindow(long startedAt, float damage) {
    }

    private GuerillaHandler() {
    }
}
