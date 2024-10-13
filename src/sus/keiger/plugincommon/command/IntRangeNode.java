package sus.keiger.plugincommon.command;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class IntRangeNode extends CommandNode
{
    // Static fields.
    public static final String RANGE_INDICATOR = "..";
    public static final char RANGE_INDICATOR_CHAR = '.';
    public static final String RANGE_INDICATOR_CHAR_STRING = ".";


    // Private fields.
    private final Function<CommandData, List<String>> _suggestionProvider;


    // Constructors.
    public IntRangeNode(Consumer<CommandData> executor,
                        String parsedDataKey,
                        Function<CommandData, List<String>> suggestionProvider)
    {
        super(executor, parsedDataKey);
        _suggestionProvider = suggestionProvider;
    }


   // Static methods.
   public static boolean VerifyRange(RangeNodeIntResult range, long min, long max, Long rangeDeltaMin)
   {
       return (range.GetMin() <= range.GetMax()) && (range.GetMin() >= min) && (range.GetMax() <= max)
               && ((rangeDeltaMin == null) || (range.GetMax() - range.GetMin() >= rangeDeltaMin));
   }


    // Private methods.
    private RangeNodeIntResult GetSingleRangeNode(String value)
    {
        try
        {
            long Value = Long.parseLong(value);
            return new RangeNodeIntResult(Value, Value);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    private RangeNodeIntResult ParseResult(String value)
    {
        int RangeIndicatorIndex = value.indexOf(RANGE_INDICATOR);
        if (RangeIndicatorIndex == -1)
        {
            return GetSingleRangeNode(value);
        }

        long Min, Max;
        if (value.startsWith(RANGE_INDICATOR))
        {
            Min = Long.MIN_VALUE;
        }
        else
        {
            try
            {
                Min = Long.parseLong(value.substring(0, RangeIndicatorIndex));
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }

        if (value.endsWith(RANGE_INDICATOR))
        {
            Max = Long.MAX_VALUE;
        }
        else
        {
            try
            {
                Max = Long.parseLong(value.substring(RangeIndicatorIndex + RANGE_INDICATOR.length()));
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }

        return new RangeNodeIntResult(Min, Max);
    }


    // Inherited methods.
    @Override
    public boolean ParseCommand(CommandData data)
    {
        RangeNodeIntResult Result = ParseResult(data.ReadWord());
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