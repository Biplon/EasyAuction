package ea.java.Command;

import ea.java.Config.LanguageManager;
import ea.java.Manager.CommandExecuteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAEPlayer implements CommandExecutor
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
                CommandExecuteManager.getInstance().showCommandsCommandExecute(player);
                return true;
            }
            else if (args[0].equals(LanguageManager.newCommandAlias))
            {
                CommandExecuteManager.getInstance().openNewAuctionGUI(player);
                return true;
            }
            else if (args[0].equals(LanguageManager.bidCommandAlias) && args.length == 2)
            {
                CommandExecuteManager.getInstance().playerBid(player, Integer.parseInt(args[1]));
            }
            else if (args[0].equals(LanguageManager.showCommandAlias))
            {
                CommandExecuteManager.getInstance().showAuctionCommandExecute(player);
                return true;
            }
            else if (args[0].equals(LanguageManager.salesCommandAlias))
            {
                CommandExecuteManager.getInstance().salesAuctionCommandExecute(player);
                return true;
            }
            else
            {
                CommandExecuteManager.getInstance().showCommandsCommandExecute(player);
                return true;
            }
            return false;
        }
        return false;
    }
}
