package sus.keiger.bsripoff.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import sus.keiger.bsripoff.BSRipoff;

import java.util.List;

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

    public Component GetFeedback()
    {
        if (_feedbackComponents != null)
        {
            return _feedbackComponents;
        }
        if (_feeedbackString != null)
        {
            NamedTextColor Color = _status == CommandStatus.Successful ?
                    NamedTextColor.WHITE : NamedTextColor.RED;


            return Component.text(_feeedbackString).color(Color);
        }

        return null;
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

    public Location GetLocation()
    {
        if (_sender instanceof Entity Sender)
        {
            return Sender.getLocation();
        }
        return new Location(BSRipoff.GetPlugin().GetServerManager().GetOverworld(), 0d, 0d, 0d, 0f, 0f);
    }

    public String GetLabel()
    {
        return _label;
    }

    public String GetCommand()
    {
        return _command;
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