package ea.java.Manager;

import ea.java.EasyAuction;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlackList
{
    //BlackList instance
    private static BlackList instance;
    //list for all blacklist items
    private final List<String> blackListLore = new ArrayList<>();
    //getter BlackList instance
    public static BlackList getInstance()
    {
        return instance;
    }

    //constructor load blacklist values and create instance
    public BlackList()
    {
        instance = this;
        loadBlackList();
    }

    //load all blacklist values
    public void loadBlackList()
    {
        blackListLore.clear();
        boolean blackListFound = true;
        int count = 0;
        while (blackListFound)
        {
            if (EasyAuction.getInstance().getConfig().getString("general.blacklist." + count + ".lore") != null)
            {
                blackListLore.add(EasyAuction.getInstance().getConfig().getString("general.blacklist." + count + ".lore"));
                count++;
            }
            else
            {
                blackListFound = false;
            }
        }
    }

    //check if item is on blacklist. Only check 1 lore string
    public boolean isNotOnBlackList(ItemStack currentItem)
    {
        if (Objects.requireNonNull(currentItem.getItemMeta()).hasLore())
        {
            String lore = currentItem.getItemMeta().getLore().get(0);
            for (String s : blackListLore)
            {
                if (lore.contains(s))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
