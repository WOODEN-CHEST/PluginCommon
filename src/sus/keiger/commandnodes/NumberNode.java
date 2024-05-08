package sus.keiger.bsripoff.command;

import sus.keiger.bsripoff.BSRipoff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NumberNode extends CommandNode
{
    // Private fields.
    private final Function<CommandData, List<String>> _suggestionSupplier;
    private final NumberNodeType _type;


    // Constructors.
    public NumberNode(BiConsumer<CommandData, HashMap<String, Object>> executor,
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
    public boolean ParseCommand(CommandData data, HashMap<String, Object> parsedData)
    {
        try
        {
            switch (_type)
            {
                case Double -> AddParsedData(Double.parseDouble(data.ReadWord()), parsedData);
                case Float -> AddParsedData(Float.parseFloat(data.ReadWord()), parsedData);
                case Long -> AddParsedData(Long.parseLong(data.ReadWord()), parsedData);
                case Integer -> AddParsedData(Integer.parseInt(data.ReadWord()), parsedData);
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