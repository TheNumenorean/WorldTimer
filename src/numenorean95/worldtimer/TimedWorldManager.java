package numenorean95.worldtimer;

import java.util.ArrayList;

import org.bukkit.World;

public class TimedWorldManager {
	
	private WorldTimer wt;
	private ArrayList<TimedWorld> worlds;

	public TimedWorldManager(WorldTimer worldTimer) {
		wt = worldTimer;
		worlds = new ArrayList<TimedWorld>();
	}

	public void addWorld(TimedWorld tw) {
		worlds.add(tw);
	}

	public TimedWorld getWorld(World world) {
		for(TimedWorld tw : worlds)
			if(tw.getWorld().equals(world.getName()))
				return tw;
		return null;
	}

}
