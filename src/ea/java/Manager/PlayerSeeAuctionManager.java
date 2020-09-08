package ea.java.Manager;

import ea.java.Config.LanguageManager;
import ea.java.Database.DatabaseManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerSeeAuctionManager
{
    private static final List<Player> playerSeeAuction = new ArrayList<>();

    public static List<Player> getPlayerSeeAuction()
    {
        return playerSeeAuction;
    }

    public static void addPlayerSeeAuction(Player p)
    {
        playerSeeAuction.add(p);
        DatabaseManager.getInstance().enablePlayerSeeAuction(p.getUniqueId());
        p.sendMessage(LanguageManager.showCommandOutput + "true");
    }

    public static void removePlayerSeeAuction(Player p)
    {
        playerSeeAuction.remove(p);
        DatabaseManager.getInstance().disablePlayerSeeAuction(p.getUniqueId());
        p.sendMessage(LanguageManager.showCommandOutput + "false");
    }

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

    public static void checkPlayerSeeAuction(Player p)
    {
        if (DatabaseManager.getInstance().playerSeeAuction(p.getUniqueId()))
        {
            playerSeeAuction.add(p);
        }
    }
}
