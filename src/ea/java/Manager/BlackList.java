package ea.java.Manager;

import ea.java.EasyAuction;
import ea.java.Struct.CoolDownGroup;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlackList
{
    private static BlackList instance;

    public static BlackList getInstance()
    {
        return instance;
    }

    private List<String> blacklistlore = new ArrayList<>();

    public BlackList()
    {
        instance = this;
        loadBlackList();
    }

    private void loadBlackList()
    {
        boolean blackListFound = true;
        int count = 0;
        while (blackListFound)
        {
            if (EasyAuction.getInstance().getConfig().getString("general.blacklist."+ count +".lore") != null)
            {
                blacklistlore.add(EasyAuction.getInstance().getConfig().getString("general.blacklist."+ count +".lore"));
                count++;
            }
            else
            {
                blackListFound = false;
            }
        }
    }

    public boolean isNotOnBlackList(ItemStack currentItem)
    {
        if (Objects.requireNonNull(currentItem.getItemMeta()).hasLore())
        {
            return !blacklistlore.contains(currentItem.getItemMeta().getLore().get(0));
        }
        else
        {
           return true;
        }
    }
}
