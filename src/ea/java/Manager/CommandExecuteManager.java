package ea.java.Manager;

import ea.java.Config.ConfigManager;
import ea.java.Config.LanguageManager;
import ea.java.Database.DatabaseManager;
import ea.java.EasyAuction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.newCommandAlias+ LanguageManager.dnewCommand);
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.bidCommandAlias+ LanguageManager.dbidCommand);
        p.sendMessage("/" + LanguageManager.bidCommandAliasShort+ LanguageManager.dbidCommand);
        p.sendMessage("/" + LanguageManager.ddetailsCommand);
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.salesCommandAlias+ LanguageManager.dsalesCommand);
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.showCommandAlias+ LanguageManager.dshowCommand);
    }

    public void showCommandsAdminCommandExecute(Player p)
    {
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.startCommandAlias + " | " + LanguageManager.winCommandAlias +" §7show player win auction");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.enableCommandAlias + " | " + LanguageManager.disableCommandAlias+" §7show player start auction");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.stopCommandAlias +" §7stop running auction");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.banCommandAlias + " playername " + "time" +" §7ban player for x min");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.pardonCommandAlias + " playername" +" §7remove player ban");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.reloadCommandAlias + " §7reload config and lang file");
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


    public void showItemDetails(Player player)
    {
        if (AuctionManager.getInstance().getCurrentAuction() != null)
        {
            GUIManager.getInstance().showItemGUI(player,AuctionManager.getInstance().getCurrentAuction().getAuctionItem());
        }
        else
        {
            player.sendMessage(LanguageManager.noAuctionRunning);
        }

    }

    public void playerBid(Player player, int bid)
    {
        if (AuctionManager.getInstance().getCurrentAuction() != null)
        {
            if (player != AuctionManager.getInstance().getCurrentAuction().getAuctionStartPlayer() && player != AuctionManager.getInstance().getCurrentAuction().getBidPlayer())
            {
                if (AuctionManager.getInstance().getCurrentAuction().getPriceCurrent() + (AuctionManager.getInstance().getCurrentAuction().getStartPrice() * (EasyAuction.getInstance().getConfig().getInt("general.bidsteps") / 100)) < bid)
                {
                    if (EconomyManager.getInstance().canBid(player, bid))
                    {
                        if (AuctionManager.getInstance().playerBid(player, bid))
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
                player.sendMessage(LanguageManager.nobidOnOwn);
            }
        }
        else
        {
            player.sendMessage(LanguageManager.noAuctionRunning);
        }
    }

    public void salesAuctionCommandExecute(Player player)
    {
        player.sendMessage(LanguageManager.salesPlayerText.replace("%money%", DatabaseManager.getInstance().getPlayerSales(player)));
    }

    //admin

    public void getPlayerStats(Player p, String name, boolean win)
    {
        String[] result = DatabaseManager.getInstance().getPlayerStats(name, win);
        for (String s : result)
        {
            p.sendMessage(s);
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
            DatabaseManager.getInstance().banPlayer(p.getUniqueId(), time);
        }
        else
        {
            DatabaseManager.getInstance().banPlayer(Bukkit.getPlayer(playerName).getUniqueId(), time);
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
        LanguageManager.loadLang();
        CoolDownManager.getInstance().loadCoolDownGroups();
        BlackList.getInstance().loadBlackList();
    }
}
