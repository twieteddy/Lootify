package de.blockartists.lootify;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
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
	private Map<String, Lootbox> lootboxes = new HashMap<>();
	private Map<String, Map<String, ItemStack>> itemMap = new HashMap<>();
	
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
			info("Lootbox " + name + " added");
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
		return this.lootboxes;
	}
	
	// Replaces essentials 
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
