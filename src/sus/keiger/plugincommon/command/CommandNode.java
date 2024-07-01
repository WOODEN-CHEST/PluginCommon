package sus.keiger.plugincommon.command;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class CommandNode
{
    // Private fields.
    private final Consumer<CommandData> _executor;
    private final List<CommandNode> _subNodes = new ArrayList<>();
    private final String _parsedDataKey;


    // Constructors.
    public CommandNode(Consumer<CommandData> executor, String parsedDataKey)
    {
        _executor = executor;
        _parsedDataKey = parsedDataKey;
    }


    // Methods.
    public List<CommandNode> GetSubNodes()
    {
        return new ArrayList<>(_subNodes);
    }

    public boolean ExecuteCommand(CommandData data)
    {
        boolean IsThisCommand = ParseCommand(data);
        if (!IsThisCommand)
        {
            return false;
        }

        data.MoveIndexToNextNonWhitespace();
        if (data.IsMoreDataAvailable())
        {
            TryPassExecuteToNextNode(data);
        }
        else if (_executor != null)
        {
            _executor.accept(data);
        }
        else
        {
            TellExecuteError(data);
        }
        return true;
    }

    public boolean TabCommand(CommandData data)
    {
        boolean IsThisCommand = ParseCommand(data);
        if (!IsThisCommand)
        {
            return false;
        }

        if (data.IsMoreDataAvailable())
        {
            data.MoveIndexToNextNonWhitespace();
            TryPassTabToNextNode(data);
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

    public abstract boolean ParseCommand(CommandData data);

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
        if (_subNodes.isEmpty() && (_executor == null))
        {
            throw new IllegalStateException("Node has no executor and sub-nodes.");
        }
    }

    public String GetParsedDataKey()
    {
        return _parsedDataKey;
    }

    public void AddParsedData(Object value, CommandData data)
    {
        if (_parsedDataKey != null)
        {
            data.AddParsedData(_parsedDataKey, value);
        }
    }


    // Private methods.
    /* Parsing. */
    private void TryPassExecuteToNextNode(CommandData data)
    {
        if (_subNodes.isEmpty())
        {
            data.SetStatus(CommandStatus.Unsuccessful);
            data.SetFeedback("Trailing command arguments: \"%s\"".formatted(
                    data.GetCommand().substring(data.GetIndex()).trim()));
            return;
        }

        int SavedIndex = data.GetIndex();
        for (CommandNode SubNode : _subNodes)
        {
            if (SubNode.ExecuteCommand(data))
            {
                return;
            }
            data.SetIndex(SavedIndex);
        }

        TellExecuteError(data);
    }

    private void TryPassTabToNextNode(CommandData data)
    {
        int SavedIndex = data.GetIndex();
        for (CommandNode SubNode : _subNodes)
        {
            if (SubNode.TabCommand(data))
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
        String Feedback = "%s! %s".formatted(ReasonMsg, Suggestions.isEmpty()
                ? "" : "Expected [%s]".formatted(String.join(" | ",  Suggestions)));
        data.SetFeedback(Feedback);
    }

    private void TellExecuteError(CommandData data)
    {
        data.SetStatus(CommandStatus.Unsuccessful);
        if (_subNodes.isEmpty() && (_executor == null))
        {
            TellBrokenCommandError(data);
        }
        else
        {
            TellInvalidCommandError(data);
        }
    }
}