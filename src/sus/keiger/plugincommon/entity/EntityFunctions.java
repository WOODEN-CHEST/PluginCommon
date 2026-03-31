package sus.keiger.plugincommon.entity;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.util.BoundingBox;
import sus.keiger.plugincommon.PCMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class EntityFunctions
{
    // Constructors.
    private EntityFunctions() { }


    // Static methods.
    public static boolean ResetAttribute(LivingEntity entity, Attribute attribute)
    {
        return ExecuteOnAttribute(entity, attribute, instance ->
        {
            RemoveAttributeModifiers(instance);
            instance.setBaseValue(instance.getDefaultValue());
        });
    }

    public static boolean ResetAttribute(LivingEntity entity, Attribute attribute, double defaultValue)
    {
        return ExecuteOnAttribute(entity, attribute, atr ->
        {
            RemoveAttributeModifiers(atr);
            atr.setBaseValue(defaultValue);
        });
    }

    public static boolean TrySetAttributeBaseValue(LivingEntity entity, Attribute attribute, double value)
    {
        return ExecuteOnAttribute(entity, attribute, atr -> atr.setBaseValue(value));
    }

    public static boolean SetHealthPortion(LivingEntity entity, double portion)
    {
        AttributeInstance HealthAttribute = entity.getAttribute(Attribute.MAX_HEALTH);
        if (HealthAttribute == null)
        {
            return false;
        }

        entity.setHealth(HealthAttribute.getValue() * Math.max(0d, Math.min(portion, 1d)));
        return true;
    }

    public static boolean SetHealthAbsolute(LivingEntity entity, double health)
    {
        AttributeInstance HealthAttribute = entity.getAttribute(Attribute.MAX_HEALTH);
        if (HealthAttribute == null)
        {
            return false;
        }

        entity.setHealth(Math.max(0d, Math.min(health, HealthAttribute.getValue())));
        return true;
    }

    public static boolean IsEntityOnGround(Entity entity)
    {
        return PCMath.AreBoundsOnGround(entity.getBoundingBox(), entity.getWorld());
    }


    // Private static methods.
    private static boolean ExecuteOnAttribute(LivingEntity entity,
                                              Attribute attribute,
                                              Consumer<AttributeInstance> func)
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("attribute is null");
        }
        if (attribute == null)
        {
            throw new IllegalArgumentException("attribute is null");
        }

        AttributeInstance TargetAttribute = entity.getAttribute(attribute);
        if (TargetAttribute == null)
        {
            return false;
        }
        func.accept(TargetAttribute);
        return true;
    }

    private static void RemoveAttributeModifiers(AttributeInstance instance)
    {
        instance.getModifiers().forEach(instance::removeModifier);
    }
}
