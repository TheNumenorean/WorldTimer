package numenorean95.worldtimer;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TimedWorld {
	

	private String world;
	private String default_world;
	private long cooldown;
	private long limit;
	private Map<String, Long> end_remaining, end_cooldown, remaining_time;
	private WorldTimer wt;

	public TimedWorld(int limit, int cooldown, String defaultName, String name, WorldTimer worldTimer) {
		this.limit = limit;
		this.cooldown = cooldown;
		default_world = defaultName;
		world = name;
		this.wt = worldTimer;
		
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
	 * @param player Player to start.
	 */
	public void startTime(final String player) {
		if(end_remaining.containsKey(player) || end_cooldown.containsKey(player))
			return;
		Long remainder = limit;
		if(remaining_time.containsKey(player)){
			end_remaining.put(player, System.currentTimeMillis() + 1000 * (remainder = remaining_time.get(player)));
			remaining_time.remove(player);
		} else
			end_remaining.put(player, System.currentTimeMillis() + 1000 * limit);
		
		Bukkit.getScheduler().runTaskLater(wt, new Runnable(){

			@Override
			public void run() {
				wt.getServer().getPlayer(player).teleport(wt.getServer().getWorld(default_world).getSpawnLocation());
				wt.getServer().getPlayer(player).sendMessage("You have run out of time on this world!");
				
			}
			
		}, remainder * 20);
		
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
	
	public long getTimeLimit(){
		return limit;
	}
	
	public long getCooldown(){
		return cooldown;
	}
	
	public String getWorld(){
		return world;
	}

}
