package sus.keiger.plugincommon.command;

import java.util.List;
import java.util.function.Consumer;

public class BooleanNode extends CommandNode
{
    // Public static fields.
    public static final String TRUE = "true";
    public static final String FALSE = "false";


    // Constructors.
    public BooleanNode(Consumer<CommandData> executor, String parsedDataKey)
    {
        super(executor, parsedDataKey);
    }


    // Inherited methods.
    @Override
    public boolean ParseCommand(CommandData data)
    {
        String Word = data.ReadWord().toLowerCase().strip();

        if (Word.equals(TRUE))
        {
            AddParsedData(true, data);
            return true;
        }
        else if (Word.equals(FALSE))
        {
            AddParsedData(false, data);
            return true;
        }
        return false;
    }

    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return List.of(TRUE, FALSE);
    }
}