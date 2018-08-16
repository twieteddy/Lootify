package de.blockartists.lootify;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Lootify extends JavaPlugin {
	
	private Logger logger = Bukkit.getLogger();
	private FileConfiguration config = this.getConfig();
	private Map<String, Lootbox> lootboxes = new HashMap<>();
	
	@Override
	public void onEnable() {
		loadConfig();
		loadItems();
		loadLootboxes();
		getServer().getPluginManager().registerEvents(new LootifyListener(this), this);		
	}
	
	@Override public void onDisable() {}
	
	private void loadConfig() {
		// Create filepointer for existence check
		File lootifyConfig = new File("plugins/Lootify/config.yml");
		
		// Create default config file from internal config.yml resource if needed
		if (!lootifyConfig.exists()) {
			YamlConfiguration configFromJar = YamlConfiguration.loadConfiguration(
					new InputStreamReader(
							this.getResource("config.yml"),
							Charset.forName("UTF-8")
							));
			this.config.setDefaults(configFromJar);
			this.config.options().copyDefaults(true);
			this.saveConfig();
		} 
	}
	
	private void loadLootboxes() {
		ConfigurationSection lootboxConfig = config.getConfigurationSection("lootbox");
		
		// If no lootbox section was found, something went horribly wrong!
		if (lootboxConfig == null) {
			severe("Couldn't find config for lootboxes");
			return;
		}
		
		for (String boxKey : lootboxConfig.getKeys(false)) {
			// Create a config section from lootbox keyname
			ConfigurationSection currentLootboxConfig = lootboxConfig.getConfigurationSection(boxKey);
			
			// Get info from lootbox config
			String prefix = currentLootboxConfig.getString("prefix");
			String name = currentLootboxConfig.getString("name");
			String message = currentLootboxConfig.getString("message");
			List<String> items = currentLootboxConfig.getStringList("items");
			
			// Continue with next box if prefix already exists
			if (prefix == null || prefix.isEmpty() || lootboxes.containsKey(prefix)) {
				info("Lootbox " + boxKey + " already exists");
				continue;
			}
			
			// Check if lootbox has items
			if (items == null || items.size() == 0) {
				info("Lootbox " + boxKey + " has no items");
				continue;
			}
			
			// Create new lootbox after sanity checks
			Lootbox lootbox = new Lootbox(prefix, name, message, items);
			lootboxes.put(prefix, lootbox);
			info("Lootbox " + boxKey + " added");
		}
	}
	
	private void loadItems() {
		ConfigurationSection itemsConfig = config.getConfigurationSection("items");
		
		// No items? Well, that's bad.
		if (itemsConfig == null) {
			severe("Couldn't find config section for items");
			return;
		}
		
		// Get all item pools from item config.
		for (String pool : itemsConfig.getKeys(false)) {			
			ConfigurationSection poolConfig = itemsConfig.getConfigurationSection(pool);

			for (String itemKey : poolConfig.getKeys(false)) {
				ConfigurationSection itemConfig = poolConfig.getConfigurationSection(itemKey);
				
				String name = itemConfig.getString("name");
				List<String> lore = itemConfig.getStringList("lore");
				String material = itemConfig.getString("material");
				int amount = itemConfig.getInt("amount");
				int weight = itemConfig.getInt("weight");
				
				// Create new lootbox blueprint and add it
				LootboxItem boxItem = new LootboxItem(name, lore, material, amount, weight);
				Lootbox.addBlueprint(pool, itemKey, boxItem);
								
				info("Item " + itemKey + " added to pool " + pool);
			}			
		}
	}
	
	public void info(String msg) {
		logger.info("[" + this.getName() + "] " + msg);
	}
	
	public void severe(String msg) {
		logger.severe("[" + this.getName() + "] " + msg);
	}
	
	public Map<String, Lootbox> getLootboxes() {
		return this.lootboxes;
	}
}
