package sus.keiger.bsripoff.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import sus.keiger.bsripoff.BSRipoff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;

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


    // Constructors.
    public PlayerSelectorNode(BiConsumer<CommandData, HashMap<String, Object>> executor,
                              boolean isSpecialSelectorAllowed,
                              int maxSelectors,
                              String parsedDataKey)
    {
        super(executor, parsedDataKey);

        _isSpecialSelectorAllowed = isSpecialSelectorAllowed;
        _maxSelectors = Math.max(1, maxSelectors);
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
        List<Player> Players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        if (Players.size() == 0)
        {
            return;
        }

        int Index = BSRipoff.GetPlugin().GetRandom().nextInt(0, Players.size());
        selectedPlayers.add(Players.get(Index));
    }

    private void SelectAllPlayers(CommandData data, List<Player> selectedPlayers)
    {
        selectedPlayers.addAll(Bukkit.getServer().getOnlinePlayers());
    }

    private void SelectClosestPlayer(CommandData data, List<Player> selectedPlayers)
    {
        Location SearchLocation = data.GetLocation();

        double ClosestDistance = Double.POSITIVE_INFINITY;
        double CurrentDistance;
        Player ClosestPlayer = null;

        for (Player MCPlayer : Bukkit.getServer().getOnlinePlayers())
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

        return new ArrayList<>(SelectedPlayers);
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
        Suggestions.addAll(Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList());
        ParsePlayerSelectors(data).stream().map(Player::getName).toList().forEach(Suggestions::remove);

        return new ArrayList<>(Suggestions);
    }

    @Override
    public boolean ParseCommand(CommandData data, HashMap<String, Object> parsedData)
    {
        AddParsedData(ParsePlayerSelectors(data), parsedData);
        return true;
    }
}
