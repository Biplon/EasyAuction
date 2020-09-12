package ea.java.Struct;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Auction
{
    //player who start auction
    private final Player auctionStartPlayer;
    //item in auction
    private final ItemStack auctionItem;
    //start price of auction
    private final int startPrice;
    //player who bid last on auction
    private Player bidPlayer;
    //current price of auction
    private int priceCurrent;

    //getter
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

    //set player who bid on auction
    public void setBidPlayer(Player bidPlayer, int priceCurrent)
    {
        this.bidPlayer = bidPlayer;
        this.priceCurrent = priceCurrent;
    }

    //constructor
    public Auction(Player auctionStartPlayer, ItemStack auctionItem, int startPrice)
    {
        this.auctionStartPlayer = auctionStartPlayer;
        this.auctionItem = auctionItem;
        this.startPrice = startPrice;
        this.priceCurrent = startPrice;
    }
}
