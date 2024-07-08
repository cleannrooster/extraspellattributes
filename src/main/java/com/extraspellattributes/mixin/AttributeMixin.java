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
    @Shadow
    private Map<EntityAttribute, EntityAttributeInstance> custom;
    @Shadow
    private DefaultAttributeContainer fallback;
    private double computeAdded(double dub, EntityAttributeInstance instance, EntityAttribute attribute) {
        double d = dub;



        double e = d;

        Iterator var5;
        EntityAttributeModifier entityAttributeModifier2;
        for(var5 = instance.getModifiers(EntityAttributeModifier.Operation.MULTIPLY_BASE).iterator(); var5.hasNext(); e += d * entityAttributeModifier2.getValue()) {
            entityAttributeModifier2 = (EntityAttributeModifier)var5.next();
        }

        for(var5 = instance.getModifiers(EntityAttributeModifier.Operation.MULTIPLY_TOTAL).iterator(); var5.hasNext(); e *= 1.0D + entityAttributeModifier2.getValue()) {
            entityAttributeModifier2 = (EntityAttributeModifier)var5.next();
        }

        return attribute.clamp(e);
    }
    private double computeValue(EntityAttributeInstance instance, EntityAttribute attribute) {
        double d = instance.getBaseValue();

        EntityAttributeModifier entityAttributeModifier;
        for(Iterator var3 = instance.getModifiers(EntityAttributeModifier.Operation.ADDITION).iterator(); var3.hasNext(); d += entityAttributeModifier.getValue()) {
            entityAttributeModifier = (EntityAttributeModifier)var3.next();
        }

        double e = d;

        Iterator var5;
        EntityAttributeModifier entityAttributeModifier2;
        for(var5 = instance.getModifiers(EntityAttributeModifier.Operation.MULTIPLY_BASE).iterator(); var5.hasNext(); e += d * entityAttributeModifier2.getValue()) {
            entityAttributeModifier2 = (EntityAttributeModifier)var5.next();
        }

        for(var5 = instance.getModifiers(EntityAttributeModifier.Operation.MULTIPLY_TOTAL).iterator(); var5.hasNext(); e *= 1.0D + entityAttributeModifier2.getValue()) {
            entityAttributeModifier2 = (EntityAttributeModifier)var5.next();
        }

        return attribute.clamp(e);
    }
    @Inject(at = @At("HEAD"), method = "getValue", cancellable = true)
    private void getAttributeValueCONVERTFROM(EntityAttribute attribute, CallbackInfoReturnable<Double> info) {
        if(attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
            AttributeContainer container = (AttributeContainer) (Object) this;

            EntityAttributeInstance percentinstance1 = (EntityAttributeInstance) this.custom.get(CONVERTFROMFIRE);
            EntityAttributeInstance percentinstance2 = (EntityAttributeInstance) this.custom.get(CONVERTFROMFROST);
            EntityAttributeInstance percentinstance3 = (EntityAttributeInstance) this.custom.get(CONVERTFROMARCANE);
            double value1 = 100;
            double value2 = 100;
            double value3 = 100;
            if (percentinstance1 != null) {
                value1 = percentinstance1.getValue();
            }
            if (percentinstance2 != null) {
                value2 = percentinstance2.getValue();
            }
            if (percentinstance3 != null) {
                value3 = percentinstance3.getValue();
            }
            double percent1 = (double) (0.01 * (value1 - 100));
            double percent2 = (double) (0.01 * (value2 - 100));
            double percent3 = (double) (0.01 * (value3 - 100));
            double added = 0;
            double added2 = 0;
            double added3 = 0;
            EntityAttributeInstance addedinstance1 = (EntityAttributeInstance) this.custom.get(SpellSchools.FIRE.attribute);

            EntityAttributeInstance addedinstance2 = (EntityAttributeInstance) this.custom.get(SpellSchools.FROST.attribute);
            EntityAttributeInstance addedinstance3 = (EntityAttributeInstance) this.custom.get(SpellSchools.ARCANE.attribute);
                if (percentinstance1 != null && addedinstance1 != null) {
                    added = addedinstance1.getValue();
                }
                if (percentinstance2 != null && addedinstance2 != null) {
                    added2 = addedinstance2.getValue();
                }
                if (percentinstance3 != null && addedinstance3 != null) {
                    added3 = addedinstance3.getValue();
                }

                EntityAttributeInstance damageinstance = (EntityAttributeInstance) this.custom.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                double total = this.fallback.getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);

                if (damageinstance != null) {
                    total = this.computeValue(damageinstance, EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    if (percent1 > 0 && attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
                        total += this.computeAdded(percent1 * added, damageinstance, EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    }
                    if (percent2 > 0 && attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
                        total += this.computeAdded(percent2 * added2, damageinstance, EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    }
                    if (percent3 > 0 && attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
                        total += this.computeAdded(percent3 * added3, damageinstance, EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    }
                }

                if (percent1 + percent2 + percent3 > 0) {
                    info.setReturnValue(total);
                }

        }
    }
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

}
