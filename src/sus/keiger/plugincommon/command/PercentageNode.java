package sus.keiger.plugincommon.command;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class PercentageNode extends CommandNode
{
    // Static fields.
    public static final String SUFFIX = "%";
    public static final double VALUE_SCALE = 100d;


    // Private fields.
    private final Function<CommandData, List<String>> _suggestionSupplier;
    private final boolean _isPercentageSymbolRequired;


    // Constructors.
    public PercentageNode(Consumer<CommandData> executor,
                          Function<CommandData, List<String>> suggestionSupplier,
                          boolean isPercentageSymbolRequired,
                          String parsedDataKey)
    {
        super(executor, parsedDataKey);
        _suggestionSupplier = suggestionSupplier;
        _isPercentageSymbolRequired = isPercentageSymbolRequired;
    }


    // Private methods.
    private String GetSuggestion(String value)
    {
        if (!_isPercentageSymbolRequired)
        {
            return value;
        }
        return value.endsWith(SUFFIX) ? value : (value + SUFFIX);
    }


    // Inherited methods.
    @Override
    public boolean ParseCommand(CommandData data)
    {
        try
        {
            String Word = data.ReadWord();
            boolean EndsWithSuffix = Word.endsWith(SUFFIX);

            if (_isPercentageSymbolRequired && !EndsWithSuffix)
            {
                return false;
            }

            String NumerString = EndsWithSuffix ? Word.substring(0, Word.length() - 1) : Word;
            AddParsedData(Double.parseDouble(NumerString) / VALUE_SCALE, data);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return Optional.ofNullable(_suggestionSupplier)
                .map(supplier -> supplier.apply(data).stream().map(this::GetSuggestion).toList())
                .orElse(Collections.emptyList());
    }
}