package sus.keiger.plugincommon.packet.clientbound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Objects;

public class SetEntityMetaDataPacket extends ClientBoundGamePacket
{
    // Static fields.
    public static final int ID = 0x58;


    // Private fields.
    private Entity _targetEntity;
    private boolean _isOnFire;
    private boolean _isCrouching;
    private boolean _isSprinting;
    private boolean _isSwimming;
    private boolean _isInvisible;
    private boolean _isGlowing;
    private boolean _isFlying;

    private final int IS_ON_FIRE_BIT_MASK = 0x01;
    private final int IS_CROUCHING_BIT_MASK = 0x02;
    private final int IS_SPRINTING_BIT_MASK = 0x08;
    private final int IS_SWIMMING_BIT_MASK = 0x10;
    private final int IS_INVISIBLE_BIT_MASK = 0x20;
    private final int IS_GLOWING_BIT_MASK = 0x40;
    private final int IS_FLYING_BIT_MASK = 0x80;


    // Constructors.
    public SetEntityMetaDataPacket(Entity entity)
    {
        super(ID);
        SetEntity(entity);
    }


    // Methods.
    public void SetEntity(Entity entity)
    {
        _targetEntity = Objects.requireNonNull(entity, "entity is null");
    }

    public Entity GetTargetEntity()
    {
        return _targetEntity;
    }

    public void SetIsGlowing(boolean value)
    {
        _isGlowing = value;
    }

    public boolean GetIsGlowing()
    {
        return _isGlowing;
    }


    // Private methods.
    private void ApplyEntityProperties(WrappedDataWatcher watcher)
    {

    }


    // Inherited methods.
    @Override
    public PacketContainer CreatePacketContainer(ProtocolManager protocolManager)
    {
        PacketContainer Packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);

        WrappedDataWatcher Watcher = new WrappedDataWatcher();

        Watcher.setEntity(_targetEntity);
        Watcher.setObject(0, WrappedDataWatcher.Registry.get(Boolean.class), (byte)IS_GLOWING_BIT_MASK, true);

        Packet.getWatchableCollectionModifier().write(0, new ArrayList<>(Watcher.getWatchableObjects()));

        Packet.getDataWatcherModifier();


        return Packet;
    }
}
