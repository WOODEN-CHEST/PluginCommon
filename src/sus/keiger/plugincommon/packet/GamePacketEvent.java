package sus.keiger.plugincommon.packet;

import org.bukkit.entity.Player;

import java.util.Objects;

public class GamePacketEvent<T extends GamePacket>
{
    // Private fields.
    private boolean _isCancelled = false;
    private T _packet;
    private Player _player;


    // Constructors.
    public GamePacketEvent(T packet, Player player)
    {
        _packet = Objects.requireNonNull(packet, "packet is null");
        _player = Objects.requireNonNull(player, "player is null");
    }


    // Methods.
    public boolean GetIsCancelled()
    {
        return _isCancelled;
    }

    public void SetIsCancelled(boolean value)
    {
        _isCancelled = value;
    }

    public Player GetPlayer()
    {
        return _player;
    }

    public T GetPacket()
    {
        return _packet;
    }
}