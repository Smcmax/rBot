package me.smc.sb.multi;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.smc.sb.main.Main;
import me.smc.sb.perm.Permissions;
import me.smc.sb.utils.Configuration;
import me.smc.sb.utils.Utils;

public class Match{

	private int players, matchNum, bestOf;
	private Team fTeam, sTeam;
	private Tournament tournament;
	private long scheduledDate;
	private MapPool pool;
	private Game game;
	private ArrayList<String> matchAdmins;
	
	public Match(Tournament t, int players){
		this(t, t.incrementMatchCount(), players, true);
	}
	
	public Match(Tournament t, int matchNum, int players, boolean append){
		this.players = players;
		this.matchNum = matchNum;
		this.tournament = t;
		this.scheduledDate = 0;
		this.bestOf = 5;
		this.fTeam = null;
		this.sTeam = null;
		this.pool = null;
		this.game = null;
		this.matchAdmins = new ArrayList<>();
		
		save(append);
		t.addMatch(this);
	}
	
	public int getPlayers(){
		return players;
	}
	
	public int getMatchNum(){
		return matchNum;
	}
	
	public Team getFirstTeam(){
		return fTeam;
	}
	
	public Team getSecondTeam(){
		return sTeam;
	}
	
	public MapPool getMapPool(){
		return pool;
	}
	
	public Tournament getTournament(){
		return tournament;
	}
	
	public int getBestOf(){
		return bestOf;
	}
	
	public String getLobbyName(){
		return tournament.getName() + ": (" + fTeam + ") vs (" + sTeam + ")";
	}
	
	public Game getGame(){
		return game;
	}
	
	public void addMatchAdmin(String admin){
		if(!matchAdmins.contains(admin))
			matchAdmins.add(admin);
	}
	
	public void removeMatchAdmin(String admin){
		if(matchAdmins.contains(admin))
			matchAdmins.remove(admin);
	}
	
	public boolean isMatchAdmin(String admin){
		if(admin.length() == 17)
			if(Permissions.check(Main.api.getUserById(admin), Permissions.IRC_BOT_ADMIN))
				return true;
		
		return matchAdmins.contains(admin);
	}
	
	public ArrayList<String> getMatchAdmins(){
		return matchAdmins;
	}
	
	public void resize(int players){
		this.players = players;
		if(game != null) game.resize();
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
	public void setTeams(Team fTeam, Team sTeam){
		this.fTeam = fTeam;
		this.sTeam = sTeam;
	}
	
	public void setTime(long time){
		scheduledDate = time;
		
		if(scheduledDate < Utils.getCurrentTimeUTC()){
			delete();
			return;
		}
		
		Timer t = new Timer();
		t.schedule(new TimerTask(){
			public void run(){
				new Game(Match.this);
			}
		}, scheduledDate - Utils.getCurrentTimeUTC());
	}
	
	public void setBestOf(int bestOf){
		this.bestOf = bestOf;
	}
	
	public void setMapPool(MapPool pool){
		this.pool = pool;
	}
	
	public void delete(){
		Configuration config = tournament.getConfig();
		ArrayList<String> savedMatches = config.getStringList("matches");
		
		if(!savedMatches.isEmpty()){
			savedMatches.remove(String.valueOf(matchNum));
			config.writeStringList("matches", savedMatches, true);
		}
		
		config.deleteKey("match-" + matchNum + "-players");
		config.deleteKey("match-" + matchNum + "-team1");
		config.deleteKey("match-" + matchNum + "-team2");
		config.deleteKey("match-" + matchNum + "-date");
		config.deleteKey("match-" + matchNum + "-pool");
		config.deleteKey("match-" + matchNum + "-bestof");
		config.deleteKey("match-" + matchNum + "-admins");
	}
	
	public void save(boolean append){
		Configuration config = tournament.getConfig();
		
		if(append) config.appendToStringList("matches", String.valueOf(matchNum), true);
		config.writeValue("match-" + matchNum + "-players", players);
		
		if(fTeam != null && sTeam != null){
			config.writeValue("match-" + matchNum + "-team1", fTeam.getTeamName());
			config.writeValue("match-" + matchNum + "-team2", sTeam.getTeamName());
		}
		
		config.writeValue("match-" + matchNum + "-date", scheduledDate);
		
		if(pool != null) config.writeValue("match-" + matchNum + "-pool", pool.getPoolNum());
		
		config.writeValue("match-" + matchNum + "-bestof", bestOf);
		config.writeStringList("match-" + matchNum + "-admins", matchAdmins, true);
	}
	
}
