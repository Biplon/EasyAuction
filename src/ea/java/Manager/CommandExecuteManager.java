package ea.java.Manager;

import ea.java.Config.ConfigManager;
import ea.java.Config.LanguageManager;
import ea.java.Database.DatabaseManager;
import ea.java.EasyAuction;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.zip.DataFormatException;

public class CommandExecuteManager
{
    private static CommandExecuteManager instance;

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
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.newCommandAlias);
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.bidCommandAlias);
        p.sendMessage("/" + LanguageManager.bidCommandAliasShort);
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.salesCommandAlias);
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.showCommandAlias);
    }

    public void showCommandsAdminCommandExecute(Player p)
    {
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.startCommandAlias + "|" + LanguageManager.winCommandAlias);
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.enableCommandAlias + "|" + LanguageManager.disableCommandAlias);
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.stopCommandAlias);
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.banCommandAlias + " playername " + "time");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.pardonCommandAlias + " playername");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.reloadCommandAlias);
    }

    public void openNewAuctionGUI(Player p)
    {
        Bukkit.getScheduler().runTask(EasyAuction.getInstance(), () ->
        {
            if (AuctionManager.getInstance().enabled)
            {
                if (AuctionManager.getInstance().getCurrentAuction() == null)
                {
                    if (!DatabaseManager.getInstance().playerBanned(p))
                    {
                        if (!CoolDownManager.getInstance().hasPlayerCoolDown(p.getUniqueId()))
                        {
                            GUIManager.getInstance().openGUI(p);
                        }
                        else
                        {
                            p.sendMessage(LanguageManager.coolDownText);
                        }
                    }
                    else
                    {
                        p.sendMessage(LanguageManager.bannedText + DatabaseManager.getInstance().getPlayerBannedTime(p));
                    }
                }
                else
                {
                    p.sendMessage(LanguageManager.auctionRunning);
                }
            }
            else
            {
                p.sendMessage(LanguageManager.auctionDisabled);
            }
        });
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

    public void playerBid(Player player, int bid)
    {
        if (AuctionManager.getInstance().getCurrentAuction() != null)
        {
            if (AuctionManager.getInstance().getCurrentAuction().getPriceCurrent() + (AuctionManager.getInstance().getCurrentAuction().getStartPrice() * (EasyAuction.getInstance().getConfig().getInt("general.bidsteps") / 100)) < bid)
            {
                if (EconomyManager.getInstance().canBid(player,bid))
                {
                    if ( AuctionManager.getInstance().playerBid(player,bid))
                    {
                        player.sendMessage(LanguageManager.youBid);
                    }
                    else
                    {
                        player.sendMessage(LanguageManager.goWrong);
                    }
                }
                else
                {
                    player.sendMessage(LanguageManager.notEnoughMoney);
                }
            }
            else
            {
                player.sendMessage(LanguageManager.bidToLow);
            }

        }
        else
        {
            player.sendMessage(LanguageManager.noAuctionRunning);
        }


        //TODO implement
    }

    public void salesAuctionCommandExecute(Player player)
    {
        player.sendMessage(LanguageManager.salesPlayerText.replace("%money%",DatabaseManager.getInstance().getPlayerSales(player)));
    }

    //admin

    public void getPlayerStats(Player p, String name, boolean win)
    {
        String[] result = DatabaseManager.getInstance().getPlayerStats(name,win);
        for (int i = 0; i < result.length; i++)
        {
            p.sendMessage(result[i]);
        }
    }

    public void enThisAuction(boolean active, boolean force)
    {
        AuctionManager.getInstance().enabled = active;
        if (force && AuctionManager.getInstance().getCurrentAuction() != null)
        {
            AuctionManager.getInstance().stopAuction(false, null);
        }
    }

    public void stopAuction()
    {
        if (AuctionManager.getInstance().getCurrentAuction() != null)
        {
            AuctionManager.getInstance().stopAuction(false, null);
        }
    }

    public void banPlayer(String playerName, int time)
    {
        if (Bukkit.getPlayer(playerName) != null)
        {
            OfflinePlayer p = Bukkit.getOfflinePlayer(playerName);
            DatabaseManager.getInstance().banPlayer(p.getUniqueId(),time);
        }
        else
        {
            DatabaseManager.getInstance().banPlayer(Bukkit.getPlayer(playerName).getUniqueId(),time);
        }

    }

    public void pardonPlayer(String playerName)
    {
        if (Bukkit.getPlayer(playerName) != null)
        {
            OfflinePlayer p = Bukkit.getOfflinePlayer(playerName);
            DatabaseManager.getInstance().pardonPlayer(p.getUniqueId());
        }
        else
        {
            DatabaseManager.getInstance().pardonPlayer(Bukkit.getPlayer(playerName).getUniqueId());
        }
    }

    public void reload()
    {
        ConfigManager.loadConfig();
        CoolDownManager.getInstance().loadCoolDownGroups();
        BlackList.getInstance().loadBlackList();
    }


}
