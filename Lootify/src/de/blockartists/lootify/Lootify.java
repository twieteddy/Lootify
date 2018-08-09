package de.blockartists.lootify;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Lootify extends JavaPlugin {
	
	private Logger log = Bukkit.getLogger();
	private FileConfiguration config = this.getConfig();
	private HashMap<String, Lootbox> lootboxCollection = new HashMap<>();
	private HashMap<String, LootboxItem> lootboxItemCollection = new HashMap<>();
	
	@Override
	public void onEnable() {	
		config.addDefault("category.common", "%");
		config.addDefault("category.rare",  "30");
		config.addDefault("category.epic",  "15");
		config.addDefault("category.legendary",  "5");

		config.addDefault("lootboxes.box1.prefix", "�l�b�1�r");
		config.addDefault("lootboxes.box1.name", "�c�lLootbox");
	
		config.addDefault("items.item1.name", "�b�lDiamant!");
		config.addDefault("items.item1.lore", "�7Der ist ganz besonders");
		config.addDefault("items.item1.material", "DIAMOND");
		config.addDefault("items.item1.amount", 3);
		config.addDefault("items.item1.category", "epic");
		
		config.addDefault("items.item2.name", "�b�lDiamant-Schwert");
		config.addDefault("items.item2.lore", "�7Der ist ganz besonders");
		config.addDefault("items.item2.material", "DIAMOND_SWORD");
		config.addDefault("items.item2.amount", 1);
		config.addDefault("items.item2.category", "legendary");
		
		config.options().copyDefaults(true);
		saveConfig();
		
		// Create list of lootboxes
		ConfigurationSection lootboxConfig = config.getConfigurationSection("lootboxes");
		for (String key : lootboxConfig.getKeys(false)) {
			ConfigurationSection currentLootbox = lootboxConfig.getConfigurationSection(key);
			
			Lootbox lootbox = new Lootbox(
					currentLootbox.getString("prefix"), 
					currentLootbox.getString("name"), 
					currentLootbox.getString("textOnOpening"));
			
			if (lootboxCollection.containsKey(key))
				log("Lootbox " + key + " already exists");
			else
				lootboxCollection.put(currentLootbox.getString("prefix"), lootbox);
		}
		
		// Create List of items
		ConfigurationSection lootboxItemConfig = config.getConfigurationSection("items");
		for (String key : lootboxItemConfig.getKeys(false)) {
			ConfigurationSection currentLootboxItem = lootboxItemConfig.getConfigurationSection(key);			
			LootboxItem item = new LootboxItem(
					currentLootboxItem.getString("name"),
					currentLootboxItem.getString("lore"),
					Material.valueOf(currentLootboxItem.getString("material")),
					currentLootboxItem.getInt("amount"),
					currentLootboxItem.getString("category"));
			
			if (lootboxItemCollection.containsKey(key)) 
				log("LootboxItem " + key + " already exists");
			else 
				lootboxItemCollection.put(key, item);
		}
			
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
	
	public HashMap<String, Lootbox> getLootboxes() {
		return this.lootboxCollection;
	}
	
}
