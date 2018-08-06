package de.blockartists.lootify;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Lootify extends JavaPlugin {
	
	private Logger log;
	private Material lootboxItem = Material.CHEST;
	
	@Override
	public void onEnable() {
		log = Bukkit.getLogger();
		
		getServer().getPluginManager().registerEvents(new LootifyListener(this), this);
		
		log.info("Lootify enabled");
	}
	
	@Override
	public void onDisable() {
		log.info("Lootify disabled");
	}
	
	public Material getLootboxItem() {
		return this.lootboxItem;
	}
}
