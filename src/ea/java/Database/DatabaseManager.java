package ea.java.Database;

import ea.java.EasyAuction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class DatabaseManager
{
    static DatabaseManager instance;

    static Connection connection;

    static String dbname;
    static String ea_logs;
    static String ea_player_see_auction;

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
        if (EasyAuction.getInstance().getConfig().getBoolean("database.mysql.host"))
        {
            ssl = "&sslMode=REQUIRED";
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
        ea_logs = EasyAuction.getInstance().getConfig().getString("database.mysql.ea_logs");
        ea_player_see_auction = EasyAuction.getInstance().getConfig().getString("database.mysql.ea_player_see_auction");
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
                            "(`uuid_player` CHAR(128) NOT NULL," +
                            "`start_time` DATETIME NULL," +
                            "`item` LONGTEXT NULL," +
                            "`uuid_winner` CHAR(128) NULL," +
                            "`price` INT NULL," +
                            " PRIMARY KEY (`uuid_player`)) ENGINE = InnoDB  DEFAULT CHARSET=utf8" +
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

    //TODO implement Insert Update Statements

    /*
    private void createStatementStrings()
    {
        addLOG = "INSERT INTO " + dbname + "." + ea_logs + " (`uuid_player`, `start_time`, `item`, `uuid_winner`, `price`) VALUES ('awdadawd', 'awdawd', '2020-08-22');";
    }
    */
}
