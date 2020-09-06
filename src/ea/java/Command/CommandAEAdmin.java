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
        //check is player and have perms
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            if (!player.hasPermission("ea.admin"))
            {
                return false;
            }
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
                CommandExecuteManager.getInstance().enThisAuction(true, false);
                player.sendMessage("enabled");
            }
            else if (args[0].equals(LanguageManager.disableCommandAlias))
            {
                CommandExecuteManager.getInstance().enThisAuction(false, args.length == 2);
                player.sendMessage("disabled");
            }
            else if (args[0].equals(LanguageManager.stopCommandAlias))
            {
                CommandExecuteManager.getInstance().stopAuction();
                player.sendMessage("stopped");
            }
            else if (args.length == 3 && args[0].equals(LanguageManager.banCommandAlias))
            {
                CommandExecuteManager.getInstance().banPlayer(args[1], Integer.parseInt(args[2]));
                player.sendMessage("banned");
            }
            else if (args.length == 2 && args[0].equals(LanguageManager.pardonCommandAlias))
            {
                CommandExecuteManager.getInstance().pardonPlayer(args[1]);
                player.sendMessage("pardon");
            }
            else if (args[0].equals(LanguageManager.reloadCommandAlias))
            {
                CommandExecuteManager.getInstance().reload();
                player.sendMessage("reload");
            }
            return true;
        }
        if (args[0].equals(LanguageManager.disableCommandAlias))
        {
            CommandExecuteManager.getInstance().enThisAuction(false, args.length == 2);
        }
        else if (args[0].equals(LanguageManager.enableCommandAlias))
        {
            CommandExecuteManager.getInstance().enThisAuction(true, false);
        }

        return false;
    }
}

