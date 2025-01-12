package sus.keiger.plugincommon;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public final class PCMath
{
    // Fields.
    public static final int TICKS_IN_SECOND = 20;
    public static final double HEALTH_IN_HEART = 2d;


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
        return health / HEALTH_IN_HEART;
    }

    public static double HeartsToHealth(double hearts)
    {
        return hearts * HEALTH_IN_HEART;
    }

    public static boolean AreNumbersEqual(double a, double b, double marginOfError)
    {
        return Math.abs(a - b) <= marginOfError;
    }

    public static boolean AreNumbersEqual(float a, float b, float marginOfError)
    {
        return Math.abs(a - b) <= marginOfError;
    }

    public static boolean AreBoundsColliding(BoundingBox a, BoundingBox b)
    {
        return (a.getMaxX() >= b.getMinX()) && (a.getMinX() <= b.getMaxX())
                && (a.getMaxY() >= b.getMinY()) && (a.getMinY() <= b.getMaxY())
                && (a.getMaxZ() >= b.getMinZ()) && (a.getMinZ() <= b.getMaxZ());
    }

    public static int Sign(int value)
    {
        if (value > 0)
        {
            return 1;
        }
        else if (value < 0)
        {
            return - 1;
        }
        return 0;
    }

    public static int Sign(double value)
    {
        if (value > 0)
        {
            return 1;
        }
        else if (value < 0)
        {
            return - 1;
        }
        return 0;
    }
}