package sus.keiger.plugincommon.command;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DoubleRangeNode extends CommandNode
{
    // Static fields.
    public static final String RANGE_INDICATOR = "..";
    public static final char RANGE_INDICATOR_CHAR = '.';
    public static final String RANGE_INDICATOR_CHAR_STRING = ".";


    // Private fields.
    private final Function<CommandData, List<String>> _suggestionProvider;


    // Constructors.
    public DoubleRangeNode(Consumer<CommandData> executor,
                           String parsedDataKey,
                           Function<CommandData, List<String>> suggestionProvider)
    {
        super(executor, parsedDataKey);
        _suggestionProvider = suggestionProvider;
    }


   // Static methods.
   public static boolean VerifyRange(RangeNodeIntResult range, double min, double max, Double rangeDeltaMin)
   {
       return (range.GetMin() <= range.GetMax()) && (range.GetMin() >= min) && (range.GetMax() <= max)
               && ((rangeDeltaMin == null) || (range.GetMax() - range.GetMin() >= rangeDeltaMin));
   }


    // Private methods.
    private RangeNodeFloatResult GetSingleRangeNode(String value)
    {
        try
        {
            double Value = Double.parseDouble(value);
            return new RangeNodeFloatResult(Value, Value);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    private RangeNodeFloatResult ParseResult(String value)
    {
        int RangeIndicatorIndex = value.indexOf(RANGE_INDICATOR);
        if (RangeIndicatorIndex == -1)
        {
            return GetSingleRangeNode(value);
        }

        double Min, Max;
        if (value.startsWith(RANGE_INDICATOR))
        {
            Min = Double.MIN_VALUE;
        }
        else
        {
            try
            {
                Min = Double.parseDouble(value.substring(0, RangeIndicatorIndex));
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }

        if (value.endsWith(RANGE_INDICATOR))
        {
            Max = Double.MAX_VALUE;
        }
        else
        {
            try
            {
                Max = Double.parseDouble(value.substring(RangeIndicatorIndex + RANGE_INDICATOR.length()));
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }

        return new RangeNodeFloatResult(Min, Max);
    }


    // Inherited methods.
    @Override
    public boolean ParseCommand(CommandData data)
    {
        RangeNodeFloatResult Result = ParseResult(data.ReadWord());
        if (Result != null)
        {
            AddParsedData(Result, data);
            return true;
        }
        return false;
    }

    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return _suggestionProvider != null ? _suggestionProvider.apply(data) : Collections.emptyList();
    }
}