package sus.keiger.plugincommon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PCString
{
    // Private static fields.p
    private static final int DEFAULT_MAX_CHARS_PER_LINE = 25;


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
        return word.toLowerCase().endsWith("s") ? word + "'" : word + "'s";
    }

    public static String ExceptionToString(Throwable e)
    {
        return "%s: %s. Stacktrace: %s".formatted(e.getClass().getName(), e.getMessage(),
                String.join("\n    at ", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()));
    }

    /**
     * Same as {@link #SplitTextIntoLines(String, int)}, with a limit of 25 characters per line.
     * */
    public static List<String> SplitTextIntoLines(String text)
    {
        return SplitTextIntoLines(text, DEFAULT_MAX_CHARS_PER_LINE);
    }

    /**
     * Splits text into lines of at most <code>maxCharsPerLine</code> characters, breaking only at whitespace.
     * <br>The whitespace where a line breaks is dropped. A word longer than the limit is not cut; it gets a line
     * of its own, which exceeds the limit. Use {@link #WrapTextIntoLines(String, int)} to cut such words instead.
     * @param text The text to split.
     * @param maxCharsPerLine The maximum number of characters per line, at least 1.
     * @return An immutable list of the lines.
     * @throws NullPointerException if <code>text</code> is null.
     * @throws IllegalArgumentException if <code>maxCharsPerLine</code> is less than 1.
     * */
    public static List<String> SplitTextIntoLines(String text, int maxCharsPerLine)
    {
        return BreakTextIntoLines(text, maxCharsPerLine, false);
    }

    /**
     * Same as {@link #WrapTextIntoLines(String, int)}, with a limit of 25 characters per line.
     * */
    public static List<String> WrapTextIntoLines(String text)
    {
        return WrapTextIntoLines(text, DEFAULT_MAX_CHARS_PER_LINE);
    }

    /**
     * Splits text into lines of at most <code>maxCharsPerLine</code> characters, breaking at whitespace.
     * <br>The whitespace where a line breaks is dropped. Unlike {@link #SplitTextIntoLines(String, int)}, a word
     * longer than the limit is cut across lines, so no line ever exceeds the limit.
     * @param text The text to wrap.
     * @param maxCharsPerLine The maximum number of characters per line, at least 1.
     * @return An immutable list of the lines.
     * @throws NullPointerException if <code>text</code> is null.
     * @throws IllegalArgumentException if <code>maxCharsPerLine</code> is less than 1.
     * */
    public static List<String> WrapTextIntoLines(String text, int maxCharsPerLine)
    {
        return BreakTextIntoLines(text, maxCharsPerLine, true);
    }


    // Private static methods.
    private static List<String> BreakTextIntoLines(String text, int maxCharsPerLine, boolean shouldCutLongWords)
    {
        Objects.requireNonNull(text, "text is null");
        if (maxCharsPerLine < 1)
        {
            throw new IllegalArgumentException("Max characters per line must be >= 1");
        }

        List<String> Lines = new ArrayList<>();
        int LineStartIndex = 0;

        while ((text.length() - LineStartIndex) > maxCharsPerLine)
        {
            int LineEndIndex = FindLineEndIndex(text, LineStartIndex, maxCharsPerLine, shouldCutLongWords);
            Lines.add(text.substring(LineStartIndex, LineEndIndex));
            LineStartIndex = SkipWhitespace(text, LineEndIndex);
        }

        if (LineStartIndex < text.length())
        {
            Lines.add(text.substring(LineStartIndex));
        }

        return Collections.unmodifiableList(Lines);
    }

    private static int FindLineEndIndex(String text,
                                        int lineStartIndex,
                                        int maxCharsPerLine,
                                        boolean shouldCutLongWords)
    {
        int MaxLineEndIndex = lineStartIndex + maxCharsPerLine;
        for (int i = MaxLineEndIndex; i > lineStartIndex; i--)
        {
            if (IsLineBreakIndex(text, i))
            {
                return i;
            }
        }

        if (shouldCutLongWords)
        {
            return MaxLineEndIndex;
        }

        // Nothing fits, so the line is a single word longer than the limit. Let it overflow until the word ends.
        for (int i = MaxLineEndIndex + 1; i < text.length(); i++)
        {
            if (IsLineBreakIndex(text, i))
            {
                return i;
            }
        }
        return text.length();
    }

    // Lines only break where a run of whitespace starts, so they never end with whitespace.
    private static boolean IsLineBreakIndex(String text, int index)
    {
        return Character.isWhitespace(text.charAt(index)) && !Character.isWhitespace(text.charAt(index - 1));
    }

    private static int SkipWhitespace(String text, int startIndex)
    {
        int EndIndex = startIndex;
        while ((EndIndex < text.length()) && Character.isWhitespace(text.charAt(EndIndex)))
        {
            EndIndex++;
        }
        return EndIndex;
    }
}