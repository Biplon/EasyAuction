package ea.java.Manager;

import ea.java.EasyAuction;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager
{
    private static EconomyManager instance;

    private static Economy econ = null;

    public static EconomyManager getInstance()
    {
        return instance;
    }

    public EconomyManager()
    {
        instance = this;
        if (!setupEconomy())
        {
            EasyAuction.getInstance().getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", EasyAuction.getInstance().getDescription().getName()));
            EasyAuction.getInstance().getServer().getPluginManager().disablePlugin(EasyAuction.getInstance());
        }
    }

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

    public boolean canBid(Player p, double money)
    {
        return econ.has(p, money);
    }

    public boolean removeMoney(Player p, double money)
    {
        return econ.withdrawPlayer(p, money).transactionSuccess();
    }

    public void addMoney(Player p, double money)
    {
        econ.depositPlayer(p, money).transactionSuccess();
    }
}
