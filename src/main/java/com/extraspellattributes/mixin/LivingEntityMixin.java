package com.extraspellattributes.mixin;

import com.extraspellattributes.PlayerInterface;
import com.extraspellattributes.ReabsorptionInit;
import com.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

import static com.extraspellattributes.ReabsorptionInit.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Shadow
	private DefaultedList<ItemStack> syncedHandStacks;
	@Shadow
	private  DefaultedList<ItemStack> syncedArmorStacks;

	private ItemStack getSyncedHandStack(EquipmentSlot slot) {
		return (ItemStack)this.syncedHandStacks.get(slot.getEntitySlotId());
	}
	private ItemStack getSyncedArmorStack(EquipmentSlot slot) {
		return (ItemStack)this.syncedArmorStacks.get(slot.getEntitySlotId());
	}
	@ModifyVariable(at = @At("HEAD"), method = "damage", argsOnly = true)
	private float damageHeadReab(float amount, DamageSource source, float originalAmount){
		LivingEntity living = (LivingEntity) (Object) this;
		amount = originalAmount;
		if(living.getAttributeInstance(GLANCINGBLOW) != null && source.getAttacker() != null){
			double glancingchance = 0.01*(living.getAttributeValue(GLANCINGBLOW)-100);
			if (living.getRandom().nextFloat() < glancingchance) {
				amount *= 0.65;
			}


		}
		Registry<DamageType> registry = ((DamageSourcesAccessor)living.getDamageSources()).getRegistry();

		if(living.getAttributeInstance(SPELLSUPPRESS) != null && source.getType().equals(registry.entryOf(DamageTypes.MAGIC).value()) || source.getType().equals(registry.entryOf(DamageTypes.INDIRECT_MAGIC).value())){
			double suppresschance = 0.01*(living.getAttributeValue(SPELLSUPPRESS)-100);

			if(living.getRandom().nextFloat() < suppresschance){
				amount *= 0.5;
				double acro = 0.01 * (living.getAttributeValue(ACRO) - 100);
				if (living.getRandom().nextFloat() <  acro) {
					amount *= 0;
				}
			}
		}
		if(living.getAttributeInstance(DEFIANCE) != null && amount > 1) {

			amount -= (float) Math.pow(living.getAttributeValue(DEFIANCE),0.5);
			amount = Math.max(1,amount);
		}
		return amount;
	}
	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	public void tick_absorption_HEAD(CallbackInfo info) {
		LivingEntity living = (LivingEntity) (Object) this;
		if(living instanceof RecoupLivingEntityInterface recoupLivingEntityInterface && living instanceof PlayerEntity player && !player.getWorld().isClient()){
			recoupLivingEntityInterface.tickRecoups();
		}
		double maximum = living.getAttributeValue(WARDING);

		if (living instanceof PlayerInterface damageInterface && maximum > 0) {

				float additional = (float)maximum*0.25F*0.05F*config.factor;

				if(living.age - damageInterface.getReabLasthurt() >= config.delay *20){

					if(living.getAbsorptionAmount() < maximum) {
						if (!living.getWorld().isClient()) {
							living.setAbsorptionAmount((float) Math.min(living.getAbsorptionAmount() + additional, maximum));
						}
					}
				}

		}
	}
	@ModifyReturnValue(at = @At("TAIL"), method = "getMaxAbsorption")
	public float getMaxReabsorption(float value) {
		LivingEntity living = (LivingEntity) (Object) this;
		double maximum = living.getAttributeValue(WARDING);


		return (float) (value+maximum);
	}
	@Unique
	private static final ThreadLocal<Boolean> PROCESSING = ThreadLocal.withInitial(() -> false);


/*	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/LivingEntity;sendEquipmentChanges(Ljava/util/Map;)V", cancellable = true)
	private void sendEquipmentChanges(Map<EquipmentSlot, ItemStack> equipmentChanges, CallbackInfo callbackInfo) {
		LivingEntity living = (LivingEntity) (Object) this;
		Map<EquipmentSlot, ItemStack> map = null;
		EquipmentSlot[] var2 = EquipmentSlot.values();
		int var3 = var2.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			EquipmentSlot equipmentSlot = var2[var4];
			ItemStack itemStack;
			if(equipmentSlot.getType().equals(EquipmentSlot.Type.HUMANOID_ARMOR)) {
				itemStack = this.getSyncedArmorStack(equipmentSlot);

				ItemStack itemStack2 = living.getEquippedStack(equipmentSlot);
				if (living.areItemsDifferent(itemStack, itemStack2)) {
					float toremove = 0;
					AttributeModifiersComponent attributeModifiersComponent = (AttributeModifiersComponent)itemStack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
					AttributeModifiersComponent attributeModifiersComponent2 = (AttributeModifiersComponent)itemStack2.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);

					for (AttributeModifiersComponent.Entry modifier : attributeModifiersComponent.modifiers()) {
						if(modifier.matches(RegistryEntry.of(WARDING),Identifier.of(MOD_ID,"warding")))
						if (modifier.modifier().operation().equals(EntityAttributeModifier.Operation.ADD_VALUE)) {
							toremove -= modifier.modifier().value();
						}
					}
					for (AttributeModifiersComponent.Entry modifier : attributeModifiersComponent2.modifiers()) {
						if(modifier.matches(RegistryEntry.of(WARDING),Identifier.of(MOD_ID,"warding")))
							if (modifier.modifier().operation().equals(EntityAttributeModifier.Operation.ADD_VALUE)) {
								toremove -= modifier.modifier().value();
							}
					}
					Collection<EntityAttributeModifier> modifiers3 = living.getAttributeInstance(RegistryEntry.of(WARDING)).getModifiers();
					Collection<EntityAttributeModifier> modifiers4 = living.getAttributeInstance(RegistryEntry.of(WARDING)).getModifiers();

					float mult = 1;
					for (EntityAttributeModifier modifier : modifiers3) {
						mult += modifier.value();
					}
					toremove *= mult;
					for (EntityAttributeModifier modifier : modifiers4) {
						toremove *= 1+modifier.getValue();
					}
					if (toremove < 0 && !living.getWorld().isClient()) {
						living.setAbsorptionAmount(living.getAbsorptionAmount() + toremove);
						if (living instanceof PlayerInterface playerDamageInterface) {
							playerDamageInterface.resetReabDamageAbsorbed();
						}
					}

				}
			}
		}
	}*/
	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void addAttributesextraspellattributes_RETURN(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
		info.getReturnValue().add(WARDING);
		info.getReturnValue().add(CONVERTFROMFIRE);
		info.getReturnValue().add(CONVERTFROMFROST);
		info.getReturnValue().add(CONVERTFROMARCANE);
		info.getReturnValue().add(CONVERTTOFIRE);
		info.getReturnValue().add(CONVERTTOFROST);
		info.getReturnValue().add(CONVERTTOARCANE);
		info.getReturnValue().add(CONVERTTOHEAL);
		info.getReturnValue().add(GLANCINGBLOW);
		info.getReturnValue().add(SPELLSUPPRESS);
		info.getReturnValue().add(ACRO);
		info.getReturnValue().add(DEFIANCE);
		info.getReturnValue().add(RECOUP);
	}
}