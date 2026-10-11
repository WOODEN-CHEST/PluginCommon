package sus.keiger.plugincommon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultTickExecutor implements ITickExecutor
{
    // Private fields.
    private final List<ITickable> _tickableItems = new ArrayList<>();


    // Inherited methods.
    @Override
    public void AddTickable(ITickable tickable)
    {
        Objects.requireNonNull(tickable, "tickable is null");
        if (_tickableItems.contains(tickable))
        {
            return;
        }

        _tickableItems.add(tickable);
    }

    @Override
    public void RemoveTickable(ITickable tickable)
    {
        Objects.requireNonNull(tickable, "tickable is null");
        _tickableItems.remove(tickable);
    }

    @Override
    public void ClearTickables()
    {
        _tickableItems.clear();
    }

    @Override
    public void Tick()
    {
        for (ITickable TickableItem : _tickableItems)
        {
            TickableItem.Tick();
        }
    }
}