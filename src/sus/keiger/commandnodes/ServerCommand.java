package sus.keiger.bsripoff.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import sus.keiger.bsripoff.BSRipoff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;


public class ServerCommand extends CommandNode implements TabExecutor
{
    // Private fields.
    private final String _label;



    // Constructors.
    public ServerCommand(String label, BiConsumer<CommandData, HashMap<String, Object>> executor)
    {
        super(executor, null);

        if (label == null)
        {
            throw new IllegalArgumentException();
        }
        if (BSRipoff.GetPlugin().getCommand(label) == null)
        {
            throw new IllegalArgumentException("Command \"%s\" not found".formatted(label));
        }

        PluginCommand Command = BSRipoff.GetPlugin().getCommand(label);
        if (Command != null)
        {
            Command.setTabCompleter(this);
            Command.setExecutor(this);
        }

        _label = label;
    }


    // Methods.
    public String GetLabel()
    {
        return _label;
    }


    // Private methods.
    private void VerifyLabel(String label)
    {
        if (!_label.equals(label))
        {
            throw new IllegalStateException("Label mismatch. Command's label is \"%s\", got \"%s\"."
                    .formatted(_label, label));
        }
    }


    // Inherited methods.
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        VerifyLabel(label);
        CommandData Data = new CommandData(_label, args, sender);
        Data.MoveIndexToNextNonWhitespace();
        ExecuteCommand(Data, new HashMap<String, Object>());

        if (Data.GetFeedback() != null)
        {
            sender.sendMessage(Data.GetFeedback());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        VerifyLabel(label);
        CommandData Data = new CommandData(_label, args, sender);
        Data.MoveIndexToNextNonWhitespace();
        if (!TabCommand(Data, new HashMap<String, Object>()))
        {
            SuggestSubNodes(Data);
        }

        return Data.GetRecommendations();
    }

    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return Collections.singletonList(_label);
    }

    @Override
    public final boolean ParseCommand(CommandData data, HashMap<String, Object> parsedData)
    {
        return true;
    }
}