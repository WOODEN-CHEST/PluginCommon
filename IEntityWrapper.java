package sus.keiger.plugincommon.entity;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;


public interface IEntityWrapper
{
    Entity GetAsEntity();

    void AddPassenger(Entity passenger);
    List<Entity> GetPassengers();
    void RemovePassenger(Entity passenger);
    void EjectPassengers();

    void AddScoreboardTag(String tag);
    void RemoveScoreboardTag(String tag);
    Set<String> GetScoreboardTags();

    boolean IsCollidingAt(Location location);

    Chunk GetChunk();

    BlockFace GetClosestFacingBlockFace();
    double GetHeight();
    float GetFallDistance();
    void SetFallDistance(float value);
    int GetFireTicks();
    void SetFireTicks(int value);
    int GetMaxFireTicks();
    int GetFreezeTicks();
    void SetFreezeTicks(int value);
    int GetMaxFreezeTicks();
    boolean IsFreezeTickingLocked();
    boolean SetIsFreezeTickingLocked();
    List<Entity> GetNearbyEntities(Vector boundingBox);
    Location GetLocation();
    void SetLocation(Location location);
    Vector GetMotion();
    void SetMotion(Vector motion);
    void AddMotion(Vector motion);
    boolean IsGlowing();
    void SetIsGlowing(boolean value);
    boolean IsInvulnerable();
    void SetIsInvulnerable(boolean value);
    Location GetOrigin();
    int GetTicksLived();
    EntityType GetType();
    boolean IsDead();
    boolean IsValid();
    boolean IsFrozen();
    boolean IsInBubbleColumn();
    boolean IsInWater();
    boolean IsInRain();
    boolean IsInLava();
    boolean IsInPowderedSnow();
    boolean IsInvisible();
    void SetIsInvisible(boolean value);
    void Remove();
    void LeaveVehicle();
    boolean IsVisualFireVisible();
    void SetIsVisualFireVisible(boolean value);
    boolean IsGravityEnabled();
    void SetIsGravityEnabled(boolean value);
    boolean IsNotPhysical();
    float GetPitch();
    float GetYaw();
}