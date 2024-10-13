package sus.keiger.plugincommon;

import java.util.Random;

public class RandomIDProvider implements IIDProvider
{
    // Private fields.
    private final Random _rng = new Random();


    // Inherited methods.
    @Override
    public long GetID()
    {
        return _rng.nextLong();
    }
}