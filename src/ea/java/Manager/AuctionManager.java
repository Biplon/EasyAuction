package ea.java.Manager;

import ea.java.Config.LanguageManager;
import ea.java.Database.DatabaseManager;
import ea.java.EasyAuction;
import ea.java.Struct.Auction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;

public class AuctionManager
{
    static AuctionManager instance;

    private Auction currentAuction = null;

    private int timerTask;

    private double remainingTimeLoop = 10;

    private double timeLeft;

    public boolean enabled = true;

    public Auction getCurrentAuction()
    {
        return currentAuction;
    }

    public static AuctionManager getInstance()
    {
        return instance;
    }

    public AuctionManager()
    {
        instance = this;
    }

    public boolean startAuction(Player p, ItemStack item,int startPrice,int time)
    {
        currentAuction = new Auction(p,item,startPrice);
        timeLeft = time;
        for (Player pl :  PlayerSeeAuctionManager.getPlayerSeeAuction())
        {
            if (pl !=null &&  pl.isOnline())
            {
                String itemtext = item.getAmount() +"x ";
                if (!item.getItemMeta().getDisplayName().equals(""))
                {
                    itemtext +=  item.getItemMeta().getDisplayName();
                }
                else
                {
                    itemtext +=  item.getType();
                }
                pl.sendMessage(LanguageManager.auctionStartText.replace("%item%",itemtext).replace("%startprice%",startPrice+""));
                pl.sendMessage(LanguageManager.auctionMinLeft.replace("%timeleft%", getTimeString(timeLeft)));
            }
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EasyAuction.getInstance(), this::endAuction,(time  * 20));
        timerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(EasyAuction.getInstance(), () ->
        {
            timeLeft -= remainingTimeLoop;
            for (Player pl :  PlayerSeeAuctionManager.getPlayerSeeAuction())
            {
                if (pl !=null &&  pl.isOnline())
                {
                    String itemtext = item.getAmount() +"x ";
                    if (!item.getItemMeta().getDisplayName().equals(""))
                    {
                        itemtext +=  item.getItemMeta().getDisplayName();
                    }
                    else
                    {
                        itemtext +=  item.getType();
                    }
                    pl.sendMessage(LanguageManager.auctionRunText.replace("%item%",itemtext).replace("%price%",currentAuction.getPriceCurrent() +""));
                    pl.sendMessage(LanguageManager.auctionMinLeft.replace("%timeleft%", getTimeString(timeLeft)));
                }
            }
        }, (long) 10 * 20, (long) 10 * 20);
        return true;
    }

    private void endAuction()
    {
        Bukkit.getScheduler().cancelTask(timerTask);
        for (Player pl :  PlayerSeeAuctionManager.getPlayerSeeAuction())
        {
            if (pl !=null &&  pl.isOnline())
            {
                pl.sendMessage(LanguageManager.auctionEnd.replace("%winner%",currentAuction.getBidPlayer() == null ? "---" : currentAuction.getBidPlayer() +""));
            }
        }
        if (currentAuction.getBidPlayer() != null)
        {
            Map<Integer, ItemStack> map = null;
            map =   currentAuction.getBidPlayer().getInventory().addItem(currentAuction.getAuctionItem());
            if (map.size() == 1)
            {
                for (final ItemStack item : map.values())
                {
                    currentAuction.getBidPlayer().getWorld().dropItemNaturally(currentAuction.getBidPlayer().getLocation(), item);
                }
                map.clear();
            }
           DatabaseManager.getInstance().createLog(currentAuction.getAuctionStartPlayer().getDisplayName(),currentAuction.getAuctionItem().toString(),currentAuction.getBidPlayer().getDisplayName(),currentAuction.getPriceCurrent());
//TODO remove money
        }
        else
        {
            Map<Integer, ItemStack> map = null;
            map =   currentAuction.getAuctionStartPlayer().getInventory().addItem(currentAuction.getAuctionItem());
            if (map.size() == 1)
            {
                for (final ItemStack item : map.values())
                {
                    currentAuction.getAuctionStartPlayer().getWorld().dropItemNaturally(currentAuction.getAuctionStartPlayer().getLocation(), item);
                }
                map.clear();
            }
            DatabaseManager.getInstance().createLog(currentAuction.getAuctionStartPlayer().getDisplayName(),currentAuction.getAuctionItem().toString(),"---",0);
        }
    }

    public void stopAuction(boolean playerOff,Player p)
    {
        Map<Integer, ItemStack> map = null;
        if (playerOff)
        {
            map =   p.getInventory().addItem(currentAuction.getAuctionItem());
            if (map.size() == 1)
            {
                for (final ItemStack item : map.values())
                {
                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                }
                map.clear();
            }
            CommandExecuteManager.getInstance().banPlayer(p.getName(),EasyAuction.getInstance().getConfig().getInt("general.bantime"));
        }
        else
        {
            map =   currentAuction.getAuctionStartPlayer().getInventory().addItem(currentAuction.getAuctionItem());
            if (map.size() == 1)
            {
                for (final ItemStack item : map.values())
                {
                    currentAuction.getAuctionStartPlayer().getWorld().dropItemNaturally(currentAuction.getAuctionStartPlayer().getLocation(), item);
                }
                map.clear();
            }
        }

        DatabaseManager.getInstance().createLog(currentAuction.getAuctionStartPlayer().getDisplayName(),currentAuction.getAuctionItem().toString(),"---",0);
        Bukkit.getScheduler().cancelTask(timerTask);
        for (Player pl :  PlayerSeeAuctionManager.getPlayerSeeAuction())
        {
            if (pl !=null &&  pl.isOnline())
            {
                pl.sendMessage(LanguageManager.auctionStop + " " + (playerOff ? LanguageManager.auctionStopPlayerOff : LanguageManager.auctionStopAdmin));
            }
        }

    }


    private String getTimeString(double time)
    {
        double minutes = (time % 3600) / 60;
        double seconds = time % 60;
        String t;
        String sec;
        if (seconds < 10)
        {
            sec = "0" + +(int) seconds;
        }
        else
        {
            sec = (int) seconds + "";
        }
        if (minutes >= 1)
        {
            t = (int) minutes + ":" + sec + LanguageManager.minText;
        }
        else
        {
            t = sec + LanguageManager.secText;
        }
        return t;
    }
}
