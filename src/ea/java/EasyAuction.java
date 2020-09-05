package ea.java;

import ea.java.Command.CommandAEAdmin;
import ea.java.Command.CommandAEPLayer;
import ea.java.Config.ConfigManager;
import ea.java.Config.LanguageManager;
import ea.java.Database.DatabaseManager;
import ea.java.Events.OnInventoryClick;
import ea.java.Events.OnInventoryClose;
import ea.java.Events.OnPlayerLeave;
import ea.java.Events.OnPlayerLogin;
import ea.java.Manager.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
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
        new CommandExecuteManager();
        new GUIManager();
        new CoolDownManager();
        new AuctionManager();
        new BlackList();
        regCommands();
        regEvents();
        new EconomyManager();
    }

    private void regCommands()
    {
        Objects.requireNonNull(this.getCommand("auction")).setExecutor(new CommandAEPLayer());
        Objects.requireNonNull(this.getCommand("auctionadmin")).setExecutor(new CommandAEAdmin());
        List<String> alias = new ArrayList<>();
        alias.add(LanguageManager.auctionCommandAlias);
        Objects.requireNonNull(this.getCommand("auction")).setAliases(alias);
        alias.clear();
        alias.add(LanguageManager.auctionAdminCommandAlias);
        Objects.requireNonNull(this.getCommand("auctionadmin")).setAliases(alias);
    }

    private void regEvents()
    {
        //TODO implement events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new OnPlayerLogin(), this);
        pm.registerEvents(new OnPlayerLeave(), this);
        pm.registerEvents(new OnInventoryClick(), this);
        pm.registerEvents(new OnInventoryClose(), this);
    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll(this);
    }
}
