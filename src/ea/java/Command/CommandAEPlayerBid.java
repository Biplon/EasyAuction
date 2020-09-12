package ea.java.Command;

import ea.java.Manager.CommandExecuteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAEPlayerBid implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        //check is player and have perms
        if (commandSender instanceof Player)
        {
            //get Player and check perms
            Player player = (Player) commandSender;
            if (!player.hasPermission("ea.player"))
            {
                return false;
            }
            //check args and send it to CommandExecuteManager
            if (args.length == 1)
            {
                CommandExecuteManager.getInstance().playerBid(player, Integer.parseInt(args[0]));
                return true;
            }
        }
        return false;
    }
}
