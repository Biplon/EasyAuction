package ea.java.Manager;

import ea.java.Config.ConfigManager;
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
    //AuctionManager instance
    static AuctionManager instance;

    //current running auction
    private Auction currentAuction = null;

    //task for notification timer
    private int timerTask;
    //task for end auction
    private int timerEndAuctionTask;
    //time between notification timer send message
    private final double remainingTimeLoop = 10;
    //remaining time for auction
    private double timeLeft;
    //auction enabled/disabled
    public boolean enabled = true;

    //getter
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


    //start a action. Create Auction object and start timer
    public void startAuction(Player p, ItemStack item, int startPrice, int time)
    {
        currentAuction = new Auction(p, item, startPrice);
        sendMessage(LanguageManager.auctionStartText);
        sendHoverMessage(LanguageManager.auctionStartItemText, item, false);
        sendMessage(LanguageManager.auctionStartPriceText.replace("%startprice%", currentAuction.getStartPrice() + ""));
        sendMessage(LanguageManager.auctionMinLeft.replace("%timeleft%", getTimeString(time)));
        startEndAuctionTask(time, false);
        startMessageTask(time, false);
    }

    //start the notification timer task
    private void startMessageTask(int time, boolean override)
    {
        timeLeft = time;
        if (override)
        {
            Bukkit.getScheduler().cancelTask(timerTask);
        }
        timerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(EasyAuction.getInstance(), () ->
        {
            timeLeft -= remainingTimeLoop;
            sendHoverMessage(LanguageManager.auctionRunText, currentAuction.getAuctionItem(), true);
            sendMessage(LanguageManager.auctionMinLeft.replace("%timeleft%", getTimeString(timeLeft)));
        }, (long) 10 * 20, (long) 10 * 20);
    }

    //start auction end task
    private void startEndAuctionTask(int time, boolean override)
    {
        if (override)
        {
            Bukkit.getScheduler().cancelTask(timerEndAuctionTask);
        }
        timerEndAuctionTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EasyAuction.getInstance(), this::endAuction, (time * 20));
    }

    //stop running auction if auction start player is off ban him and give item back. If stop per admin give start player item back.
    public void stopAuction(boolean playerOff, Player p)
    {
        Map<Integer, ItemStack> map;
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
            CommandExecuteManager.getInstance().banPlayer(p.getName(), ConfigManager.banTime);
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

    //end auction
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

    //if auction ends with winner check if bid player online(if not ban). Set start player cooldown
    private void auctionEndWithWinner()
    {
        CoolDownManager.getInstance().addPlayerHasCoolDown(currentAuction.getAuctionStartPlayer());
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
            DatabaseManager.getInstance().banPlayer(currentAuction.getBidPlayer().getUniqueId(), ConfigManager.banTime);
            DatabaseManager.getInstance().createLog(currentAuction.getAuctionStartPlayer().getDisplayName(), currentAuction.getAuctionItem().toString(), "---", 0);
            getAuctionStartItemBack();

        }
    }

    //if auction ends with no winner give start player item back and set cooldown
    private void auctionEndWithNoWinner()
    {
        CoolDownManager.getInstance().addPlayerHasCoolDown(currentAuction.getAuctionStartPlayer());
        DatabaseManager.getInstance().createLog(currentAuction.getAuctionStartPlayer().getDisplayName(), currentAuction.getAuctionItem().toString(), "---", 0);
        sendMessage(LanguageManager.auctionEndNoWinner);
        getAuctionStartItemBack();
    }

    //give/remove all bid and star player item and money.
    private boolean giveWinnerItemAndPay()
    {
        if (currentAuction.getBidPlayer() != null && currentAuction.getBidPlayer().isOnline())
        {
            Map<Integer, ItemStack> map;
            map = currentAuction.getBidPlayer().getInventory().addItem(currentAuction.getAuctionItem());
            if (map.size() == 1)
            {
                for (final ItemStack item : map.values())
                {
                    currentAuction.getBidPlayer().getWorld().dropItemNaturally(currentAuction.getBidPlayer().getLocation(), item);
                }
                map.clear();
            }

            if ( EconomyManager.getInstance().removeMoney(currentAuction.getBidPlayer(), currentAuction.getPriceCurrent()))
            {
                currentAuction.getBidPlayer().sendMessage(LanguageManager.moneyRemoved.replace("%money%", currentAuction.getPriceCurrent() + ""));
                String itemtext = currentAuction.getAuctionItem().getAmount() + "x ";
                if (!currentAuction.getAuctionItem().getItemMeta().getDisplayName().equals(""))
                {
                    itemtext += currentAuction.getAuctionItem().getItemMeta().getDisplayName();
                }
                else
                {
                    itemtext += currentAuction.getAuctionItem().getType();
                }
                currentAuction.getBidPlayer().sendMessage(LanguageManager.itemGet.replace("%item%", itemtext));

                EconomyManager.getInstance().addMoney(currentAuction.getAuctionStartPlayer(), currentAuction.getPriceCurrent() - (((double) ConfigManager.fee / 100) * currentAuction.getPriceCurrent()));
                currentAuction.getAuctionStartPlayer().sendMessage(LanguageManager.moneyGet.replace("%money%", "" + (currentAuction.getPriceCurrent() - ((((double) ConfigManager.fee) / 100) * currentAuction.getPriceCurrent()))));
                currentAuction.getAuctionStartPlayer().sendMessage(LanguageManager.itemRemoved.replace("%item%", itemtext));
                return true;
            }
            else
            {
                return false;
            }

        }
        else
        {
            return false;
        }
    }

    //give start player item back
    private void getAuctionStartItemBack()
    {
        Map<Integer, ItemStack> map;
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

    //set player bid and send notification to all player. If time lower 10 increase to 20
    public boolean playerBid(Player p, int bid)
    {

        if (currentAuction != null && currentAuction.getPriceCurrent() < bid)
        {
            currentAuction.setBidPlayer(p, bid);
            sendMessage(LanguageManager.playerBid.replace("%player%", currentAuction.getBidPlayer().getName() + "").replace("%price%", currentAuction.getPriceCurrent() + ""));
            if (timeLeft == 10)
            {
                startMessageTask(20, true);
                startEndAuctionTask(20, true);
                sendMessage(LanguageManager.timeincreasse.replace("%time%", timeLeft + ""));
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    //send message to all player who can see auction
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

    //send hovertext to all player who can see auction
    private void sendHoverMessage(String msg, ItemStack item, boolean update)
    {
        String itemtext = item.getAmount() + "x ";
        if (!item.getItemMeta().getDisplayName().equals(""))
        {
            itemtext += item.getItemMeta().getDisplayName();
        }
        else
        {
            itemtext += item.getType().name();
        }
        if (update)
        {
            msg = msg.replace("%price%", currentAuction.getPriceCurrent() + "").replace("%bidplayer%", currentAuction.getBidPlayer() != null ? currentAuction.getBidPlayer().getName() : "---");
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

                TextComponent messageh = new TextComponent("[" + itemtext + "] ");
                messageh.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/details"));
                messageh.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ComponentBuilder(h).create()));
                text.addExtra(messageh);
            }
            else
            {
                text.addExtra(parts[i] + " ");
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

    //get time string from double
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
