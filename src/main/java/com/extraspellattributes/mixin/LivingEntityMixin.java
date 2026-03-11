package com.extraspellattributes.mixin;

import com.extraspellattributes.Calculations;
import com.extraspellattributes.PlayerInterface;
import com.extraspellattributes.api.Sign;
import com.extraspellattributes.api.SneakAttackable;
import com.extraspellattributes.interfaces.RecoupLivingEntityInterface;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.entity.SmokerBlockEntity;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.internals.container.SpellContainerSource;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_engine.utils.WorldScheduler;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchool;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;

import static com.extraspellattributes.ReabsorptionInit.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements SneakAttackable {

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
	@Inject(at = @At("TAIL"), method = "applyDamage",cancellable = true)
	private void damageDissolution( DamageSource source, float originalAmount,CallbackInfo ci){


	}
    private Entity prevTarget;
	private Entity newTarget;
    private boolean changedTarget;

    private long brittleTime = 0;

    @ModifyVariable(at = @At("HEAD"), method = "damage", argsOnly = true)
	private float damageHeadReab(float amount, DamageSource source, float originalAmount){
		LivingEntity living = (LivingEntity) (Object) this;
		amount = originalAmount;
		if (!living.isInvulnerableTo(source)) {
            prevTarget = source.getAttacker();
			if (applyAttributeModifiers(1, Sign.POSITIVE.wrap(living.getAttributeInstance(BLUR))) - 1 > 0) {
				amount *= (float) (Calculations.blur(living));
				if (Calculations.blur(living) - 1 < living.getRandom().nextFloat()) {
					amount = 0;
				}
			}
			if (living.age - living.getLastAttackedTime() > 80 || living.age - brittleTime < 80) {
				if (living.getAttributeValue(BRITTLE) > 100) {
					amount *= (float) Calculations.brittlenegative(living);
				}
				if(living.age - living.getLastAttackedTime() > 80){
					this.brittleTime = living.age;
				}
			} else {
				if (living.getAttributeValue(BRITTLE) > 100) {
					amount *= (float) (Calculations.brittle(living));

				}
			}
			if (living.getAttributeInstance(GLANCINGBLOW) != null && source.getAttacker() != null) {
				double glancingchance = Calculations.glancingBlow(living) - 1;
				if (living.getRandom().nextFloat() < glancingchance) {
					amount *= 0.65F;
				}


			}

			Registry<DamageType> registry = ((DamageSourcesAccessor) living.getDamageSources()).getRegistry();

			if (living.getAttributeInstance(SPELLSUPPRESS) != null &&  source.getTypeRegistryEntry().isIn(TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("c", "is_magic")))) {
				double suppresschance = Calculations.spellSuppress(living) - 1;

				if (living.getRandom().nextFloat() < suppresschance) {
					amount *= 0.5F;
					double acro = Calculations.spellbreak(living) - 1;
					if (living.getRandom().nextFloat() < acro) {
						amount *= 0;
					}
				}
			}
			if(Calculations.fortitude(living) > living.getMaxHealth()/5F ||
					Calculations.endurance(living) < 10F/(15F) ){
				float damageAbove = (float) Math.max(0,living.getAbsorptionAmount() + living.getHealth() - Calculations.fortitude(living));
				if(amount > damageAbove) {
					float damageBelow = (float) ((amount - damageAbove) * Calculations.endurance(living));
					if (damageBelow > 0) {
						amount = damageAbove + damageBelow;
					}
				}
			}


		}

		return amount;
	}
	@Inject(at = @At("RETURN"), method = "getHealth", cancellable = true)
	public void getHealthDissolution(CallbackInfoReturnable<Float> cir) {

		LivingEntity living = (LivingEntity) (Object) this;
		if (living.getAttributeValue(DISSOLUTION) > 0 && living.hasStatusEffect(DISSOLUTIONEFFECT) ) {
			if(cir.getReturnValue() != null && cir.getReturnValue() > 0.0F) {
				cir.setReturnValue(living.getMaxHealth());
			}
			else{
				cir.setReturnValue(0F);
			}

		}
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
		if(Calculations.reabsorbarmormax(living) > 0){
			return (float) Math.min(value+maximum,(float) (living.getAttributeValue(REABSORBARMORMAX)*living.getArmor()));
		}

		return (float) (value+maximum);
	}
	@ModifyReturnValue(at = @At("TAIL"), method = "applyArmorToDamage")

	protected float applyArmorToDamageReab(float value, DamageSource source, float amount) {
		LivingEntity living = (LivingEntity) (Object) this;
		double imbalanced = Calculations.imbalanced(living);
		double magebane = Calculations.magebane(living);

		if(magebane > 1){

			if(source.getTypeRegistryEntry().getIdAsString().contains("spell_power")){
			living.damageArmor(source, value);
				value = DamageUtil.getDamageLeft(living, value, source, (float)living.getArmor(), (float)living.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
			}
			value *= 1.2F;
		}
		if(imbalanced > 1){
			if (!source.isIn(DamageTypeTags.BYPASSES_ARMOR)) {
				living.damageArmor(source, amount);
				value *= 3;
				value = DamageUtil.getDamageLeft(living, value, source, (float) living.getArmor(), (float) living.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
			}
		}
		if (!source.isIn(DamageTypeTags.BYPASSES_ARMOR)) {

			if (living.getAttributeInstance(DEFIANCE) != null && amount > 1) {

				value -= (float) Math.pow(Calculations.defiance(living), 0.5);
			}
		}
		return value;
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
		info.getReturnValue().add(RECOUPABSORB);
		info.getReturnValue().add(REABSORBARMORMAX);
		info.getReturnValue().add(IMBALANCEDGUARD);
		info.getReturnValue().add(MAGEBANE);
		info.getReturnValue().add(BLUR);
		info.getReturnValue().add(BRITTLE);
		info.getReturnValue().add(CULL);
		info.getReturnValue().add(PHYSIQUE);
		info.getReturnValue().add(FINESSE);
		info.getReturnValue().add(ATTUNEMENT);
		info.getReturnValue().add(ENDURANCE);
		info.getReturnValue().add(FORTITUDE);
		info.getReturnValue().add(DISSOLUTION);
        info.getReturnValue().add(VULNCRIT);
        info.getReturnValue().add(VULNDAMAGE);
        info.getReturnValue().add(VULNARMOR);
        info.getReturnValue().add(VULNCRITDAMAGE);
        info.getReturnValue().add(VULNERABILITY);

	}
    public boolean isVulnerable(LivingEntity mob) {
        return mob.hasStatusEffect(VULN);
    }
    private boolean doSneakAttack(LivingEntity entity,  boolean should, SpellSchool school) {
        if(!should) return false;
        if (!((Object)this instanceof LivingEntity living)) return false;
        var spellOptional = school.equals(ExternalSpellSchools.PHYSICAL_RANGED) ? SpellRegistry.from(living.getWorld()).getEntry(Identifier.of(MOD_ID,"gouge_ranged")) :  SpellRegistry.from(living.getWorld()).getEntry(Identifier.of(MOD_ID,"gouge"));
        if(spellOptional.isEmpty()) return false;
        var spellRef = spellOptional.get();
        if(!(entity instanceof PlayerEntity player)) return false;
        var caster = (SpellCasterEntity) player;
        if (caster.getCooldownManager().isCoolingDown(spellRef)) return false;
        var source = school.equals(ExternalSpellSchools.PHYSICAL_RANGED) ? SpellContainerSource.getFirstSourceOfSpell(Identifier.of(MOD_ID,"try_gouge_ranged"),player) :  SpellContainerSource.getFirstSourceOfSpell(Identifier.of(MOD_ID,"try_gouge"),player);
        var newSource = new SpellContainerSource.SourcedContainer("source_vulnerability",null,source.container());
        SpellHelper.imposeCooldown(player,newSource ,spellRef,1.0F);
        return spellOptional.filter(spellReference -> SpellHelper.performImpacts(living.getWorld(), entity, living, living, spellReference, spellReference.value().impacts,
                new SpellHelper.ImpactContext(1.0F, 1.0F, living.getPos(), SpellPower.getSpellPower(school, entity), SpellTarget.FocusMode.DIRECT, 0))).isPresent();
    }
    @Override
    public boolean processSneakAttack(LivingEntity attacker, SpellSchool school) {
        return doSneakAttack(attacker,shouldSneakAttack(attacker), school);
    }
    private boolean shouldSneakAttack(Entity attacker) {
        if (!((Object)this instanceof LivingEntity living)) return false;
        if (!(living instanceof MobEntity mob)) return false;
        if (Objects.isNull(attacker)) return false;
        if(isVulnerable(mob)) return true;
        if (mob instanceof HostileEntity) {
            return Objects.isNull( mob.getTarget()) || !Objects.equals(mob.getTarget(), attacker);
        }
        if (mob instanceof PassiveEntity passive) {
            if (passive.isPanicking()) return true;
            return !Objects.isNull( mob.getTarget())
                    && !Objects.equals(mob.getTarget(), attacker);
        }
        return false;
    }
}