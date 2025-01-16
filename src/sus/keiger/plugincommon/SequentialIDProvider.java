package sus.keiger.plugincommon;

public class SequentialIDProvider implements IIDProvider
{
    // Static fields.
    public static final long INVALID_ID = 0;


    // Private fields.
    private long _availableID = 1L;


    // Methods.
    public void SetCurrentID(long value)
    {
        _availableID = value;
    }

    public long PeekID()
    {
        return _availableID;
    }


    // Inherited methods.
    @Override
    public long GetID()
    {
        long ID = _availableID;
        _availableID++;
        return ID;
    }
}