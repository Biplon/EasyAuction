package ea.java.Manager;

import ea.java.EasyAuction;
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

    private final List<String> blacklistlore = new ArrayList<>();

    public BlackList()
    {
        instance = this;
        loadBlackList();
    }

    public void loadBlackList()
    {
        blacklistlore.clear();
        boolean blackListFound = true;
        int count = 0;
        while (blackListFound)
        {
            if (EasyAuction.getInstance().getConfig().getString("general.blacklist." + count + ".lore") != null)
            {
                blacklistlore.add(EasyAuction.getInstance().getConfig().getString("general.blacklist." + count + ".lore"));
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
            String lore = currentItem.getItemMeta().getLore().get(0);
            for (String s : blacklistlore)
            {
                if (s.contains(lore))
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            return true;
        }
    }
}
