package io.github.jwolff52.botduck;

import io.github.jwolff52.botduck.database.Database;
import io.github.jwolff52.botduck.listener.PIRCListener;
import io.github.jwolff52.botduck.util.FTPUtil;

import java.io.IOException;
import java.util.Scanner;

import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotDuck {
	
  static final Logger logger = LoggerFactory.getLogger(BotDuck.class);
  
  public static void main(String[] args) {
	Database.initDBConnection();
	Database.getDb();
    PIRCListener listener = new PIRCListener();
    Builder<PircBotX> build = new Builder<PircBotX>().setName("botduck").setLogin("botduck").setServerPassword(""/*ENTER YOUR OAUTH HERE*/).setServerHostname("irc.twitch.tv").setServerPort(6667).addListener(listener).setVersion("Version 1.0");
    Scanner scan = new Scanner(System.in);
    FTPUtil.initFTP("");
    if (args.length == 0)
    {
      String channel = "";
      while (!channel.equalsIgnoreCase("/done"))
      {
        logger.info("Please list a channel you would like to join. Type \"/done\" if you don't to join anymore channels!");
        channel = scan.nextLine();
        if (!channel.equalsIgnoreCase("/done")) {
          build.addAutoJoinChannel("#" + channel);
        }
      }
    }
    else
    {
      for (int i = 0; i < args.length; i++) {
        build.addAutoJoinChannel("#" + args[i]);
      }
    }
    scan.close();
    Configuration<PircBotX> config = build.buildConfiguration();
    PircBotX bot = new PircBotX(config);
    try
    {
      bot.startBot();
    }
    catch (IOException|IrcException|IndexOutOfBoundsException e) {
    	e.printStackTrace();
    }
  }
}