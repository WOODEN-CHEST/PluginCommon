package sus.keiger.plugincommon.packet;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

public abstract class GamePacket
{
    // Private fields.
    private final int _id;



    // Constructors.
    public GamePacket(int id)
    {
        _id = id;
    }


    // Methods.
    public int GetID()
    {
        return _id;
    }

    public abstract PacketContainer CreatePacketContainer(ProtocolManager protocolManager);
}