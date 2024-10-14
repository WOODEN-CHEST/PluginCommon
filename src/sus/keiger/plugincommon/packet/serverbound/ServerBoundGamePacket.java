package sus.keiger.plugincommon.packet.serverbound;

import sus.keiger.plugincommon.packet.GamePacket;

public abstract class ServerBoundGamePacket extends GamePacket
{
    // Constructors.
    public ServerBoundGamePacket(int id)
    {
        super(id);
    }
}
