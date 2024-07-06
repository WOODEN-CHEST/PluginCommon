package sus.keiger.plugincommon;

import java.util.*;

public class IterationSafeList<T> implements List<T>
{
    // Private fields.
    private final List<T> _items = new ArrayList<>();
    private List<T> _latestItemsCopy;
    private boolean _isContentsChanged = true;


    // Constructors.
    public IterationSafeList() { }

    public IterationSafeList(Collection<T> items)
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
        _isContentsChanged = true;
        return _items.add(t);
    }

    @Override
    public boolean remove(Object o)
    {
        boolean IsRemoved = _items.remove(o);
        if (IsRemoved)
        {
            _isContentsChanged = true;
        }
        return IsRemoved;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return Set.of(_items).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        if (!c.isEmpty())
        {
            _isContentsChanged = true;
        }
        return _items.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c)
    {
        if (!c.isEmpty())
        {
            _isContentsChanged = true;
        }
        return  _items.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        if (!c.isEmpty())
        {
            _isContentsChanged = true;
        }
        return _items.removeAll(c);
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
    public void clear()
    {
        _isContentsChanged = true;
        _items.clear();
    }

    @Override
    public T get(int index)
    {
        return _items.get(index);
    }

    @Override
    public T set(int index, T element)
    {
        _isContentsChanged = true;
        return _items.set(index, element);
    }

    @Override
    public void add(int index, T element)
    {
        _isContentsChanged = true;
        _items.add(index, element);
    }

    @Override
    public T remove(int index)
    {
        _isContentsChanged = true;
        return _items.remove(index);
    }

    @Override
    public int indexOf(Object o)
    {
        return _items.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return _items.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator()
    {
        throw new UnsupportedOperationException("Iteration safe list does not support list iterators");
    }

    @Override
    public ListIterator<T> listIterator(int index)
    {
        throw new UnsupportedOperationException("Iteration safe list does not support list iterators");
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex)
    {
        return _items.subList(fromIndex, toIndex);
    }
}