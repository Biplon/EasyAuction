package ea.java.Database;

import com.mysql.fabric.xmlrpc.base.Array;
import ea.java.EasyAuction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DatabaseManager
{
    static DatabaseManager instance;

    static Connection connection;

    static String dbname;
    static String ea_logs;
    static String ea_player_see_auction;
    static String ea_player_ban_auction;

    public static DatabaseManager getInstance()
    {
        return instance;
    }

    public DatabaseManager()
    {
        instance = this;
        loadConfigValues();
        EasyAuction.getInstance().getLogger().info("Connecting to a selected database...");
        createConnection(false);
        EasyAuction.getInstance().getLogger().info("Connected database successfully...");
        setupTables();
    }

    //create connection with db
    private void createConnection(boolean reconnect)
    {
        String ssl = "";
        String username = EasyAuction.getInstance().getConfig().getString("database.mysql.user");
        String password = EasyAuction.getInstance().getConfig().getString("database.mysql.password");
        if (EasyAuction.getInstance().getConfig().getBoolean("database.mysql.sslEnabled"))
        {
            Bukkit.getLogger().info("awdawdawd");
            ssl = "&sslMode=true";
        }
        String server = "jdbc:mysql://" + EasyAuction.getInstance().getConfig().getString("database.mysql.host") + ":" + EasyAuction.getInstance().getConfig().getString("database.mysql.port") + "/" + dbname + "?autoReconnect=true&allowMultiQueries=true&rewriteBatchedStatements=true" + ssl;
        try
        {
            connection = DriverManager.getConnection(server, username, password);
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
            if (reconnect)
            {
                EasyAuction.getInstance().getLogger().warning("Database can not reconnect");
            }
        }

    }

    //close db connection
    public void closeConnection()
    {
        try
        {
            EasyAuction.getInstance().getLogger().info("Closing database connection...");
            connection.close();
            connection = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void loadConfigValues()
    {
        dbname = EasyAuction.getInstance().getConfig().getString("database.mysql.databaseName");
        ea_logs = EasyAuction.getInstance().getConfig().getString("database.mysql.tableNameLog");
        ea_player_see_auction = EasyAuction.getInstance().getConfig().getString("database.mysql.tableNameseeAuction");
        ea_player_ban_auction = EasyAuction.getInstance().getConfig().getString("database.mysql.tableNameAuctionBanPlayer");
    }

    private void setupTables()
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query = null;
                try
                {

                    data = "CREATE TABLE IF NOT EXISTS " + dbname + "." + ea_logs + " " +
                            "(`id` INT NOT NULL  AUTO_INCREMENT," +
                            "`player` CHAR(128) NOT NULL," +
                            "`start_time` DATETIME NULL," +
                            "`item` LONGTEXT NULL," +
                            "`winner` CHAR(128) NULL," +
                            "`price` INT NULL," +
                            " PRIMARY KEY (`id`)) ENGINE = InnoDB  DEFAULT CHARSET=utf8" +
                            " ROW_FORMAT = DYNAMIC;";

                    query = connection.prepareStatement(data);
                    query.execute();

                    data = "CREATE TABLE IF NOT EXISTS " + dbname + "." + ea_player_see_auction + " " +
                            "(`uuid_player` CHAR(128) NOT NULL," +
                            "`see_auction` TINYINT NULL," +
                            " PRIMARY KEY (`uuid_player`)) ENGINE = InnoDB  DEFAULT CHARSET=utf8" +
                            " ROW_FORMAT = DYNAMIC;";

                    query = connection.prepareStatement(data);
                    query.execute();

                    data = "CREATE TABLE IF NOT EXISTS " + dbname + "." + ea_player_ban_auction + " " +
                            "(`uuid_player` CHAR(128) NOT NULL," +
                            "`end_time` DATETIME NULL," +
                            " PRIMARY KEY (`uuid_player`)) ENGINE = InnoDB  DEFAULT CHARSET=utf8" +
                            " ROW_FORMAT = DYNAMIC;";

                    query = connection.prepareStatement(data);
                    query.execute();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error creating tables! Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                }
                finally
                {
                    try
                    {
                        if (query != null)
                        {
                            query.close();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
    }

    public boolean playerSeeAuction(UUID id)
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {
                    data = "SELECT see_auction FROM " + dbname + "." + ea_player_see_auction + " where uuid_player='" + id + "' ; ";

                    query = connection.prepareStatement(data);

                    ResultSet rs = query.executeQuery();
                    if (rs.next())
                    {
                       return rs.getBoolean("see_auction");
                    }
                    else
                    {
                        createPlayer(id);
                    }

                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                    return false;
                }
            }
            return false;
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
        return false;
        //TODO implement
    }

    private void createPlayer(UUID id)
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {
                    data = "Insert into " + dbname + "." + ea_player_see_auction + " (uuid_player,see_auction) values('" + id + "','0'); ";
                    data += "Insert into " + dbname + "." + ea_player_ban_auction + " (uuid_player) values('" + id + "'); ";
                    query = connection.prepareStatement(data);
                    query.addBatch();
                    query.executeBatch();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error load player inventory! Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                }
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void createLog(String player,String item,String winner,int price)
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {
                    Date dt = new Date();
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy.MM.dd HH:mm:ss ");
                    String date = sdf.format(dt);
                    data = "Insert into " + dbname + "." + ea_logs + " (player,start_time,item,winner,price) values('" + player + "','"+ date +"','"+ item +"','"+ winner +"','"+ price +"'); ";
                    query = connection.prepareStatement(data);
                    query.addBatch();
                    query.executeBatch();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error load player inventory! Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                }
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void disablePlayerSeeAuction(UUID id)
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {
                    data = "UPDATE " + dbname + "." + ea_player_see_auction + " SET `see_auction` = '0' WHERE (`uuid_player` = '"+ id +"');";

                    query = connection.prepareStatement(data);

                    query.execute();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                }
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void enablePlayerSeeAuction(UUID id)
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {
                    data = "UPDATE " + dbname + "." + ea_player_see_auction + " SET `see_auction` = '1' WHERE (`uuid_player` = '"+ id +"');";

                    query = connection.prepareStatement(data);

                    query.execute();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                }
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void banPlayer(UUID id,int time)
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {


                    int hours = time / 60;
                    int minutes = time % 60;
                    LocalDateTime actualDateTime = LocalDateTime.now();
                    data = "UPDATE " + dbname + "." + ea_player_ban_auction + " SET `end_time` = '"+ actualDateTime.plusHours(hours).plusMinutes(minutes) +"' WHERE (`uuid_player` = '"+id+"');";

                    query = connection.prepareStatement(data);

                    query.execute();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                }
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void pardonPlayer(UUID id)
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {

                    LocalDateTime actualDateTime = LocalDateTime.now();
                    data = "UPDATE " + dbname + "." + ea_player_ban_auction + " SET `end_time` = '"+ actualDateTime +"' WHERE (`uuid_player` = '"+id+"');";


                    query = connection.prepareStatement(data);

                    query.execute();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                }
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
    }

    public boolean playerBanned(Player p)
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {
                    data = "SELECT end_time FROM " + dbname + "." + ea_player_ban_auction + " where uuid_player='" + p.getUniqueId() + "' ; ";

                    query = connection.prepareStatement(data);

                    ResultSet rs = query.executeQuery();
                    if (rs.next())
                    {
                        Date dt = new Date();
                        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date=formatter.parse(rs.getString("end_time"));
                        return date.after(dt);
                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                    return false;
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
        return false;
    }

    public CharSequence getPlayerSales(Player p)
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {
                    data = " SELECT SUM(price) FROM  " + dbname + "." + ea_logs + " where player='" + p.getName() + "' ; ";

                    query = connection.prepareStatement(data);

                    ResultSet rs = query.executeQuery();
                    if (rs.next())
                    {
                        return rs.getString("SUM(price)");
                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                }
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
        return "0";
    }

    public String[] getPlayerStats(String name, boolean win)
    {
        try
        {
            List<String> tmp = new ArrayList<>();
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {
                    if (win)
                    {
                        data = " SELECT * FROM  " + dbname + "." + ea_logs + " where winner='" + name + "' limit 10;";
                    }
                    else
                    {
                        data = " SELECT * FROM  " + dbname + "." + ea_logs + " where player='" + name + "' limit 10;";
                    }

                    query = connection.prepareStatement(data);

                    ResultSet rs = query.executeQuery();
                    while (rs.next())
                    {
                        tmp.add("Start:"+ rs.getString("player")+", Time: " +rs.getString("start_time")+ " Item: " + rs.getString("item")+ " Winner: "+ rs.getString("winner")+" Price: " +rs.getString("price"));
                    }
                    String[] w = new String[tmp.size()];
                    tmp.toArray(w);
                    return w;
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                }
            }
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }

        return new String[0];
    }

    public String getPlayerBannedTime(Player p)
    {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                String data = "";
                PreparedStatement query;
                try
                {
                    data = "SELECT end_time FROM " + dbname + "." + ea_player_ban_auction + " where uuid_player='" + p.getUniqueId() + "' ; ";

                    query = connection.prepareStatement(data);

                    ResultSet rs = query.executeQuery();
                    if (rs.next())
                    {
                        return rs.getString("end_time");
                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                    EasyAuction.getInstance().getLogger().severe("Error: " + e.getMessage());
                    EasyAuction.getInstance().getLogger().severe(data);
                    return "";
                }
            }
            return "";
        }
        catch (SQLException throwable)
        {
            throwable.printStackTrace();
        }
        return "";
    }

    //TODO implement Insert Update Statements

    /*
    private void createStatementStrings()
    {

        addLOG = "INSERT INTO " + dbname + "." + ea_logs + " (`uuid_player`, `start_time`, `item`, `uuid_winner`, `price`) VALUES ('awdadawd', 'awdawd', '2020-08-22');";
    }
    */
}
