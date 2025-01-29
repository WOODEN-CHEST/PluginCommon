package sus.keiger.plugincommon.command;

import org.bukkit.NamespacedKey;

import java.util.Collections;
import java.util.List;

import java.util.function.Consumer;
import java.util.function.Function;

public class NamespacedKeyNode extends CommandNode
{
    // Private fields.
    private final Function<CommandData, List<String>> _suggestionSupplier;

    public NamespacedKeyNode(Consumer<CommandData> executor,
                             Function<CommandData, List<String>> suggestionSupplier,
                             String parsedDataKey)
    {
        super(executor, parsedDataKey);
        _suggestionSupplier = suggestionSupplier;
    }


    // Inherited methods.
    @Override
    public boolean ParseCommand(CommandData data)
    {
        String Word = data.ReadWord();
        NamespacedKey Key = NamespacedKey.fromString(Word);
        if (Key != null)
        {
            AddParsedData(Key, data);
            return true;
        }
        return false;
    }

    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return _suggestionSupplier != null ? _suggestionSupplier.apply(data) : Collections.emptyList();
    }
}
