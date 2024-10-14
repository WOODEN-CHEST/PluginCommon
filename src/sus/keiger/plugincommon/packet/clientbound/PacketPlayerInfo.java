package sus.keiger.plugincommon.packet.clientbound;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PacketPlayerInfo
{
    // Private fields.
    private Player _player;
    private String _playerName;
    private GameMode _gameMode;
    private int _ping;
    private Component _tabName;


    // Constructors.
    public PacketPlayerInfo(Player player)
    {
        SetPlayer(player);
        SetPing(player.getPing());
        SetGameMode(player.getGameMode());
        SetTabName(Component.text(player.getName()));
        SetPlayerName(player.getName());
    }



    // Methods.
    public Player GetPlayer()
    {
        return _player;
    }

    public String GetPlayerName()
    {
        return _playerName;
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

    public void SetPlayer(Player player)
    {
        _player = Objects.requireNonNull(player, "player is null");
    }

    public void SetPlayerName(String name)
    {
        _playerName = Objects.requireNonNull(name, "name is null");
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