package ea.java.Config;

import ea.java.EasyAuction;

import java.io.File;

public class ConfigManager
{
    //min auction time
    public static int auctionMinTime;
    //max auction time
    public static int auctionMaxTime;
    //default time between 2 auctions for 1 Player
    public static int defaultAuctionCoolDown;
    //step for bid in %
    public static int bidStep;
    //default ban time
    public static int banTime;
    //fee in % after auction
    public static int fee;

    //load values from config
    public static void loadValues()
    {
        auctionMinTime = EasyAuction.getInstance().getConfig().getInt("general.auctiontimeMin");
        auctionMaxTime = EasyAuction.getInstance().getConfig().getInt("general.auctiontimeMax");
        defaultAuctionCoolDown = EasyAuction.getInstance().getConfig().getInt("general.timerauction");
        bidStep = EasyAuction.getInstance().getConfig().getInt("general.bidsteps");
        banTime = EasyAuction.getInstance().getConfig().getInt("general.bantime");
        fee = EasyAuction.getInstance().getConfig().getInt("general.fee");
    }

    //load config if not found create default config
    public static void loadConfig()
    {
        File configFile = new File("plugins" + File.separator + EasyAuction.getInstance().getName() + File.separator + "config.yml");
        if (!configFile.exists())
        {
            EasyAuction.getInstance().getLogger().info("Creating config ...");
            EasyAuction.getInstance().saveDefaultConfig();
        }
        try
        {
            EasyAuction.getInstance().getLogger().info("Loading the config ...");
            EasyAuction.getInstance().getConfig().load(configFile);
        }
        catch (Exception e)
        {
            EasyAuction.getInstance().getLogger().severe("Could not load the config! Error: " + e.getMessage());
            e.printStackTrace();
        }
        loadValues();
    }
}
