package sus.keiger.plugincommon.packet;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import sus.keiger.plugincommon.PCPluginEvent;
import sus.keiger.plugincommon.packet.clientbound.*;
import sus.keiger.plugincommon.packet.serverbound.ServerBoundGamePacket;

import java.util.*;
import java.util.function.Consumer;

public class PCGamePacketController implements IGamePacketController, PacketListener
{
    // Private fields.
    private final ProtocolManager _protocolManager;
    private final Plugin _plugin;
    private final Map<PacketType, Consumer<PacketEvent>> _packetEventPropagators = new HashMap<>();

    private final ListeningWhitelist _sendingWhitelist = ListeningWhitelist.newBuilder().types(
            PacketType.Play.Server.UPDATE_HEALTH,
            PacketType.Play.Server.PLAYER_INFO,
            PacketType.Play.Server.PLAYER_INFO_REMOVE).normal().build();

    private final ListeningWhitelist _receivingWhitelist = ListeningWhitelist.newBuilder().types().build();



    /* Packet types. */
    private final PCPluginEvent<GamePacketEvent<? extends GamePacket>> _packetSendEvent = new PCPluginEvent<>();
    private final PCPluginEvent<GamePacketEvent<? extends GamePacket>> _packetReceiveEvent = new PCPluginEvent<>();

    private final PCPluginEvent<GamePacketEvent<SetHealthPacket>>
            _setHealthPacketEvent = new PCPluginEvent<>();
    private final PCPluginEvent<GamePacketEvent<PlayerInfoUpdatePacket>>
            _playerInfoUpdatePacketEvent = new PCPluginEvent<>();
    private final PCPluginEvent<GamePacketEvent<PlayerInfoRemovePacket>>
            _playerInfoRemovePacketEvent = new PCPluginEvent<>();
    private final PCPluginEvent<GamePacketEvent<UnloadChunkPacket>>
            _unloadChunkPacketEvent = new PCPluginEvent<>();


    // Constructors.
    public PCGamePacketController(Plugin plugin, ProtocolManager protocolManager)
    {
        _protocolManager = Objects.requireNonNull(protocolManager, "protocolManager is null");
        _plugin = Objects.requireNonNull(plugin, "plugin is null");

        _packetEventPropagators.put(PacketType.Play.Server.UPDATE_HEALTH, this::OnSetHealthSending);
        _packetEventPropagators.put(PacketType.Play.Server.PLAYER_INFO, this::OnPlayerInfoUpdateSending);
        _packetEventPropagators.put(PacketType.Play.Server.PLAYER_INFO_REMOVE, this::OnPlayerInfoRemoveSending);
        _packetEventPropagators.put(PacketType.Play.Server.UNLOAD_CHUNK, this::OnUnloadChunkSending);
    }



    // Private methods.
    private void PropagatePacket(PacketEvent event)
    {
        Consumer<PacketEvent> Propagator = _packetEventPropagators.get(event.getPacketType());
        if (Propagator == null)
        {
            return;
        }

        Propagator.accept(event);
    }

    private <T extends GamePacket>
    void FirePacketEvent(PacketEvent sourceEvent,
                         PCPluginEvent<GamePacketEvent<? extends GamePacket>> genericEvent,
                         PCPluginEvent<GamePacketEvent<T>> event,
                         T packet)
    {
        GamePacketEvent<T> PacketEvent = new GamePacketEvent<>(packet, sourceEvent.getPlayer());
        genericEvent.FireEvent(PacketEvent);
        event.FireEvent(PacketEvent);
        sourceEvent.setCancelled(PacketEvent.GetIsCancelled());

        if (!PacketEvent.GetIsCancelled())
        {
            sourceEvent.setPacket(packet.CreatePacketContainer(_protocolManager));
        }
    }

    private void OnSetHealthSending(PacketEvent event)
    {
        FirePacketEvent(event, _packetSendEvent, _setHealthPacketEvent,
                new SetHealthPacket(event.getPacket()));
    }

    private void OnPlayerInfoUpdateSending(PacketEvent event)
    {
        FirePacketEvent(event, _packetSendEvent, _playerInfoUpdatePacketEvent,
                new PlayerInfoUpdatePacket(event.getPacket()));
    }

    private void OnPlayerInfoRemoveSending(PacketEvent event)
    {
        FirePacketEvent(event, _packetSendEvent, _playerInfoRemovePacketEvent,
                new PlayerInfoRemovePacket(event.getPacket()));
    }

    private void OnUnloadChunkSending(PacketEvent event)
    {
        FirePacketEvent(event, _packetSendEvent, _unloadChunkPacketEvent,
                new UnloadChunkPacket(event.getPacket()));
    }


    // Inherited methods.
    @Override
    public void SendPacket(ClientBoundGamePacket packet, Player player)
    {
        Objects.requireNonNull(packet, "packet is null");
        Objects.requireNonNull(player, "player is null");

        _protocolManager.sendServerPacket(player, packet.CreatePacketContainer(_protocolManager));
    }

    @Override
    public void SendPacket(ClientBoundGamePacket packet, Collection<? extends Player> players)
    {
        Objects.requireNonNull(packet, "packet is null");
        Objects.requireNonNull(players, "players is null");

        _protocolManager.broadcastServerPacket(packet.CreatePacketContainer(_protocolManager), players);
    }


    @Override
    public void ReceiveClientPacket(ServerBoundGamePacket packet, Player player)
    {
        Objects.requireNonNull(packet, "packet is null");
        Objects.requireNonNull(player, "player is null");

        _protocolManager.receiveClientPacket(player, packet.CreatePacketContainer(_protocolManager));
    }

    @Override
    public void StartListeningForPackets()
    {
        _protocolManager.addPacketListener(this);
    }

    @Override
    public void StopListeningForPackets()
    {
        _protocolManager.removePacketListener(this);
    }

    @Override
    public PCPluginEvent<GamePacketEvent<? extends GamePacket>> GetPacketSendEvent()
    {
        return _packetSendEvent;
    }

    @Override
    public PCPluginEvent<GamePacketEvent<? extends GamePacket>> GetPacketReceiveEvent()
    {
        return _packetReceiveEvent;
    }

    @Override
    public PCPluginEvent<GamePacketEvent<SetHealthPacket>> GetSetHealthPacketEvent()
    {
        return _setHealthPacketEvent;
    }

    @Override
    public PCPluginEvent<GamePacketEvent<PlayerInfoUpdatePacket>> GetPlayerInfoUpdatePacketEvent()
    {
        return _playerInfoUpdatePacketEvent;
    }

    @Override
    public PCPluginEvent<GamePacketEvent<PlayerInfoRemovePacket>> GetPlayerInfoRemovePacketEvent()
    {
        return _playerInfoRemovePacketEvent;
    }

    @Override
    public void onPacketSending(PacketEvent event)
    {
        PropagatePacket(event);
    }

    @Override
    public void onPacketReceiving(PacketEvent event)
    {
        PropagatePacket(event);
    }

    @Override
    public ListeningWhitelist getSendingWhitelist()
    {
        return _sendingWhitelist;
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist()
    {
        return _receivingWhitelist;
    }

    @Override
    public Plugin getPlugin()
    {
        return _plugin;
    }
}
