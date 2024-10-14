package sus.keiger.plugincommon.value;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.plugincommon.ITickable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameModifiableValue implements ITickable
{
    // Private fields.
    private double _baseValue;
    private final HashMap<Long, GameModifiableValueModifier> _modifiers = new HashMap<>();
    private final List<Long> _modifiersToRemove = new ArrayList<>();


    // Constructors.
    public GameModifiableValue(double baseValue)
    {
        _baseValue = baseValue;
    }


    // Methods.
    public double GetBaseValue()
    {
        return _baseValue;
    }

    public void SetBaseValue(double value)
    {
        _baseValue = value;
    }

    public double GetValue()
    {
        if (_modifiers.isEmpty())
        {
            return _baseValue;
        }

        double ResultingValue = _baseValue;
        ResultingValue = ApplyModifiersOfType(ResultingValue, GameModifiableValueOperator.Add);
        ResultingValue = ApplyModifiersOfType(ResultingValue, GameModifiableValueOperator.Multiply);
        ResultingValue = ApplyModifiersOfType(ResultingValue, GameModifiableValueOperator.Exponentiate);

        return ResultingValue;
    }

    public void ClearModifiers()
    {
        _modifiers.clear();
    }

    public void AddModifier(GameModifiableValueModifier modifier)
    {
        if (modifier == null)
        {
            throw new NullArgumentException("modifier is null");
        }

        _modifiers.put(modifier.GetID(), modifier);
    }

    public void RemoveModifier(GameModifiableValueModifier modifier)
    {
        if (modifier == null)
        {
            throw new NullArgumentException("modifier is null");
        }

        _modifiers.remove(modifier.GetID());
    }

    public void RemoveModifier(long id)
    {
        _modifiers.remove(id);
    }


    // Private methods.
    private double ApplyModifiersOfType(double value, GameModifiableValueOperator operator)
    {
        double ResultingValue = value;
        for (GameModifiableValueModifier Modifier : _modifiers.values())
        {
            if (Modifier.GetOperator() == operator)
            {
                ResultingValue = Modifier.ModifyValue(ResultingValue);
            }
        }
        return ResultingValue;
    }



    // Inherited methods.
    @Override
    public void Tick()
    {
        if (_modifiers.isEmpty())
        {
            return;
        }

        _modifiersToRemove.clear();

        for (GameModifiableValueModifier Modifier : _modifiers.values())
        {
            Modifier.Tick();
            if (Modifier.GetRemainingLifetime() <= 0)
            {
                _modifiersToRemove.add(Modifier.GetID());
            }
        }

        _modifiersToRemove.forEach(_modifiers::remove);
    }
}