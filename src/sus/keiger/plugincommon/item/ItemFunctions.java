package sus.keiger.plugincommon.item;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemFunctions
{
    // Constructors.
    private ItemFunctions()
    {
    }


    // Static methods.
    public static void HideAllFlags(ItemMeta meta)
    {
        /* The following attribute stuff is required for some item flags to work, see API note at
        * https://jd.papermc.io/paper/1.21.4/org/bukkit/inventory/ItemFlag.html */

        meta.removeAttributeModifier(Attribute.LUCK);
        meta.addAttributeModifier(Attribute.LUCK, new AttributeModifier(
                new NamespacedKey("pc", "hide_flag_modifier"), 0d, AttributeModifier.Operation.ADD_NUMBER));

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_DYE, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_STORED_ENCHANTS);
    }

    public static boolean IsItemEmpty(ItemStack item)
    {
        return (item == null) || (item.equals(ItemStack.empty()));
    }

    public static ItemStack ItemOrNull(ItemStack item)
    {
        return IsItemEmpty(item) ? null : item;
    }
}