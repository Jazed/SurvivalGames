package org.curlybrace.double0negative.logging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import me.beechboy2000.survivalgames.GameStatus;

import org.bukkit.plugin.Plugin;


public class DatabaseManager {
    private  Connection conn;
    private  Plugin p;
    private  Logger log;
    private  static DatabaseManager instance = new DatabaseManager();
    
    private DatabaseManager(){

    }

    public static DatabaseManager getInstance(){
        return instance;
    }

    
    public void setup(Plugin p){
        log = p.getLogger();
        GameStatus.SQLConnected = setup();
    }
    
    
    public  Connection getMysqlConnection()
    {
        return conn;
    }
    
    public boolean connectToDB(String host, int port, String db, String user, String pass)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
            log.info("Connected to database.");
            return true;
        }
        catch (ClassNotFoundException e)
        {
            log.warning("Couldn't start MySQL Driver. Stopping...\n" + e.getMessage());
            
            return false;
        }
        catch (SQLException e)
        {
            log.warning("Couldn't connect to MySQL database. Stopping...\n" + e.getMessage());
            GameStatus.SQLConnected = false;
            return false;
        }
    }
    
    public PreparedStatement createStatement(String query)
    {
        try
        {
            return conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        }
        catch (SQLException e)
        {
            log.warning("Error creating query '" + query + "'.");
            e.printStackTrace();
        }
        return null;
    }
    
    public Statement createStatement()
    {
        try
        {
            return conn.createStatement();
        }
        catch (SQLException e)
        {
            return null;
        }
    }
    

    private boolean setup()
    {
        log.info("Connecting to database...");
        String host =  p.getConfig().getString("db.host", "localhost");
        int port    =  p.getConfig().getInt("db.port",  3306);
        String db   =  p.getConfig().getString("db.database", "SurvivalGames");
        String user =  p.getConfig().getString("db.user", "root");
        String pass =  p.getConfig().getString("db.pass",  "");
        return this.connectToDB(host, port, db, user,pass);
    }

}
