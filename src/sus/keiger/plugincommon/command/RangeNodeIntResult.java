package sus.keiger.plugincommon.command;

public final class RangeNodeIntResult
{
    // Private fields.
    private final long _min;
    private final long _max;


    // Constructors.
    public RangeNodeIntResult(long min, long max)
    {
        _min = min;
        _max = max;
    }


    // Methods.
    public long GetMin()
    {
        return _min;
    }

    public long GetMax()
    {
        return _max;
    }
}