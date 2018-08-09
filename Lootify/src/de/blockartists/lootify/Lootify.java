package de.blockartists.lootify;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Lootify extends JavaPlugin {
	
	private Logger log = Bukkit.getLogger();
	private FileConfiguration config = this.getConfig();
	private List<Lootbox> lootboxCollection = new ArrayList<Lootbox>();
	
	@Override
	public void onEnable() {	
		config.addDefault("category.common", "%");
		config.addDefault("category.rare",  "30");
		config.addDefault("category.epic",  "15");
		config.addDefault("category.legendary",  "5");

		config.addDefault("lootbox.box1.prefix", "§l§b§1§r");
		config.addDefault("lootbox.box1.name", "§c§lLootbox");
		config.addDefault("lootbox.box1.material", "CHEST");
	
		config.addDefault("items.item1.name", "§b§lDiamant!");
		config.addDefault("items.item1.lore", "§7Der ist ganz besonders");
		config.addDefault("items.item1.material", "DIAMOND");
		config.addDefault("items.item1.amount", 3);
		config.addDefault("items.item1.category", "epic");
		
		config.addDefault("items.item2.name", "§b§lDiamant-Schwert");
		config.addDefault("items.item2.lore", "§7Der ist ganz besonders");
		config.addDefault("items.item2.material", "DIAMOND_SWORD");
		config.addDefault("items.item2.amount", 1);
		config.addDefault("items.item2.category", "legendary");
		
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
	
	public List<Lootbox> getLootboxes() {
		return this.lootboxCollection;
	}
	
}
