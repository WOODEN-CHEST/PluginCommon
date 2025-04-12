package sus.keiger.plugincommon.packet.serverbound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

import java.util.Objects;

public class RenameItemPacket extends ServerBoundGamePacket
{
    // Static fields.
    public static final int ID = 0x2E;


    // Private fields.
    private String _itemName = "";


    // Constructors.
    public RenameItemPacket()
    {
        super(ID);
    }

    public RenameItemPacket(PacketContainer packet)
    {
        super(ID);

        _itemName = packet.getStrings().read(0);
    }


    // Methods.
    public String GetItemName()
    {
        return _itemName;
    }

    public void SetItemName(String name)
    {
        _itemName = Objects.requireNonNull(name);
    }


    // Inherited methods.
    @Override
    public PacketContainer CreatePacketContainer(ProtocolManager protocolManager)
    {
        PacketContainer Packet = protocolManager.createPacket(PacketType.Play.Client.ITEM_NAME);
        Packet.getStrings().write(0, _itemName);
        return Packet;
    }
}
