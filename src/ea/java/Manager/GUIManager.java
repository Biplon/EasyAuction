package ea.java.Manager;

import ea.java.Config.LanguageManager;
import ea.java.EasyAuction;
import ea.java.Enum.EGUIChange;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GUIManager
{
    static GUIManager instance;

    public static GUIManager getInstance()
    {
        return instance;
    }

    private static final ItemStack defaultGuiItem = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " ", "");
    private static final ItemStack defaultGuiItem2 = createGuiItem(Material.YELLOW_STAINED_GLASS_PANE, " ", "");
    public GUIManager()
    {
        instance = this;
    }

    public void changeItemValue(EGUIChange change,int value,Inventory inv)
    {
        switch (change)
        {
            case Time:
                int time =  Integer.parseInt(inv.getItem(22).getItemMeta().getLore().get(0))+value;
                if (time < EasyAuction.getInstance().getConfig().getInt("general.auctiontimeMin"))
                {
                    time = EasyAuction.getInstance().getConfig().getInt("general.auctiontimeMin");
                }
                else if (time > EasyAuction.getInstance().getConfig().getInt("general.auctiontimeMax"))
                {
                    time = EasyAuction.getInstance().getConfig().getInt("general.auctiontimeMax");
                }
                inv.setItem(22,createGuiItem(Material.PAPER,LanguageManager.timecur,""+ time));
                break;
            case Money:
                int money = Integer.parseInt(inv.getItem(25).getItemMeta().getLore().get(0) )+value;
                if (money <= 0)
                {
                    money = 1;
                }
                inv.setItem(25,createGuiItem(Material.PAPER,LanguageManager.moneycur,""+ money));
                break;
        }
    }

    public void openGUI(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 54, "A:" + LanguageManager.createAuctionGui);
        for (int i = 0; i < 9; i++)
        {
            inv.setItem(i, defaultGuiItem);
            inv.setItem(i + 45, defaultGuiItem);
        }
        inv.setItem(9, defaultGuiItem);
        inv.setItem(11, defaultGuiItem);
        inv.setItem(12, defaultGuiItem);
        inv.setItem(14, defaultGuiItem);
        inv.setItem(16, defaultGuiItem);
        inv.setItem(21, defaultGuiItem);
        inv.setItem(23, defaultGuiItem);
        inv.setItem(30, defaultGuiItem);
        inv.setItem(32, defaultGuiItem);
        inv.setItem(34, defaultGuiItem);
        inv.setItem(36, defaultGuiItem);
        inv.setItem(38, defaultGuiItem);
        inv.setItem(39, defaultGuiItem);
        inv.setItem(40, defaultGuiItem);
        inv.setItem(41, defaultGuiItem);
        inv.setItem(43, defaultGuiItem);
        setDefaultGuiItem2(inv);
        setSpecialItems(inv);
        p.openInventory(inv);
    }

    private void setDefaultGuiItem2(Inventory inv)
    {
        inv.setItem(10, defaultGuiItem2);
        inv.setItem(18, defaultGuiItem2);
        inv.setItem(20, defaultGuiItem2);
        inv.setItem(27, defaultGuiItem2);
        inv.setItem(29, defaultGuiItem2);
        inv.setItem(37, defaultGuiItem2);
    }

    private void setSpecialItems(Inventory inv)
    {
        inv.setItem(13,createGuiItem(Material.CLOCK,LanguageManager.timeplus,"+5"));
        inv.setItem(31,createGuiItem(Material.CLOCK,LanguageManager.timeminus,"-5"));


        inv.setItem(15,createGuiItem(Material.IRON_INGOT,LanguageManager.moneyplus,"+1"));
        inv.setItem(17,createGuiItem(Material.IRON_INGOT,LanguageManager.moneyminus,"-1"));
        inv.setItem(24,createGuiItem(Material.GOLD_INGOT,LanguageManager.moneyplus,"+10"));
        inv.setItem(26,createGuiItem(Material.GOLD_INGOT,LanguageManager.moneyminus,"-10"));
        inv.setItem(33,createGuiItem(Material.EMERALD,LanguageManager.moneyplus,"+100"));
        inv.setItem(35,createGuiItem(Material.EMERALD,LanguageManager.moneyminus,"-100"));
        inv.setItem(42,createGuiItem(Material.DIAMOND,LanguageManager.moneyplus,"+1000"));
        inv.setItem(44,createGuiItem(Material.DIAMOND,LanguageManager.moneyminus,"-1000"));


        inv.setItem(19,createGuiItem(Material.PAPER,LanguageManager.insertitem));

        inv.setItem(22,createGuiItem(Material.PAPER,LanguageManager.timecur,""+EasyAuction.getInstance().getConfig().getInt("general.auctiontimeMin")));

        inv.setItem(25,createGuiItem(Material.PAPER,LanguageManager.moneycur,"1"));

        inv.setItem(49,createGuiItem(Material.LIME_DYE,LanguageManager.startauction));
    }

    private static ItemStack createGuiItem(final Material material, final String name, final String... lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
}
