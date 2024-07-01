package sus.keiger.plugincommon.command;

import java.util.List;
import java.util.function.Consumer;

public class TextNode extends CommandNode
{
    // Constructors.
    public TextNode(Consumer<CommandData> executor, String parsedDataKey)
    {
        super(executor, parsedDataKey);
    }


    // Inherited methods.
    @Override
    public boolean ParseCommand(CommandData data)
    {
        data.MoveIndexToNextNonWhitespace();
        if (data.IsMoreDataAvailable())
        {
            data.AddParsedData(GetParsedDataKey(), data.GetCommand().substring(data.GetIndex()));
            data.SetIndex(data.GetCommand().length());
            return true;
        }
        return false;
    }

    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return List.of();
    }
}