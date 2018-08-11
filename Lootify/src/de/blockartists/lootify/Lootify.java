package de.blockartists.lootify;

import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Lootify extends JavaPlugin {
	
	private Logger log = Bukkit.getLogger();
	private FileConfiguration config = this.getConfig();
	private HashMap<String, Lootbox> lootboxMap = new HashMap<>();
	private HashMap<String, HashMap<String, ItemStack>> itemMap = new HashMap<String, HashMap<String, ItemStack>>();
	
	@Override
	public void onEnable() {
		loadConfig();
		loadItemConfig();
		loadLootboxConfig();
		getServer().getPluginManager().registerEvents(new LootifyListener(this), this);
	}
	
	
	@Override
	public void onDisable() {
		
	}
	
	// TODO: Rewriting config loading
	private void loadConfig() {
		// Create filepointer for existence check
		File lootifyConfig = new File("plugins/Lootify/config.yml");
		
		// Create default config file from internal config.yml resource if needed
		if (!lootifyConfig.exists()) {
			YamlConfiguration configFromJar = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("config.yml")));
			this.config.setDefaults(configFromJar);
			this.config.options().copyDefaults(true);
			this.saveConfig();
		} 
	}
	
	
	
	private void loadLootboxConfig() {
		ConfigurationSection lootboxConfig = config.getConfigurationSection("lootbox");
		
		// If no lootbox section was found, something went horribly wrong!
		if (lootboxConfig == null) {
			logSevere("Couldn't find config for lootboxes");
			return;
		}
		
		for (String lootboxName : lootboxConfig.getKeys(false)) {
			// Create a config section from lootbox keyname
			ConfigurationSection currentLootboxConfig = lootboxConfig.getConfigurationSection(lootboxName);
			
			// Lootbox object which we are going to fill
			Lootbox lootbox = new Lootbox();
			
			// Get info from lootbox config
			String prefix = currentLootboxConfig.getString("prefix");
			String name = currentLootboxConfig.getString("name");
			String message = currentLootboxConfig.getString("message");
			List<String> items = currentLootboxConfig.getStringList("items");
			
			// Prefix is needed for identifying lootbox. Must be unique
			if (prefix != null && !prefix.isEmpty() && !lootboxMap.containsKey(lootboxName)) {
				lootbox.setPrefix(prefix);
			} else {
				logInfo("Skipping lootbox " + lootboxName);
				continue;
			}
			
			// Setting optional display name for lootbox shown in the upper left corner
			if (name != null && !name.isEmpty()) {
				lootbox.setName(name);
			}
			
			// Setting optional personal player text message on opening lootbox
			if (message != null && !message.isEmpty()) {
				lootbox.setMessage(message);
			}
			
			// Start loading items into lootbox
			if (items != null) {
				// itemBreadcrumb should look like this: "custom.votediamonds" or "random.?"
				for (String itemBreadcrumb : items) {
					String [] itemPath = itemBreadcrumb.split(".");
					
					// Path to item must have the length of 2
					if (itemPath.length != 2) {
						logInfo("Can't find item " + itemBreadcrumb);
						continue;
					}
					
					// Add possible item to lootbox
					lootbox.addItem(itemMap.get(itemPath[0]).get(itemPath[1]));
				}
			}
		}
	}
	
	private void loadItemConfig() {
		
	}
	
	// TODO: Delete
	/*private void loadLootboxes() {
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
	}*/
	
	
	
	public void logInfo(String msg) {
		log.info("[" + this.getName() + "] " + msg);
	}
	
	public void logSevere(String msg) {
		log.severe("[" + this.getName() + "] " + msg);
	}
	
	public HashMap<String, Lootbox> getLootboxes() {
		return this.lootboxMap;
	}
	
}
