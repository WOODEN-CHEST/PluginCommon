package sus.keiger.plugincommon.player.actionbar;

import io.sentry.protocol.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;
import sus.keiger.plugincommon.IterationSafeList;

import java.util.ArrayList;
import java.util.HashMap;
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

        if (!_actionbarMessages.isEmpty())
        {
            mcPlayer.sendActionBar(_combinedActionbarMessage);
        }
    }

    public void AddMessage(ActionbarMessage message)
    {
        Objects.requireNonNull(message, "message is null");
        if (_actionbarMessages.size() >= MAX_MESSAGES)
        {
            return;
        }

        _actionbarMessages.add(message);
        _actionbarChanged = true;
    }

    public void RemoveMessage(long id)
    {
        for (int i = 0; i < _actionbarMessages.size(); i++)
        {
            if (_actionbarMessages.get(i).GetID() == id)
            {
                _actionbarMessages.remove(i);
                return;
            }
        }
    }

    public void ClearMessages()
    {
        _actionbarMessages.clear();
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
