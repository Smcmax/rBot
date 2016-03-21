package me.smc.sb.irccommands;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import me.smc.sb.multi.Tournament;
import me.smc.sb.perm.Permissions;
import me.smc.sb.utils.Utils;

public class CreateTournamentCommand extends IRCCommand{

	public CreateTournamentCommand(){
		super("Creates a tournament.",
			  "<tournament name> ",
			  Permissions.IRC_BOT_ADMIN,
			  "tournamentcreate");
	}
	
	@Override
	public String onCommand(MessageEvent<PircBotX> e, PrivateMessageEvent<PircBotX> pe, String discord, String[] args){
		String argCheck = Utils.checkArguments(args, 1);
		if(argCheck.length() > 0) return argCheck;
		
		String tournamentName = "";
		
		for(int i = 0; i < args.length; i++) tournamentName += args[i] + " ";
		new Tournament(tournamentName.substring(0, tournamentName.length() - 1));
		
		return "Created the " + tournamentName.substring(0, tournamentName.length() - 1) + " tournament!";
	}
	
}
