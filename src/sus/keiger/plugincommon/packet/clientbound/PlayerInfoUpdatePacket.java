package sus.keiger.plugincommon.packet.clientbound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.Bukkit;
import sus.keiger.plugincommon.packet.UninitializedPacketException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PlayerInfoUpdatePacket extends ClientBoundGamePacket
{
    // Static fields.
    public static final int ID = 0x3e;


    // Private fields.
    private List<PacketPlayerInfo> _playerInfo;
    private Set<EnumWrappers.PlayerInfoAction> _actions;


    // Constructors.
    public PlayerInfoUpdatePacket()
    {
        super(ID);
    }

    public PlayerInfoUpdatePacket(PacketContainer packet)
    {
        super(ID);
        Objects.requireNonNull(packet, "packet is null");

        _actions = Set.copyOf(packet.getPlayerInfoActions().read(0));
        _playerInfo = packet.getPlayerInfoDataLists().read(1).stream().map(packetData ->
        {

            String Name = packetData.getProfile().getName();
            int Ping = packetData.getLatency();
            EnumWrappers.NativeGameMode GameMode = packetData.getGameMode();
            WrappedChatComponent TabName = packetData.getDisplayName();

            PacketPlayerInfo Info = new PacketPlayerInfo(Bukkit.getPlayer(packetData.getProfileId()));
            if (Name != null)
            {
                Info.SetPlayerName(Name);
            }
            Info.SetPing(Ping);
            if (GameMode != null)
            {
                Info.SetGameMode(GameMode.toBukkit());
            }
            if (TabName != null)
            {
                Info.SetTabName(JSONComponentSerializer.json().deserialize(TabName.getJson()));
            }

            return Info;
        }).toList();
    }


    // Methods.
    public void SetPlayerInfo(List<PacketPlayerInfo> info)
    {
        _playerInfo = List.copyOf(Objects.requireNonNull(info, "info is null"));
    }

    public List<PacketPlayerInfo> GetPlayerInfo()
    {
        return _playerInfo;
    }

    public void SetPlayerInfoActions(Set<EnumWrappers.PlayerInfoAction> actions)
    {
        _actions = Set.copyOf(Objects.requireNonNull(actions, "actions is null"));
    }

    public Set<EnumWrappers.PlayerInfoAction> GetPlayerInfoAction()
    {
        return _actions;
    }


    // Inherited methods.
    @Override
    public PacketContainer CreatePacketContainer(ProtocolManager protocolManager)
    {
        if (_playerInfo == null)
        {
            throw new UninitializedPacketException("PlayerInfo list not initialized.");
        }
        if (_actions == null)
        {
            throw new UninitializedPacketException("PlayerInfoAction list not initialized.");
        }


        PacketContainer Packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);

        Packet.getPlayerInfoActions().write(0, _actions);
        Packet.getPlayerInfoDataLists().write(1, _playerInfo.stream().map(info -> new PlayerInfoData(
                WrappedGameProfile.fromPlayer(info.GetPlayer()).withName(info.GetPlayerName()), info.GetPing(),
                EnumWrappers.NativeGameMode.fromBukkit(info.GetGameMode()),
                WrappedChatComponent.fromJson(JSONComponentSerializer.json().serialize(info.GetTabName()))))
                .toList());

        return Packet;
    }
}
