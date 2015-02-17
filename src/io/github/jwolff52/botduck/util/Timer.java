package io.github.jwolff52.botduck.util;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Timer implements Runnable{
	
	static final Logger logger=LoggerFactory.getLogger(Timer.class);

	
	private Channel channel;
	
	private String message;
	
	private int delay;
	private int messageCount;
	private int currentCount;
	
	public static ArrayList<Timer> timers=new ArrayList<>();
	
	public Timer(Channel c, String message, int delay) {
		this.channel=c;
		this.message=message;
		this.delay=delay*1000;
		this.messageCount=-1;
		this.currentCount=0;
		timers.add(this);
		new Thread(this).start();
	}
	
	public Timer(Channel c, String message, int delay, int messageCount) {
		this.channel=c;
		this.message=message;
		this.delay=delay;
		this.messageCount=messageCount;
		this.currentCount=0;
		timers.add(this);
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		if(messageCount==-1){
			while(true) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					logger.error("Error occurred with a timer!\n" + e.toString());
				}
				channel.send().message(message);
			}
		} else {
			while(true) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					logger.error("Error occurred with a timer!\n" + e.toString());
				}
				while(currentCount<messageCount);
				currentCount=0;
				channel.send().message(message);
			}
		}
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public int getMessageCount(){
		return messageCount;
	}
	
	public void incrementCurrentCount(){
		currentCount++;
	}
	
	public static ArrayList<Timer> getTimers(){
		return timers;
	}
}
