package me.smc.sb.irccommands;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import me.smc.sb.multi.Tournament;
import me.smc.sb.perm.Permissions;
import me.smc.sb.utils.Utils;

public class SetTwitchChannelCommand extends IRCCommand{

	public SetTwitchChannelCommand(){
		super("Sets the twitch channel to use for the tournament.",
			  "<tournament name> <channel name> ",
			  Permissions.IRC_BOT_ADMIN,
			  "tournamentsettwitch");
	}

	@Override
	public String onCommand(MessageEvent<PircBotX> e, PrivateMessageEvent<PircBotX> pe, String discord, String[] args){
		String argCheck = Utils.checkArguments(args, 2);
		if(argCheck.length() > 0) return argCheck;
		
		String tournamentName = "";
		
		for(int i = 0; i < args.length - 1; i++) tournamentName += args[i] + " ";
		Tournament t = Tournament.getTournament(tournamentName.substring(0, tournamentName.length() - 1));
		
		if(t == null) return "Invalid tournament!";
		
		t.setTwitchChannel(args[args.length - 1].toLowerCase());
		t.save(false);
		
		return "Set the tournament's twitch channel to " + t.getTwitchChannel() + "!";
	}
	
}
