package sus.keiger.plugincommon;

import java.util.Optional;

/**
 * A simple boolean success value with an optional message.
 */
public class ExplainedResult
{
    // Fields.
    private final boolean _value;
    private final String _message;


    /**
     * Creates new <code>ExplainedResult</code> with no message.
     * @param value The result.
     */
    // Constructors.
    public ExplainedResult(boolean value)
    {
        this(value, null);
    }

    /**
     * Creates a new explained result with a message.
     * @param value The result.
     * @param message The message, may be <code>null</code>.
     */
    public ExplainedResult(boolean value, String message)
    {
        _value = value;
        _message = message;
    }


    // Static methods.

    /**
     * Creates a successful result with no message.
     * @return Success result.
     */
    public static ExplainedResult Success()
    {
        return new ExplainedResult(true);
    }


    /**
     * Creates an error result with an error message. The message may be <code>null</code>,
     * thought it is STRONGLY recommended to provide a message for clarity.
     * @param message The error message, or <code>null</code> for no message.
     * @return Error result.
     */
    public static ExplainedResult Error(String message)
    {
        return new ExplainedResult(false, message);
    }


    // Methods.
    /**
     * Gets the result of the operation.
     * <br><code>true</code> for successful operations and <code>false</code> for failures.
     * @return The operation's result.
     */

    public boolean IsSuccessful()
    {
        return _value;
    }

    /**
     * Gets the message associated with the outcome.
     * <br>Usually empty for successful outcomes and a non-empty value for failures.
     * @return The message.
     */
    public String GetMessage()
    {
        return Optional.ofNullable(_message).orElse("No message provided.");
    }
}