package sus.keiger.plugincommon;

import org.bukkit.util.Vector;

public final class PCMath
{
    // Fields.
    public static final int TICKS_IN_SECOND = 20;


    // Static functions.
    public static Vector RotationToVectorDeg(float yaw, float pitch)
    {
        return RotationToVectorRad((float)Math.toRadians(yaw), (float)Math.toRadians(pitch));
    }

    public static Vector RotationToVectorRad(float yaw, float pitch)
    {
        Vector RotatedNormal = new Vector(0d, 0d, 1d);
        RotatedNormal.rotateAroundY(-yaw);
        RotatedNormal.rotateAroundAxis(new Vector(RotatedNormal.getZ(), 0d, -RotatedNormal.getX()), pitch);
        return RotatedNormal;
    }

    public static int SecondsToTicks(double seconds)
    {
        return (int)Math.ceil(TICKS_IN_SECOND * seconds);
    }

    public static double TicksToSeconds(int ticks)
    {
        return (double)ticks / (double)TICKS_IN_SECOND;
    }

    public static double HealthToHearts(double health)
    {
        return health * 0.5d;
    }

    public static boolean AreNumbersEqual(double a, double b, double marginOfError)
    {
        return Math.abs(a - b) <= marginOfError;
    }

    public static boolean AreNumbersEqual(float a, float b, float marginOfError)
    {
        return Math.abs(a - b) <= marginOfError;
    }
}