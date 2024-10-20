package sus.keiger.plugincommon;

import java.util.Objects;
import java.util.function.Consumer;

public class TickClock implements ITickable
{
    // Private fields.
    private int _ticksLeft = -1;
    private boolean _isRunning = false;
    private Consumer<TickClock> _tickFunction;
    private Consumer<TickClock> _handler;


    // Methods.
    public void SetTickFunction(Consumer<TickClock> function)
    {
        _tickFunction = Objects.requireNonNull(function, "function is null");
    }

    public void SetHandler(Consumer<TickClock> handler)
    {
        _handler = Objects.requireNonNull(handler, "function is null");
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