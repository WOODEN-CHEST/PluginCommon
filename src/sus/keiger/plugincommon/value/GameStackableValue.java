package sus.keiger.plugincommon.value;

import sus.keiger.plugincommon.ITickable;
import sus.keiger.plugincommon.IterationSafeMap;
import sus.keiger.plugincommon.PCMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GameStackableValue<T> implements ITickable
{
    // Private fields.
    private T _baseValue;
    private T _currentValue;
    private final Map<String, ValueOverride<T>> _overrides = new IterationSafeMap<>();
    private final Function<List<T>, T> _valueSelector;
    private final boolean _isNullAllowed;


    // Constructors.
    public GameStackableValue(T baseValue, Function<List<T>, T> valueSelector, boolean isNullAllowed)
    {
        _valueSelector = valueSelector != null ? valueSelector : List::getFirst;
        _baseValue = baseValue;
        _isNullAllowed = isNullAllowed;
    }


    // Static methods.
    public static GameStackableValue<Boolean> CreateBoolean(boolean baseValue, boolean dominantValue)
    {
        return new GameStackableValue<>(baseValue, values ->
        {
            for (boolean Value : values)
            {
                if (Value == dominantValue)
                {
                    return dominantValue;
                }
            }
            return !dominantValue;
        }, false);
    }

    public static GameStackableValue<Integer> CreateInteger(int baseValue, int dominantDirection)
    {
        if ((dominantDirection < -1) || (dominantDirection > 1))
        {
            throw new IllegalArgumentException("dominantDirection is out of range [-1;1]");
        }

        return new GameStackableValue<>(baseValue, values ->
        {
            int ClosestMatch = values.getFirst();
            for (int Value : values)
            {
                if (PCMath.Sign(Value - ClosestMatch) * dominantDirection > 0)
                {
                    ClosestMatch = Value;
                }
            }
            return ClosestMatch;
        }, false);
    }

    public static GameStackableValue<Double> CreateDouble(double baseValue, int dominantDirection)
    {
        if ((dominantDirection < -1) || (dominantDirection > 1))
        {
            throw new IllegalArgumentException("dominantDirection is out of range [-1;1]");
        }

        return new GameStackableValue<>(baseValue, values ->
        {
            double ClosestMatch = values.getFirst();
            for (double Value : values)
            {
                if (PCMath.Sign(Value - ClosestMatch) * dominantDirection > 0)
                {
                    ClosestMatch = Value;
                }
            }
            return ClosestMatch;
        }, false);
    }



    // Methods.
    public void Set(T value)
    {
        if ((value == null) && !_isNullAllowed)
        {
            throw new IllegalArgumentException("value is null");
        }
        _baseValue = value;
        UpdateValue();
    }

    public void SetOverride(T value, String key)
    {
        SetOverride(value, key, Integer.MAX_VALUE);
        UpdateValue();
    }

    public void SetOverride(T value, String key, int ticks)
    {
        if (key == null)
        {
            throw new IllegalArgumentException("key is null");
        }
        if ((value == null) && !_isNullAllowed)
        {
            throw new IllegalArgumentException("value is null");
        }
        _overrides.put(key, new ValueOverride<>(value, ticks, key));
        UpdateValue();
    }

    public void SetOverrideTicks(String key, int ticks)
    {
        if (key == null)
        {
            throw new IllegalArgumentException("key is null");
        }
        ValueOverride<T> Override = _overrides.get(key);
        if (Override != null)
        {
            Override.TicksLeft = ticks;
        }
    }

    public T Get()
    {
        return _currentValue;
    }

    public T GetBase()
    {
        return _baseValue;
    }

    public T GetOverride(String key)
    {
        if (key == null)
        {
            throw new IllegalArgumentException("key is null");
        }
        ValueOverride<T> Override = _overrides.get(key);
        if (Override != null)
        {
            return Override.Value;
        }
        return null;
    }

    public int GetOverrideTicksLeft(String key)
    {
        if (key == null)
        {
            throw new IllegalArgumentException("key is null");
        }
        ValueOverride<T> Override = _overrides.get(key);
        if (Override != null)
        {
            return Override.TicksLeft;
        }
        return 0;
    }

    public void ClearOverrides()
    {
        _overrides.clear();
        UpdateValue();
    }


    // Private methods.
    private void UpdateValue()
    {
        List<T> AllValues = new ArrayList<>();
        AllValues.add(_baseValue);
        AllValues.addAll(_overrides.values().stream().map(override -> override.Value).toList());
        _currentValue = _valueSelector.apply(AllValues);
    }


    // Inherited methods.
    @Override
    public void Tick()
    {
        boolean MustUpdateValue = false;
        for (ValueOverride<T> Override : _overrides.values())
        {
            Override.Tick();
            if (Override.TicksLeft <= 0)
            {
                MustUpdateValue = true;
                _overrides.remove(Override.Key);
            }
        }

        if (MustUpdateValue)
        {
            UpdateValue();
        }
    }


    // Types.
    private static class ValueOverride<T> implements ITickable
    {
        // Fields.
        public final T Value;
        public int TicksLeft;
        public final String Key;


        // Constructors.
        public ValueOverride(T value, int ticksLeft, String key)
        {
            Value = value;
            TicksLeft = ticksLeft;
            Key = key;
        }

        @Override
        public void Tick()
        {
            if (TicksLeft != Integer.MAX_VALUE)
            {
                TicksLeft--;
            }
        }
    }
}