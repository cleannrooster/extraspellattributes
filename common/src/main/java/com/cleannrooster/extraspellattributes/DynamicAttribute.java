package com.cleannrooster.extraspellattributes;


import com.cleannrooster.extraspellattributes.api.Signed;
import com.cleannrooster.extraspellattributes.mixin.EntityAttributeInstanceInvoker;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;

public class DynamicAttribute extends EntityAttribute {
    public DynamicAttribute(String translationKey) {
        super(translationKey, Double.NaN);
    }
    public static RegistryEntry<EntityAttribute> WARDING ;
    public static RegistryEntry<EntityAttribute> CONVERTFROMFIRE ;
    public static RegistryEntry<EntityAttribute> CONVERTFROMFROST;
    public static RegistryEntry<EntityAttribute> CONVERTFROMARCANE ;
    public static RegistryEntry<EntityAttribute> CONVERTTOFIRE ;
    public static RegistryEntry<EntityAttribute> CONVERTTOFROST ;
    public static RegistryEntry<EntityAttribute> CONVERTTOARCANE ;
    public static RegistryEntry<EntityAttribute> CONVERTTOHEAL ;
    public static RegistryEntry<EntityAttribute> GLANCINGBLOW;
    public static RegistryEntry<EntityAttribute> SPELLSUPPRESS;
    public static RegistryEntry<EntityAttribute> ACRO;
    public static RegistryEntry<EntityAttribute> DEFIANCE ;
    public static RegistryEntry<EntityAttribute> ENDURANCE ;
    public static RegistryEntry<EntityAttribute> FORTITUDE ;
    public static RegistryEntry<EntityAttribute> DISSOLUTION ;

    public static RegistryEntry<EntityAttribute> RECOUP;
    public static RegistryEntry<EntityAttribute> RECOUPABSORB;
    public static RegistryEntry<EntityAttribute> REABSORBARMORMAX;

    public static RegistryEntry<EntityAttribute> IMBALANCEDGUARD;
    public static RegistryEntry<EntityAttribute> MAGEBANE;
    public static RegistryEntry<EntityAttribute> BLUR;
    public static RegistryEntry<EntityAttribute> BRITTLE;
    public static RegistryEntry<EntityAttribute> CULL;
    public static RegistryEntry<EntityAttribute> PHYSIQUE;
    public static RegistryEntry<EntityAttribute> FINESSE;
    public static RegistryEntry<EntityAttribute> ATTUNEMENT;
    public static RegistryEntry<EntityAttribute> INEVITABILITY;
    @SafeVarargs
    public static double applyAttributeModifiers(
            double initial,
            Signed<EntityAttributeInstance>... attributes
    ) {
        for (var signedAttribute : attributes) {
            if (signedAttribute.value() == null) {
                continue;
            }
            for (var modifier : ((EntityAttributeInstanceInvoker) signedAttribute.value())
                    .invokeGetModifiersByOperation(EntityAttributeModifier.Operation.ADD_VALUE)
            ) {
                switch (signedAttribute.sign()) {
                    case POSITIVE -> initial += modifier.value();
                    case NEGATIVE -> initial -= modifier.value();
                    default -> throw new IllegalStateException();
                }
            }
        }
        double result = initial;
        for (var signedAttribute : attributes) {
            if (signedAttribute.value() == null) {
                continue;
            }
            for (var modifier : ((EntityAttributeInstanceInvoker) signedAttribute.value())
                    .invokeGetModifiersByOperation(EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            ) {
                switch (signedAttribute.sign()) {
                    case POSITIVE -> result += initial * modifier.value();
                    case NEGATIVE -> result -= initial * modifier.value();
                    default -> throw new IllegalStateException();
                }
            }
        }
        for (var signedAttribute : attributes) {
            if (signedAttribute.value() == null) {
                continue;
            }
            for (var modifier : ((EntityAttributeInstanceInvoker) signedAttribute.value())
                    .invokeGetModifiersByOperation(EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            ) {
                switch (signedAttribute.sign()) {
                    case POSITIVE -> result *= 1.0 + modifier.value();
                    case NEGATIVE -> result *= 1.0 - modifier.value();
                    default -> throw new IllegalStateException();
                }
            }
        }
        for (var signedAttribute : attributes) {
            if (signedAttribute.value() == null) {
                continue;
            }
            result = signedAttribute.value().getAttribute().value().clamp(result);
        }
        return result;
    }
}