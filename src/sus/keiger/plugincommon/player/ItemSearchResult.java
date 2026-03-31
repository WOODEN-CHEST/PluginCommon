package sus.keiger.plugincommon.player;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ItemSearchResult
{
    // Fields.
    private final Map<Integer, ItemStack> _items;


    // Constructors.
    public ItemSearchResult(Map<Integer, ItemStack> foundItems)
    {
        Objects.requireNonNull(foundItems, "foundItems is null");
        _items = Map.copyOf(foundItems);
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