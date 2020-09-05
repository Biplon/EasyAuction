package ea.java.Struct;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Auction
{
    private final Player auctionStartPlayer;

    private final ItemStack auctionItem;

    private final int startPrice;

    private Player bidPlayer;

    private int priceCurrent = 0;

    public Player getAuctionStartPlayer()
    {
        return auctionStartPlayer;
    }

    public ItemStack getAuctionItem()
    {
        return auctionItem;
    }

    public int getStartPrice()
    {
        return startPrice;
    }

    public Player getBidPlayer()
    {
        return bidPlayer;
    }

    public int getPriceCurrent()
    {
        return priceCurrent;
    }

    public void setBidPlayer(Player bidPlayer,int priceCurrent)
    {
        this.bidPlayer = bidPlayer;
        this.priceCurrent = priceCurrent;
    }

    public Auction(Player auctionStartPlayer, ItemStack auctionItem, int startPrice)
    {
        this.auctionStartPlayer = auctionStartPlayer;
        this.auctionItem = auctionItem;
        this.startPrice = startPrice;
        this.priceCurrent = startPrice;
    }
}
