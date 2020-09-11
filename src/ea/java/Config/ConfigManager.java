package ea.java.Config;

import ea.java.EasyAuction;

import java.io.File;

public class ConfigManager
{
    public static int auctionMinTime;
    public static int auctionMaxTime;

    public static int defaultAuctionCoolDown;

    public static int bidStep;

    public static int banTime;

    public static int fee;

    public static void loadValues()
    {
        auctionMinTime = EasyAuction.getInstance().getConfig().getInt("general.auctiontimeMin");
        auctionMaxTime = EasyAuction.getInstance().getConfig().getInt("general.auctiontimeMax");
        defaultAuctionCoolDown = EasyAuction.getInstance().getConfig().getInt("general.timerauction");
        bidStep = EasyAuction.getInstance().getConfig().getInt("general.bidsteps");
        banTime = EasyAuction.getInstance().getConfig().getInt("general.bantime");
        fee = EasyAuction.getInstance().getConfig().getInt("general.fee");
    }

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
