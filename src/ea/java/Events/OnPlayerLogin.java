package ea.java.Events;

import ea.java.EasyAuction;
import ea.java.Manager.PlayerSeeAuctionManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerLogin implements Listener
{
    @EventHandler
    public void onLogin(final PlayerJoinEvent event)
    {
        //on player join check async see auction
        Bukkit.getScheduler().runTaskAsynchronously(EasyAuction.getInstance(), () -> PlayerSeeAuctionManager.checkPlayerSeeAuction(event.getPlayer()));
    }
}
