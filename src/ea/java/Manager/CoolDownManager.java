package ea.java.Manager;

import ea.java.Config.ConfigManager;
import ea.java.Database.DatabaseManager;
import ea.java.EasyAuction;
import ea.java.Struct.CoolDownGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoolDownManager
{
    //CoolDownManager instance
    private static CoolDownManager instance;
      //list off all cooldown groups
    private final List<CoolDownGroup> cdGroups = new ArrayList<>();
    //getter CoolDownManager instance
    public static CoolDownManager getInstance()
    {
        return instance;
    }

    //constructor
    public CoolDownManager()
    {
        instance = this;
        loadCoolDownGroups();
    }

    //load all cooldown groups from config
    public void loadCoolDownGroups()
    {
        cdGroups.clear();
        boolean groupFound = true;
        int count = 0;
        while (groupFound)
        {
            if (EasyAuction.getInstance().getConfig().getString("general.reducedtimerauction." + count + ".perm") != null)
            {
                cdGroups.add(new CoolDownGroup(EasyAuction.getInstance().getConfig().getString("general.reducedtimerauction." + count + ".perm"), EasyAuction.getInstance().getConfig().getInt("general.reducedtimerauction." + count + ".time")));
                count++;
            }
            else
            {
                groupFound = false;
            }
        }
    }

    //check if player has cooldown
    public boolean hasPlayerCoolDown(Player p)
    {
        return DatabaseManager.getInstance().playerCoolDown(p);
    }

    //add cooldown to player
    public void addPlayerHasCoolDown(Player p)
    {
        UUID id = p.getUniqueId();
        Bukkit.getScheduler().runTask(EasyAuction.getInstance(), () -> DatabaseManager.getInstance().setCoolDownPlayer(id,getPlayerCoolDown(p) ));
    }

    //get player cooldown end
    public String getPlayerCoolDownTime(Player p)
    {
        return DatabaseManager.getInstance().getPlayerCoolDownTime(p);
    }

    //get player cooldown time from cooldowngroup
    private int getPlayerCoolDown(Player p)
    {
        for (CoolDownGroup cdg : cdGroups)
        {
            if (p.hasPermission(cdg.getPermission()))
            {
                return cdg.getCoolDown();
            }
        }
        return ConfigManager.defaultAuctionCoolDown;
    }
}
