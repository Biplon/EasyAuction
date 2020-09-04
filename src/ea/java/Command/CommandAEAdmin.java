package ea.java.Command;

import ea.java.Config.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

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
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.startCommandAlias))
            {
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.winCommandAlias))
            {
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.enableCommandAlias))
            {
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.disableCommandAlias))
            {
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.stopCommandAlias))
            {
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.banCommandAlias))
            {
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.pardonCommandAlias))
            {
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.reloadCommandAlias))
            {
                //TODO implement
            }
        }
        return false;
    }
}

