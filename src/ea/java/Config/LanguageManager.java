package ea.java.Config;

import ea.java.EasyAuction;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageManager
{


    public static void loadLang()
    {
        File configFile = new File("plugins" + File.separator + EasyAuction.getInstance().getName() + File.separator + EasyAuction.getInstance().getConfig().getString("general.lang") + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
        //TODO implement language
    }
}
