package sus.keiger.plugincommon.command;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ColorNode extends CommandNode
{
    // Private fields.
    private Function<CommandData, List<String>> _suggestionProvider;



    // Constructors,
    public ColorNode(Consumer<CommandData> executor, String parsedDataKey)
    {
        super(executor, parsedDataKey);
    }


    // Private methods.
    private TextColor GetNamedColor(String color)
    {
        return NamedTextColor.NAMES.value(color);
    }

    private TextColor GetCustomColor(String color)
    {
        try
        {
            int Color = Integer.parseUnsignedInt(color, 16);
            return TextColor.color(Color);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }



    // Inherited methods.
    @Override
    public boolean ParseCommand(CommandData data)
    {
        String ColorName = data.ReadWord();
        TextColor Color;

        if (ColorName.length() >= 2 && ColorName.startsWith(NamedTextColor.HEX_PREFIX))
        {
            Color = GetCustomColor(ColorName.substring(1));
        }
        else
        {
            Color = GetNamedColor(ColorName);
        }

        if (Color != null)
        {
            AddParsedData(Color, data);
            return true;
        }
        return false;
    }


    @Override
    public List<String> GetSelfSuggestions(CommandData data)
    {
        return _suggestionProvider != null ? _suggestionProvider.apply(data)
                : new ArrayList<>(NamedTextColor.NAMES.keys());
    }
}