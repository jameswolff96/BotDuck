package io.github.jwolff52.botduck.util;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Timeouts implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(Timeouts.class);
	
	private Channel channel;
	private User user;
	private int time;
	private TType type;
	
	public Timeouts(Channel c, User u, int t, TType type) {
		channel=c;
		user=u;
		time=t;
		this.type=type;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.error("Unable to timeout user: "+user.getNick());
		}
		if (type.previousOffender(user.getNick())) {
			if (type.getOffender(user.getNick()) == 1) {
				channel.send().message("/timeout "+user.getNick()+" 300");
				channel.send().message(type.getRandomMessage() + " Second warning, 5 minute timeout!");
				type.updateOffender(user.getNick(), 2);
			} else if (type.getOffender(user.getNick()) == 2) {
				channel.send().message("/timeout "+user.getNick()+" 600");
				channel.send().message(type.getRandomMessage() + "Third warning, 10 minute timeout!");
				type.updateOffender(user.getNick(), 3);
			} else if (type.getOffender(user.getNick()) == 3) {
				channel.send().message("/timeout "+user.getNick()+" 900");
				channel.send().message(type.getRandomMessage() + " Fourth warning, 15 minute timeout!");
				type.updateOffender(user.getNick(), 4);
			} else if (type.getOffender(user.getNick()) == 4) {
				channel.send().message("/timeout "+user.getNick()+" 1200");
				channel.send().message(type.getRandomMessage() + " Fifth warning, 20 minute timeout!");
				type.updateOffender(user.getNick(), 5);
			} else {
				channel.send().message("/timeout "+user.getNick()+" 7200");
				channel.send().message(type.getRandomMessage() + " Sixth warning, 2 hour timeout! You dun' goofed!");
				type.removeOffender(user.getNick());
			}
		} else {
			type.updateOffender(user.getNick(), 1);
			channel.send().message("/timeout "+user.getNick()+" "+time);
			channel.send().message(type.getRandomMessage() + " First warning");
		}
	}
}
