package me.smc.sb.irccommands;

import java.util.ArrayList;
import java.util.List;

import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import me.smc.sb.tourney.Game;
import me.smc.sb.tourney.GameState;
import me.smc.sb.utils.Utils;

public class SkipWarmupCommand extends IRCCommand{

	public static List<Game> gamesAllowedToSkip;
	
	public SkipWarmupCommand(){
		super("Allows teams to skip their warmups.",
			  " ",
			  null,
			  "warmupskip");
		gamesAllowedToSkip = new ArrayList<>();
	}
	
	@Override
	public String onCommand(MessageEvent e, PrivateMessageEvent pe, String discord, String[] args){
		if(e == null || discord != null || pe != null) return "You cannot skip a warmup in here!";
		
		String userName = Utils.toUser(e, pe);
		
		if(!gamesAllowedToSkip.isEmpty())
			for(Game game : gamesAllowedToSkip)
				if(game.getLobbyManager().verify(userName) &&
				   game.getNextTeam().getTeam().has(userName) &&
				   !game.getState().eq(GameState.ENDED)){
					game.getSelectionManager().skipWarmup();
					
					return "";
				}
		
		return "There are no warmups that can be skipped!";
	}
	
}
