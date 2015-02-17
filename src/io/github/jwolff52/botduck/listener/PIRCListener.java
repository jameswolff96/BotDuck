package io.github.jwolff52.botduck.listener;

import io.github.jwolff52.botduck.database.Database;
import io.github.jwolff52.botduck.util.FTPUtil;
import io.github.jwolff52.botduck.util.PointsRunnable;
import io.github.jwolff52.botduck.util.TFileReader;
import io.github.jwolff52.botduck.util.TFileWriter;
import io.github.jwolff52.botduck.util.TType;
import io.github.jwolff52.botduck.util.Timeouts;
import io.github.jwolff52.botduck.util.Timer;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.SetChannelBanEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PIRCListener extends ListenerAdapter<PircBotX> {

	private static final Logger logger = LoggerFactory.getLogger(PIRCListener.class);

	public void onMessage(MessageEvent<PircBotX> e) throws Exception {
		if(!isRegular(e.getUser().getNick(), e.getChannel())){
			if(e.getMessage().matches("[A-Z]{20,}")){
				new Timeouts(e.getChannel(), e.getUser(), 1, TType.CAPS);
			} else if(e.getMessage().matches("([A-Za-z0-9_:/\\-@\\\\s.]+[\\s?\\.\\s?]?)+([\\s?\\.\\s?](c\\s?o\\s?m|n\\s?e\\s?t|o\\s?r\\s?g|c\\s?o|a\\s?u|u\\s?k|u\\s?s|m\\s?e|b\\s?z|i\\s?n\\s?t|e\\s?d\\s?u|g\\s?o\\s?v\\s?|m\\s?i\\s?l|a\\s?c)(\\s)?(/)?)+")) {
				new Timeouts(e.getChannel(), e.getUser(), 1, TType.LINK);
			} else if(e.getMessage().matches("[\\W_]{15,}")) {
				new Timeouts(e.getChannel(), e.getUser(), 1, TType.SYMBOLS);
			} else if(e.getMessage().length()>=250) {
				new Timeouts(e.getChannel(), e.getUser(), 1, TType.PARAGRAPH);
			} else if(e.getMessage().matches("(:\\(|:\\)|:/|:D|:o|:p|:z|;\\)|;p|<3|>\\(|B\\)|o_o|R\\)|4Head|ANELE|ArsonNoSexy|AsianGlow|AtGL|AthenaPMS|AtIvy|BabyRage|AtWW|BatChest|BCWarrior|BibleThump|BigBrother|BionicBunion|BlargNaut|BloodTrail|BORT|BrainSlug|BrokeBack|BuddhaBar|CougarHunt|DAESuppy|DansGame|DatSheffy|DBstyle|DendiFace|DogFace|EagleEye|EleGiggle|EvilFetus|FailFish|FPSMarksman|FrankerZ|FreakinStinkin|FUNgineer|FunRun|FuzzyOtterOO|GasJoker|GingerPower|GrammarKing|HassaanChop|HassanChop|HeyGuys|HotPokket|HumbleLife|ItsBoshyTime|Jebaited|KZowl|JKanStyle|JonCarnage|KAPOW|Kappa|Keepo|KevinTurtle|Kippa|Kreygasm|KZassault|KZcover|KZguerilla|KZhelghast|KZskull|Mau5|mcaT|MechaSupes|MrDestructoid|MrDestructoid|MVGame|NightBat|NinjaTroll|NoNoSpot|noScope|NotAtk|OMGScoots|OneHand|OpieOP|OptimizePrime|panicBasket|PanicVis|PazPazowitz|PeoplesChamp|PermaSmug|PicoMause|PipeHype|PJHarley|PJSalt|PMSTwin|PogChamp|Poooound|PRChase|PunchTrees|PuppeyFace|RaccAttack|RalpherZ|RedCoat|ResidentSleeper|RitzMitz|RuleFive|Shazam|shazamicon|ShazBotstix|ShazBotstix|ShibeZ|SMOrc|SMSkull|SoBayed|SoonerLater|SriHead|SSSsss|StoneLightning|StrawBeary|SuperVinlin|SwiftRage|TF2John|TheRinger|TheTarFu|TheThing|ThunBeast|TinyFace|TooSpicy|TriHard|TTours|UleetBackup|UncleNox|UnSane|Volcania|WholeWheat|WinWaker|WTRuck|WutFace|YouWHY|\\(mooning\\)|\\(poolparty\\)|\\(puke\\)|:'\\(|:tf:|aPliS|BaconEffect|BasedGod|BroBalt|bttvNice|ButterSauce|cabbag3|CandianRage|CHAccepted|CiGrip|ConcernDoge|D:|DatSauce|FapFapFap|FishMoley|ForeverAlone|FuckYea|GabeN|HailHelix|HerbPerve|Hhhehehe|HHydro|iAMbh|iamsocal|iDog|JessSaiyan|JuliAwesome|KaRappa|KKona|LLuda|M&Mjc|ManlyScreams|NaM|OhGod|OhGodchanZ|OhhhKee|OhMyGoodness|PancakeMix|PedoBear|PedoNam|PokerFace|PoleDoge|RageFace|RebeccaBlack|RollIt!|rStrike|SexPanda|She'llBeRight|ShoopDaWhoop|SourPls|SuchFraud|SwedSwag|TaxiBro|tEh|ToasTy|TopHam|TwaT|UrnCrown|VisLaud|WatChuSay|WhatAYolk|YetiZ|PraiseIt|\\s){8,}")) {
				new Timeouts(e.getChannel(), e.getUser(), 1, TType.EMOTE);
			} else if(e.getMessage().matches(TFileReader.readFile(new File("spam.txt")).get(0)+"+")) {
				new Timeouts(e.getChannel(), e.getUser(), 1, TType.SPAM);
			}
		}
		
		
		if ((e.getMessage().contains("Now Playing:"))
				&& (e.getUser().getNick().equalsIgnoreCase("monstercat"))) {
			File f = new File("currentsong.txt");
			TFileWriter.overWriteFile(f, e.getMessage().substring(e.getMessage().indexOf(":") + 2));
			FTPUtil.upload(f);
		} else if (e.getMessage().equalsIgnoreCase("!top")) {
			e.getChannel().send().message(topPlayers(5, e.getChannel()));
		} else if (e.getMessage().toLowerCase().startsWith("!top ")) {
			int amnt = -1;
			try {
				amnt = Double.valueOf(e.getMessage().substring(e.getMessage().indexOf(" ") + 1)).intValue();
			} catch (NumberFormatException e1) {
				e.getChannel().send().message(e.getMessage().substring(
										e.getMessage().indexOf(" ") + 1)
										+ " isn't a number, silly!");
				return;
			}
			if (amnt > 25) {
				e.getChannel().send().message("Hey! No spamming! (Number cannot be greater than 25)");
				return;
			}
			if (amnt <= 0) {
				e.getChannel().send().message("Hey! Let's not break reality here! (Number cannot be less than 1)");
				return;
			}
			e.getChannel().send().message(topPlayers(amnt, e.getChannel()));
		} else if (e.getMessage().equalsIgnoreCase("!points")) {
			String pts = getPoints(e.getUser().getNick(), e.getChannel());
			if (pts == null) {
				e.getChannel().send().message(e.getUser().getNick() + " has no points!");
			} else {
				e.getChannel().send().message(e.getUser().getNick() + " has " + pts
										+ " points!");
			}
		} else if (e.getMessage().toLowerCase().startsWith("!points ")) {
			if((!e.getChannel().isOp(e.getUser()))||(e.getChannel().isOwner(e.getUser()))) {
				return;
			}
			String[] args = getArgs(e.getMessage().toLowerCase());
			if (args.length < 3) {
				e.getChannel().send().message("Hey, thats not right, use !points <add|remove> <user> <ammount>");
				return;
			}			if (!args[0].matches("(add|remove)")) {
				e.getChannel().send().message("Hey, thats not right, use !points <add|remove> <user> <ammount>");
				return;
			}
			if (getPoints(args[1], e.getChannel()) == null) {
				e.getChannel().send().message("Sorry, "
										+ e.getUser().getNick()
										+ ". "
										+ args[1]
										+ " has never been in the channel before!");
				return;
			}
			int amt = -1;
			int currentPoints = Integer.valueOf(getPoints(args[1], e.getChannel()));
			
			try {
				try {
					amt = Math.abs(Integer.valueOf(args[2]));
				} catch(Exception e1){
					e.getChannel().send().message("That number is too big!");
				}
				if(amt>9001){
					e.getChannel().send().message("That number is too big!");
				}
			} catch (NumberFormatException e1) {
				e.getChannel().send().message("Hey, thats not right, use !points <add|remove> <user> <ammount>");
				logger.error("NAN\n" + e1.toString());
				return;
			}
			
			if (args[0].equalsIgnoreCase("add")) {
				addPoints(args[1], e.getChannel(), amt);
			} else {
				if(amt>currentPoints){
					addPoints(args[1], e.getChannel(), -currentPoints);
					return;
				}
				addPoints(args[1], e.getChannel(), -amt);
			}
			
			e.getChannel().send().message("Succesfully changed the points for " + args[1]
									+ " to "
									+ getPoints(args[1], e.getChannel()));
		} else if (e.getMessage().equalsIgnoreCase("!commands")) {
			e.getChannel().send().message("The commands avaliable for this channel are located at http://bit.ly/19gkTzZ");
		} else if (e.getMessage().equalsIgnoreCase("!crew")) {
			e.getChannel().send().message("Want to play GTA V with me? Join the crew at http://bit.ly/17JhHMA");
		} else if (e.getMessage().equalsIgnoreCase("!darkness")) {
			e.getChannel().send().message("Do you want mobs? Because that's how you get mobs.");
		} else if (e.getMessage().equalsIgnoreCase("!gt")) {
			e.getChannel().send().message("My XBOX Live gamertag is donald10101");
		} else if (e.getMessage().equalsIgnoreCase("!hermann")) {
			e.getChannel().send().message("\"Lift off in 10, 9, 8, 7, 6, 5, 4, 3, 2, q\" Damnit Houston...Cancel the launch");
		} else if (e.getMessage().equalsIgnoreCase("!kspmods")) {
			e.getChannel().send().message("My current KSP Mods are all of the mods found at http://bit.ly/1c3lUcM");
		} else if (e.getMessage().equalsIgnoreCase("!mawp")) {
			e.getChannel().send().message("MAWP MAWP MAWP, Tinnitus sucks!");
		} else if (e.getMessage().equalsIgnoreCase("!rawr")) {
			e.getChannel().send().message(e.getUser().getNick()+" RAWRS loudly at all within range!!");
		} else if (e.getMessage().equalsIgnoreCase("!skyfactory")) {
			e.getChannel().send().message("I am currently playing sky factory found on the ATLauncher (atlauncher.com)!");
		} else if (e.getMessage().equalsIgnoreCase("!treetwerker")) {
			e.getChannel().send().message("My name is donald10101 and I twerk for the trees!");
		} else if (e.getMessage().equalsIgnoreCase("!countdown")) {
			
		} else if (e.getMessage().toLowerCase().startsWith("!reg ")) {
			if((!e.getChannel().isOp(e.getUser()))||(e.getChannel().isOwner(e.getUser()))) {
				return;
			}
			
			String[] args=getArgs(e.getMessage().toLowerCase());
			
			if(args.length!=2){
				e.getChannel().send().message("Hey, thats not right, use !reg <add|remove> <user>");
				return;
			}
			
			if(args[0].equals("add")){
				if(Database.executeUpdate("INSERT INTO "+Database.DEFAULT_SCHEMA+".donald10101Regulars VALUES (\'"+args[1]+"\')")) {
					e.getChannel().send().message("Succelfully added "+args[1]+" to the regulars list");
				}
			} else if(args[0].equals("remove")) {
				if(Database.executeUpdate("DELETE FROM "+Database.DEFAULT_SCHEMA+".donald10101Regulars WHERE userID=\'"+args[1]+"\'")) {
					e.getChannel().send().message("Succelfully removed "+args[1]+" from the regulars list");
				}
			} else {
				e.getChannel().send().message("Hey, thats not right, use !reg <add|remove> <user>");
				return;
			}
		}
		
		for(Timer t:Timer.getTimers()){
			if(t.getMessageCount()!=-1&&t.getChannel().getName().equalsIgnoreCase(e.getChannel().getName())){
				t.incrementCurrentCount();
			}
		}
		/*
		 * else
		 * if(e.getChannel().getName().equalsIgnoreCase("botduck")||e.getMessage
		 * ().equalsIgnoreCase("!join")) { Channels.addChannel(e.getUser()); }
		 * else
		 * if(e.getChannel().getName().equalsIgnoreCase("botduck")||e.getMessage
		 * ().equalsIgnoreCase("!leave")) { Channels.removeChannel(e.getUser());
		 * }
		 */
	}

	public void onJoin(JoinEvent<PircBotX> e) throws Exception {
		if (e.getUser().getNick().equalsIgnoreCase("botduck")) {
			if(e.getChannel().getName().equalsIgnoreCase("#donald10101")) {
				new Timer(e.getChannel(), "Check out all of my chat commands over at http://bit.ly/19gkTzZ", 600, 25);
				new Timer(e.getChannel(), "Don't forget to follow if you're enjoying the stream!", 600, 25);
			}
			e.getChannel().send().message("Yo! I'm BotDuck (DuckBot was taken :() I am owned by Donald10101. Make sure you mod me so I don't get timed out by other bots!");
			return;
		}
		if(e.getChannel().getName().toLowerCase().contains("donald")){
			if(!PointsRunnable.containsUser(e.getUser().getNick())){
				new PointsRunnable(e.getUser().getNick(), e.getChannel());
			}
			if(isRegular(e.getUser().getNick(), e.getChannel())) {
				if (e.getUser().getNick().equalsIgnoreCase("ahleyah")) {
					e.getChannel().send().message("Don't welcome "
											+ e.getUser().getNick()
											+ " to the chat because they want to lurk and I'm not supposed to say anything...oops");
				} else if (e.getUser().getNick().equalsIgnoreCase("6hikari6")) {
					e.getChannel().send().message("Hey "
							+ e.getUser().getNick()
							+ "! Get under your bridge you troll!");
				} else {
					e.getChannel().send().message("Welcome "
											+ e.getUser().getNick()
											+ " to the chat! You are now earning points!");
				}
			}
		}//else{
//			if (e.getUser().getNick().equalsIgnoreCase("ahleyah")) {
//				e.getChannel().send().message("Don't welcome "
//										+ e.getUser().getNick()
//										+ " to the chat because they want to lurk and I'm not supposed to say anything...oops");
//			} else {
//				e.getChannel().send().message("Welcome "
//										+ e.getUser().getNick()
//										+ " to the chat!");
//			}
//		}
	}

	public void onPart(PartEvent<PircBotX> e) throws Exception {
		PointsRunnable.removeUser(e.getUser().getNick());
	}

	public void onSetChannelBan(SetChannelBanEvent<PircBotX> e)
			throws Exception {
		ArrayList<String> points = TFileReader.readFile(new File(e.getChannel()
				.getName() + "Points.txt"));
		for (int i = 0; i < points.size(); i++) {
			if (((String) points.get(i)).contains(e.getUser().getNick())) {
				points.remove(i);
				break;
			}
		}
		TFileWriter.overWriteFile(new File(e.getChannel().getName()
				+ "Points.txt"), points);
	}

	private String topPlayers(int number, Channel channel) {
		StringBuilder output = new StringBuilder();
		output.append("The top " + number + " points holder(s) are: ");
		ResultSet rs=Database.executeQuery("SELECT * FROM "+Database.DEFAULT_SCHEMA+".donald10101Points ORDER BY points DESC");
		try {
			for(int i=0;i<6;i++){
				rs.next();
			}
			while(rs.next()&&number>1){
				output.append(rs.getString(1)+": "+rs.getInt(2) + ", ");
				number--;
			}
			output.append(rs.getString(1)+": "+rs.getInt(2));
		} catch (SQLException e) {
			logger.error("Error occurred creating Top list!\n");
			e.printStackTrace();
		}
		return output.toString();
	}

	private String getPoints(String nick, Channel c) {
		ResultSet rs=Database.executeQuery("SELECT * FROM "+Database.DEFAULT_SCHEMA+".donald10101Points WHERE userID=\'"+nick+"\'");
		try {
			if(!rs.next()){
				Database.executeUpdate("INSERT INTO "+Database.DEFAULT_SCHEMA+".donald10101Points VALUES (\'"+nick+"\',1)");
				return 1+"";
			}
			return rs.getInt(2)+"";
		} catch (SQLException e) {
			logger.error("Unable to retrieve points for: "+nick+"\n"+e.toString());
		}
		return null;
	}

	private String[] getArgs(String message) {
		message = message.substring(message.indexOf(" ") + 1);
		return message.split("\\s");
	}

	private void addPoints(String nick, Channel c, int ammount) {
		ResultSet rs=Database.executeQuery("SELECT * FROM "+Database.DEFAULT_SCHEMA+".donald10101Points WHERE userID=\'"+nick+"\'");
		if(rs==null) {
			Database.executeUpdate("INSERT INTO "+Database.DEFAULT_SCHEMA+".donald10101Points VALUES(\'"+nick+"\', 1)");
			return;
		}
		try {
			while(rs.next()){
				Database.executeUpdate("UPDATE "+Database.DEFAULT_SCHEMA+".donald10101Points SET "
						+ "userID=\'"+nick+"\',"
						+ "points="+(rs.getInt(2)+ammount)
						+ " WHERE userID=\'"+nick+"\'");
			}
		} catch (SQLException e) {
			logger.error("An Error occured updating "+nick+"'s points!\n"+e.toString());
		}
	}
	
	private boolean isRegular(String u, Channel c){
		ResultSet rs=Database.executeQuery("SELECT * FROM "+Database.DEFAULT_SCHEMA+".donald10101Regulars WHERE userID=\'"+u+"\'");
		try {
			return rs.next();
		} catch (SQLException e) {
			logger.error(u+" is not a regular!");
		}
		return false;
	}
}