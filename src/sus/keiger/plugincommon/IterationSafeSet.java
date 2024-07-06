package sus.keiger.plugincommon;

import java.util.*;

public class IterationSafeSet<T> implements Set<T>
{
    // Private fields.
    private final HashSet<T> _items = new HashSet<>();
    private List<T> _latestItemsCopy;
    private boolean _isContentsChanged = true;


    // Constructors.
    public IterationSafeSet() { }

    public IterationSafeSet(Collection<T> items)
    {
        _items.addAll(items);
    }


    // Inherited methods.
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
    public boolean contains(Object o)
    {
        return _items.contains(o);
    }

    @Override
    public Iterator<T> iterator()
    {
        if (_isContentsChanged)
        {
            _latestItemsCopy = List.copyOf(_items);
            _isContentsChanged = false;
        }
        return _latestItemsCopy.iterator();
    }

    @Override
    public Object[] toArray()
    {
        return _items.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a)
    {
        return _items.toArray(a);
    }

    @Override
    public boolean add(T t)
    {
        if (_items.add(t))
        {
            _isContentsChanged = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o)
    {
        if (_items.remove(o))
        {
            _isContentsChanged = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return _items.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        if (_items.addAll(c))
        {
            _isContentsChanged = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        if (_items.retainAll(c))
        {
            _isContentsChanged = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        if (_items.removeAll(c))
        {
            _isContentsChanged = true;
            return true;
        }
        return false;
    }

    @Override
    public void clear()
    {
        _isContentsChanged = true;
        _items.clear();
    }
}