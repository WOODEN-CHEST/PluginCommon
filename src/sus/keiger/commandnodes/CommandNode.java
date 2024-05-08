package sus.keiger.bsripoff.command;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class CommandNode
{
    // Private fields.
    private final BiConsumer<CommandData, HashMap<String, Object>> _executor;
    private final List<CommandNode> _subNodes = new ArrayList<>();
    private final String _parsedDataKey;


    // Constructors.
    public CommandNode(BiConsumer<CommandData, HashMap<String, Object>> executor, String parsedDataKey)
    {
        _executor = executor;
        _parsedDataKey = parsedDataKey;
    }


    // Methods.
    public List<CommandNode> GetSubNodes()
    {
        return new ArrayList<>(_subNodes);
    }

    public boolean ExecuteCommand(CommandData data, HashMap<String, Object> parsedData)
    {
        boolean IsThisCommand = ParseCommand(data, parsedData);
        if (!IsThisCommand)
        {
            return false;
        }

        data.MoveIndexToNextNonWhitespace();
        if (data.IsMoreDataAvailable())
        {
            TryPassExecuteToNextNode(data, parsedData);
        }
        else if (_executor != null)
        {
            _executor.accept(data, parsedData);
        }
        else
        {
            TellExecuteError(data);
        }
        return true;
    }

    public boolean TabCommand(CommandData data, HashMap<String, Object> parsedData)
    {
        boolean IsThisCommand = ParseCommand(data, parsedData);
        if (!IsThisCommand)
        {
            return false;
        }

        if (data.IsMoreDataAvailable())
        {
            data.MoveIndexToNextNonWhitespace();
            TryPassTabToNextNode(data, parsedData);
        }
        else
        {
            return false;

        }
        return true;
    }


    public void AddSubNode(CommandNode subNode)
    {
        if (subNode == null)
        {
            throw new NullArgumentException("subNode is null");
        }

        if (!_subNodes.contains(subNode))
        {
            _subNodes.add(subNode);
        }
    }

    public abstract boolean ParseCommand(CommandData data, HashMap<String, Object> parsedData);

    public abstract List<String> GetSelfSuggestions(CommandData data);

    public void SuggestSubNodes(CommandData data)
    {
        int SavedIndex = data.GetIndex();
        List<String> Suggestions = new ArrayList<>();
        for (CommandNode SubNode : _subNodes)
        {
            Suggestions.addAll(SubNode.GetSelfSuggestions(data));
            data.SetIndex(SavedIndex);
        }
        data.SetSuggestions(Suggestions);
    }

    public void ValidateNode()
    {
        if ((_subNodes.size() == 0) && (_executor == null))
        {
            throw new IllegalStateException("Node has no executor and sub-nodes.");
        }
    }

    public String GetParsedDataKey()
    {
        return _parsedDataKey;
    }

    public void AddParsedData(Object value, HashMap<String, Object> parsedData)
    {
        if (_parsedDataKey != null)
        {
            parsedData.put(_parsedDataKey, value);
        }
    }


    // Private methods.
    /* Parsing. */
    private void TryPassExecuteToNextNode(CommandData data, HashMap<String, Object> parsedData)
    {
        if (_subNodes.size() == 0)
        {
            data.SetStatus(CommandStatus.Unsuccessful);
            data.SetFeedback("Trailing command arguments: \"%s\"".formatted(
                    data.GetCommand().substring(data.GetIndex()).trim()));
            return;
        }

        int SavedIndex = data.GetIndex();
        for (CommandNode SubNode : _subNodes)
        {
            if (SubNode.ExecuteCommand(data, parsedData))
            {
                return;
            }
            data.SetIndex(SavedIndex);
        }

        TellExecuteError(data);
    }

    private void TryPassTabToNextNode(CommandData data, HashMap<String, Object> parsedData)
    {
        int SavedIndex = data.GetIndex();
        for (CommandNode SubNode : _subNodes)
        {
            if (SubNode.TabCommand(data, parsedData))
            {
                return;
            }
            data.SetIndex(SavedIndex);
        }

        SuggestSubNodes(data);
    }


    /* Errors. */
    private void TellBrokenCommandError(CommandData data)
    {
        data.SetFeedback("Badly formatted command node with no sub-nodes and no executor. " +
                "This is a bug and should be reported to the server's admins.");
        Bukkit.getLogger().warning(("Badly made command? Command \"%s\" with args ".formatted(data.GetLabel()) +
                "\"%s\" got a case with no executor and no sub-nodes.").formatted(data.GetCommand()));
    }

    private void TellInvalidCommandError(CommandData data)
    {
        List<String> Suggestions = new ArrayList<>();
        int SavedIndex = data.GetIndex();
        for (CommandNode SubNode : _subNodes)
        {
            Suggestions.addAll(SubNode.GetSelfSuggestions(data));
            data.SetIndex(SavedIndex);
        }

        String ReasonMsg = data.GetCommand().substring(data.GetIndex()).trim().isBlank()
                ? "Incomplete command" : "Invalid command";
        String Feedback = "%s! Expected [%s]".formatted(ReasonMsg, String.join(" | ",  Suggestions));
        data.SetFeedback(Feedback);
    }

    private void TellExecuteError(CommandData data)
    {
        data.SetStatus(CommandStatus.Unsuccessful);
        if ((_subNodes.size() == 0) && (_executor == null))
        {
            TellBrokenCommandError(data);
        }
        else
        {
            TellInvalidCommandError(data);
        }
    }
}