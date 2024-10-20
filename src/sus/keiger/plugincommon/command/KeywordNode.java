package sus.keiger.plugincommon.command;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class KeywordNode extends CommandNode
{
    // Private fields.
    private final String _keyword;


    // Constructors.
    public KeywordNode(String keyword, Consumer<CommandData> executor, String parsedDataKey)
    {
        super(executor, parsedDataKey);

        if (keyword == null)
        {
            throw new IllegalArgumentException("Keyword is null");
        }
        if (keyword.isBlank())
        {
            throw new IllegalArgumentException("Keyword may not be blank.");
        }

        _keyword = keyword;
    }


    // Methods.
    public String GetKeyword()
    {
        return _keyword;
    }



    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return Collections.singletonList(_keyword.contains(" ") ? "\"%s\"".formatted(_keyword) : _keyword);
    }

    @Override
    public boolean ParseCommand(CommandData data)
    {
        data.MoveIndexToNextNonWhitespace();
        String Keyword = data.IsMoreDataAvailable() && data.GetCommand().charAt(data.GetIndex()) == StringNode.QUOTE ?
                StringNode.ParseQuotedString(data) : data.ReadWord();
        if (_keyword.equals(Keyword))
        {
            AddParsedData(Keyword, data);
            return true;
        }
        return false;
    }
}
