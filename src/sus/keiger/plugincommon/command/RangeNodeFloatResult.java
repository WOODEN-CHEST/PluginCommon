package sus.keiger.plugincommon.command;

public final class RangeNodeFloatResult
{
    // Private fields.
    private final double _min;
    private final double _max;


    // Constructors.
    public RangeNodeFloatResult(double min, double max)
    {
        _min = min;
        _max = max;
    }


    // Methods.
    public double GetMin()
    {
        return _min;
    }

    public double GetMax()
    {
        return _max;
    }
}