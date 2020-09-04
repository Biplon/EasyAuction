package ea.java.Command;

import ea.java.Config.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class CommandAEPLayer implements CommandExecutor
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
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.newCommandAlias))
            {
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.bidCommandAlias))
            {
                //TODO implement
            }
            else if (args[0].equals(LanguageManager.showCommandAlias))
            {
                //TODO implement
            }
        }

        return false;
    }
}
