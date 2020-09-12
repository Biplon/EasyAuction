package ea.java.Manager;

import ea.java.Config.LanguageManager;
import ea.java.Database.DatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerSeeAuctionManager
{
    //list off player that see auction messages
    private static final List<Player> playerSeeAuction = new ArrayList<>();

    //get of playerSeeAuction list
    public static List<Player> getPlayerSeeAuction()
    {
        return playerSeeAuction;
    }

    //add player to playerSeeAuction list
    public static void addPlayerSeeAuction(Player p)
    {
        playerSeeAuction.add(p);
        DatabaseManager.getInstance().enablePlayerSeeAuction(p.getUniqueId());
        p.sendMessage(LanguageManager.showCommandOutput + "true");
    }

    //remove player to playerSeeAuction list
    public static void removePlayerSeeAuction(Player p)
    {
        playerSeeAuction.remove(p);
        DatabaseManager.getInstance().disablePlayerSeeAuction(p.getUniqueId());
        p.sendMessage(LanguageManager.showCommandOutput + "false");
        ItemStack i = new ItemStack(Material.DIAMOND_SWORD,3);
    }

    //check player can see auction
    public static boolean playerSeeAuctions(Player p)
    {
        for (Player pl : playerSeeAuction)
        {
            if (pl != null && pl.isOnline())
            {
                if (pl == p)
                {
                    return true;
                }
            }
        }
        return false;
    }

    //check player see auction and add to playerSeeAuction list(for playerJoin)
    public static void checkPlayerSeeAuction(Player p)
    {
        if (DatabaseManager.getInstance().playerSeeAuction(p.getUniqueId()))
        {
            playerSeeAuction.add(p);
        }
    }
}
