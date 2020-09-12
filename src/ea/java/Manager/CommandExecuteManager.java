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
    //CommandExecuteManager instance
    private static CommandExecuteManager instance;

    //getter CommandExecuteManager instance
    public static CommandExecuteManager getInstance()
    {
        return instance;
    }

    //constructor
    public CommandExecuteManager()
    {
        instance = this;
    }

    //show player all commands for /auction
    public void showCommandsCommandExecute(Player p)
    {
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.newCommandAlias + LanguageManager.dnewCommand);
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.bidCommandAlias + LanguageManager.dbidCommand);
        p.sendMessage("/" + LanguageManager.bidCommandAliasShort + LanguageManager.dbidCommand);
        p.sendMessage("/" + LanguageManager.ddetailsCommand);
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.salesCommandAlias + LanguageManager.dsalesCommand);
        p.sendMessage("/" + LanguageManager.auctionCommandAlias + " " + LanguageManager.showCommandAlias + LanguageManager.dshowCommand);
    }

    //show player all commands for /auctionadmin
    //TODO put sting at the end into langfile
    public void showCommandsAdminCommandExecute(Player p)
    {
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.startCommandAlias + " | <" + LanguageManager.winCommandAlias + "> §7show player win auction");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.enableCommandAlias + " | <" + LanguageManager.disableCommandAlias + "> [force] §7show player start auction (force stop running auction)");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.stopCommandAlias + " §7stop running auction");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.banCommandAlias + " <playername> " + "<time>" + " §7ban player for x min");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.pardonCommandAlias + " <playername>" + " §7remove player ban");
        p.sendMessage("/" + LanguageManager.auctionAdminCommandAlias + " " + LanguageManager.reloadCommandAlias + " §7reload config and lang file");
    }

    //ope inventory gui for player
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
                        if (!CoolDownManager.getInstance().hasPlayerCoolDown(p))
                        {
                            GUIManager.getInstance().openGUI(p);
                        }
                        else
                        {
                            p.sendMessage(LanguageManager.coolDownText + CoolDownManager.getInstance().getPlayerCoolDownTime(p));
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

    //send to DBManager that player will change visibility of auctions
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

    //open inventory with the auction item for player. (To see skins by optifine + resspack)
    public void showItemDetails(Player player)
    {
        if (AuctionManager.getInstance().getCurrentAuction() != null)
        {
            GUIManager.getInstance().showItemGUI(player, AuctionManager.getInstance().getCurrentAuction().getAuctionItem());
        }
        else
        {
            player.sendMessage(LanguageManager.noAuctionRunning);
        }

    }

    //check if player can bid and send it to the AuctionManager
    public void playerBid(Player player, int bid)
    {
        if (AuctionManager.getInstance().getCurrentAuction() != null)
        {
            if (player != AuctionManager.getInstance().getCurrentAuction().getAuctionStartPlayer() && player != AuctionManager.getInstance().getCurrentAuction().getBidPlayer())
            {
                if (AuctionManager.getInstance().getCurrentAuction().getPriceCurrent() + (AuctionManager.getInstance().getCurrentAuction().getStartPrice() * (ConfigManager.bidStep / 100)) < bid)
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

    //send the player his sales
    public void salesAuctionCommandExecute(Player player)
    {
        Bukkit.getScheduler().runTask(EasyAuction.getInstance(), () -> player.sendMessage(LanguageManager.salesPlayerText.replace("%money%", DatabaseManager.getInstance().getPlayerSales(player))));
    }

    //admin

    //give the star/win stats from 1 player
    public void getPlayerStats(Player p, String name, boolean win)
    {
        Bukkit.getScheduler().runTask(EasyAuction.getInstance(), () ->
        {
            String[] result = DatabaseManager.getInstance().getPlayerStats(name, win);
            for (String s : result)
            {
                p.sendMessage(s);
            }
        });
    }

    //enable/disable the auctions(reset after restart Server)
    public void endThisAuction(boolean active, boolean force)
    {
        AuctionManager.getInstance().enabled = active;
        if (force && AuctionManager.getInstance().getCurrentAuction() != null)
        {
            AuctionManager.getInstance().stopAuction(false, null);
        }
    }

    //stop the running auction
    public void stopAuction()
    {
        if (AuctionManager.getInstance().getCurrentAuction() != null)
        {
            AuctionManager.getInstance().stopAuction(false, null);
        }
    }

    //ban player for x min
    public void banPlayer(String playerName, int time)
    {
        Bukkit.getScheduler().runTask(EasyAuction.getInstance(), () ->
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
        });
    }

    //pardon player
    public void pardonPlayer(String playerName)
    {
        Bukkit.getScheduler().runTask(EasyAuction.getInstance(), () ->
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
        });
    }

    //reload all config values and the langfile
    public void reload()
    {
        ConfigManager.loadConfig();
        LanguageManager.loadLang();
        CoolDownManager.getInstance().loadCoolDownGroups();
        BlackList.getInstance().loadBlackList();
    }
}
