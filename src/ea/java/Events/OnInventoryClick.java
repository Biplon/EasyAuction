package ea.java.Events;

import ea.java.Config.LanguageManager;
import ea.java.Enum.EGUIChange;
import ea.java.Manager.AuctionManager;
import ea.java.Manager.BlackList;
import ea.java.Manager.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class OnInventoryClick implements Listener
{
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e)
    {
        if (!e.getView().getTitle().contains("A:"))
        {
            return;
        }

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR)
        {
            return;
        }

        final Player p = (Player) e.getWhoClicked();

        if (e.getSlot() == 28)
        {
            p.getInventory().addItem(e.getCurrentItem());
            e.getInventory().remove(e.getCurrentItem());
            return;
        }
        else if (Objects.requireNonNull(e.getClickedInventory()).getType() == InventoryType.PLAYER)
        {
            if (e.getInventory().getItem(28) == null ||e.getInventory().getItem(28).getType()  == Material.AIR)
            {
                if (BlackList.getInstance().isNotOnBlackList(e.getCurrentItem()))
                {
                    e.getInventory().setItem(28, e.getCurrentItem());
                    e.getClickedInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
                }
            }
            return;
        }



        if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(LanguageManager.timeplus))
        {
            GUIManager.getInstance().changeItemValue(EGUIChange.Time,5,e.getClickedInventory());
        }
        else if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(LanguageManager.timeminus))
        {
            GUIManager.getInstance().changeItemValue(EGUIChange.Time,-5,e.getClickedInventory());
        }
        else if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(LanguageManager.moneyplus))
        {
            GUIManager.getInstance().changeItemValue(EGUIChange.Money,Integer.parseInt(e.getCurrentItem().getItemMeta().getLore().get(0)),e.getClickedInventory());
        }
        else if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(LanguageManager.moneyminus))
        {
            GUIManager.getInstance().changeItemValue(EGUIChange.Money,Integer.parseInt(e.getCurrentItem().getItemMeta().getLore().get(0)),e.getClickedInventory());
        }
        else if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(LanguageManager.startauction))
        {
            if (AuctionManager.getInstance().getCurrentAuction() == null && (e.getInventory().getItem(28) != null || e.getInventory().getItem(28).getType() != Material.AIR))
            {
                AuctionManager.getInstance().startAuction(p,e.getInventory().getItem(28),Integer.parseInt(e.getInventory().getItem(25).getItemMeta().getLore().get(0)),Integer.parseInt(e.getInventory().getItem(22).getItemMeta().getLore().get(0)));
                e.getInventory().setItem(28,null);
                p.closeInventory();
            }
            else
            {
                //TODO text can not start
            }
            //TODO implement start auction
        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e)
    {
        if (e.getView().getTitle().contains("A:"))
        {
            e.setCancelled(true);
        }
    }
}
