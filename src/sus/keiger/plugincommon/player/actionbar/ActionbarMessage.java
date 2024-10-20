package sus.keiger.plugincommon.player.actionbar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.NullArgumentException;
import sus.keiger.plugincommon.ITickable;

import java.util.Random;

public class ActionbarMessage implements ITickable
{
    // Private fields.
    private final long _id;
    private int _ticksLeft;
    private Component _contents;


    // Constructors.
    public ActionbarMessage(int lifespanTicks, Component contents)
    {
        this(lifespanTicks, contents, new Random().nextLong());
    }

    public ActionbarMessage(int lifespanTicks, Component contents, long id)
    {
        if (contents == null)
        {
            throw new NullArgumentException("contents are null");
        }

        _ticksLeft = lifespanTicks;
        _contents = contents;
        _id = id;
    }


    // Methods.
    public long GetID()
    {
        return _id;
    }

    public void SetContents(TextComponent contents)
    {
        if (contents == null)
        {
            throw new NullArgumentException("contents are null");
        }

        _contents = contents;
    }

    public Component GetContents()
    {
        return _contents;
    }

    public void SetRemainingLifetime(int value)
    {
        _ticksLeft = Math.max(0, value);
    }

    public int GetRemainingLifetime()
    {
        return _ticksLeft;
    }

    // Inherited methods.
    @Override
    public String toString()
    {
        return _contents.toString();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof ActionbarMessage Message)
        {
            return Message.GetID() == _id;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return (int)_id;
    }

    @Override
    public void Tick()
    {
        _ticksLeft--;;
    }
}