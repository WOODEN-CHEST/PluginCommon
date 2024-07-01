package sus.keiger.plugincommon.command;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ListNode extends CommandNode
{
    // Private fields.
    private final List<String> _options;
    private final Function<CommandData, List<String>> _optionSupplier;


    // Constructors.
    public ListNode(Consumer<CommandData> executor, String parsedDataKey, String ... options)
    {
        super(executor, parsedDataKey);

        if (options == null)
        {
            throw new IllegalArgumentException("options are null");
        }
        if (options.length == 0)
        {
            throw new IllegalArgumentException("Length of options is 0");
        }
        for (String Option : options)
        {
            if (Option.isBlank())
            {
                throw new IllegalArgumentException("List option may not be blank.");
            }
        }

        _options = List.of(options);
        _optionSupplier = null;
    }

    public ListNode(Consumer<CommandData> executor,
                    String parsedDataKey,
                    Function<CommandData, List<String>> optionSupplier)
    {
        super(executor, parsedDataKey);

        if (optionSupplier == null)
        {
            throw new IllegalArgumentException("optionSupplier is null");
        }

        _options = null;
        _optionSupplier = optionSupplier;
    }


    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return _options != null ? _options : _optionSupplier.apply(data).stream()
                .map(Option -> Option.contains(" ") ? "\"%s\"".formatted(Option) : Option).toList();
    }

    @Override
    public boolean ParseCommand(CommandData data)
    {
        List<String> Options =  _options != null ? _options : _optionSupplier.apply(data);
        String Word = (data.IsMoreDataAvailable() && data.GetCommand().charAt(data.GetIndex()) == StringNode.QUOTE) ?
                StringNode.ParseQuotedString(data) : data.ReadWord();

        if (Options.contains(Word))
        {
            AddParsedData(Word, data);
            return true;
        }
        return false;
    }
}