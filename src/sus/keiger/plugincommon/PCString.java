package sus.keiger.plugincommon;

import java.util.Arrays;

public class PCString
{
    // Constructors.
    private PCString() { }


    // Static methods.
    public static String Pluralize(String word, boolean isPlural)
    {
        if (!isPlural)
        {
            return word;
        }
        return word.endsWith("s") ? word : word + "s";
    }

    public static String Pluralize(String word, long count)
    {
        return Pluralize(word, (count != 1) && (count != -1));
    }

    public static String PossessiveForm(String word)
    {
        return word.endsWith("s") ? word + "'" : word + "'s";
    }

    public static String ExceptionToString(Exception e)
    {
        return "%s: %s. Stacktrace: %s".formatted(e.getClass().getName(), e.getMessage(),
                String.join("\n    at ", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()));
    }
}