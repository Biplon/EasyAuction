package ea.java.Events;

import ea.java.Manager.AuctionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class OnPlayerLeave implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
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
