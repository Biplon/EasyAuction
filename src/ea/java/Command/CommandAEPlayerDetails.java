package ea.java.Command;

import ea.java.Manager.CommandExecuteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAEPlayerDetails implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        //check is player and have perms
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            if (!player.hasPermission("ea.player"))
            {
                return false;
            }
            if (args.length == 0)
            {
                CommandExecuteManager.getInstance().showItemDetails(player);
                return true;
            }
        }
        return false;
    }
}
