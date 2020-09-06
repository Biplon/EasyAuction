package ea.java.Command;

import com.google.gson.internal.$Gson$Types;
import ea.java.Config.LanguageManager;
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
            Player player = (Player) commandSender;
            if (!player.hasPermission("ea.player"))
            {
                return false;
            }
            if (args.length == 1)
            {
                CommandExecuteManager.getInstance().playerBid(player,Integer.parseInt(args[0]));
                return true;
            }
        }
        return false;
    }
}
