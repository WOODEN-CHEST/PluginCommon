package sus.keiger.plugincommon.command;

import org.bukkit.Location;
import org.bukkit.World;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public abstract class CoordinateNode extends CommandNode
{
    // Static fields.
    public static final String RELATIVE_SYMBOL = "~";


    // Private fields.
    private final int _coordinateCount;


    // Constructors.
    public CoordinateNode(Consumer<CommandData> executor,
                          int coordinateCount,
                          String parsedDataKey)
    {
        super(executor, parsedDataKey);
        _coordinateCount = coordinateCount;
    }


    // Methods.
    public abstract double GetCoordinateByIndex(Location location, int index);

    public abstract Location ParsedCoordinatesToLocation(World world, List<Double> coordinates);


    // Private methods.
    private double ParseCoordinate(CommandData data, double relativeLocation) throws NumberFormatException
    {
        String StringCoordinate = data.ReadWord();

        if (StringCoordinate.startsWith(RELATIVE_SYMBOL))
        {
            if (StringCoordinate.equals(RELATIVE_SYMBOL))
            {
                return relativeLocation;
            }
            return relativeLocation + Double.parseDouble(StringCoordinate.substring(1));
        }

        return Double.parseDouble(StringCoordinate);
    }

    private List<Double> ParseAllCoordinates(CommandData data)
    {
        List<Double> Coordinates = new ArrayList<>();
        Location RelativeLocation = data.GetLocation();

        for (int i = 0; i < _coordinateCount; i++)
        {
            data.MoveIndexToNextNonWhitespace();
            try
            {
                double Coordinate = ParseCoordinate(data, GetCoordinateByIndex(RelativeLocation, i));
                Coordinates.add(Coordinate);
            }
            catch (NumberFormatException e)
            {
                return Coordinates;
            }
        }

        return Coordinates;
    }

    private String GetRelativeSuggestion(int startIndex)
    {
        StringBuilder Suggestion = new StringBuilder();

        for (int i = startIndex; i < _coordinateCount; i++)
        {
            if (i != 0)
            {
                Suggestion.append(' ');
            }
            Suggestion.append(RELATIVE_SYMBOL);
        }

        return Suggestion.toString();
    }

    private String GetAbsoluteSuggestion(int startIndex, Location location)
    {
        DecimalFormat Format = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));
        StringBuilder Suggestion = new StringBuilder();

        for (int i = startIndex; i < _coordinateCount; i++)
        {
            if (i != 0)
            {
                Suggestion.append(' ');
            }
            Suggestion.append(Format.format(GetCoordinateByIndex(location, i)));
        }

        return Suggestion.toString();
    }


    // Inherited methods.
    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        List<String> Suggestions = new ArrayList<>();
        List<Double> ParsedCoordinates = ParseAllCoordinates(data);

        Suggestions.add(GetRelativeSuggestion(ParsedCoordinates.size()));
        Suggestions.add(GetAbsoluteSuggestion(ParsedCoordinates.size(), data.GetLocation()));

        return Suggestions;
    }

    @Override
    public boolean ParseCommand(CommandData data)
    {
        List<Double> ParsedCoordinates = ParseAllCoordinates(data);

        if (ParsedCoordinates.size() < _coordinateCount)
        {
            return false;
        }

        AddParsedData(ParsedCoordinatesToLocation(data.GetLocation().getWorld(), ParsedCoordinates), data);

        return true;
    }
}
