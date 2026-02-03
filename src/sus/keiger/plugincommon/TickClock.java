package sus.keiger.plugincommon;

import java.util.Objects;
import java.util.function.Consumer;

public class TickClock implements ITickable
{
    // Static fields.
    public static final int TICK_RAN_OUT = -1;


    // Private fields.
    private int _ticksLeft = TICK_RAN_OUT;
    private boolean _isRunning = false;
    private Consumer<TickClock> _tickFunction;
    private Consumer<TickClock> _handler;


    // Methods.
    public void SetTickFunction(Consumer<TickClock> function)
    {
        _tickFunction = function;
    }

    public void SetHandler(Consumer<TickClock> handler)
    {
        _handler = handler;
    }

    public boolean GetIsRunning()
    {
        return _isRunning;
    }

    public void SetIsRunning(boolean value)
    {
        _isRunning = value;
    }

    public int GetTicksLeft()
    {
        return _ticksLeft;
    }

    public void SetTicksLeft(int ticks)
    {
        _ticksLeft = Math.max(ticks, -1);
    }

    public void SetSecondsLeft(double seconds)
    {
        SetTicksLeft(PCMath.SecondsToTicks(seconds));
    }


    // Inherited methods.
    @Override
    public void Tick()
    {
        if (!_isRunning)
        {
            return;
        }

        if ((_ticksLeft > 0) && (_tickFunction != null))
        {
            _tickFunction.accept(this);
        }
        else if ((_ticksLeft == 0) && (_handler != null))
        {
            _handler.accept(this);
        }

        if (_ticksLeft >= 0)
        {
            _ticksLeft--;
        }
    }
}