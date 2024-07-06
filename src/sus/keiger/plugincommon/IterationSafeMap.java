package sus.keiger.plugincommon;

import java.util.*;

public class IterationSafeMap<T, V> implements Map<T, V>
{
    // Private fields.
    private final HashMap<T, V> _items = new HashMap<>();
    private Set<T> _latestKeysCopy;
    private List<V> _latestValuesCopy;
    private Set<Entry<T, V>> _latestEntrySetCopy;
    private boolean _isKeysChanged = true;
    private boolean _isValuesChanged = true;
    private boolean _isEntrySetChanged = true;


    // Constructors.
    public IterationSafeMap() { }


    // Private methods.
    private void MarkDataAsChanged()
    {
        _isKeysChanged = true;
        _isValuesChanged = true;
        _isEntrySetChanged = true;
    }


    // Inherited methods,
    @Override
    public int size()
    {
        return _items.size();
    }

    @Override
    public boolean isEmpty()
    {
        return _items.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return _items.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return _items.containsValue(value);
    }

    @Override
    public V get(Object key)
    {
        return _items.get(key);
    }

    @Override
    public V put(T key, V value)
    {
        MarkDataAsChanged();
        return _items.put(key, value);
    }

    @Override
    public V remove(Object key)
    {
        MarkDataAsChanged();
        return _items.remove(key);
    }

    @Override
    public void putAll(Map<? extends T, ? extends V> m)
    {
        MarkDataAsChanged();
        _items.putAll(m);
    }

    @Override
    public void clear()
    {
        MarkDataAsChanged();
        _items.clear();
    }

    @Override
    public Set<T> keySet()
    {
        if (_isKeysChanged)
        {
            _latestKeysCopy = Set.copyOf(_items.keySet());
            _isKeysChanged = false;
        }
        return _latestKeysCopy;
    }

    @Override
    public Collection<V> values()
    {
        if (_isValuesChanged)
        {
            _latestValuesCopy = List.copyOf(_items.values());
            _isValuesChanged = false;
        }
        return _latestValuesCopy;
    }

    @Override
    public Set<Entry<T, V>> entrySet()
    {
        if (_isEntrySetChanged)
        {
            _latestEntrySetCopy = Set.copyOf(_items.entrySet());
            _isEntrySetChanged = false;
        }
        return _latestEntrySetCopy;
    }
}