package ea.java.Events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;

public class OnInventoryClose implements Listener
{
    @EventHandler
    public void onClose(final InventoryCloseEvent e)
    {
        if (e.getView().getTitle().contains("A:"))
        {
            if (e.getInventory().getItem(28) != null && Objects.requireNonNull(e.getInventory().getItem(28)).getType() != Material.AIR)
            {
                Map<Integer, ItemStack> map;
                map = e.getPlayer().getInventory().addItem(e.getInventory().getItem(28));
                if (map.size() == 1)
                {
                    for (final ItemStack item : map.values())
                    {
                        e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), item);
                    }
                    map.clear();
                }
            }
        }
    }
}