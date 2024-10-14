package sus.keiger.plugincommon.packet.clientbound;

import org.bukkit.entity.Player;
import sus.keiger.plugincommon.packet.GamePacket;

public abstract class ClientBoundGamePacket extends GamePacket
{
    // Constructors.
    public ClientBoundGamePacket(int id)
    {
        super(id);
    }
}