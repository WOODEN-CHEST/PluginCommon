package sus.keiger.plugincommon.player;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import sus.keiger.plugincommon.entity.EntityFunctions;
import sus.keiger.plugincommon.item.ItemFunctions;

import java.util.*;
import java.util.function.Predicate;

public final class PlayerFunctions
{
    // Static fields.
    /* Inventory slots, figured out through trial and error because online sources are wrong. */
    public static final int SLOT_ARMOR_HEAD = 39;
    public static final int SLOT_ARMOR_CHEST = 38;
    public static final int SLOT_ARMOR_LEGS = 37;
    public static final int SLOT_ARMOR_FEET = 36;

    public static final int SLOT_ROW1_COLUMN1 = 9;
    public static final int SLOT_ROW1_COLUMN2 = 10;
    public static final int SLOT_ROW1_COLUMN3 = 11;
    public static final int SLOT_ROW1_COLUMN4 = 12;
    public static final int SLOT_ROW1_COLUMN5 = 13;
    public static final int SLOT_ROW1_COLUMN6 = 14;
    public static final int SLOT_ROW1_COLUMN7 = 15;
    public static final int SLOT_ROW1_COLUMN8 = 16;
    public static final int SLOT_ROW1_COLUMN9 = 17;
    public static final int SLOT_ROW2_COLUMN1 = 18;
    public static final int SLOT_ROW2_COLUMN2 = 19;
    public static final int SLOT_ROW2_COLUMN3 = 20;
    public static final int SLOT_ROW2_COLUMN4 = 21;
    public static final int SLOT_ROW2_COLUMN5 = 22;
    public static final int SLOT_ROW2_COLUMN6 = 23;
    public static final int SLOT_ROW2_COLUMN7 = 24;
    public static final int SLOT_ROW2_COLUMN8 = 25;
    public static final int SLOT_ROW2_COLUMN9 = 26;
    public static final int SLOT_ROW3_COLUMN1 = 27;
    public static final int SLOT_ROW3_COLUMN2 = 28;
    public static final int SLOT_ROW3_COLUMN3 = 29;
    public static final int SLOT_ROW3_COLUMN4 = 30;
    public static final int SLOT_ROW3_COLUMN5 = 31;
    public static final int SLOT_ROW3_COLUMN6 = 32;
    public static final int SLOT_ROW3_COLUMN7 = 33;
    public static final int SLOT_ROW3_COLUMN8 = 34;
    public static final int SLOT_ROW3_COLUMN9 = 35;

    public static final int SLOT_HOTBAR1 = 0;
    public static final int SLOT_HOTBAR2 = 1;
    public static final int SLOT_HOTBAR3 = 2;
    public static final int SLOT_HOTBAR4 = 3;
    public static final int SLOT_HOTBAR5 = 4;
    public static final int SLOT_HOTBAR6 = 5;
    public static final int SLOT_HOTBAR7 = 6;
    public static final int SLOT_HOTBAR8 = 7;
    public static final int SLOT_HOTBAR9 = 8;

    public static final int SLOT_OFFHAND = 40;

    public static final int SLOT_CURSOR = -1;

    public static final int CRAFTING_SLOT_RESULT = 0;
    public static final int CRAFTING_SLOT_R1C1 = 1;
    public static final int CRAFTING_SLOT_R1C2 = 2;
    public static final int CRAFTING_SLOT_R2C1 = 3;
    public static final int CRAFTING_SLOT_R2C2 = 4;

    public static final int SLOT_INVENTORY_MIN = SLOT_CURSOR;
    public static final int SLOT_INVENTORY_MAX = SLOT_OFFHAND;


    /* Other constants. */
    public static final float MAX_SATURATION = 20f;
    public static final int MAX_FOOD = 20;
    public static final double DEFAULT_HEALTH = 20d;

    public static final String PROFILE_KEY_TEXTURES = "textures";


    // Constructors.
    private PlayerFunctions() { }


    // Static functions.
    /* Sound. */
    public static void PlayClickSound(Player mcPlayer, boolean isPrivate)
    {
        PlayUISound(mcPlayer, Sound.UI_BUTTON_CLICK, 0.3f, 1f, isPrivate);
    }

    public static void PlayErrorSound(Player mcPlayer, boolean isPrivate)
    {
        PlayUISound(mcPlayer, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.8f, 0.8f, isPrivate);
    }


    /* Attacks. */
    public static boolean IsAttackCritical(Player mcPlayer)
    {
        Objects.requireNonNull(mcPlayer, "mcPlayer is null");

        return (mcPlayer.getFallDistance() > 0f) && !mcPlayer.isSprinting()
                && (mcPlayer.getAttackCooldown() >= 0.9f);
    }


    /* Inventory. */
    public static ItemSearchResult FindItems(Player mcPlayer, Predicate<ItemStack> predicate)
    {
        Objects.requireNonNull(mcPlayer, "mcPlayer is null");
        Objects.requireNonNull(predicate, "predicate is null");

        Map<Integer, ItemStack> FoundItems = new HashMap<>();

        for (int i = SLOT_CURSOR; i <= SLOT_OFFHAND; i++)
        {
            ItemStack TargetItem = GetItem(mcPlayer, i);
            if ((TargetItem != null) && predicate.test(TargetItem))
            {
                FoundItems.put(i, TargetItem);
            }
        }
        return new ItemSearchResult(FoundItems);
    }

