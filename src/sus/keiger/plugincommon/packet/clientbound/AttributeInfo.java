package sus.keiger.plugincommon.packet.clientbound;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AttributeInfo
{
    // Private fields.
    private Attribute _attribute;
    private double _value = 0d;
    private List<AttributeModifier> _attributeModifiers = Collections.emptyList();


    // Constructors.
    public AttributeInfo(Attribute attribute)
    {
        SetAttribute(attribute);
    }


    // Methods.
    public double GetValue()
    {
        return _value;
    }

    public void SetValue(double value)
    {
        _value = value;
    }

    public List<AttributeModifier> GetModifiers()
    {
        return List.copyOf(_attributeModifiers);
    }

    public void SetModifiers(Collection<AttributeModifier> modifiers)
    {
        _attributeModifiers = List.copyOf(Objects.requireNonNull(modifiers, "modifiers is null"));
    }

    public Attribute GetAttribute()
    {
        return _attribute;
    }

    public void SetAttribute(Attribute attribute)
    {
        _attribute = Objects.requireNonNull(attribute, "attribute is null");
    }

}