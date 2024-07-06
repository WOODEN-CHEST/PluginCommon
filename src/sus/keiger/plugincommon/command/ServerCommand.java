package sus.keiger.plugincommon.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


public class ServerCommand extends CommandNode implements TabExecutor
{
    // Private fields.
    private final String _label;



    // Constructors.
    public ServerCommand(String label, Consumer<CommandData> executor)
    {
        super(executor, null);

        if (label == null)
        {
            throw new IllegalArgumentException();
        }
        if (Bukkit.getPluginCommand(label) == null)
        {
            throw new IllegalArgumentException("Command \"%s\" not found".formatted(label));
        }

        PluginCommand Command = Bukkit.getPluginCommand(label);
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
    private void VerifyName(Command command)
    {
        if (!_label.equals(command.getName()))
        {
            throw new IllegalStateException("Label mismatch. Command's label is \"%s\", got \"%s\"."
                    .formatted(_label, command.getName()));
        }
    }


    // Inherited methods.
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        VerifyName(command);
        CommandData Data = new CommandData(_label, args, sender);
        Data.MoveIndexToNextNonWhitespace();
        try
        {
            ExecuteCommand(Data);
        }
        catch (Exception e)
        {
            Data.SetFeedback(Component.text("A critical exception occurred while executing this command. " +
                    "Please report this to an admin immediately. Error message: %s".formatted(e.getMessage()))
                    .color(NamedTextColor.RED));
            Bukkit.getLogger().severe("(%s) Stack trace: %s".formatted(e.getMessage(),
                    String.join(" ", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList())));
        }

        if (Data.GetFeedback() != null)
        {
            sender.sendMessage(Data.GetFeedback());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        VerifyName(command);
        CommandData Data = new CommandData(_label, args, sender);
        Data.MoveIndexToNextNonWhitespace();
        try
        {
            if (!TabCommand(Data))
            {
                SuggestSubNodes(Data);
            }
        }
        catch (Exception e)
        {
            Data.SetFeedback(Component.text("A critical exception occurred while tabbing this command. " +
                    "Please report this to an admin immediately.").color(NamedTextColor.RED));
            Bukkit.getLogger().severe("(%s) Stack trace: %s".formatted(e.getMessage(),
                    String.join(" ", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList())));
        }

        return Data.GetRecommendations();
    }

    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return Collections.singletonList(_label);
    }

    @Override
    public final boolean ParseCommand(CommandData data)
    {
        return true;
    }
}