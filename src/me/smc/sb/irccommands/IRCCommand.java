package me.smc.sb.irccommands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import me.smc.sb.main.Main;
import me.smc.sb.perm.Permissions;
import me.smc.sb.utils.Log;
import me.smc.sb.utils.Utils;

public abstract class IRCCommand{

	private final String[] names;
	private final String description, usage;
	private final Permissions perm;
	public static List<IRCCommand> commands;
	
	public IRCCommand(String description, String usage, Permissions perm, String...names){
		this.names = names;
		this.perm = perm;
		this.description = description;
		this.usage = usage;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getUsage(){
		return usage;
	}
	
	public String[] getNames(){
		return names;
	}
	
	public Permissions getPerm(){
		return perm;
	}
	
	public boolean isName(String name){
		for(String n : names)
			if(n.equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public static void handleCommand(MessageEvent<PircBotX> e, PrivateMessageEvent<PircBotX> pe, String discord, String msg){
		if(pe != null)
			try{
				Main.ircBot.sendIRC().joinChannel(pe.getUser().getNick());
			}catch(Exception ex){
				Log.logger.log(Level.INFO, "Could not join channel " + pe.getUser().getNick());
			}
		
		String user = Utils.toUser(e, pe);
		
		String[] split = msg.split(" ");
		for(IRCCommand ic : commands)
			if(ic.isName(split[0]) && me.smc.sb.perm.Permissions.hasPerm(user, ic.perm)){
				String[] args = msg.replace(split[0] + " ", "").split(" ");
				if(!msg.contains(" ")) args = new String[]{};
				ic.onCommand(e, pe, discord, args);
				return;
			}
		
		Utils.info(e, pe, discord, "This is not a command! Use !help if you are lost!");
	}
	
	public static void registerCommands(){
		commands = new ArrayList<IRCCommand>();
		commands.add(new HelpCommand());
		commands.add(new CreateTournamentCommand());
		commands.add(new CreateMapPoolCommand());
		commands.add(new CreateTeamCommand());
		commands.add(new CreateMatchCommand());
		commands.add(new DeleteTournamentCommand());
		commands.add(new DeleteMapPoolCommand());
		commands.add(new DeleteTeamCommand());
		commands.add(new DeleteMatchCommand());
		commands.add(new ListMapPoolsCommand());
		commands.add(new ListMapsInPoolCommand());
		commands.add(new ListTournamentsCommand());
		commands.add(new ListTeamsCommand());
		commands.add(new ListTeamPlayersCommand());
		commands.add(new ListMatchesCommand());
		commands.add(new ListGamesCommand());
		commands.add(new ListMatchAdminsCommand());
		commands.add(new AddMatchAdminCommand());
		commands.add(new RemoveMatchAdminCommand());
		commands.add(new SetScoreV2Command());
		commands.add(new AddMapToPoolCommand());
		commands.add(new RemoveMapFromPoolCommand());
		commands.add(new SetMapPoolSheetCommand());
		commands.add(new SetTeamPlayersCommand());
		commands.add(new SetMatchPoolCommand());
		commands.add(new SetMatchTeamsCommand());
		commands.add(new SetBestOfCommand());
		commands.add(new SetMatchScheduleCommand());
		commands.add(new ForceStopGameCommand());
		commands.add(new JoinMatchCommand());
		commands.add(new RandomCommand());
		commands.add(new SelectMapCommand());
		commands.add(new InvitePlayerCommand());
		commands.add(new BanMapCommand());
	}
	
	public abstract void onCommand(MessageEvent<PircBotX> e, PrivateMessageEvent<PircBotX> pe, String discord, String[] args);
	
}