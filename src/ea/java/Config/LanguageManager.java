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
    public static String salesCommandAlias;
    public static String showCommandAlias;

    public static String auctionCommandAlias;

    //player commands outputText

    public static String showCommandOutput;
    public static String coolDownText;

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

    public static void loadLang()
    {
        File configFile = new File("plugins" + File.separator + EasyAuction.getInstance().getName() + File.separator + EasyAuction.getInstance().getConfig().getString("general.lang") + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
        //player command alias
        newCommandAlias = cfg.getString("newCommandAlias") != null ? cfg.getString("newCommandAlias") : "new";
        bidCommandAlias = cfg.getString("bidCommandAlias") != null ? cfg.getString("bidCommandAlias") : "bid";
        salesCommandAlias = cfg.getString("salesCommandAlias") != null ? cfg.getString("salesCommandAlias") : "sales";
        showCommandAlias = cfg.getString("showCommandAlias") != null ? cfg.getString("showCommandAlias") : "show";
        auctionCommandAlias = cfg.getString("auctionCommandAlias") != null ? cfg.getString("auctionCommandAlias") : "auction";
        //player commands outputText
        showCommandOutput = cfg.getString("showCommandOutput") != null ? cfg.getString("showCommandOutput") : "Show auctions:";

        coolDownText = cfg.getString("coolDownText") != null ? cfg.getString("coolDownText") : "Can not start auction. You have cooldown!";

        //GUI text
        createAuctionGui = cfg.getString("createAuctionGui") != null ? cfg.getString("createAuctionGui") : "New Auction";
        timeplus = cfg.getString("timeplus") != null ? cfg.getString("timeplus") : "Increase auction time";
        timeminus = cfg.getString("timeminus") != null ? cfg.getString("timeminus") : "Decrease auction time";
        timecur = cfg.getString("timecur") != null ? cfg.getString("timecur") : "Auction time: ";
        insertitem = cfg.getString("insertitem") != null ? cfg.getString("insertitem") : "Insert item below:";

        moneyplus = cfg.getString("moneyplus") != null ? cfg.getString("moneyplus") : "Increase start price:";


        moneyminus = cfg.getString("moneyminus") != null ? cfg.getString("moneyminus") : "Lower start price:";

        moneycur = cfg.getString("moneycur") != null ? cfg.getString("moneycur") : "Start price:";

        startauction = cfg.getString("startauction") != null ? cfg.getString("startauction") : "Start auction";


        //admin command alias
        startCommandAlias = cfg.getString("startCommandAlias") != null ? cfg.getString("startCommandAlias") : "start";
        winCommandAlias = cfg.getString("winCommandAlias") != null ? cfg.getString("winCommandAlias") : "win";
        enableCommandAlias = cfg.getString("enableCommandAlias") != null ? cfg.getString("enableCommandAlias") : "enable";
        disableCommandAlias = cfg.getString("disableCommandAlias") != null ? cfg.getString("disableCommandAlias") : "disable";
        stopCommandAlias = cfg.getString("stopCommandAlias") != null ? cfg.getString("stopCommandAlias") : "stop";
        banCommandAlias = cfg.getString("banCommandAlias") != null ? cfg.getString("banCommandAlias") : "ban";
        pardonCommandAlias = cfg.getString("pardonCommandAlias") != null ? cfg.getString("pardonCommandAlias") : "pardon";
        reloadCommandAlias = cfg.getString("reloadCommandAlias") != null ? cfg.getString("reloadCommandAlias") : "reload";
        auctionAdminCommandAlias= cfg.getString("auctionAdminCommandAlias") != null ? cfg.getString("auctionAdminCommandAlias") : "auctionadmin";

        //chat text
        auctionMinLeft = cfg.getString("auctionMinLeft") != null ? cfg.getString("auctionMinLeft") : "Auction ends in: %timeleft%";
        auctionStartText = cfg.getString("auctionStartText") != null ? cfg.getString("auctionStartText") : "Auction started! %item% Price: %startprice%";
        auctionRunText = cfg.getString("auctionRunText") != null ? cfg.getString("auctionRunText") : "%item% Price: %price%";
        minText = cfg.getString("mintext") != null ? cfg.getString("mintext") : "min";
        secText = cfg.getString("sectext") != null ? cfg.getString("sectext") : "sec";
        auctionEnd = cfg.getString("auctionEnd") != null ? cfg.getString("auctionEnd") : "Auction ends. Winner is: %winner%";

        auctionStop = cfg.getString("auctionStop") != null ? cfg.getString("auctionStop") : "Auction stopped.";
        auctionStopPlayerOff = cfg.getString("auctionStopPlayerOff") != null ? cfg.getString("auctionStopPlayerOff") : "Player offline";
        auctionStopAdmin = cfg.getString("auctionStopAdmin") != null ? cfg.getString("auctionStopAdmin") : "by Admin";
        //TODO implement language
    }
}
