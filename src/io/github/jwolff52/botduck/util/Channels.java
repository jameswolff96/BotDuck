package io.github.jwolff52.botduck.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.pircbotx.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Channels{

	static final Logger logger = LoggerFactory.getLogger(PointsRunnable.class);
	private static HashMap<User, Process> currentChannels = new HashMap<>();

	public static void addChannel(User u){
		File f=new File(u.getNick()+".bat");
		if (!f.exists()) {
			TFileWriter.writeFile(f, "java -jar BotDuck.jar dulakoh5 "+u.getNick());
		}
		try {
			currentChannels.put(u,Runtime.getRuntime().exec("cmd /c start "+f.getName()));
		} catch (IOException e) {
			logger.error("Error starting process for " + u.getNick() + "\n" + e.toString());
		}
	}
	
	public static void removeChannel(User u){
		currentChannels.remove(u).destroy();
	}
}
