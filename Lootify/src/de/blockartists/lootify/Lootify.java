package de.blockartists.lootify;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Lootify extends JavaPlugin {
	
	private Logger log = Bukkit.getLogger();
	private FileConfiguration config = this.getConfig();
	private Map<String, Lootbox> lootboxMap = new HashMap<>();
	private Map<String, Map<String, ItemStack>> itemMap = new HashMap<>();
	private Map<String, Integer> itemWeightMap = new HashMap<String, Integer>();
	
	@Override
	public void onEnable() {
		loadConfig();
		loadItems();
		loadLootboxes();
		getServer().getPluginManager().registerEvents(new LootifyListener(this), this);
	}
	
	
	@Override
	public void onDisable() {
		
	}
	
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
	
	
	
	private void loadLootboxes() {
		ConfigurationSection lootboxConfig = config.getConfigurationSection("lootbox");
		
		// If no lootbox section was found, something went horribly wrong!
		if (lootboxConfig == null) {
			severe("Couldn't find config for lootboxes");
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
			
			// Prefix is needed to identify lootbox. Must be unique
			if (prefix != null && !prefix.isEmpty() && !lootboxMap.containsKey(lootboxName)) {
				lootbox.setPrefix(prefix);
			} else {
				info("Skipping lootbox " + lootboxName);
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
				// itemFQDN should look like this: "custom.votediamonds" or "random.?"
				for (String itemFQDN : items) {
					String [] path = itemFQDN.split("\\.");
					
					// Path to item must have the length of 2
					if (path.length != 2) {
						info("Can't find item " + itemFQDN);
						continue;
					}
					
					lootbox.addItem(itemMap.get(path[0]).get(path[1]));
				}
			}
		}
	}
	
	private void loadItems() {
		ConfigurationSection itemsConfig = config.getConfigurationSection("items");
		
		// No items? Well, that's bad.
		if (itemsConfig == null) {
			severe("Couldn't find config for items");
			return;
		}
		
		
		// Get all item pools from item config.
		for (String pool : itemsConfig.getKeys(false)) {
			// Get pool config
			ConfigurationSection poolConfig = itemsConfig.getConfigurationSection(pool);
			
			// Create a new HashMap for items
			this.itemMap.put(pool, new LinkedHashMap<String, ItemStack>());
			info("Pool " + pool + " created");
			
			// Get every item from pool
			for (String itemName : poolConfig.getKeys(false)) {
				ConfigurationSection itemConfig = poolConfig.getConfigurationSection(itemName);
				
				String name = itemConfig.getString("name");
				String lore = itemConfig.getString("lore");
				String material = itemConfig.getString("material");
				int amount = itemConfig.getInt("amount");
				int weight = itemConfig.getInt("weight");
				
				// Check material
				if (material == null || Material.getMaterial(material.toUpperCase()) == null) {
					severe(String.format("Couldn't find material \"%s\" for item \"%s\"", material, itemName));
					continue;
				}
				
				// Adjust to stack size
				if (amount < 1) {
					amount = 1;
				} else if (amount > 64) {
					amount = 64;
				}
				
				// Item is creatable now
				ItemStack item = new ItemStack(Material.getMaterial(material.toUpperCase()), amount);

				// Check for custom name information and set if needed
				if (name != null && !name.isEmpty()) {
					item.getItemMeta().setDisplayName(name);
				}
				
				// Check for custom lore information and set if needed
				if (lore != null && !lore.isEmpty()) {
					item.getItemMeta().setLore(Arrays.asList(lore));
				}
				
				this.itemMap.get(pool).put(itemName,  item);
				info("Item " + itemName + " added to pool " + pool);
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
		return this.lootboxMap;
	}
	
}
