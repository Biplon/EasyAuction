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

    //admin commands
    public static String startCommandAlias;
    public static String winCommandAlias;
    public static String enableCommandAlias;
    public static String disableCommandAlias;
    public static String stopCommandAlias;
    public static String banCommandAlias;
    public static String pardonCommandAlias;
    public static String reloadCommandAlias;

    public static void loadLang()
    {
        File configFile = new File("plugins" + File.separator + EasyAuction.getInstance().getName() + File.separator + EasyAuction.getInstance().getConfig().getString("general.lang") + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
        //player command alias
        newCommandAlias = cfg.getString("newCommandAlias") != null ? cfg.getString("newCommandAlias") : "new";
        bidCommandAlias = cfg.getString("bidCommandAlias") != null ? cfg.getString("bidCommandAlias") : "bid";
        salesCommandAlias = cfg.getString("salesCommandAlias") != null ? cfg.getString("salesCommandAlias") : "sales";
        showCommandAlias = cfg.getString("showCommandAlias") != null ? cfg.getString("showCommandAlias") : "show";
        //admin command alias
        startCommandAlias = cfg.getString("startCommandAlias") != null ? cfg.getString("startCommandAlias") : "start";
        winCommandAlias = cfg.getString("winCommandAlias") != null ? cfg.getString("winCommandAlias") : "win";
        enableCommandAlias = cfg.getString("enableCommandAlias") != null ? cfg.getString("enableCommandAlias") : "enable";
        disableCommandAlias = cfg.getString("disableCommandAlias") != null ? cfg.getString("disableCommandAlias") : "disable";
        stopCommandAlias = cfg.getString("stopCommandAlias") != null ? cfg.getString("stopCommandAlias") : "stop";
        banCommandAlias = cfg.getString("banCommandAlias") != null ? cfg.getString("banCommandAlias") : "ban";
        pardonCommandAlias = cfg.getString("pardonCommandAlias") != null ? cfg.getString("pardonCommandAlias") : "pardon";
        reloadCommandAlias = cfg.getString("reloadCommandAlias") != null ? cfg.getString("reloadCommandAlias") : "reload";
        //TODO implement language
    }
}
