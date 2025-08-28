package sus.keiger.plugincommon.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CommandData
{
    // Private fields.
    private final String _label;
    private final String _command;
    private final CommandSender _sender;
    private Component _feedbackComponents = null;
    private String _feeedbackString = null;
    private List<String> _tabRecommendations = null;
    int _index = 0;
    private final HashMap<String, Object> _parsedData = new HashMap<>();


    // Private fields.
    private CommandStatus _status = CommandStatus.Successful;



    // Constructors.
    public CommandData(String label, String[] args, CommandSender sender)
    {
        if (args == null)
        {
            throw new IllegalArgumentException("Args are null");
        }
        if (sender == null)
        {
            throw new IllegalArgumentException("Sender is null");
        }
        if (label == null)
        {
            throw new IllegalArgumentException("Label is null");
        }

        _command = String.join(" ", args);
        _sender = sender;
        _label = label;
    }


    // Methods.
    /* Getters and setters. */
    public List<String> GetRecommendations()
    {
        return _tabRecommendations;
    }

    public void SetSuggestions(List<String> recommendations)
    {
        if (recommendations == null)
        {
            throw new NullArgumentException("Recommendations are null");
        }

        _tabRecommendations = recommendations;
    }

    public void SetFeedback(String feedback)
    {
        _feeedbackString = feedback;
    }

    public void SetFeedback(Component feedback)
    {
        _feedbackComponents = feedback;
    }

    public Optional<Component> GetFeedback()
    {
        if (_feedbackComponents != null)
        {
            return Optional.of(_feedbackComponents);
        }
        if (_feeedbackString != null)
        {
            NamedTextColor Color = _status == CommandStatus.Successful ?
                    NamedTextColor.WHITE : NamedTextColor.RED;

            return Optional.of(Component.text(_feeedbackString).color(Color));
        }

        return Optional.empty();
    }


    public CommandStatus GetStatus()
    {
        return _status;
    }

    public void SetStatus(CommandStatus status)
    {
        if  (status == null)
        {
            throw new IllegalArgumentException("Status is null");
        }

        _status = status;
    }

    public int GetIndex()
    {
        return _index;
    }

    public void SetIndex(int index)
    {
        _index = index;
    }

    public CommandSender GetSender()
    {
        return _sender;
    }

    public Optional<Player> GetPlayerSender()
    {
        if (_sender instanceof Player PlayerSender)
        {
            return Optional.of(PlayerSender);
        }
        return Optional.empty();
    }

    public Location GetLocation()
    {
        if (_sender instanceof Entity Sender)
        {
            return Sender.getLocation();
        }
        return new Location(Bukkit.getServer().getWorld(new NamespacedKey("minecraft", "overworld")),
                0d, 0d, 0d, 0f, 0f);
    }

    public String GetLabel()
    {
        return _label;
    }

    public String GetCommand()
    {
        return _command;
    }

    public void AddParsedData(String key, Object value)
    {
        _parsedData.put(key, value);
    }

    @SuppressWarnings("unchecked") // Yay, suppressing warnings!
    public <T> T GetParsedData(String key)
    {
        Object TargetObject = _parsedData.get(key);
        if (TargetObject == null)
        {
            return null;
        }
        return (T)TargetObject;
    }


    /* Parsing functions. */
    public boolean IsMoreDataAvailable()
    {
        return _index < _command.length();
    }

    public void MoveIndexToNextNonWhitespace()
    {
        while ((_index < _command.length()) && (Character.isWhitespace(_command.charAt(_index))))
        {
            _index++;
        }
    }

    public String ReadWord()
    {
        StringBuilder WordBuilder = new StringBuilder();

        while ((_index < _command.length()) && !Character.isWhitespace(_command.charAt(_index)))
        {
            WordBuilder.append(_command.charAt(_index));
            _index++;
        }

        return WordBuilder.toString();
    }


    public int CountWordsLeft()
    {
        boolean IsInWord = false;
        int Count = 0;
        int LocalIndex = _index;

        while (LocalIndex < _command.length())
        {
            if (Character.isWhitespace(_command.charAt(LocalIndex)))
            {
                IsInWord = false;
            }
            else
            {
                if (!IsInWord)
                {
                    Count++;
                }
                IsInWord = true;
            }

            LocalIndex++;
        }

        return Count;
    }
}