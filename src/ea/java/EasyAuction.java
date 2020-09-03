package ea.java;

import ea.java.Config.ConfigManager;
import ea.java.Config.LanguageManager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class EasyAuction extends JavaPlugin
{
    private static EasyAuction instance;

    public static EasyAuction getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        instance = this;
        ConfigManager.loadConfig();
        LanguageManager.loadLang();
        regCommands();
        regEvents();
    }

    private void regCommands()
    {
        //TODO implement commands
        //Objects.requireNonNull(this.getCommand("epgive")).setExecutor(new GivePlayerPetCommand());

    }

    private void regEvents()
    {
        //TODO implement events
        PluginManager pm = getServer().getPluginManager();
      //  pm.registerEvents(new OnPlayerClicks(), this);

    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll(this);
    }
}
