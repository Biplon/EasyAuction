package ea.java.Config;

import ea.java.EasyAuction;

import java.io.File;

public class ConfigManager
{
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
    }
}
