package sus.keiger.plugincommon.packet.clientbound;

import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import sus.keiger.plugincommon.player.PlayerFunctions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PacketPlayerInfo
{
    // Private fields.
    private Player _player;
    private String _customName;
    private GameMode _gameMode;
    private int _ping;
    private Component _tabName;
    private Map<String, ProfileProperty> _properties = new HashMap<>();


    // Constructors.
    public PacketPlayerInfo(Player player)
    {
        SetPlayer(player);
        SetPing(player.getPing());
        SetGameMode(player.getGameMode());
        SetTabName(Component.text(player.getName()));
        SetName(player.getName());

        player.getPlayerProfile().getProperties().stream().filter(
                property -> property.getName().equals(PlayerFunctions.PROFILE_KEY_TEXTURES)).findFirst()
                .ifPresent(property -> SetTexture(property.getValue(), property.getSignature()));
    }


    // Methods.
    public Player GetPlayer()
    {
        return _player;
    }

    public String GetName()
    {
        return _customName;
    }

    public GameMode GetGameMode()
    {
        return _gameMode;
    }

    public int GetPing()
    {
        return _ping;
    }

    public Component GetTabName()
    {
        return _tabName;
    }

    public ProfileProperty GetTexture()
    {
        return _properties.get(PlayerFunctions.PROFILE_KEY_TEXTURES);
    }

    public void SetPlayer(Player player)
    {
        _player = Objects.requireNonNull(player, "player is null");
    }

    public void SetName(String name)
    {
        _customName = Objects.requireNonNull(name, "name is null");
    }

    public void SetTexture(String texture, String signature)
    {
        _properties.put(PlayerFunctions.PROFILE_KEY_TEXTURES,
                new ProfileProperty(PlayerFunctions.PROFILE_KEY_TEXTURES, texture, signature));

    }

    public void SetPing(int ping)
    {
        _ping = ping;
    }

    public void SetGameMode(GameMode gameMode)
    {
        _gameMode = Objects.requireNonNull(gameMode, "gameMode is null");
    }

    public void SetTabName(Component name)
    {
        _tabName = Objects.requireNonNull(name, "name is null");
    }
}