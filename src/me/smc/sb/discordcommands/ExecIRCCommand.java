package me.smc.sb.discordcommands;

import me.smc.sb.irccommands.IRCCommand;
import me.smc.sb.main.Main;
import me.smc.sb.utils.Utils;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class ExecIRCCommand extends GlobalCommand{

	public ExecIRCCommand(){
		super(null, 
			  " - Executes an irc command", 
			  "{prefix}execIRC\nThis command executes an IRC specific command.\n\n" +
			  "----------\nUsage\n----------\n{prefix}execIRC {command} - Executes the IRC command\n\n" +
			  "----------\nAliases\n----------\nThere are no aliases.",  
			  true,
			  "execIRC");
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args){
		if(!Utils.checkArguments(e, args, 1)) return;
		
		String message = "";
		for(int i = 0; i < args.length; i++)
			message += " " + args[i];
		message = message.substring(1);
		
		String id = e.isPrivate() ? e.getPrivateChannel().getId() : e.getTextChannel().getId();
		
		if(Main.ircBot == null) Utils.info(e.getChannel(), "The IRC bot object is null! Make sure to tell Smc!");
		else IRCCommand.handleCommand(null, null, id, message);
	}
	
}