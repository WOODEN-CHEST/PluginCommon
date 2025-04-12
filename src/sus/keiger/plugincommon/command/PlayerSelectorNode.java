package sus.keiger.plugincommon.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerSelectorNode extends CommandNode
{
    // Static fields.
    public static final String SELECTOR_SELF = "@s";
    public static final String SELECTOR_CLOSEST = "@p";
    public static final String SELECTOR_ALL = "@a";
    public static final String SELECTOR_RANDOM = "@r";


    // Private fields.
    private final boolean _isSpecialSelectorAllowed;
    private final int _maxSelectors;
    private final int _maxPlayers;
    private final Function<CommandData, List<? extends Player>> _playerSupplier;


    // Constructors.
    public PlayerSelectorNode(Consumer<CommandData> executor,
                              boolean isSpecialSelectorAllowed,
                              Function<CommandData, List<? extends Player>> playerSupplier,
                              int maxSelectors,
                              int maxPlayers,
                              String parsedDataKey)
    {
        super(executor, parsedDataKey);

        _isSpecialSelectorAllowed = isSpecialSelectorAllowed;
        _maxSelectors = Math.max(1, maxSelectors);
        _maxPlayers = Math.max(1, maxPlayers);
        _playerSupplier = playerSupplier != null ? playerSupplier : (data) -> List.copyOf(Bukkit.getOnlinePlayers());
    }


    // Static methods.
    public boolean EnsurePlayerCount(CommandData data, List<Player> players, int min)
    {
        return EnsurePlayerCount(data, players, min, _maxPlayers);
    }

    public boolean EnsurePlayerCount(CommandData data, List<Player> players, int min, int max)
    {
        if (players.size() < min)
        {
            data.SetStatus(CommandStatus.Unsuccessful);
            data.SetFeedback(players.isEmpty() ? "No players found." : "Expected at least %d player%s, got %d"
                    .formatted(min, min != 1 ? "s" : "", players.size()));
            return false;
        }
        if (players.size() > max)
        {
            data.SetStatus(CommandStatus.Unsuccessful);
            data.SetFeedback("Expected fewer than %d player%s, got %d"
                    .formatted(max, max != 1 ? "s" : "", players.size()));
            return false;
        }
        return true;
    }


    // Private methods.
    private List<Player> SelectPlayers(CommandData data, String selector)
    {
        List<Player> SelectedPlayers = new ArrayList<>();

        if (selector.startsWith("@") && _isSpecialSelectorAllowed)
        {
            SelectFromSpecialSelector(data, selector, SelectedPlayers);
        }
        else
        {
            Player SelectedPlayer = Bukkit.getServer().getPlayerExact(selector);
            if (SelectedPlayer != null)
            {
                SelectedPlayers.add(SelectedPlayer);
            }
        }

        return SelectedPlayers;
    }

    private void SelectFromSpecialSelector(CommandData data, String selector, List<Player> players)
    {
        switch (selector)
        {
            case "@s" -> SelectSelfPlayer(data, players);
            case "@r" -> SelectRandomPlayer(data, players);
            case "@p" -> SelectClosestPlayer(data, players);
            case "@a" -> SelectAllPlayers(data, players);
        }
    }

    private void SelectSelfPlayer(CommandData data, List<Player> selectedPlayers)
    {
        if (data.GetSender() instanceof Player)
        {
            selectedPlayers.add((Player)data.GetSender());
        }
    }

    private void SelectRandomPlayer(CommandData data, List<Player> selectedPlayers)
    {
        List<Player> Players = new ArrayList<>(_playerSupplier.apply(data));
        if (Players.isEmpty())
        {
            return;
        }

        int Index = new Random().nextInt(0, Players.size());
        selectedPlayers.add(Players.get(Index));
    }

    private void SelectAllPlayers(CommandData data, List<Player> selectedPlayers)
    {
        selectedPlayers.addAll(_playerSupplier.apply(data));
    }

    private void SelectClosestPlayer(CommandData data, List<Player> selectedPlayers)
    {
        Location SearchLocation = data.GetLocation();

        double ClosestDistance = Double.POSITIVE_INFINITY;
        double CurrentDistance;
        Player ClosestPlayer = null;

        for (Player MCPlayer : _playerSupplier.apply(data))
        {
            CurrentDistance = SearchLocation.distance(MCPlayer.getLocation());
            if ((CurrentDistance < ClosestDistance) && (data.GetSender() != MCPlayer))
            {
                ClosestDistance = CurrentDistance;
                ClosestPlayer = MCPlayer;
            }
        }

        if (ClosestPlayer != null)
        {
            selectedPlayers.add(ClosestPlayer);
        }
    }

    private List<Player> ParsePlayerSelectors(CommandData data)
    {
        HashSet<Player> SelectedPlayers = new HashSet<>();
        for (int i = 0; i < _maxSelectors; i++)
        {
            data.MoveIndexToNextNonWhitespace();
            if (!data.IsMoreDataAvailable())
            {
                break;
            }

            String Selector = data.ReadWord();
            SelectedPlayers.addAll(SelectPlayers(data, Selector));
        }

        return List.copyOf(SelectedPlayers);
    }


    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        HashSet<String > Suggestions = new HashSet<>();

        if (_isSpecialSelectorAllowed)
        {
            Suggestions.addAll(List.of(SELECTOR_SELF, SELECTOR_CLOSEST, SELECTOR_ALL, SELECTOR_RANDOM));
        }
        Suggestions.addAll(_playerSupplier.apply(data).stream().map(Player::getName).toList());
        ParsePlayerSelectors(data).stream().map(Player::getName).toList().forEach(Suggestions::remove);

        return new ArrayList<>(Suggestions);
    }

    @Override
    public boolean ParseCommand(CommandData data)
    {
        List<Player> Players = ParsePlayerSelectors(data);
        if (!EnsurePlayerCount(data, Players, 1))
        {
            return false;
        }
        AddParsedData(Players, data);
        return true;
    }
}
