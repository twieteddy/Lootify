package de.blockartists.lootify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Lootify extends JavaPlugin {
	private LootifyLogger log = null;
	
	private LootifyConfig configYml = null;
	private LootifyConfig lootboxesYml = null;
	private ItemsConfig itemsConfig = null;
	
	private Map<String, Lootbox> lootboxes = null;
	
	@Override
	public void onEnable() {
		log = new LootifyLogger(this.getName());
		
		configYml = new LootifyConfig(this, "config.yml");
		lootboxesYml = new LootifyConfig(this, "lootboxes.yml");
		itemsConfig = new ItemsConfig(this);
		
		lootboxes = loadLootboxes();
		
		getServer().getPluginManager().registerEvents(new LootifyListener(this), this);
		getCommand("lootify").setExecutor(new LootifyCommandExecutor(this));
	}
	
	@Override public void onDisable() {}
	
	private HashMap<String, Lootbox> loadLootboxes() {
		YamlConfiguration lootboxesCfg = lootboxesYml.getConfig();
		HashMap<String, Lootbox> loot = new HashMap<>();
		
		for (String box : lootboxesCfg.getKeys(false)) {
			// Create a config section from lootbox keyname
			ConfigurationSection lootboxCfg = lootboxesCfg.getConfigurationSection(box);
			
			// Get info from lootbox config
			String identifier = lootboxCfg.getString("identifier", "");
			String message = lootboxCfg.getString("message", "");
			List<String> items = lootboxCfg.getStringList("items");

			// Make sure that identifier is not empty
			if (identifier.isEmpty()) {
				log.info("Skipping lootbox " + box + ". Identifier is empty");
				continue;
			}
			
			// Continue with next box if index already exists
			if (loot.containsKey(identifier)) {
				log.info("Skipping lootbox " + box + ". Identifier already exists");
				continue;
			}
			
			// Check if lootbox has items
			if (items.isEmpty()) {
				log.info("Skipping lootbox " + box + ". It has no items");
				continue;
			}
			
			// Create new lootbox after sanity checks
			loot.put(identifier, new Lootbox(this, identifier, message, items));
		}
		
		return loot;
	}
		
	public LootifyLogger getLootifyLogger() { return this.log; }
	public LootifyConfig getConfigYml() { return this.configYml; }
	public LootifyConfig getLootboxesYml() { return this.lootboxesYml; }
	public ItemsConfig getItemsConfig() { return this.itemsConfig; }
	public Map<String, Lootbox> getLootboxes() { return this.lootboxes; }
}
