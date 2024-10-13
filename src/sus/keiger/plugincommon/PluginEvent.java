package sus.keiger.plugincommon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * An event which can be fired, subscribed and unsubscribed from.
 * */
public class PluginEvent<T>
{
    // Private fields.
    private final Map<Object, List<PluginEventHandler<T>>> _handlers = new IterationSafeMap<>();


    // Constructors.
    public PluginEvent() { }


    // Private methods.
    private void SortHandlerList(List<PluginEventHandler<T>> handlers, int minIndex, int maxIndex)
    {
        if (minIndex >= maxIndex)
        {
            return;
        }

        int PivotIndex = minIndex;
        int LeftIndex = minIndex + 1;
        int RightIndex = maxIndex;
        while (LeftIndex <= RightIndex)
        {
            if (handlers.get(LeftIndex).Priority > handlers.get(PivotIndex).Priority)
            {
                LeftIndex++;
            }
            else if (handlers.get(RightIndex).Priority < handlers.get(PivotIndex).Priority)
            {
                RightIndex--;
            }
            else
            {
                PluginEventHandler<T> TempHandler = handlers.get(LeftIndex);
                handlers.set(LeftIndex, handlers.get(RightIndex));
                handlers.set(RightIndex, TempHandler);
                LeftIndex++;
                RightIndex--;
            }
        }

        PluginEventHandler<T> TempHandler = handlers.get(RightIndex);
        handlers.set(RightIndex, handlers.get(PivotIndex));
        handlers.set(PivotIndex, TempHandler);

        SortHandlerList(handlers, minIndex, RightIndex - 1);
        SortHandlerList(handlers, RightIndex + 1, maxIndex);
    }


    // Methods.
    /**
     * Subscribes to the event with the default priority of 0.
     * @param source The object who is subscribing.
     * @param handler The event handler.
     * @throws NullPointerException if <code>source</code> or <code>handler</code> is null.
     * */
    public void Subscribe(Object source, Consumer<T> handler)
    {
        Subscribe(source, handler, 0);
    }

    /**
     * Subscribes to the event with the specified priority.
     * <br>Event handlers with a higher priority (number in + direction) are handled first, while
     * event handlers with a lower priority (number in - direction) are handled later.
     * <br>While any number may be passed in as the priority, is it advised to stick to these numbers:
     * <ul>
     *     <li><code>-2</code>: Very low priority</li>
     *     <li><code>-1</code>: Low priority</li>
     *     <li><code>0</code>: Normal priority</li>
     *     <li><code>1</code>: High priority</li>
     *     <li><code>2</code>: Very high priority</li>
     * </ul>
     * @param source The object who is subscribing.
     * @param handler The event handler.
     * @param priority The handling priority.
     * @throws NullPointerException if <code>source</code> or <code>handler</code> is null.
     * */
    public void Subscribe(Object source, Consumer<T> handler, int priority)
    {
        Objects.requireNonNull(source, "source is null");
        Objects.requireNonNull(handler, "handler is null");

        if (!_handlers.containsKey(source))
        {
            _handlers.put(source, new ArrayList<>());
        }
        List<PluginEventHandler<T>> SourceHandlers = _handlers.get(source);
        SourceHandlers.add(new PluginEventHandler<>(handler, priority));
        SortHandlerList(SourceHandlers, 0, SourceHandlers.size() - 1);
    }

    /**
     * Removes all event handlers from a source object.
     * @param source The object whose event handlers to remove.
     * @throws NullPointerException if <code>source</code> is null.
     * */
    public void Unsubscribe(Object source)
    {
        _handlers.remove(Objects.requireNonNull(source));
    }

    /**
     * Removes a specific event handler from a source object.
     * @param source The object whose event handler to remove.
     * @param function The handler to remove
     * @throws NullPointerException if <code>source</code> or <code>function</code> is null.
     * */
    public void Unsubscribe(Object source, Consumer<T> function)
    {
        Objects.requireNonNull(source);
        Objects.requireNonNull(function);
        if (!_handlers.containsKey(source))
        {
            return;
        }

        PluginEventHandler<T> TargetHandler = _handlers.get(source).stream()
                .filter(handler -> handler.Function == function).findFirst().orElse(null);
        if (TargetHandler != null)
        {
            List<PluginEventHandler<T>> SourceHandlers = _handlers.get(source);
            SourceHandlers.remove(TargetHandler);
            SortHandlerList(SourceHandlers, 0, SourceHandlers.size() - 1);
        }
    }

    /**
     * Fires the event, notifying all handlers in order with respect to their priority.
     * @param args The event arguments, may be <code>null</code>.
     * */
    public void FireEvent(T args)
    {
        for (List<PluginEventHandler<T>> Handlers : _handlers.values())
        {
            Handlers.forEach(singleHandler -> singleHandler.Function.accept(args));
        }
    }


    // Types.
    private static class PluginEventHandler<T>
    {
        // Fields.
        public final Consumer<T> Function;
        public final int Priority;


        // Methods.
        public PluginEventHandler(Consumer<T> function, int priority)
        {
            Function = Objects.requireNonNull(function, "handler is null");
            Priority = priority;
        }
    }
}