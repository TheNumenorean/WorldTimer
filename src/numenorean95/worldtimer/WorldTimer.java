package numenorean95.worldtimer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldTimer extends JavaPlugin {
	
	public static Logger log;
	private FileConfiguration conf;
	private TimedWorldManager wm;
	
	@Override
	public void onLoad(){
		log = getLogger();
		wm = new TimedWorldManager(this);
		conf = this.getConfig();
		
		conf.options().copyDefaults(true);
		
		ConfigurationSection worlds = conf.getConfigurationSection("worlds");
		if(worlds == null)
			worlds = conf.createSection("worlds");
		for(String s : worlds.getKeys(false)){
			loadWorld(worlds.getConfigurationSection(s), s);
		}
		
		try {
			conf.save(this.getDataFolder().getPath() + File.separator + "config.yml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadWorld(ConfigurationSection cs, String s) {
		cs.addDefault("limit", 15);
		cs.addDefault("interval", 60);
		cs.addDefault("default_world", "world");
		
		TimedWorld tw = new TimedWorld(cs.getInt("limit"), cs.getInt("interval"), cs.getString("default_world"), s, this);
		
		wm.addWorld(tw);
		
		log.info("World " + s + " loaded.");
	}

	@Override
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		log.info("Finished loading!");
	}
	
	@Override
	public void onDisable(){

		for(TimedWorld s : wm.getWorlds()){
			ConfigurationSection players  = conf.getConfigurationSection("worlds." + s.getWorld() + ".players");
			if(players == null)
				players = conf.createSection("worlds." + s.getWorld() + ".players");
			
			log.info("Saving world " + s.getWorld());
			s.save(players);
			
		}
		log.info("Unloaded.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
		if(args.length < 1 || getServer().getWorld(args[0]) == null)
			return false;
		
		ConfigurationSection cs = conf.getConfigurationSection("worlds").createSection(args[0]);
		cs.addDefault("limit", 15);
		cs.addDefault("interval", 60);
		cs.addDefault("default_world", "world");
		
		
		
		try {
			conf.save(this.getDataFolder().getPath() + File.separator + "config.yml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info("World " + args[0] + " added.");
		
		return true;
	}
	
	public TimedWorldManager getTimedWorldManager(){
		return wm;
	}

}
