package sus.keiger.plugincommon.packet.clientbound;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

import java.util.Objects;


public class SetHealthPacket extends ClientBoundGamePacket
{
    // Static fields.
    public static final int ID = 0x5D;


    // Private fields.
    private float _health;
    private int _food;
    private float _saturation;


    // Constructors.
    public SetHealthPacket()
    {
        super(ID);
    }

    public SetHealthPacket(PacketContainer packet)
    {
        super(ID);
        Objects.requireNonNull(packet, "packet is null");

        _health = packet.getFloat().read(0);
        _saturation = packet.getFloat().read(1);
        _food = packet.getIntegers().read(0);
    }


    // Methods.
    public float GetHealth()
    {
        return _health;
    }

    public void SetHealth(float value)
    {
        _health = value;
    }

    public int GetFood()
    {
        return -_food;
    }

    public void SetFood(int value)
    {
        _food = value;
    }

    public float GetSaturation()
    {
        return _saturation;
    }

    public void SetSaturation(float value)
    {
        _saturation = value;
    }


    // Inherited methods.
    @Override
    public PacketContainer CreatePacketContainer(ProtocolManager protocolManager)
    {
        Objects.requireNonNull(protocolManager, "protocolManger is null");
        PacketContainer Packet = protocolManager.createPacket(PacketType.Play.Server.UPDATE_HEALTH);

        Packet.getFloat().write(0, _health);
        Packet.getFloat().write(1, _saturation);
        Packet.getIntegers().write(0, _food);

        return Packet;
    }
}