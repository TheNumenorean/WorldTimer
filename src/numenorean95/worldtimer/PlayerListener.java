package numenorean95.worldtimer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
	
	private WorldTimer wt;
	private TimedWorldManager twm;
	
	public PlayerListener(WorldTimer worldTimer) {
		wt = worldTimer;
		twm = wt.getTimedWorldManager();
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent e){
		
		
		TimedWorld from = twm.getWorld(e.getFrom());
		if(from != null){
			from.stopTime(e.getPlayer().getName());
		}
		
		refreshPlayer(e.getPlayer());
		
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e){
		refreshPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent e){
		TimedWorld from = twm.getWorld(e.getPlayer().getWorld());
		if(from != null){
			from.stopTime(e.getPlayer().getName());
		}
	}
	
	private void refreshPlayer(Player p){
		TimedWorld wor = twm.getWorld(p.getWorld());
		if(wor == null) // || e.getPlayer().hasPermission("wt.bypass")
			return;
		
		long timeLeft = wor.timeLeft(p.getName());
		if(timeLeft > 0){
			
			wor.startTime(p.getName());
			p.sendMessage(ChatColor.GOLD + "Starting time, you have " + timeLeft / 60.0 + " minutes left.");
			
		} else {
			p.sendMessage(ChatColor.RED + "You do not have any time left on this world! Come back in " + wor.getRemainingCooldown(p.getName()) / 60.0 + " minutes.");
			p.teleport(wt.getServer().getWorld(wor.getDefaultWorld()).getSpawnLocation());
		}
	}
	
	

}
