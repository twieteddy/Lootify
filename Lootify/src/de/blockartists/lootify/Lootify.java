package de.blockartists.lootify;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Lootify extends JavaPlugin {
	
	private Logger log = Bukkit.getLogger();
	private FileConfiguration config = this.getConfig();
	private ArrayList<Lootbox> lootboxCollection = new ArrayList<Lootbox>();
	
	// TODO: Implement via config file
	private Material lootboxItem = Material.CHEST;
	private String lootboxPrefix = "§6§l";
	private String lootboxName = "§6§lLootbox";
	
	@Override
	public void onEnable() {
		// TODO: Config
		config.options().copyDefaults(true);
		saveConfig();
		
		getServer().getPluginManager().registerEvents(new LootifyListener(this), this);
		log("enabled");
	}
	
	@Override
	public void onDisable() {
		log("disabled");
	}
	
	public void log(String msg) {
		log.info("[" + this.getName() + "] " + msg);
	}
	
	public Material getLootboxItem() {
		return this.lootboxItem;
	}
	
	public String getLootboxPrefix() {
		return this.lootboxPrefix;
	}
	
	public String getLootboxName() {
		return this.lootboxName;
	}
}
