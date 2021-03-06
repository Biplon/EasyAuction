package ea.java.Config;

import ea.java.EasyAuction;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageManager
{
    //player commands
    public static String newCommandAlias;
    public static String bidCommandAlias;
    public static String bidCommandAliasShort;
    public static String salesCommandAlias;
    public static String showCommandAlias;
    public static String auctionCommandAlias;

    public static String ddetailsCommand;
    public static String dnewCommand;
    public static String dbidCommand;
    public static String dsalesCommand;
    public static String dshowCommand;
    public static String dstartwinCommand;
    public static String denabledisableCommand;
    public static String dstopCommand;
    public static String dbanCommand;
    public static String dpardonCommand;
    public static String dreloadCommand;


    //player commands outputText

    public static String showCommandOutput;
    public static String coolDownText;
    public static String bannedText;
    public static String auctionRunning;
    public static String auctionDisabled;
    public static String salesPlayerText;
    public static String noAuctionRunning;
    public static String nobidOnOwn;

    //GUI text
    public static String createAuctionGui;

    public static String timeplus;
    public static String timeminus;
    public static String timecur;
    public static String insertitem;
    public static String moneyplus;
    public static String moneyminus;
    public static String startauction;
    public static String moneycur;

    //admin commands
    public static String startCommandAlias;
    public static String winCommandAlias;
    public static String enableCommandAlias;
    public static String disableCommandAlias;
    public static String stopCommandAlias;
    public static String banCommandAlias;
    public static String pardonCommandAlias;
    public static String reloadCommandAlias;
    public static String auctionAdminCommandAlias;

    //chat text
    public static String auctionMinLeft;
    public static String auctionStartText;
    public static String auctionRunText;
    public static String minText;
    public static String secText;
    public static String auctionEnd;
    public static String auctionStop;
    public static String auctionStopPlayerOff;
    public static String auctionStopAdmin;
    public static String notEnoughMoney;
    public static String bidToLow;
    public static String goWrong;
    public static String youBid;
    public static String playerBid;
    public static String auctionEndWinnerOff;
    public static String moneyGet;
    public static String itemGet;
    public static String moneyRemoved;
    public static String itemRemoved;
    public static String auctionEndNoWinner;
    public static String auctionStartItemText;
    public static String auctionStartPriceText;
    public static String timeincreasse;


    //get lang file and load all String
    public static void loadLang()
    {
        File configFile = new File("plugins" + File.separator + EasyAuction.getInstance().getName() + File.separator + EasyAuction.getInstance().getConfig().getString("general.lang") + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
        //player command alias
        newCommandAlias = cfg.getString("newCommandAlias") != null ? cfg.getString("newCommandAlias") : "new";
        bidCommandAlias = cfg.getString("bidCommandAlias") != null ? cfg.getString("bidCommandAlias") : "bid";
        bidCommandAliasShort = cfg.getString("bidCommandAliasShort") != null ? cfg.getString("bidCommandAliasShort") : "bid";
        salesCommandAlias = cfg.getString("salesCommandAlias") != null ? cfg.getString("salesCommandAlias") : "sales";
        showCommandAlias = cfg.getString("showCommandAlias") != null ? cfg.getString("showCommandAlias") : "show";
        auctionCommandAlias = cfg.getString("auctionCommandAlias") != null ? cfg.getString("auctionCommandAlias") : "auction";
        //player commands outputText
        showCommandOutput = cfg.getString("showCommandOutput") != null ? cfg.getString("showCommandOutput") : "Show auctions:";

        coolDownText = cfg.getString("coolDownText") != null ? cfg.getString("coolDownText") : "Can not start auction. You have cooldown!";
        bannedText = cfg.getString("bannedText") != null ? cfg.getString("bannedText") : "Can not start auction. You banned!";
        auctionRunning = cfg.getString("auctionRunning") != null ? cfg.getString("auctionRunning") : "Can not start auction. Auction all ready running";
        auctionDisabled = cfg.getString("auctionDisabled") != null ? cfg.getString("auctionDisabled") : "Can not start auction. Auction disabled!";
        salesPlayerText = cfg.getString("salesPlayerText") != null ? cfg.getString("salesPlayerText") : "Your sales: %money%";
        noAuctionRunning = cfg.getString("noAuctionRunning") != null ? cfg.getString("noAuctionRunning") : "No auction running";
        notEnoughMoney = cfg.getString("notEnoughMoney") != null ? cfg.getString("notEnoughMoney") : "not enough money to bid";
        bidToLow = cfg.getString("bidToLow") != null ? cfg.getString("bidToLow") : "bid to low!";
        goWrong = cfg.getString("goWrong") != null ? cfg.getString("goWrong") : "something went wrong!";
        youBid = cfg.getString("youBid") != null ? cfg.getString("youBid") : "bid accepted!";
        auctionEndWinnerOff = cfg.getString("auctionEndWinnerOff") != null ? cfg.getString("auctionEndWinnerOff") : "Winner is offline auction canceled!";
        auctionEndNoWinner = cfg.getString("auctionEndNoWinner") != null ? cfg.getString("auctionEndNoWinner") : "Auction ends. No one bid!";
        moneyGet = cfg.getString("moneyGet") != null ? cfg.getString("moneyGet") : "Auction end. You got after fee: %money%.";
        itemGet = cfg.getString("itemGet") != null ? cfg.getString("itemGet") : "Auction end. You got: %item%.";
        moneyRemoved = cfg.getString("moneyRemoved") != null ? cfg.getString("moneyRemoved") : "Auction end. You paid: %money%.";
        itemRemoved = cfg.getString("itemRemoved") != null ? cfg.getString("itemRemoved") : "Auction end. Item removed: %item%.";
        nobidOnOwn = cfg.getString("nobidOnOwn") != null ? cfg.getString("nobidOnOwn") : "You can not bid.";


        ddetailsCommand = cfg.getString("ddetailsCommand") != null ? cfg.getString("ddetailsCommand") : "show item in auction";
        dnewCommand = cfg.getString("dnewCommand") != null ? cfg.getString("dnewCommand") : "create new auction";
        dbidCommand = cfg.getString("dbidCommand") != null ? cfg.getString("dbidCommand") : "bid on running auction";
        dsalesCommand = cfg.getString("dsalesCommand") != null ? cfg.getString("dsalesCommand") : "show your sales value";
        dshowCommand = cfg.getString("dshowCommand") != null ? cfg.getString("dshowCommand") : "enable/disable auctions notifications";

        dstartwinCommand = cfg.getString("dstartwinCommand") != null ? cfg.getString("dstartwinCommand") : "show player win/start auction";
        denabledisableCommand = cfg.getString("denabledisableCommand") != null ? cfg.getString("denabledisableCommand") : "enable/disable auction system (force stop running auction)";
        dstopCommand = cfg.getString("dstopCommand") != null ? cfg.getString("dstopCommand") : "stop running auction";
        dbanCommand = cfg.getString("dbanCommand") != null ? cfg.getString("dbanCommand") : "ban player for x min";
        dpardonCommand = cfg.getString("dpardonCommand") != null ? cfg.getString("dpardonCommand") : "remove ban from player";
        dreloadCommand = cfg.getString("dreloadCommand") != null ? cfg.getString("dreloadCommand") : "reload config and lang file";



        //GUI text
        createAuctionGui = cfg.getString("createAuctionGui") != null ? cfg.getString("createAuctionGui") : "New Auction";
        timeplus = cfg.getString("timeplus") != null ? cfg.getString("timeplus") : "Increase auction time";
        timeminus = cfg.getString("timeminus") != null ? cfg.getString("timeminus") : "Decrease auction time";
        timecur = cfg.getString("timecur") != null ? cfg.getString("timecur") : "Auction time:";
        insertitem = cfg.getString("insertitem") != null ? cfg.getString("insertitem") : "Insert item below:";
        moneyplus = cfg.getString("moneyplus") != null ? cfg.getString("moneyplus") : "Increase start price:";
        moneyminus = cfg.getString("moneyminus") != null ? cfg.getString("moneyminus") : "Lower start price:";
        moneycur = cfg.getString("moneycur") != null ? cfg.getString("moneycur") : "Start price:";
        startauction = cfg.getString("startauction") != null ? cfg.getString("startauction") : "Start auction!";

        //admin command alias
        startCommandAlias = cfg.getString("startCommandAlias") != null ? cfg.getString("startCommandAlias") : "start";
        winCommandAlias = cfg.getString("winCommandAlias") != null ? cfg.getString("winCommandAlias") : "win";
        enableCommandAlias = cfg.getString("enableCommandAlias") != null ? cfg.getString("enableCommandAlias") : "enable";
        disableCommandAlias = cfg.getString("disableCommandAlias") != null ? cfg.getString("disableCommandAlias") : "disable";
        stopCommandAlias = cfg.getString("stopCommandAlias") != null ? cfg.getString("stopCommandAlias") : "stop";
        banCommandAlias = cfg.getString("banCommandAlias") != null ? cfg.getString("banCommandAlias") : "ban";
        pardonCommandAlias = cfg.getString("pardonCommandAlias") != null ? cfg.getString("pardonCommandAlias") : "pardon";
        reloadCommandAlias = cfg.getString("reloadCommandAlias") != null ? cfg.getString("reloadCommandAlias") : "reload";
        auctionAdminCommandAlias = cfg.getString("auctionAdminCommandAlias") != null ? cfg.getString("auctionAdminCommandAlias") : "auctionadmin";

        //chat text
        auctionMinLeft = cfg.getString("auctionMinLeft") != null ? cfg.getString("auctionMinLeft") : "Auction ends in: %timeleft%";
        auctionStartText = cfg.getString("auctionStartText") != null ? cfg.getString("auctionStartText") : "Auction started!";
        auctionStartItemText = cfg.getString("auctionStartItemText") != null ? cfg.getString("auctionStartItemText") : "Item: %item%";
        auctionStartPriceText = cfg.getString("auctionStartPriceText") != null ? cfg.getString("auctionStartPriceText") : "Start price: %startprice%";

        playerBid = cfg.getString("playerBid") != null ? cfg.getString("playerBid") : "%player% bid. New price: %price%";

        auctionRunText = cfg.getString("auctionRunText") != null ? cfg.getString("auctionRunText") : "%item% Price: %price% Last bid: %bidplayer%";
        minText = cfg.getString("mintext") != null ? cfg.getString("mintext") : "min";
        secText = cfg.getString("sectext") != null ? cfg.getString("sectext") : "sec";
        auctionEnd = cfg.getString("auctionEnd") != null ? cfg.getString("auctionEnd") : "Auction ends. Winner is: %winner%";

        auctionStop = cfg.getString("auctionStop") != null ? cfg.getString("auctionStop") : "Auction stopped.";
        auctionStopPlayerOff = cfg.getString("auctionStopPlayerOff") != null ? cfg.getString("auctionStopPlayerOff") : "Player offline";
        auctionStopAdmin = cfg.getString("auctionStopAdmin") != null ? cfg.getString("auctionStopAdmin") : "by Admin";
        timeincreasse = cfg.getString("timeincreasse") != null ? cfg.getString("timeincreasse") : "Auction time increased: %time%!";
    }
}
