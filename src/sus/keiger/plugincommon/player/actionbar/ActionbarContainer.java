package sus.keiger.plugincommon.player.actionbar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import sus.keiger.plugincommon.IterationSafeList;

import java.util.List;
import java.util.Objects;

public class ActionbarContainer
{
    // Static fields.
    public static final int MAX_MESSAGES = 10;


    // Private fields.
    private final List<ActionbarMessage> _actionbarMessages = new IterationSafeList<>();
    private Component _combinedActionbarMessage;
    private boolean _actionbarChanged = false;


    // Constructors.
    public ActionbarContainer() { }


    // Methods.
    public void Tick(Player mcPlayer)
    {
        if (_actionbarMessages.isEmpty())
        {
            return;
        }

        for (ActionbarMessage Message : _actionbarMessages)
        {
            Message.Tick();
            if (Message.GetRemainingLifetime() < 0)
            {
                _actionbarMessages.remove(Message);
                _actionbarChanged = true;
            }
        }

        if (_actionbarChanged)
        {
            BuildActionbarMessage();
        }

        mcPlayer.sendActionBar(_combinedActionbarMessage);
    }

    public void AddMessage(ActionbarMessage message)
    {
        Objects.requireNonNull(message, "message is null");
        if (_actionbarMessages.size() >= MAX_MESSAGES)
        {
            return;
        }

        _actionbarChanged = true;
        for (int i = 0; i < _actionbarMessages.size(); i++)
        {
            if (_actionbarMessages.get(i).GetID() == message.GetID())
            {
                _actionbarMessages.set(i, message);
                return;
            }
        }

        _actionbarMessages.add(message);
    }

    public void RemoveMessage(long id)
    {
        for (int i = 0; i < _actionbarMessages.size(); i++)
        {
            if (_actionbarMessages.get(i).GetID() == id)
            {
                _actionbarMessages.remove(i);
                _actionbarChanged = true;
                return;
            }
        }
    }

    public void ClearMessages()
    {
        _actionbarMessages.clear();
        _actionbarChanged = true;
    }


    // Private methods.
    private void BuildActionbarMessage()
    {
        TextComponent.Builder Builder = Component.text();

        boolean HadElement = false;
        for (ActionbarMessage Message : _actionbarMessages)
        {
            if (HadElement)
            {
                Builder.append(Component.text(" | ").color(NamedTextColor.WHITE));
            }
            Builder.append(Message.GetContents());
            HadElement = true;
        }

        _combinedActionbarMessage = Builder.build();
        _actionbarChanged = false;
    }

}
