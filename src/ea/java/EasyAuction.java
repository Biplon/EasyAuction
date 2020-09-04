package ea.java;

import ea.java.Command.CommandAEAdmin;
import ea.java.Command.CommandAEPLayer;
import ea.java.Config.ConfigManager;
import ea.java.Config.LanguageManager;
import ea.java.Database.DatabaseManager;
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
        new DatabaseManager();
        regCommands();
        regEvents();
    }

    private void regCommands()
    {
        Objects.requireNonNull(this.getCommand("auction")).setExecutor(new CommandAEPLayer());
        Objects.requireNonNull(this.getCommand("auctionadmin")).setExecutor(new CommandAEAdmin());
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
