package sus.keiger.plugincommon.player.actionbar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionbarContainer
{
    // Static fields.
    public static final int MAX_MESSAGES = 10;


    // Private fields.
    private final HashMap<Long, ActionbarMessage> _actionbarMessages = new HashMap<>();
    private Component _combinedActionbarMessage;
    private boolean _actionbarChanged = false;
    private final ArrayList<Long> _messagesToRemove = new ArrayList<>();



    // Constructors.
    public ActionbarContainer() { }


    // Methods.
    public void Tick(Player mcPlayer)
    {
        if (_actionbarMessages.size() == 0)
        {
            return;
        }

        for (ActionbarMessage Message : _actionbarMessages.values() )
        {
            if (Message.Tick())
            {
                _messagesToRemove.add(Message.GetID());
                _actionbarChanged = true;
            }
        }

        if (_messagesToRemove.size() > 0)
        {
            for (long MessageID : _messagesToRemove)
            {
                _actionbarMessages.remove(MessageID);
            }
        }

        if (_actionbarChanged)
        {
            BuildActionbarMessage();
        }

        if (_actionbarMessages.size() != 0)
        {
            mcPlayer.sendActionBar(_combinedActionbarMessage);
        }
        _messagesToRemove.clear();
    }

    public void AddMessage(ActionbarMessage message)
    {
        if (message == null)
        {
            throw new NullArgumentException("message is null");
        }
        if (_actionbarMessages.size() >= MAX_MESSAGES)
        {
            return;
        }
        _actionbarMessages.put(message.GetID(), message);
        _actionbarChanged = true;
    }

    public void RemoveMessage(long id)
    {
        _messagesToRemove.add(id);
    }

    public void ClearMessages()
    {
        _messagesToRemove.addAll(_actionbarMessages.keySet());
    }


    // Private methods.
    private void BuildActionbarMessage()
    {
        TextComponent.Builder Builder = Component.text();

        boolean HadElement = false;
        for (ActionbarMessage Message : _actionbarMessages.values())
        {
            if (HadElement)
            {
                Builder.append(Component.text(" | ").color(NamedTextColor.WHITE));
            }
            Builder.append(Message.GetContents());
            HadElement = true;
        }

        _combinedActionbarMessage = Builder.build();
    }

}