    public static boolean ReplaceOrSetItems(Player mcPlayer,
                                            int maxItemCount,
                                            ItemStack item,
                                            int slot,
                                            Predicate<ItemStack> itemSearchPredicate,
                                            boolean addIfSlotOccupied)
    {
        ItemSearchResult SearchResult = FindItems(mcPlayer, itemSearchPredicate);

        if (SearchResult.IsEmpty())
        {
            if (addIfSlotOccupied)
            {
                return TrySetOrAddItem(item, slot, mcPlayer);
            }
            SetItem(mcPlayer, slot, item);
            return true;
        }

        int ItemsReplaced = 0;
        for (Integer Slot : SearchResult.GetSlots())
        {
            if (ItemsReplaced > maxItemCount)
            {
                return false;
            }
            SetItem(mcPlayer, Slot, item);
            ItemsReplaced++;
        }
        return true;
    }

    public static boolean ReplaceItems(Player mcPlayer,
                                       int maxItemCount,
                                       ItemStack item,
                                       Predicate<ItemStack> itemSearchPredicate)
    {
        ItemSearchResult SearchResult = FindItems(mcPlayer, itemSearchPredicate);

        if ((SearchResult.GetItemCount() > maxItemCount) || SearchResult.IsEmpty())
        {
            return false;
        }

        for (Integer Slot : SearchResult.GetSlots())
        {
            SetItem(mcPlayer, Slot, item);
        }
        return true;
    }

    public static void SetItem(Player mcPlayer, int slot, ItemStack item)
    {
        Objects.requireNonNull(mcPlayer, "mcPlayer is null");

        if (slot == SLOT_CURSOR)
        {
            mcPlayer.setItemOnCursor(item);
        }
        else
        {
            mcPlayer.getInventory().setItem(slot, item);
        }
    }

    public static ItemStack GetItem(Player mcPlayer, int slot)
    {
        Objects.requireNonNull(mcPlayer, "mcPlayer is null");
        ItemStack FoundItem;

        if (slot == SLOT_CURSOR)
        {
            FoundItem = mcPlayer.getItemOnCursor();
            return FoundItem.equals(ItemStack.empty()) ? null : FoundItem;
        }

        return ItemFunctions.ItemOrNull(mcPlayer.getInventory().getItem(slot));
    }

    public static HashMap<Integer, ItemStack> AddItem(Player mcPlayer, ItemStack ... items)
    {
        Objects.requireNonNull(mcPlayer, "mcPlayer is null");
        return mcPlayer.getInventory().addItem(items);
    }

    public static int RemoveItems(Player mcPlayer, Predicate<ItemStack> predicate, int count)
    {
        ItemSearchResult SearchResult = FindItems(mcPlayer, predicate);
        List<Integer> Slots = SearchResult.GetSlots();

        for (int i = 0; (i < Slots.size()) && (i < count); i++)
        {
            SetItem(mcPlayer, Slots.get(i), null);
        }

        return Math.min(count, Slots.size());
    }

    public static void ClearInventory(Player mcPlayer)
    {
        Objects.requireNonNull(mcPlayer, "mcPlayer is null");
        mcPlayer.getInventory().clear();
        mcPlayer.setItemOnCursor(ItemStack.empty());
    }

    public static boolean SetOrAddItem(ItemStack item, int slot, Player mcPlayer)
    {
        return TrySetOrAddItem(item, slot, mcPlayer);
    }

    public static Integer FindLowestFreeInventorySpot(Player mcPlayer)
    {
        return FindLowestFreeInventorySpot(mcPlayer, Collections.emptySet());
    }

    public static Integer FindLowestFreeInventorySpot(Player mcPlayer, Set<Integer> ignoredSlots)
    {
        PlayerInventory TargetInventory = mcPlayer.getInventory();
        ItemStack[] Contents = TargetInventory.getContents();
        for (int i = 0; i < Contents.length; i++)
        {
            if (ItemFunctions.IsItemEmpty(Contents[i]) && !ignoredSlots.contains(i))
            {
                return i;
            }
        }

        if (ItemFunctions.IsItemEmpty(mcPlayer.getItemOnCursor()) && !ignoredSlots.contains(SLOT_CURSOR))
        {
            return SLOT_CURSOR;
        }
        return null;
    }


    /* Attributes. */
    public static void ResetAttributes(Player mcPlayer)
    {
        RegistryAccess.registryAccess().getRegistry(RegistryKey.ATTRIBUTE).forEach(attribute ->
        {
            EntityFunctions.ResetAttribute(mcPlayer, attribute);
        });

        // For some reason the player default movement speed is wrong, so this has to be manually set.
        EntityFunctions.ResetAttribute(mcPlayer, Attribute.MOVEMENT_SPEED, 0.1d);
    }


    // Private static methods.
    private static boolean TrySetOrAddItem(ItemStack item, int slot, Player mcPlayer)
    {
        if (!ItemFunctions.IsItemEmpty(GetItem(mcPlayer, slot)))
        {
            return mcPlayer.getInventory().addItem(item).isEmpty();
        }

        SetItem(mcPlayer, slot, item);
        return true;
    }

    private static void PlayUISound(Player mcPlayer ,Sound sound, float volume, float pitch, boolean isPrivate)
    {
        if (mcPlayer == null)
        {
            throw new IllegalArgumentException("mcPlayer is null");
        }

        SoundCategory Category = SoundCategory.PLAYERS;
        Location TargetLocation = mcPlayer.getLocation();

        if (isPrivate)
        {
            mcPlayer.playSound(TargetLocation, sound, Category, volume, pitch);
        }
        else
        {
            mcPlayer.getWorld().playSound(TargetLocation, sound, Category, volume, pitch);
        }
    }
}
