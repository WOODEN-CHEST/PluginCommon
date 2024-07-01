package sus.keiger.plugincommon.command;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.function.Consumer;

public class LocationNode extends CoordinateNode
{
    // Constructors.
    public LocationNode(Consumer<CommandData> executor, String parsedDataKey)
    {
        super(executor, 3, parsedDataKey);
    }


    // Inherited methods.
    @Override
    public double GetCoordinateByIndex(Location location, int index)
    {
        switch (index)
        {
            case 0 -> { return location.getX(); }
            case 1 -> { return location.getY(); }
            case 2 -> { return location.getZ(); }
        }

        throw new IllegalArgumentException("Index (%d) outside of accepted range".formatted(index));
    }

    @Override
    public Location ParsedCoordinatesToLocation(World world, List<Double> coordinates)
    {
        return new Location(world, coordinates.get(0), coordinates.get(1), coordinates.get(2), 0f, 0f);
    }
}