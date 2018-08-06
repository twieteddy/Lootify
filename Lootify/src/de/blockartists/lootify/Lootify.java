package de.blockartists.lootify;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Lootify extends JavaPlugin {
	
	private Logger log = Bukkit.getLogger();
	private Material lootboxItem = Material.CHEST;
	private String lootboxPrefix = "§6§l";
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new LootifyListener(this), this);
		log.info(this.getName() + " enabled");
	}
	
	@Override
	public void onDisable() {
		log.info(this.getName() + "disabled");
	}
	
	public Material getLootboxItem() {
		return this.lootboxItem;
	}
	
	public String getLootboxPrefix() {
		return this.lootboxPrefix;
	}
}
