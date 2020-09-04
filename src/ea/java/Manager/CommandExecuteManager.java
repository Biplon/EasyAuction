package ea.java.Manager;

import ea.java.Config.LanguageManager;
import ea.java.EasyAuction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandExecuteManager
{
    static CommandExecuteManager instance;

    public static CommandExecuteManager getInstance()
    {
        return instance;
    }

    public CommandExecuteManager()
    {
        instance = this;
    }


    public void showCommandsCommandExecute(Player p)
    {
        p.sendMessage("/"+ LanguageManager.auctionCommandAlias+ " "+ LanguageManager.newCommandAlias);
        p.sendMessage("/"+ LanguageManager.auctionCommandAlias+ " "+ LanguageManager.bidCommandAlias);
        p.sendMessage("/"+ LanguageManager.auctionCommandAlias+ " "+ LanguageManager.salesCommandAlias);
        p.sendMessage("/"+ LanguageManager.auctionCommandAlias+ " "+ LanguageManager.showCommandAlias);
    }

    public void showCommandsAdminCommandExecute(Player p)
    {
        p.sendMessage("/"+ LanguageManager.auctionAdminCommandAlias+ " "+ LanguageManager.startCommandAlias + "|" + LanguageManager.winCommandAlias);
        p.sendMessage("/"+ LanguageManager.auctionAdminCommandAlias+ " "+ LanguageManager.enableCommandAlias + "|" + LanguageManager.disableCommandAlias);
        p.sendMessage("/"+ LanguageManager.auctionAdminCommandAlias+ " "+ LanguageManager.stopCommandAlias);
        p.sendMessage("/"+ LanguageManager.auctionAdminCommandAlias+ " "+ LanguageManager.banCommandAlias+ " playername "+ "time");
        p.sendMessage("/"+ LanguageManager.auctionAdminCommandAlias+ " "+ LanguageManager.pardonCommandAlias+ " playername");
        p.sendMessage("/"+ LanguageManager.auctionAdminCommandAlias+ " "+ LanguageManager.reloadCommandAlias);
    }

    public void openNewAuctionGUI(Player p)
    {
        //TODO check player banned
        if (! CoolDownManager.getInstance().hasPlayerCoolDown(p.getUniqueId()))
        {
            GUIManager.getInstance().openGUI(p);
        }
        else
        {
            p.sendMessage(LanguageManager.coolDownText);
        }

    }

    public void showAuctionCommandExecute(Player p)
    {
        if (PlayerSeeAuctionManager.playerSeeAuctions(p))
        {
            Bukkit.getScheduler().runTaskAsynchronously(EasyAuction.getInstance(), () -> PlayerSeeAuctionManager.removePlayerSeeAuction(p));
        }
        else
        {
            Bukkit.getScheduler().runTaskAsynchronously(EasyAuction.getInstance(), () -> PlayerSeeAuctionManager.addPlayerSeeAuction(p));
        }
    }

    public void playerBid(Player player, int parseInt)
    {
        //TODO implement
    }
}
