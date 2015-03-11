/*	  It's a Twitch bot, because we can.
 *    Copyright (C) 2015  Logan Saso, James Wolff, Kyle Nabinger
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.jewsofhazard.pcmrbot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.jewsofhazard.pcmrbot.Main;
import me.jewsofhazard.pcmrbot.util.TOptions;

public class Database {

	private static Connection conn;

	private static final String URL = "jdbc:mysql://localhost:3306/pcmrbot?";

	public static final String DATABASE = "pcmrbot";

	static final Logger logger = Logger.getLogger(Database.class + "");

	/**
	 * Creates a connection to the database.
	 * 
	 * @return - true if connection is successful
	 */
	public static boolean initDBConnection(String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			logger.log(
					Level.SEVERE,
					"Unable to find Driver in classpath!"
							,e);
		}
		try {
			conn = DriverManager.getConnection(String.format("%suser=bot&password=%s", URL, pass));
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	/**
	 * Creates the tables for the provided channel
	 * 
	 * @param channelNoHash - the channel we are connecting to.
	 * @return - true if it has to create the tables
	 */
	public static boolean getChannelTables(String channelNoHash) {
		Statement stmt;
		Statement stmt1;
		Statement stmt2;
		Statement stmt3;
		Statement stmt4;
		Statement stmt5;
		Statement stmt6;
		Statement stmt7;
		Statement stmt8;
		try {
			stmt = conn.createStatement();
			stmt.closeOnCompletion();
			stmt.executeQuery(String.format("SELECT * FROM %s.%sMods", DATABASE, channelNoHash));
			return false;
		} catch (SQLException e) {
			try {
				stmt1 = conn.createStatement();
				stmt1.closeOnCompletion();
				stmt1.executeUpdate(String.format("CREATE TABLE %s.%sMods(userID varchar(25), PRIMARY KEY (userID))", DATABASE, channelNoHash));
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, String.format("Unable to create table %sMods!",DATABASE), ex);
			}
			try {
				stmt2 = conn.createStatement();
				stmt2.closeOnCompletion();
				stmt2.executeUpdate(String.format("CREATE TABLE %s.%sOptions(optionID varchar(50), value varchar(4000), PRIMARY KEY (optionID))", DATABASE, channelNoHash));
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, String.format("Unable to create table %sOptions!", channelNoHash), ex );
			}
			try {
				stmt3 = conn.createStatement();
				stmt3.closeOnCompletion();
				stmt3.executeUpdate(String.format("CREATE TABLE %s.%sSpam(word varchar(25), PRIMARY KEY (word))", DATABASE, channelNoHash));
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, String.format("Unable to create table %sSpam!", channelNoHash), ex);
			}
			try {
				stmt4 = conn.createStatement();
				stmt4.closeOnCompletion();
				stmt4.executeUpdate(String.format("CREATE TABLE %s.%sAutoReplies(keyWord varchar(255), reply varchar(4000), PRIMARY KEY (keyWord))", DATABASE, channelNoHash));
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, String.format("Unable to create table %sAutoReplies!", channelNoHash), ex);
			}
			try {
				stmt5 = conn.createStatement();
				stmt5.closeOnCompletion();
				stmt5.executeUpdate(String.format("CREATE TABLE %s.%sWhitelist(userID varchar(30), PRIMARY KEY (userID))", DATABASE, channelNoHash));
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, String.format("Unable to create table %sWhitelist!", channelNoHash), ex);
			}
			try{
                stmt6=conn.createStatement();
                stmt6.closeOnCompletion();
                stmt6.executeUpdate(String.format("CREATE TABLE %s.%sPoints(userID varchar(25), points INTEGER, PRIMARY KEY (userID))", DATABASE, channelNoHash));
            }catch(SQLException ex){
                logger.log(Level.SEVERE, "Unable to create table Points!", ex);
            }
            try{
                stmt7=conn.createStatement();
                stmt7.closeOnCompletion();
                stmt7.executeUpdate(String.format("CREATE TABLE %s.%sRegulars(userID varchar(25), PRIMARY KEY (userID))", DATABASE, channelNoHash));
            }catch(SQLException ex){
                logger.log(Level.SEVERE, "Unable to create table Regulars!", ex);
            }
            try{
                stmt8=conn.createStatement();
                stmt8.closeOnCompletion();
                stmt8.executeUpdate(String.format("CREATE TABLE %s.%sCommands(command varchar(25), parameters varchar(255), reply varchar(4000), PRIMARY KEY (command))", DATABASE, channelNoHash));
            }catch(SQLException ex){
                logger.log(Level.SEVERE, "Unable to create table Commands!", ex);
            }
			return true;
		}
	}

	/**
	 * Sends an update to the database (eg. INSERT, DELETE, etc.)
	 * 
	 * @param sqlCommand
	 * @return - true if it successfully executes the update
	 */
	protected static boolean executeUpdate(String sqlCommand) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.closeOnCompletion();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("Unable to create connection for SQLCommand: %s", sqlCommand), e);
			return false;
		}
		try {
			stmt.executeUpdate(sqlCommand);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("Unable to execute statment: %s", sqlCommand), e);
			return false;
		}
		return true;
	}

	/**
	 * Sends a query to the database (eg. SELECT, etc.)
	 * @param sqlQuery
	 * @return
	 */
	protected static ResultSet executeQuery(String sqlQuery) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt=conn.createStatement();
			stmt.closeOnCompletion();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("Unable to create connection for SQLQuery: %s", sqlQuery), e);
		}
		try {
			rs = stmt.executeQuery(sqlQuery);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("Unable to execute query: %s", sqlQuery), e);
		}
		return rs;
	}

	/**
	 * Sends an update to the database (eg. INSERT, DELETE, etc.)
	 * 
	 * @param stmt
	 * @return - true if it successfully executes the update
	 */
	protected static boolean executeUpdate(PreparedStatement stmt) {
		try {
			stmt.closeOnCompletion();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("Unable to create connection for SQLCommand"), e);
			return false;
		}
		try {
			stmt.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("Unable to execute statment"), e);
			return false;
		}
		return true;
	}

	/**
	 * Sends a query to the database (eg. SELECT, etc.)
	 * @param stmt
	 * @return
	 */
	protected static ResultSet executeQuery(PreparedStatement stmt) {
		ResultSet rs = null;
		try {
			stmt.closeOnCompletion();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("Unable to create connection for SQLQuery"), e);
		}
		try {
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("Unable to execute query"), e);
		}
		return rs;
	}
	
	/**
	 * Clears the auto replies table for the channel provided.
	 * 
	 * @param channelNoHash - the channel to clear auto replies for
	 */
	public static void clearAutoRepliesTable(String channelNoHash) {
		executeUpdate(String.format("DROP TABLE %s.%sAutoReplies", DATABASE, channelNoHash));
		executeUpdate(String.format("CREATE TABLE %s.%sAutoReplies(keyWord varchar(255), reply varchar(255), PRIMARY KEY (keyWord))", DATABASE, channelNoHash));
	}
	
	/**
	 * @param user - user to get the oauth for
	 * @return oauth code for the specified user
	 */
	public static String getUserOAuth(String user) {
		ResultSet rs=executeQuery(String.format("SELECT * FROM "+DATABASE+".userOAuth WHERE userID=\'%s\'", user));
		try {
			if(rs.next()) {
				return rs.getString("oAuth");
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("An error occurred getting %s\'s OAuth from the database", user), e);
		}
		return null;
	}

	/**
	 * @param channelNoHash - channel to get the option for without the leading #
	 * @param option - Timeout Option
	 * @return value if the option
	 */
	public static int getOption(String channelNoHash, TOptions option) {
		ResultSet rs=executeQuery(String.format("SELECT * FROM %s.%sOptions WHERE optionID=\'%s\'", DATABASE, channelNoHash, option.getOptionID()));
		try {
			if(rs.next()) {
				return Integer.valueOf(rs.getString(2));
			}
			return -1;
		} catch (SQLException | NumberFormatException e) {
			logger.log(Level.SEVERE, String.format("Unable to get welcome message for %s", channelNoHash), e);
		}
		return -1;
	}
	
	/**
	 * @param channelNoHash - channel to get the welcome message for, without the leading #
	 * @return The welcome message
	 */
	public static String getWelcomeMessage(String channelNoHash) {
		ResultSet rs=executeQuery(String.format("SELECT * FROM %s.%sOptions WHERE optionID=\'%s\'", DATABASE, channelNoHash, TOptions.welcomeMessage));
		try {
			if(rs.next()) {
				return rs.getString(2);
			}
			return null;
		} catch (SQLException | NumberFormatException e) {
			logger.log(Level.SEVERE, String.format("Unable to get welcome message for %s", channelNoHash), e);
		}
		return null;
	}

	/**
	 * @param channelNoHash - channel to set the welcome message for, without the leading #
	 * @param option - timeout option
	 * @param value - new welcome message
	 * @return true if the message is set successfully
	 */
	public static boolean setWelcomeMessage(String channelNoHash, TOptions option, String value) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("UPDATE %s.%sOptions SET optionID=?,value=? WHERE optionID=?", DATABASE, channelNoHash));
			stmt.setString(1, option.getOptionID());
			stmt.setString(2, value);
			stmt.setString(3, option.getOptionID());
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "An error occurred setting the welcome message", e);
		}
		return executeUpdate(stmt);
	}

	/**
	 * @param channelNoHash - channel to set the option for, without the leading #
	 * @param option - timeout option
	 * @param value - value to set for the option
	 * @return true if the message is set successfully
	 */
	public static boolean setOption(String channelNoHash, TOptions option, int value) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("UPDATE %s.%sOptions SET optionID=?,value=? WHERE optionID=?", DATABASE, channelNoHash));
			stmt.setString(1, option.getOptionID());
			stmt.setString(2, value+"");
			stmt.setString(3, option.getOptionID());
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to set option", e);
		}
		return executeUpdate(stmt);
	}
	
	/**
	 * @param channelNoHash - channel to add the option for, without the leading #
	 * @param option - timeout option
	 * @param value - value to set the option to
	 * @return true if the option is added successfully
	 */
	public static boolean addOption(String channelNoHash, TOptions option, String value) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("INSERT INTO %s.%sOptions VALUES(? , ?)", DATABASE, channelNoHash));
			stmt.setString(1, option.getOptionID());
			stmt.setString(2, value+"");
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to add option", e);
		}
		return executeUpdate(stmt);
	}

	/**
	 * @param moderator - person to check if their a moderator
	 * @param channelNoHash - channel to check if their a moderator in, without the leading #
	 * @return true if user is a moderator in channel
	 */
	public static boolean isMod(String moderator, String channelNoHash) {
		ResultSet rs = executeQuery(String.format("SELECT * FROM %s.%sMods WHERE userID=\'%s\'", DATABASE, channelNoHash, moderator));
		try {
			return rs.next();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("An error occurred checking if %s is in %s's Mod List.", moderator, channelNoHash), e);
		}
		return false;
	}
	
	/**
	 * @param moderator - moderator to add
	 * @param channelNoHash - channel to add the mod to, without the #
	 */
	public static void addMod(String moderator, String channelNoHash) {
		executeUpdate(String.format("INSERT INTO %s.%sMods VALUES(\'%s\')", DATABASE, channelNoHash, moderator));
	}
	
	/**
	 * @param channelNoHash - channel to add the auto reply to
	 * @param keywords - keywords to trigger the auto reply
	 * @param reply - auto reply to be sent on trigger
	 */
	public static void addAutoReply(String channelNoHash, String keywords, String reply) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("INSERT INTO %s.%sAutoReplies VALUES(? , ?)", DATABASE, channelNoHash));
			stmt.setString(1, keywords);
			stmt.setString(2, reply);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to set option", e);
		}
		executeUpdate(stmt);
	}

	/**
	 * @param channelNoHash - channel to get the auto replies for, without the leading #
	 * @return a result set of the auto replies
	 */
	public static ResultSet getAutoReplies(String channelNoHash) {
		return executeQuery(String.format("SELECT * FROM %s.%sAutoReplies", DATABASE, channelNoHash));
	}
	
	/**
	 * @param channelNoHash - channel to add the command for, without the leading #
	 * @param command - command to be added
	 * @param parameters - parameters that should be passed
	 * @param reply - reply to be sent on command
	 */
	public static void addCommand(String channelNoHash, String command, String parameters, String reply) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("INSERT INTO %s.%sCommands VALUES(? , ?, ?)", DATABASE, channelNoHash));
			stmt.setString(1, command);
			stmt.setString(2, parameters);
			stmt.setString(3, reply);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to set option", e);
		}
		executeUpdate(stmt);
	}
	
	/**
	 * @param channelNoHash - channel to get spam for
	 * @return result set of spam words
	 */
	public static ResultSet getSpam(String channelNoHash) {
		return executeQuery(String.format("SELECT * FROM %s.%sSpam", DATABASE, channelNoHash));
	}

	/**
	 * @param moderator - moderator to remove
	 * @param channelNoHash - channel to remove the moderator from, without the leading #
	 * @return true if the moderator is removed
	 */
	public static boolean delModerator(String moderator, String channelNoHash) {
		if(!Main.isDefaultMod(moderator, channelNoHash)) {
			return executeUpdate(String.format("DELETE FROM %s.%sMods WHERE userID=\'%s\'", DATABASE, channelNoHash, moderator));
		}
		return false;
	}

	/**
	 * @param channelNoHash - channel to delete the auto reply from, without the leading #
	 * @param keywords - keywords of the auto reply
	 * @return true if the auto reply is removed
	 */
	public static boolean delAutoReply(String channelNoHash, String keywords) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("DELETE FROM %s.%sAutoReplies WHERE keyWord=?", DATABASE, channelNoHash));
			stmt.setString(1, keywords);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to set option", e);
		}
		return executeUpdate(stmt);
	}

	/**
	 * @param channelNoHash - channel to get the custom commands for, without the leading #
	 * @return result set of custom commands
	 */
	public static ResultSet getCustomCommands(String channelNoHash) {
		return executeQuery(String.format("SELECT * FROM %s.%sCommands WHERE command LIKE '!%%'", DATABASE, channelNoHash));
	}

	/**
	 * @param channelNoHash - channel to add spam to, without the leading #
	 * @param word - word to add to the table
	 * @return true if the word is added
	 */
	public static boolean addSpam(String channelNoHash, String word) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("INSERT INTO %s.%sSpam VALUES(?)", DATABASE, channelNoHash));
			stmt.setString(1, word);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to set option", e);
		}
		return executeUpdate(stmt);
	}
	
	/**
	 * @param channelNoHash - channel to delete the spam from, without the leading #
	 * @param word - word to delete
	 * @return true if the word is deleted
	 */
	public static boolean delSpam(String channelNoHash, String word) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("DELETE FROM %s.%sSpam WHERE word=?", DATABASE, channelNoHash));
			stmt.setString(1, word);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to set option", e);
		}
		return executeUpdate(stmt);
	}

	/**
	 * @param channelNoHash - channel to add the whitelist to, whithout the leading #
	 * @param target - person to add to the whitelist
	 * @return true if they are added successfully
	 */
	public static boolean addToWhiteList(String channelNoHash, String target) {
		return executeUpdate(String.format("INSERT INTO %s.%sWhitelist VALUES(\'%s\')", DATABASE, channelNoHash, target));
	}

	/**
	 * @param channelNoHash - channel to delete the whitelist for, without the leading #
	 * @param target - person to remove
	 * @return true if successfully removed
	 */
	public static boolean delWhitelist(String channelNoHash, String target) {
		return executeUpdate(String.format("DELETE FROM %s.%sWhitelist WHERE userID=\'%s\'", DATABASE, channelNoHash, target));
	}

	/**
	 * @param sender - person to check
	 * @param channelNoHash - channel to check for, without the leading #
	 * @return true if the user is in the whitelist
	 */
	public static boolean isWhitelisted(String sender, String channelNoHash) {
		ResultSet rs = executeQuery(String.format("SELECT * FROM %s.%sWhitelist WHERE userID=\'%s\'", DATABASE, channelNoHash, sender));
		try {
			return rs.next();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "An error occurred checking if %user% is whitelisted!".replace("%user%", sender), e);
		}
		return false;
	}

	/**
	 * @param nick - person to add points to
	 * @param channelNoHash - channel the user is in, without the leading #
	 * @param ammount - the number of points to add
	 */
	public static void addPoints(String nick, String channelNoHash, int ammount) {
		ResultSet rs = Database.executeQuery(String.format("SELECT * FROM %s.%sPoints WHERE userID=\'%s\'", DATABASE, channelNoHash, nick));
		try {
			if(!rs.next()){
				Database.executeUpdate(String.format("INSERT INTO %s.%sPoints VALUES (\'%s\',1)", DATABASE, channelNoHash, nick));
				return;
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "An Error occured updating "+nick+"'s points!\n", e);
		}
		try {
			Database.executeUpdate(String.format("UPDATE %s.%sPoints SET userID=\'%s\',points=%d WHERE userID=\'%s\'", DATABASE, channelNoHash, nick, rs.getInt(2)+ammount, nick));
			if(rs.getInt(2)+ammount==getOption(channelNoHash, TOptions.regular)) {
				Database.executeUpdate(String.format("INSERT INTO %s, %sRegulars VALUES (\'%s\')", DATABASE, channelNoHash, nick));
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "An Error occured updating "+nick+"'s points!\n", e);
		}
	}

	/**
	 * @param sender - person to get points for
	 * @param channelNoHash - channel the user is in, without the leading #
	 * @return number of points the user has
	 */
	public static String getPoints(String sender, String channelNoHash) {
		ResultSet rs = executeQuery(String.format("SELECT * FROM %s.%sPoints WHERE userID=\'%s\'", DATABASE, channelNoHash, sender));
		try {
			if(rs.next()) {
				return rs.getInt(2)+"";
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "An error occurred getting a user's points.", e);
		}
		return null;
	}

	/**
	 * @param ammount - number of players to get
	 * @param channelNoHash - channel the people are in
	 * @return formatted string of top x players
	 */
	public static String topPlayers(int ammount, String channelNoHash) {
		StringBuilder output = new StringBuilder();
		output.append("The top " + ammount + " points holder(s) are: ");
		ResultSet rs=executeQuery(String.format("SELECT * FROM %s.%sPoints ORDER BY points DESC", DATABASE, channelNoHash));
		try {
			while(rs.next()&&ammount>1){
				if(!rs.getString(1).equalsIgnoreCase("pcmrbot") && !rs.getString(1).equalsIgnoreCase("pcmrbottester") && !rs.getString(1).equalsIgnoreCase("botduck") && !rs.getString(1).equalsIgnoreCase(channelNoHash)) {
					output.append(rs.getString(1)+": "+rs.getInt(2) + ", ");
					ammount--;
				}
			}
			output.append(rs.getString(1)+": "+rs.getInt(2));
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error occurred creating Top list!", e);
		}
		return output.toString();
	}

	/**
	 * @param sender - person to check if is regular
	 * @param channelNoHash - channel the person is in, without the leading #
	 * @return true if {@link sender} is a regular in {@link channelNoHash}
	 */
	public static boolean isRegular(String sender, String channelNoHash) {
		ResultSet rs=Database.executeQuery(String.format("SELECT * FROM %s.%sRegulars WHERE userID=\'%s\'", DATABASE, channelNoHash, sender));
		try {
			return rs.next();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "User is not a regular!", e);
		}
		return false;
	}

	/**
	 * @param channelNoHash - channel the person is in, without the leading #
	 * @param regular - person to add to the list
	 * @return true if adding is successful
	 */
	public static boolean addRegular(String channelNoHash, String regular) {
		return executeUpdate(String.format("INSERT INTO %s.%sRegulars VALUES (\'%s\')", DATABASE, channelNoHash, regular));
	}

	/**
	 * @param channelNoHash - channel the person is in, without the leading #
	 * @param regular - person to remove from the list
	 * @return true if deletion is successful
	 */
	public static boolean delRegular(String channelNoHash, String regular) {
		return executeUpdate(String.format("DELETE FROM %s.%sRegulars WHERE userID=\'%s\'", DATABASE, channelNoHash, regular));
	}

	public static boolean delCommand(String channelNoHash, String command) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(String.format("DELETE FROM %s.%sCommands WHERE command=?", DATABASE, channelNoHash));
			stmt.setString(1, command);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to set option", e);
		}
		return executeUpdate(stmt);
	}
}
