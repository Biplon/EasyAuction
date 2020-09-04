package ea.java.Manager;

import ea.java.EasyAuction;
import ea.java.Struct.CoolDownGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoolDownManager
{
    private List<UUID> hasCoolDown = new ArrayList<>();

    private List<CoolDownGroup> cdGroups = new ArrayList<>();

    static CoolDownManager instance;

    public static CoolDownManager getInstance()
    {
        return instance;
    }

    public CoolDownManager()
    {
        instance = this;
        loadCoolDownGroups();
    }
    
    private void loadCoolDownGroups()
    {
        boolean groupFound = true;
        int count = 0;
        while (groupFound)
        {
            if (EasyAuction.getInstance().getConfig().getString("general.reducedtimerauction."+ count +".perm") != null)
            {
                cdGroups.add(new CoolDownGroup(EasyAuction.getInstance().getConfig().getString("general.reducedtimerauction."+ count +".perm"),EasyAuction.getInstance().getConfig().getInt("general.reducedtimerauction."+ count +".time")));
                count++;
            }
            else
            {
                groupFound = false;
            }
        }
    }

    public boolean hasPlayerCoolDown(UUID id)
    {
       return hasCoolDown.contains(id);
    }

    public void addPlayerHasCoolDown(Player p)
    {
        //TODO implement in auction
        hasCoolDown.add(p.getUniqueId());
        UUID id = p.getUniqueId();
        Bukkit.getScheduler().runTaskLaterAsynchronously(EasyAuction.getInstance(),() -> removePlayerCoolDown(id),getPlayerCoolDown(p) * 60 * 20);
    }

    public void removePlayerCoolDown(UUID id)
    {
        hasCoolDown.remove(id);
    }

    private int getPlayerCoolDown(Player p)
    {
        for (CoolDownGroup cdg: cdGroups)
        {
            if (p.hasPermission(cdg.getPermission()))
            {
                return cdg.getCoolDown();
            }
        }
        return EasyAuction.getInstance().getConfig().getInt("general.timerauction");
    }
}
