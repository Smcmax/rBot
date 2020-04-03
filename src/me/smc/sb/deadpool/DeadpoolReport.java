package me.smc.sb.deadpool;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.TextChannel;

public class DeadpoolReport{
	
	private String id;
	private TextChannel reportChannel;
	private int lastFightId;
	private long lastActivity;
	private List<DeadpoolUser> voters;
	
	public DeadpoolReport(String id, TextChannel reportChannel){
		this.id = id;
		this.reportChannel = reportChannel;
		lastFightId = 0;
		setLastActivity();
		voters = new ArrayList<>();
	}
	
	public String getId(){
		return id;
	}
	
	public TextChannel getReportChannel(){
		return reportChannel;
	}
	
	public int getLastFightId(){
		return lastFightId;
	}
	
	public List<DeadpoolUser> getVoters(){
		return voters;
	}
	
	public boolean isExpired(){
		return System.currentTimeMillis() - lastActivity > 5400000; // 1h30m
	}

	public void setLastFightId(int lastFightId){
		this.lastFightId = lastFightId;
	}
	
	public void setLastActivity(){
		lastActivity = System.currentTimeMillis();
	}
	
	public void setVoters(List<DeadpoolUser> voters){
		this.voters = voters;
	}
}
