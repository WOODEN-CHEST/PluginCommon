package sus.keiger.plugincommon.packet.clientbound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerInfoRemovePacket extends ClientBoundGamePacket
{
    // Static fields.
    public static final int ID = 0x3d;


    // Private fields.
    private List<UUID> _playerUUIDs = Collections.emptyList();


    // Constructors.
    public PlayerInfoRemovePacket()
    {
        super(ID);
    }

    public PlayerInfoRemovePacket(PacketContainer packet)
    {
        super(ID);
        Objects.requireNonNull(packet, "packet is null");

        _playerUUIDs = List.copyOf(packet.getUUIDLists().read(0));
    }


    // Methods.
    public void SetUUIDs(List<UUID> uuids)
    {
        _playerUUIDs = List.copyOf(Objects.requireNonNull(uuids, "uuids is null"));
    }

    public List<UUID> GetUUIDs()
    {
        return _playerUUIDs;
    }


    // Inherited methods.
    @Override
    public PacketContainer CreatePacketContainer(ProtocolManager protocolManager)
    {
        PacketContainer Packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        Packet.getUUIDLists().write(0, _playerUUIDs);
        return Packet;
    }
}
