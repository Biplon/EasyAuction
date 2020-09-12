package ea.java;

import ea.java.Command.CommandAEAdmin;
import ea.java.Command.CommandAEPlayer;
import ea.java.Command.CommandAEPlayerBid;
import ea.java.Command.CommandAEPlayerDetails;
import ea.java.Config.ConfigManager;
import ea.java.Config.LanguageManager;
import ea.java.Database.DatabaseManager;
import ea.java.Events.OnInventoryClick;
import ea.java.Events.OnInventoryClose;
import ea.java.Events.OnPlayerLeave;
import ea.java.Events.OnPlayerLogin;
import ea.java.Manager.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EasyAuction extends JavaPlugin
{
    //EasyAuction instance
    private static EasyAuction instance;
    //getter EasyAuction instance
    public static EasyAuction getInstance()
    {
        return instance;
    }

    //on enable create all class instances and reg commands events and co
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

    //register all commands and the alias
    private void regCommands()
    {
        Objects.requireNonNull(this.getCommand("auction")).setExecutor(new CommandAEPlayer());
        Objects.requireNonNull(this.getCommand("bid")).setExecutor(new CommandAEPlayerBid());
        Objects.requireNonNull(this.getCommand("details")).setExecutor(new CommandAEPlayerDetails());
        Objects.requireNonNull(this.getCommand("auctionadmin")).setExecutor(new CommandAEAdmin());
        List<String> alias = new ArrayList<>();
        alias.add(LanguageManager.auctionCommandAlias);
        Objects.requireNonNull(this.getCommand("auction")).setAliases(alias);
        alias.clear();
        alias.add(LanguageManager.auctionAdminCommandAlias);
        Objects.requireNonNull(this.getCommand("auctionadmin")).setAliases(alias);
        alias.clear();
        alias.add(LanguageManager.auctionAdminCommandAlias);
        Objects.requireNonNull(this.getCommand("bid")).setAliases(alias);
    }

    //register all event listener
    private void regEvents()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new OnPlayerLogin(), this);
        pm.registerEvents(new OnPlayerLeave(), this);
        pm.registerEvents(new OnInventoryClick(), this);
        pm.registerEvents(new OnInventoryClose(), this);
    }

    //unregister all event listener and close db connection
    @Override
    public void onDisable()
    {
        DatabaseManager.getInstance().closeConnection();
        HandlerList.unregisterAll(this);
    }
}
