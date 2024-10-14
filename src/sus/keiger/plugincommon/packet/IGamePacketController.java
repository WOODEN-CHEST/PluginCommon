package sus.keiger.plugincommon.packet;

import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import sus.keiger.plugincommon.PCPluginEvent;
import sus.keiger.plugincommon.packet.clientbound.ClientBoundGamePacket;
import sus.keiger.plugincommon.packet.clientbound.SetHealthPacket;
import sus.keiger.plugincommon.packet.serverbound.ServerBoundGamePacket;

import java.util.Collection;
import java.util.List;

public interface IGamePacketController
{
    // Methods.
    void SendPacket(ClientBoundGamePacket packet, Player player);
    void SendPacket(ClientBoundGamePacket packet, Collection<? extends  Player> players);
    void ReceiveClientPacket(ServerBoundGamePacket packet, Player player);
    void StartListeningForPackets();
    void StopListeningForPackets();


    PCPluginEvent<GamePacketEvent<? extends GamePacket>> GetPacketSendEvent();
    PCPluginEvent<GamePacketEvent<? extends GamePacket>> GetPacketReceiveEvent();
    PCPluginEvent<GamePacketEvent<SetHealthPacket>> GetSetHealthPacketEvent();
}