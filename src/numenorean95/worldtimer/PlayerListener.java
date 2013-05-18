package numenorean95.worldtimer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerListener implements Listener {
	
	private WorldTimer wt;
	
	public PlayerListener(WorldTimer worldTimer) {
		wt = worldTimer;
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent e){
		TimedWorldManager twm = wt.getTimedWorldManager();
		
		TimedWorld from = twm.getWorld(e.getFrom());
		if(from != null){
			from.stopTime(e.getPlayer().getName());
		}
		
		TimedWorld wor = twm.getWorld(e.getPlayer().getWorld());
		if(wor == null) // || e.getPlayer().hasPermission("wt.bypass")
			return;
		
		long timeLeft = wor.timeLeft(e.getPlayer().getName());
		if(timeLeft > 0){
			
			wor.startTime(e.getPlayer().getName());
			e.getPlayer().sendMessage("Starting time, you have " + timeLeft + " seconds left.");
			
		} else {
			e.getPlayer().sendMessage("You do not have any time left on this world!");
			e.getPlayer().teleport(wt.getServer().getWorld(wor.getDefaultWorld()).getSpawnLocation());
		}
		
	}

}
