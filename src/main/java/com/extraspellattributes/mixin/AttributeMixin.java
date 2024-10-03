package com.extraspellattributes.mixin;

import net.minecraft.entity.attribute.*;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Map;

import static com.extraspellattributes.ReabsorptionInit.*;

@Mixin(value = AttributeContainer.class)
public class AttributeMixin {
    /*
    @Shadow
    private Map<EntityAttribute, EntityAttributeInstance> custom;
    @Shadow
    private DefaultAttributeContainer fallback;

    @Inject(at = @At("HEAD"), method = "getValue", cancellable = true)
    private void getAttributeValueCONVERTTO(EntityAttribute attribute, CallbackInfoReturnable<Double> info) {
        /*if(attribute == (SpellSchools.ARCANE).attribute){
            AttributeContainer container = (AttributeContainer) (Object) this;
            EntityAttributeInstance percentinstance3 = (EntityAttributeInstance)this.custom.get(CONVERTTOARCANE);
            double value3 = 100;
            if(percentinstance3 != null) {
                value3 = percentinstance3.getValue();
            }
            double percent3 = (double) (0.01*(value3-100));
            double added = 0;
            double added2 = 0;
            double added3 = 0;

            if (this.custom.get(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
                EntityAttributeInstance addedinstance3 = (EntityAttributeInstance) this.custom.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                if (percentinstance3 != null) {
                    added3 = addedinstance3.getValue();
                }
                EntityAttributeInstance damageinstance = (EntityAttributeInstance) this.custom.get((SpellSchools.ARCANE).attribute);
                double total = this.fallback.getValue((SpellSchools.ARCANE).attribute);

                if (damageinstance != null) {
                    total = this.computeValue(damageinstance, (SpellSchools.ARCANE).attribute);
                    if (percent3 > 0 && attribute == (SpellSchools.ARCANE).attribute) {
                        total += this.computeAdded(percent3 * added3, damageinstance, (SpellSchools.ARCANE).attribute);
                    }
                }

                if (percent3 > 0) {
                    info.setReturnValue(total);
                }
            }
        }
        if(attribute == SpellSchools.FIRE.attribute){
            AttributeContainer container = (AttributeContainer) (Object) this;
            EntityAttributeInstance percentinstance3 = (EntityAttributeInstance)this.custom.get(CONVERTTOFIRE);
            double value3 = 100;
            if(percentinstance3 != null) {
                value3 = percentinstance3.getValue();
            }
            double percent3 = (double) (0.01*(value3-100));
            double added = 0;
            double added2 = 0;
            double added3 = 0;

            if (this.custom.get(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
                EntityAttributeInstance addedinstance3 = (EntityAttributeInstance) this.custom.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                if (percentinstance3 != null) {
                    added3 = addedinstance3.getValue();
                }
                EntityAttributeInstance damageinstance = (EntityAttributeInstance) this.custom.get(SpellSchools.FIRE.attribute);
                double total = this.fallback.getValue(SpellSchools.FIRE.attribute);

                if (damageinstance != null) {
                    total = this.computeValue(damageinstance, SpellSchools.FIRE.attribute);
                    if (percent3 > 0 && attribute == SpellSchools.FIRE.attribute) {
                        total += this.computeAdded(percent3 * added3, damageinstance, SpellSchools.FIRE.attribute);
                    }
                }

                if (percent3 > 0) {
                    info.setReturnValue(total);
                }
            }
        }
        if(attribute == SpellSchools.FROST.attribute){
            AttributeContainer container = (AttributeContainer) (Object) this;
            EntityAttributeInstance percentinstance3 = (EntityAttributeInstance)this.custom.get(CONVERTTOFROST);
            double value3 = 100;
            if(percentinstance3 != null) {
                value3 = percentinstance3.getValue();
            }
            double percent3 = (double) (0.01*(value3-100));
            double added = 0;
            double added2 = 0;
            double added3 = 0;

            if (this.custom.get(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
                EntityAttributeInstance addedinstance3 = (EntityAttributeInstance)this.custom.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                if(percentinstance3  != null) {
                    added3 = addedinstance3.getValue();
                }

                EntityAttributeInstance damageinstance = (EntityAttributeInstance) this.custom.get(SpellSchools.FROST.attribute);
                double total = this.fallback.getValue(SpellSchools.FROST.attribute);
                if (damageinstance != null) {
                    total = this.computeValue(damageinstance, SpellSchools.FROST.attribute);
                    if (percent3 > 0 && attribute == SpellSchools.FROST.attribute) {
                        total += this.computeAdded(percent3 * added3, damageinstance, SpellSchools.FROST.attribute);
                    }
                }

                if (percent3 > 0) {
                    info.setReturnValue(total);
                }
            }
        }
        if(attribute == SpellSchools.HEALING.attribute){
            AttributeContainer container = (AttributeContainer) (Object) this;
            EntityAttributeInstance percentinstance3 = (EntityAttributeInstance)this.custom.get(CONVERTTOHEAL);
            double value3 = 100;
            if(percentinstance3 != null) {
                value3 = percentinstance3.getValue();
            }
            double percent3 = (double) (0.01*(value3-100));
            double added = 0;
            double added2 = 0;
            double added3 = 0;
            if (this.custom.get(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
                EntityAttributeInstance addedinstance3 = (EntityAttributeInstance) this.custom.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                if (percentinstance3 != null) {
                    added3 = addedinstance3.getValue();
                }
                EntityAttributeInstance damageinstance = (EntityAttributeInstance) this.custom.get(SpellSchools.HEALING.attribute);
                double total = this.fallback.getValue(SpellSchools.HEALING.attribute);
                if (damageinstance != null) {
                    total = this.computeValue(damageinstance, SpellSchools.HEALING.attribute);
                    if (percent3 > 0 && attribute == SpellSchools.HEALING.attribute) {
                        total += this.computeAdded(percent3 * added3, damageinstance, SpellSchools.HEALING.attribute);
                    }
                }

                if (percent3 > 0) {
                    info.setReturnValue(total);
                }
            }
        }*/


}
