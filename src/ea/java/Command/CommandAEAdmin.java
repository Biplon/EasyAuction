package ea.java.Command;

import ea.java.Config.LanguageManager;
import ea.java.Manager.CommandExecuteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAEAdmin implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        //check is player
        if (commandSender instanceof Player)
        {
            //get Player and check perms
            Player player = (Player) commandSender;
            if (!player.hasPermission("ea.admin"))
            {
                return false;
            }
            //check args and send it to CommandExecuteManager
            if (args.length == 0)
            {
                CommandExecuteManager.getInstance().showCommandsAdminCommandExecute(player);
            }
            else if (args.length == 2 && args[0].equals(LanguageManager.startCommandAlias))
            {
                CommandExecuteManager.getInstance().getPlayerStats(player, args[1], false);
            }
            else if (args.length == 2 && args[0].equals(LanguageManager.winCommandAlias))
            {
                CommandExecuteManager.getInstance().getPlayerStats(player, args[1], true);
            }
            else if (args[0].equals(LanguageManager.enableCommandAlias))
            {
                CommandExecuteManager.getInstance().endThisAuction(true, false);
                player.sendMessage("Easy auction enabled");
            }
            else if (args[0].equals(LanguageManager.disableCommandAlias))
            {
                CommandExecuteManager.getInstance().endThisAuction(false, args.length == 2);
                player.sendMessage("Easy auction disabled");
            }
            else if (args[0].equals(LanguageManager.stopCommandAlias))
            {
                CommandExecuteManager.getInstance().stopAuction();
                player.sendMessage("Auction stopped!");
            }
            else if (args.length == 3 && args[0].equals(LanguageManager.banCommandAlias))
            {
                CommandExecuteManager.getInstance().banPlayer(args[1], Integer.parseInt(args[2]));
                player.sendMessage(args[1]+ " banned!");
            }
            else if (args.length == 2 && args[0].equals(LanguageManager.pardonCommandAlias))
            {
                CommandExecuteManager.getInstance().pardonPlayer(args[1]);
                player.sendMessage("pardon " +args[1] +"!");
            }
            else if (args[0].equals(LanguageManager.reloadCommandAlias))
            {
                CommandExecuteManager.getInstance().reload();
                player.sendMessage("Easy auction reload!");
            }
            return true;
        }
        //enable and disable works over console
        if (args[0].equals(LanguageManager.disableCommandAlias))
        {
            CommandExecuteManager.getInstance().endThisAuction(false, args.length == 2);
        }
        else if (args[0].equals(LanguageManager.enableCommandAlias))
        {
            CommandExecuteManager.getInstance().endThisAuction(true, false);
        }
        return false;
    }
}

