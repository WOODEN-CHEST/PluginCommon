package sus.keiger.plugincommon;

public class SequentialIDProvider implements IIDProvider
{
    // Private fields.
    private long _availableID = 1L;


    // Inherited methods.
    @Override
    public long GetID()
    {
        long ID = _availableID;
        _availableID++;
        return ID;
    }
}