package sus.keiger.plugincommon.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class NumberNode extends CommandNode
{
    // Private fields.
    private final Function<CommandData, List<String>> _suggestionSupplier;
    private final NumberNodeType _type;


    // Constructors.
    public NumberNode(Consumer<CommandData> executor,
                      Function<CommandData, List<String>> suggestionSupplier,
                      String parsedDataKey,
                      NumberNodeType type)
    {
        super(executor, parsedDataKey);

        _suggestionSupplier = suggestionSupplier;
        if (type == null)
        {
            throw new IllegalArgumentException("type is null");
        }
        _type = type;
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
                case Double -> AddParsedData(Double.parseDouble(data.ReadWord()), data);
                case Float -> AddParsedData(Float.parseFloat(data.ReadWord()), data);
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