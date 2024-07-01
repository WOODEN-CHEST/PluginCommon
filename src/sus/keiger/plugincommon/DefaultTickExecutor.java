package sus.keiger.plugincommon;

import java.util.HashSet;

public class DefaultTickExecutor implements ITickExecutor, ITickable
{
    // Private fields.
    private final HashSet<ITickable> _tickableItems = new HashSet<>();


    // Inherited methods.
    @Override
    public void AddTickable(ITickable tickable)
    {
        if (tickable == null)
        {
            throw new IllegalArgumentException("tickable is null");
        }
        _tickableItems.add(tickable);
    }

    @Override
    public void RemoveTickable(ITickable tickable)
    {
        if (tickable == null)
        {
            throw new IllegalArgumentException("tickable is null");
        }
        _tickableItems.add(tickable);
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