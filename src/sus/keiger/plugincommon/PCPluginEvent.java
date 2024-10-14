package sus.keiger.plugincommon;

import java.util.*;
import java.util.function.Consumer;

/**
 * An event which can be fired, subscribed and unsubscribed from.
 * */
public class PCPluginEvent<T>
{
    // Private fields.
    private final List<PluginEventHandler<T>> _handlers = new ArrayList<>();


    // Constructors.
    public PCPluginEvent() { }


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

        _handlers.add(new PluginEventHandler<>(source, handler, priority));
        SortHandlerList(_handlers, 0, _handlers.size() - 1);
    }

    /**
     * Removes all event handlers from a source object.
     * @param source The object whose event handlers to remove.
     * @throws NullPointerException if <code>source</code> is null.
     * */
    public void Unsubscribe(Object source)
    {
        _handlers.stream().filter(handler -> handler.Source.equals(source)).forEach(_handlers::remove);
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

        _handlers.stream().filter(handler -> (handler.Source.equals(source)) && (handler.Function.equals(function)))
                .forEach(_handlers::remove);
    }

    /**
     * Fires the event, notifying all handlers in order with respect to their priority.
     * @param args The event arguments, may be <code>null</code>.
     * */
    public void FireEvent(T args)
    {
        _handlers.forEach(singleHandler -> singleHandler.Function.accept(args));
    }

    /**
     * Gets the amount of subscriptions to this event.
     * @return The subscriber count.
     */
    public int SubscriberCount()
    {
        return _handlers.size();
    }


    // Types.
    private static class PluginEventHandler<T>
    {
        // Fields.
        public final Object Source;
        public final Consumer<T> Function;
        public final int Priority;


        // Methods.
        public PluginEventHandler(Object source, Consumer<T> function, int priority)
        {
            Source = Objects.requireNonNull(source, "source is null");
            Function = Objects.requireNonNull(function, "handler is null");
            Priority = priority;
        }
    }
}