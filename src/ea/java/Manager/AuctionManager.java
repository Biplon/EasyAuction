package ea.java.Manager;

import ea.java.Config.LanguageManager;
import ea.java.Database.DatabaseManager;
import ea.java.EasyAuction;
import ea.java.Struct.Auction;
import net.md_5.bungee.api.chat.*;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class AuctionManager
{
    static AuctionManager instance;

    private Auction currentAuction = null;

    private int timerTask;

    private int timerEndAuctionTask;

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

    public boolean startAuction(Player p, ItemStack item, int startPrice, int time)
    {
        currentAuction = new Auction(p, item, startPrice);
        sendMessage(LanguageManager.auctionStartText);
        sendHoverMessage(LanguageManager.auctionStartItemText, item,false);
        sendMessage(LanguageManager.auctionStartPriceText.replace("%startprice%",currentAuction.getStartPrice()+""));
        sendMessage(LanguageManager.auctionMinLeft.replace("%timeleft%", getTimeString(time)));
        startEndAuctionTask(time,false);
        startMessageTask(time,false);
        return true;
    }

    private void startMessageTask(int time,boolean override)
    {
        timeLeft = time;
        if (override)
        {
            Bukkit.getScheduler().cancelTask(timerTask);
        }
        timerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(EasyAuction.getInstance(), () ->
        {
            timeLeft -= remainingTimeLoop;
            sendHoverMessage(LanguageManager.auctionRunText,currentAuction.getAuctionItem(),true);
            sendMessage(LanguageManager.auctionMinLeft.replace("%timeleft%", getTimeString(timeLeft)));
        }, (long) 10 * 20, (long) 10 * 20);
    }

    private void startEndAuctionTask(int time,boolean override)
    {
        if (override)
        {
            Bukkit.getScheduler().cancelTask(timerEndAuctionTask);
        }
        timerEndAuctionTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EasyAuction.getInstance(), this::endAuction, (time * 20));
    }

    private void endAuction()
    {
        if (currentAuction != null)
        {
            Bukkit.getScheduler().cancelTask(timerTask);
            if (currentAuction.getBidPlayer() != null)
            {
                auctionEndWithWinner();
            }
            else
            {
                auctionEndWithNoWinner();
            }
            currentAuction = null;
        }
    }

    private void auctionEndWithWinner()
    {
        CoolDownManager.getInstance().addPlayerHasCoolDown(currentAuction.getAuctionStartPlayer());
        String ae = "";
        if (currentAuction.getBidPlayer().isOnline())
        {
            DatabaseManager.getInstance().createLog(currentAuction.getAuctionStartPlayer().getDisplayName(), currentAuction.getAuctionItem().toString(), currentAuction.getBidPlayer().getDisplayName(), currentAuction.getPriceCurrent());
            sendMessage(LanguageManager.auctionEnd.replace("%winner%", currentAuction.getBidPlayer().getName() + ""));
            if (!giveWinnerItemAndPay())
            {
                getAuctionStartItemBack();
                currentAuction.getAuctionStartPlayer().sendMessage(LanguageManager.goWrong);
                currentAuction.getBidPlayer().sendMessage(LanguageManager.goWrong);
            }
        }
        else
        {
            sendMessage(LanguageManager.auctionEndWinnerOff);
            CommandExecuteManager.getInstance().banPlayer(currentAuction.getBidPlayer().getName(), EasyAuction.getInstance().getConfig().getInt("general.bantime"));
            DatabaseManager.getInstance().createLog(currentAuction.getAuctionStartPlayer().getDisplayName(), currentAuction.getAuctionItem().toString(), "---", 0);
        }
    }

    private void auctionEndWithNoWinner()
    {
        CoolDownManager.getInstance().addPlayerHasCoolDown(currentAuction.getAuctionStartPlayer());
        DatabaseManager.getInstance().createLog(currentAuction.getAuctionStartPlayer().getDisplayName(), currentAuction.getAuctionItem().toString(), "---", 0);
        sendMessage(LanguageManager.auctionEndNoWinner);
        getAuctionStartItemBack();
    }

    private boolean giveWinnerItemAndPay()
    {
        if (currentAuction.getBidPlayer() != null && currentAuction.getBidPlayer().isOnline())
        {
            Map<Integer, ItemStack> map = null;
            map = currentAuction.getBidPlayer().getInventory().addItem(currentAuction.getAuctionItem());
            if (map.size() == 1)
            {
                for (final ItemStack item : map.values())
                {
                    currentAuction.getBidPlayer().getWorld().dropItemNaturally(currentAuction.getBidPlayer().getLocation(), item);
                }
                map.clear();
            }

            EconomyManager.getInstance().removeMoney(currentAuction.getBidPlayer(), currentAuction.getPriceCurrent());
            currentAuction.getBidPlayer().sendMessage(LanguageManager.moneyRemoved.replace("%money%", currentAuction.getPriceCurrent() + ""));
            currentAuction.getBidPlayer().sendMessage(LanguageManager.itemGet.replace("%item%", currentAuction.getAuctionItem().getType() + ""));

            EconomyManager.getInstance().addMoney(currentAuction.getAuctionStartPlayer(), currentAuction.getPriceCurrent() - ((((double) EasyAuction.getInstance().getConfig().getInt("general.fee")) / 100) * currentAuction.getPriceCurrent()));
            currentAuction.getAuctionStartPlayer().sendMessage(LanguageManager.moneyGet.replace("%money%", "" + (currentAuction.getPriceCurrent() - ((((double) EasyAuction.getInstance().getConfig().getInt("general.fee")) / 100) * currentAuction.getPriceCurrent()))));
            currentAuction.getAuctionStartPlayer().sendMessage(LanguageManager.itemRemoved.replace("%item%", currentAuction.getAuctionItem().getType() + ""));
            return true;
        }
        else
        {
            return false;
        }
    }

    private void getAuctionStartItemBack()
    {
        Map<Integer, ItemStack> map = null;
        map = currentAuction.getAuctionStartPlayer().getInventory().addItem(currentAuction.getAuctionItem());
        if (map.size() == 1)
        {
            for (final ItemStack item : map.values())
            {
                currentAuction.getAuctionStartPlayer().getWorld().dropItemNaturally(currentAuction.getAuctionStartPlayer().getLocation(), item);
            }
            map.clear();
        }
    }


    private void sendMessage(String msg)
    {
        for (Player pl : PlayerSeeAuctionManager.getPlayerSeeAuction())
        {
            if (pl != null && pl.isOnline())
            {
                pl.sendMessage(msg);
            }
        }
    }

    private void sendHoverMessage(String msg, ItemStack item,boolean update)
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
        if (update)
        {
           msg = msg.replace("%price%",currentAuction.getPriceCurrent()+"").replace("%bidplayer%",currentAuction.getBidPlayer() != null ? currentAuction.getBidPlayer().getName() : "---");
        }
        String[] parts = msg.split(" ");
        int split = 0;


        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].contains("%item%"))
            {
                split = i;
            }
        }

        net.minecraft.server.v1_15_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = new NBTTagCompound();
        nms.save(tag);
        TextComponent text = new TextComponent();
        for (int i = 0; i < parts.length; i++)
        {
            if (i == split)
            {
                TextComponent h = new TextComponent(tag.toString());

                TextComponent messageh = new TextComponent("["+itemtext+"] ");
                messageh.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ComponentBuilder(h).create()));
                text.addExtra(messageh);
            }
            else
            {
                text.addExtra(parts[i]+ " ");
            }
        }

        for (Player pl : PlayerSeeAuctionManager.getPlayerSeeAuction())
        {
            if (pl != null && pl.isOnline())
            {
                pl.spigot().sendMessage(text);
            }
        }
    }

    public void stopAuction(boolean playerOff, Player p)
    {
        Map<Integer, ItemStack> map = null;
        if (playerOff)
        {
            map = p.getInventory().addItem(currentAuction.getAuctionItem());
            if (map.size() == 1)
            {
                for (final ItemStack item : map.values())
                {
                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                }
                map.clear();
            }
            CommandExecuteManager.getInstance().banPlayer(p.getName(), EasyAuction.getInstance().getConfig().getInt("general.bantime"));
        }
        else
        {
            map = currentAuction.getAuctionStartPlayer().getInventory().addItem(currentAuction.getAuctionItem());
            if (map.size() == 1)
            {
                for (final ItemStack item : map.values())
                {
                    currentAuction.getAuctionStartPlayer().getWorld().dropItemNaturally(currentAuction.getAuctionStartPlayer().getLocation(), item);
                }
                map.clear();
            }
        }

        DatabaseManager.getInstance().createLog(currentAuction.getAuctionStartPlayer().getDisplayName(), currentAuction.getAuctionItem().toString(), "---", 0);
        Bukkit.getScheduler().cancelTask(timerTask);
        Bukkit.getScheduler().cancelTask(timerEndAuctionTask);
        for (Player pl : PlayerSeeAuctionManager.getPlayerSeeAuction())
        {
            if (pl != null && pl.isOnline())
            {
                pl.sendMessage(LanguageManager.auctionStop + " " + (playerOff ? LanguageManager.auctionStopPlayerOff : LanguageManager.auctionStopAdmin));
            }
        }
        currentAuction = null;
    }


    public boolean playerBid(Player p, int bid)
    {

        if (currentAuction != null && currentAuction.getPriceCurrent() < bid)
        {
            currentAuction.setBidPlayer(p, bid);
            sendMessage(LanguageManager.playerBid.replace("%player%",currentAuction.getBidPlayer().getName()+"").replace("%price%",currentAuction.getPriceCurrent()+""));
            if (timeLeft == 10)
            {
                startMessageTask(20,true);
                startEndAuctionTask(20,true);
                sendMessage(LanguageManager.timeincreasse.replace("%time%",timeLeft+""));
            }
            return true;
        }
        else
        {
            return false;
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
