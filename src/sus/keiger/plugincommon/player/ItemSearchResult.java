package sus.keiger.plugincommon.player;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public final class ItemSearchResult
{
    // Fields.
    private final HashMap<Integer, ItemStack> _items = new HashMap<>();


    // Constructors.
    public ItemSearchResult(List<ItemStack> foundItems, List<Integer> slots)
    {
        if (foundItems == null)
        {
            throw new IllegalArgumentException("foundItems is null");
        }
        if (slots == null)
        {
            throw new IllegalArgumentException("slots is null");
        }
        if (foundItems.size() != slots.size())
        {
            throw new IllegalArgumentException("Found item count not equal to slot count");
        }

        for (int i = 0; i < foundItems.size(); i++)
        {
            _items.put(slots.get(i), foundItems.get(i));
        }
    }


    // Methods.
    public int GetItemCount()
    {
        return _items.size();
    }

    public List<ItemStack> GetItems()
    {
        return List.copyOf(_items.values());
    }

    public List<Integer> GetSlots()
    {
        return List.copyOf(_items.keySet());
    }

    public ItemStack GetItem(int slot)
    {
        return _items.get(slot);
    }

    public boolean ContainsSlot(int slot)
    {
        return _items.containsKey(slot);
    }

    public boolean IsEmpty()
    {
        return _items.isEmpty();
    }
}