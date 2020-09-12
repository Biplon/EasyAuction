package ea.java.Manager;

import ea.java.EasyAuction;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager
{
    //EconomyManager instance
    private static EconomyManager instance;
    //Economy object
    private static Economy econ = null;
    //getter EconomyManager instance
    public static EconomyManager getInstance()
    {
        return instance;
    }

    //constructor and setup Economy object
    public EconomyManager()
    {
        instance = this;
        if (!setupEconomy())
        {
            EasyAuction.getInstance().getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", EasyAuction.getInstance().getDescription().getName()));
            EasyAuction.getInstance().getServer().getPluginManager().disablePlugin(EasyAuction.getInstance());
        }
    }

    //setup economy
    private boolean setupEconomy()
    {
        if (EasyAuction.getInstance().getServer().getPluginManager().getPlugin("Vault") == null)
        {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = EasyAuction.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    //check if player has enough money for bid
    public boolean canBid(Player p, double money)
    {
        return econ.has(p, money);
    }

    //remove money from player
    public boolean removeMoney(Player p, double money)
    {
        return econ.withdrawPlayer(p, money).transactionSuccess();
    }

    //add money to player
    public void addMoney(Player p, double money)
    {
        econ.depositPlayer(p, money).transactionSuccess();
    }
}
