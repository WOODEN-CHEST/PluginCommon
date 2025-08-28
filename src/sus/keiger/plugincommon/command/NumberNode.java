package sus.keiger.plugincommon.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class NumberNode extends CommandNode
{
    // Private fields.
    private final Function<CommandData, List<String>> _suggestionSupplier;
    private final NumberNodeType _type;
    private final boolean _allowNaN;
    private final boolean _allowInfinity;


    // Constructors.
    public NumberNode(Consumer<CommandData> executor,
                      Function<CommandData, List<String>> suggestionSupplier,
                      String parsedDataKey,
                      NumberNodeType type,
                      boolean allowNaN,
                      boolean allowInfinity)
    {
        super(executor, parsedDataKey);

        _suggestionSupplier = suggestionSupplier;
        _type = Objects.requireNonNull(type, "type is  null");

        _allowInfinity = allowInfinity;
        _allowNaN = allowNaN;
    }


    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return _suggestionSupplier != null ? _suggestionSupplier.apply(data) : new ArrayList<>();
    }

    @Override
    public boolean ParseCommand(CommandData data)
    {
        try
        {
            switch (_type)
            {
                case Double ->
                {
                    double Value = Double.parseDouble(data.ReadWord());
                    if ((!_allowNaN && Double.isNaN(Value)) || (!_allowInfinity && Double.isInfinite(Value)))
                    {
                        return false;
                    }
                    AddParsedData(Value, data);
                }
                case Float ->
                {
                    float Value = Float.parseFloat(data.ReadWord());
                    if ((!_allowNaN && Float.isNaN(Value)) || (!_allowInfinity && Float.isInfinite(Value)))
                    {
                        return false;
                    }
                    AddParsedData(Value, data);
                }
                case Long -> AddParsedData(Long.parseLong(data.ReadWord()), data);
                case Integer -> AddParsedData(Integer.parseInt(data.ReadWord()), data);
                default-> throw new IllegalStateException("Unknown number node type \"%s\""
                        .formatted(_type.toString()));
            }

            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}