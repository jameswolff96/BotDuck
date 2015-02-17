package io.github.jwolff52.botduck.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
	
    private static Connection conn;
    
    private static final String URL = "jdbc:derby:BotDuckDB";
    
    public static final String DEFAULT_SCHEMA="BOTDUCK";
    
    static final Logger logger=LoggerFactory.getLogger(Database.class);
    
    public static boolean initDBConnection() {
        try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			logger.error("Unable to find EmbeddedDriver in classpath!\n"+e.toString());
		}
	    try{
	        conn=DriverManager.getConnection(URL);
	    }catch(SQLException e){
            SQLException f=(SQLException) e;
            while(f.getNextException()!=null){
                f=f.getNextException();
            }
            if(f.getSQLState().equals("XSDB6")){
                javax.swing.JOptionPane.showMessageDialog(null, "The Program is already running in another instance!");
                System.exit(0);
            }
            try {
                conn=DriverManager.getConnection(URL+";create=true;");
            } catch (SQLException ex) {
                logger.error("An Internal Communication Error Occurred With the Database");
                return false;
            }
	    }
	    return true;
    }
    
    public static void getDb(){
        Statement stmt;
        Statement stmt1;
        Statement stmt2;
        Statement stmt3;
        try {
            stmt=conn.createStatement();
            stmt.closeOnCompletion();
            stmt.executeUpdate("CREATE SCHEMA "+DEFAULT_SCHEMA);
        } catch (SQLException ex) {
            if(!ex.getSQLState().equals("X0Y68")){
                logger.error("Unable To Create Necessary Database Resources\n"+ex.toString());
            }
        }
        try{
            stmt1=conn.createStatement();
            stmt1.closeOnCompletion();
            stmt1.executeQuery("SELECT * FROM "+DEFAULT_SCHEMA+".donald10101Points");
        }catch(SQLException e){
            try{
                stmt2=conn.createStatement();
                stmt2.closeOnCompletion();
                stmt2.executeUpdate("CREATE TABLE "+DEFAULT_SCHEMA+".donald10101Points(userID varchar(25), points INTEGER, PRIMARY KEY (userID))");
            }catch(SQLException ex){
                logger.error("Unable to create table donald10101Points!\n"+ex.toString());
            }
            try{
                stmt3=conn.createStatement();
                stmt3.closeOnCompletion();
                stmt3.executeUpdate("CREATE TABLE "+DEFAULT_SCHEMA+".donald10101Regulars(userID varchar(25), PRIMARY KEY (userID))");
            }catch(SQLException ex){
                logger.error("Unable to create table donald10101Points!\n"+ex.toString());
            }
        }
    }
    
    public static boolean executeUpdate(String sqlCommand) {
    	Statement stmt=null;
    	try {
			stmt=conn.createStatement();
			stmt.closeOnCompletion();
		} catch (SQLException e) {
			logger.error("Unable to create connection for SQLCommand: "+sqlCommand+"\n"+e.toString());
			return false;
		}
        try {
			stmt.executeUpdate(sqlCommand);
		} catch (SQLException e) {
			logger.error("Unable to execute statment: "+sqlCommand+"\n"+e.toString());
			return false;
		}
        return true;
    }
    
    public static ResultSet executeQuery(String sqlQuery){
    	Statement stmt=null;
    	ResultSet rs=null;
    	try {
			stmt=conn.createStatement();
			stmt.closeOnCompletion();
		} catch (SQLException e) {
			logger.error("Unable to create connection for SQLQuery: "+sqlQuery+"\n"+e.toString());
		}
        try {
			rs=stmt.executeQuery(sqlQuery);
		} catch (SQLException e) {
			logger.error("Unable to execute query: "+sqlQuery+"\n"+e.toString());
		}
        return rs;
    }
    
}
