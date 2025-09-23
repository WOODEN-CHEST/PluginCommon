package sus.keiger.plugincommon;

import java.util.*;

public class WeightedCollection<T> implements Iterable<T>
{
    // Private fields.
    private final List<Entry<T>> _entries = new ArrayList<>();
    private Random _rng;



    // Constructors.
    public WeightedCollection()
    {
        this(new Random());
    }

    public WeightedCollection(Random rng)
    {
        _rng = Objects.requireNonNull(rng, "rng is null");
    }



    // Methods.
    public void SetRNG(Random rng)
    {
        _rng = Objects.requireNonNull(rng, "rng is null");
    }

    public T GetRandomElement()
    {
        if (_entries.isEmpty())
        {
            throw new NoSuchElementException("Weighted collection is empty, cannot get random element.");
        }

        return _entries.get(GetElementIndex(_rng.nextDouble() * GetMaxIndex())).Value;
    }

    public int Size()
    {
        return _entries.size();
    }

    public boolean IsEmpty()
    {
        return _entries.isEmpty();
    }

    public void SetElements(Map<T, Double> elements)
    {
        Objects.requireNonNull(elements, "elements is null");
        _entries.clear();

        double CurrentWeight = 0d;
        for (Map.Entry<T, Double> TargetEntry : elements.entrySet())
        {
            double Weight = TargetEntry.getValue();
            if (Double.isNaN(Weight) || Double.isInfinite(Weight) || (Weight < 0d))
            {
                throw new IllegalArgumentException("Invalid entry (cannot be NaN, infinite or < 0): %f"
                        .formatted(Weight));
            }

            double EntryWeight = TargetEntry.getValue();
            _entries.add(new Entry<>(CurrentWeight, EntryWeight, TargetEntry.getKey()));
            CurrentWeight += EntryWeight;
        }
    }

    public void ClearElements()
    {
        _entries.clear();
    }


    // Private methods.
    private double GetMaxIndex()
    {
        return _entries.getLast().IndexStart() + _entries.getLast().Weight;
    }

    private int GetElementIndex(double value)
    {
        int MinIndex = 0;
        int MaxIndex = _entries.size() - 1;

        while (MinIndex <= MaxIndex)
        {
            int MiddleIndex = MinIndex + (MaxIndex - MinIndex) / 2;

            Entry<T> Element = _entries.get(MiddleIndex);
            if (Element.GetMinIndex() > value)
            {
                MaxIndex = MiddleIndex - 1;
            }
            else if (Element.GetMaxIndex() <= value)
            {
                MinIndex = MiddleIndex + 1;
            }
            else
            {
                return MiddleIndex;
            }
        }

        return value >= GetMaxIndex() ? (_entries.size() - 1) : 0;
    }


    // Inherited methods.
    @Override
    public Iterator<T> iterator()
    {
        return new EntryIterator<>(this);
    }


    // Types.
    private record Entry<T>(double IndexStart, double Weight, T Value)
    {
        public double GetMinIndex () { return IndexStart; }
        public double GetMaxIndex () { return IndexStart + Weight; }
    }

    private static class EntryIterator<T> implements Iterator<T>
    {
        // Fields.
        public final WeightedCollection<T> TargetCollection;
        public int Index = 0;


        // Constructors.
        private EntryIterator(WeightedCollection<T> targetCollection)
        {
            TargetCollection = targetCollection;
        }


        // Inherited methods.
        @Override
        public boolean hasNext()
        {
            return Index < TargetCollection.Size();
        }

        @Override
        public T next()
        {
            return TargetCollection._entries.get(Index++).Value;
        }
    }
}