package ea.java.Events;

import ea.java.Manager.AuctionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class OnPlayerLeave implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisconnect(final PlayerQuitEvent event)
    {
        if (AuctionManager.getInstance().getCurrentAuction() != null)
        {
            if (AuctionManager.getInstance().getCurrentAuction().getAuctionStartPlayer() == event.getPlayer())
            {
                AuctionManager.getInstance().stopAuction(true,event.getPlayer());
            }
        }
    }
}
