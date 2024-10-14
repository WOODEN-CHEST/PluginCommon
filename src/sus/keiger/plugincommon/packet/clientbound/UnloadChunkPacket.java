package sus.keiger.plugincommon.packet.clientbound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;

import java.util.Objects;

public class UnloadChunkPacket extends ClientBoundGamePacket
{
    // Static fields.
    public static final int ID = 0x21;


    // Private fields.
    private int _chunkX;
    private int _chunkZ;


    // Constructors.
    public UnloadChunkPacket()
    {
        super(ID);
    }

    public UnloadChunkPacket(PacketContainer packet)
    {
        super(ID);
        Objects.requireNonNull(packet, "packet is null");

        ChunkCoordIntPair Pair = packet.getChunkCoordIntPairs().read(0);
        _chunkX = Pair.getChunkX();
        _chunkZ = Pair.getChunkZ();
    }


    // Methods.
    public void SetChunkX(int x)
    {
        _chunkX = x;
    }

    public void SetChunkZ(int z)
    {
        _chunkZ = z;
    }

    public int GetChunkX()
    {
        return _chunkX;
    }

    public int GetChunkZ()
    {
        return _chunkZ;
    }


    // Inherited methods.
    @Override
    public PacketContainer CreatePacketContainer(ProtocolManager protocolManager)
    {
        PacketContainer Packet = protocolManager.createPacket(PacketType.Play.Server.UNLOAD_CHUNK);
        Packet.getChunkCoordIntPairs().write(0, new ChunkCoordIntPair(_chunkX, _chunkZ));
        return Packet;
    }
}
