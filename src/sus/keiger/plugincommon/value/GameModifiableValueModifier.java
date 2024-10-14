package sus.keiger.plugincommon.value;

import org.apache.commons.lang.NullArgumentException;
import sus.keiger.plugincommon.ITickable;

import java.util.Random;

public class GameModifiableValueModifier implements ITickable
{
    // Private fields.
    private final GameModifiableValueOperator _operator;
    public final double _value;
    public final long _id;
    private int _ticksLeft;


    // Constructors.
    public GameModifiableValueModifier(GameModifiableValueOperator operator, double value, int lifetime)
    {
        this(operator, value, lifetime, new Random().nextLong());
    }


    public GameModifiableValueModifier(GameModifiableValueOperator operator, double value, int lifespan, long id)
    {
        if (operator == null)
        {
            throw new NullArgumentException("Operator is null");
        }

        _operator = operator;
        _value = value;
        _ticksLeft = lifespan;
        _id = id;
    }


    // Methods.
    public long GetID()
    {
        return _id;
    }

    public GameModifiableValueOperator GetOperator()
    {
        return _operator;
    }

    public double GetValue()
    {
        return _value;
    }

    public int GetRemainingLifetime()
    {
        return _ticksLeft;
    }

    public void SetRemainingLifetime(int value)
    {
        _ticksLeft = value;
    }

    public double ModifyValue(double valueIn)
    {
        return switch (_operator)
            {
                case Add -> valueIn + _value;
                case Multiply -> valueIn * _value;
                case Exponentiate -> Math.pow(valueIn, _value);
                default -> valueIn;
            };
    }


    // Inherited methods.
    @Override
    public int hashCode()
    {
        return (int)_id;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof GameModifiableValueModifier Modifier)
        {
            return Modifier.GetID() == _id;
        }
        return false;
    }

    @Override
    public void Tick()
    {
        _ticksLeft--;
    }
}