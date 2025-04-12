package sus.keiger.plugincommon.packet.clientbound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.comphenix.protocol.wrappers.WrappedAttribute;
import com.comphenix.protocol.wrappers.WrappedAttributeModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UpdateAttributePacket extends ClientBoundGamePacket
{
    // Static fields.
    public static final int ID = 0x7C;


    // Private fields.
    private List<AttributeInfo> _attributeInfo = Collections.emptyList();
    private LivingEntity _targetEntity;


    // Constructors.
    public UpdateAttributePacket(LivingEntity entity)
    {
        super(ID);
        SetEntity(entity);
    }


    // Methods.
    public List<AttributeInfo> GetAttributeInfo()
    {
        return _attributeInfo;
    }

    public void SetAttributeInfo(List<AttributeInfo> attributeInfo)
    {
        _attributeInfo = Objects.requireNonNull(attributeInfo, "attributeInfo is null");
    }

    public LivingEntity GetEntity()
    {
        return _targetEntity;
    }

    public void SetEntity(LivingEntity entity)
    {
        _targetEntity = Objects.requireNonNull(entity, "entity is null");
    }



    // Private methods.
    private WrappedAttributeModifier.Operation ConvertOperation(AttributeModifier.Operation operation)
    {
        return switch (operation)
        {
            case ADD_NUMBER -> WrappedAttributeModifier.Operation.ADD_NUMBER;
            case ADD_SCALAR -> WrappedAttributeModifier.Operation.ADD_PERCENTAGE;
            case MULTIPLY_SCALAR_1 -> WrappedAttributeModifier.Operation.MULTIPLY_PERCENTAGE;
        };
    }

    private WrappedAttributeModifier ConvertModifiers(AttributeModifier modifier)
    {
        return WrappedAttributeModifier.newBuilder()
                .amount(modifier.getAmount())
                .operation(ConvertOperation(modifier.getOperation()))
                .key(new MinecraftKey(modifier.getKey().toString()))
                .build();
    }

    private List<WrappedAttributeModifier> GetWrappedAttributeModifiers(AttributeInfo info)
    {
        return info.GetModifiers().stream().map(this::ConvertModifiers).toList();
    }

    private WrappedAttribute GetWrappedAttribute(AttributeInfo info)
    {
        return WrappedAttribute
                .newBuilder()
                .baseValue(info.GetValue())
                .modifiers(GetWrappedAttributeModifiers(info))
                .attributeKey(info.GetAttribute().getKey().toString())
                .build();
    }

    private List<WrappedAttribute> GetAttributes()
    {
        return _attributeInfo.stream().map(this::GetWrappedAttribute).toList();
    }



    // Inherited methods.
    @Override
    public PacketContainer CreatePacketContainer(ProtocolManager protocolManager)
    {
        PacketContainer Packet = protocolManager.createPacket(PacketType.Play.Server.UPDATE_ATTRIBUTES);

        Packet.getIntegers().write(0, _targetEntity.getEntityId());
        Packet.getAttributeCollectionModifier().write(0, GetAttributes());

        return Packet;
    }
}