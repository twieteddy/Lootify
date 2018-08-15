package de.blockartists.lootify;

import java.io.File;
import java.io.InputStreamReader;
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
	
	private Logger log = Bukkit.getLogger();
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
			YamlConfiguration configFromJar = 
					YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("config.yml")));
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
			lootboxes.put(replaceFormat(prefix), lootbox);
			info("Lootbox " + name + " added");
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
				Lootbox.addBlueprint(pool, name, boxItem);
								
				//this.items.get(pool).put(itemName,  itemStack);
				info("Item " + itemKey + " added to pool " + pool);
			}			
		}
	}
	
	public void info(String msg) {
		log.info("[" + this.getName() + "] " + msg);
	}
	
	public void severe(String msg) {
		log.severe("[" + this.getName() + "] " + msg);
	}
	
	public Map<String, Lootbox> getLootboxes() {
		return this.lootboxes;
	}
		
	// Replaces essentials text format 
	public static String replaceFormat(String s) {
		return s.replace("&0", "§0")
				.replace("&1", "§1")
				.replace("&2", "§2")
				.replace("&3", "§3")
				.replace("&4", "§4")
				.replace("&5", "§5")
				.replace("&6", "§6")
				.replace("&7", "§7")
				.replace("&8", "§8")
				.replace("&9", "§9")
				.replace("&a", "§a")
				.replace("&b", "§b")
				.replace("&c", "§c")
				.replace("&d", "§d")
				.replace("&e", "§e")
				.replace("&f", "§f")
				.replace("&l", "§l")
				.replace("&n", "§m")
				.replace("&o", "§o")
				.replace("&k", "§k")
				.replace("&m", "§m")
				.replace("&r", "§r");
	}
}
