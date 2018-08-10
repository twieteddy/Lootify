package de.blockartists.lootify;

import java.io.File;
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
		createConfigDefaults();
		loadLootboxes();
		loadLootboxItems();
			
		getServer().getPluginManager().registerEvents(new LootifyListener(this), this);
	}
	
	
	@Override
	public void onDisable() {
		
	}
	
	/* 
	 * Creates the default settings for config.yml
	 */
	private void createConfigDefaults() {
		config.addDefault("category.common", "%");
		config.addDefault("category.rare",  "30");
		config.addDefault("category.epic",  "15");
		config.addDefault("category.legendary",  "5");

		// items need the following entries: prefix, name, textOnOpening (optional)
		config.addDefault("lootboxes", "");
		// items need the following entries: name, lore, material, amount, category
		config.addDefault("items", "");
		
		// If file doesn't exists, add example values
		if (!new File("plugins/Lootify/config.yml").exists()) {
			config.addDefault("lootboxes.box1.prefix", "�l�b�1�r");
			config.addDefault("lootboxes.box1.name", "�c�lLootbox");
		
			config.addDefault("items.item1.name", "�b�lDiamant!");
			config.addDefault("items.item1.lore", "�7Der ist ganz besonders");
			config.addDefault("items.item1.material", "DIAMOND");
			config.addDefault("items.item1.amount", 3);
			config.addDefault("items.item1.category", "epic");
		}
		
		// Save to disk
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	/*
	 * Load lootboxes from config.yml
	 */
	private void loadLootboxes() {
		ConfigurationSection lootboxConfig = config.getConfigurationSection("lootboxes");
		if (lootboxConfig != null) {
			for (String key : lootboxConfig.getKeys(false)) {
				ConfigurationSection currentLootbox = lootboxConfig.getConfigurationSection(key);
				Lootbox lootbox = new Lootbox(
						currentLootbox.getString("prefix"), 
						currentLootbox.getString("name"), 
						currentLootbox.getString("textOnOpening"));
				
				if (lootboxCollection.containsKey(key)) {
					logInfo("Lootbox " + key + " already exists");
				} else {
					lootboxCollection.put(currentLootbox.getString("prefix"), lootbox);
					logInfo("Lootbox " + key + " loaded");
				}
				
			}
		} else {
			logInfo("No lootboxes loaded");
		}
	}
	
	/*
	 * Load lootbox items from config.yml
	 */
	private void loadLootboxItems() {
		ConfigurationSection lootboxItemConfig = config.getConfigurationSection("items");
		if (lootboxItemConfig != null) {
			for (String key : lootboxItemConfig.getKeys(false)) {
				ConfigurationSection currentLootboxItem = lootboxItemConfig.getConfigurationSection(key);			
				LootboxItem item = new LootboxItem(
						currentLootboxItem.getString("name"),
						currentLootboxItem.getString("lore"),
						Material.valueOf(currentLootboxItem.getString("material")),
						currentLootboxItem.getInt("amount"),
						currentLootboxItem.getString("category"));
				
				if (lootboxItemCollection.containsKey(key)) {
					logInfo("LootboxItem " + key + " already exists");
				} else { 
					lootboxItemCollection.put(key, item);
				}
			}
		}
	}
	
	/* 
	 * Wrapper function for logger
	 */
	public void logInfo(String msg) {
		log.info("[" + this.getName() + "] " + msg);
	}
	
	/*
	 * Retrieve our lootbox hashmap from outside
	 */
	public HashMap<String, Lootbox> getLootboxes() {
		return this.lootboxCollection;
	}
	
}
