package io.github.jwolff52.botduck.util;

import io.github.jwolff52.botduck.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.pircbotx.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointsRunnable implements Runnable {
	static final Logger logger = LoggerFactory.getLogger(PointsRunnable.class);
	private String user;
	private Channel channel;
	private static HashMap<String, Channel> currentUsers = new HashMap<>();

	public PointsRunnable(String u, Channel c) {
		this.user = u;
		this.channel = c;
		currentUsers.put(u, c);
		new Thread(this).start();
	}

	public void run() {
		addPoints(this.user, this.channel);
		try {
			Thread.sleep(300000L);
		} catch (InterruptedException e) {
			logger.error("Error adding points to: " + this.user + "\n"
					+ e.toString());
		}
		while (currentUsers.containsKey(this.user)) {
			try {
				Thread.sleep(300000L);
			} catch (InterruptedException e) {
				logger.error("Error adding points to: " + this.user + "\n"
						+ e.toString());
			}
			addPoints(this.user, this.channel);
		}
	}

	private void addPoints(String nick, Channel c) {
		ResultSet rs=Database.executeQuery("SELECT * FROM "+Database.DEFAULT_SCHEMA+".donald10101Points WHERE userID=\'"+nick+"\'");
		try {
			if(!rs.next()){
				Database.executeUpdate("INSERT INTO "+Database.DEFAULT_SCHEMA+".donald10101Points VALUES (\'"+nick+"\',1)");
				return;
			}
		} catch (SQLException e1) {
			logger.error("An Error occured updating "+nick+"'s points!\n"+e1.toString());
		}
		try {
			Database.executeUpdate("UPDATE "+Database.DEFAULT_SCHEMA+".donald10101Points SET "
					+ "userID=\'"+nick+"\',"
					+ "points="+(rs.getInt(2)+1)
					+ " WHERE userID=\'"+nick+"\'");
			if(rs.getInt(2)+1==72) {
				Database.executeUpdate("INSERT INTO "+Database.DEFAULT_SCHEMA+".donald10101Regulars VALUES (\'"+nick+"\')");
			}
		} catch (SQLException e) {
			logger.error("An Error occured updating "+nick+"'s points!\n"+e.toString());
		}
		
	}
	
	public static boolean containsUser(String u) {
		return currentUsers.containsKey(u);
	}

	public static void removeUser(String u) {
		currentUsers.remove(u);
	}
	
	public static void removeAllUsers() {
		currentUsers=new HashMap<>();
	}
}