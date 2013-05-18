package numenorean95.worldtimer;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.entity.Player;

public class TimedWorld {
	

	private String world;
	private String default_world;
	private int cooldown;
	private int limit;
	private Map<String, Long> end_remaining, end_cooldown, remaining_time;

	public TimedWorld(int limit, int cooldown, String defaultName, String name) {
		this.limit = limit;
		this.cooldown = cooldown;
		default_world = defaultName;
		world = name;
		
		end_remaining = new TreeMap<String, Long>();
		end_cooldown = new TreeMap<String, Long>();
		remaining_time = new TreeMap<String, Long>();
	}

	/**
	 * Returns remaining time in seconds.
	 * @param player Player to check
	 * @return Time remaining for the specified player on this world.
	 */
	public long timeLeft(String player) {
		
		checkPlayerTimeLimit(player);
		checkPlayerCooldown(player);
		
		if(end_cooldown.containsKey(player))
			return 0;
		
		if(end_remaining.containsKey(player))
			return (end_remaining.get(player) - System.currentTimeMillis()) / 1000;
		
		if(remaining_time.containsKey(player))
			return remaining_time.get(player);
		
		return limit;
			
	}
	
	/**
	 * Starts the counter for this player, assuming it is not already going or they ran out of time and the
	 * cooldown has not passed.
	 * @param player Player to star.
	 */
	public void startTime(String player) {
		if(end_remaining.containsKey(player) || end_cooldown.containsKey(player))
			return;
		
		if(remaining_time.containsKey(player)){
			end_remaining.put(player, System.currentTimeMillis() + 1000 * remaining_time.get(player));
			remaining_time.remove(player);
		} else
			end_remaining.put(player, System.currentTimeMillis() + 1000 * limit);
		
	}
	
	public void stopTime(String player){
		checkPlayerTimeLimit(player);
		
		if(end_remaining.containsKey(player)){
			remaining_time.put(player, (end_remaining.get(player) - System.currentTimeMillis()) / 1000);
			end_remaining.remove(player);
		}
		
	}
	
	private void checkPlayerCooldown(String player){
		if(end_cooldown.containsKey(player) && end_cooldown.get(player) - System.currentTimeMillis() < 0)
			end_cooldown.remove(player);
	}
	
	private void checkPlayerTimeLimit(String player){
		if(end_remaining.containsKey(player) && end_remaining.get(player) - System.currentTimeMillis() < 0){
			end_cooldown.put(player, System.currentTimeMillis() + 1000 * cooldown);
			end_remaining.remove(player);
		}
	}

	public String getDefaultWorld() {
		return default_world;
	}
	
	public int getTimeLimit(){
		return limit;
	}
	
	public int getCooldown(){
		return cooldown;
	}
	
	public String getWorld(){
		return world;
	}

}
