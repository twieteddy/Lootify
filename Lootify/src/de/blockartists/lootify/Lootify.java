package de.blockartists.lootify;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Lootify extends JavaPlugin {
	private LootifyLogger log = null;
	
	private LootifyConfig configYml = null;
	private LootifyConfig itemsYml = null;
	private LootifyConfig lootboxesYml = null;
	
	private Map<String, Lootbox> lootboxes = null;
	private Map<String, Map<String, LootboxItem>> items = null;
	

	@Override
	public void onEnable() {
		log = new LootifyLogger(this.getName());
		
		configYml = new LootifyConfig(this, "config.yml");
		itemsYml = new LootifyConfig(this, "items.yml");
		lootboxesYml = new LootifyConfig(this, "lootboxes.yml");
		
		lootboxes = new HashMap<>();
		items = new HashMap<>();
		
		addDefaultItemExample();
		addDefaultLootboxExample();
		
		loadLootboxes();
		loadItems();
		
		getServer().getPluginManager().registerEvents(new LootifyListener(this), this);
		getCommand("lootify").setExecutor(new LootifyCommandExecutor(this));
	}
	
	@Override public void onDisable() {}
	
	private void addDefaultItemExample() {
		ItemStack itemStack = new ItemStack(Material.DIAMOND_AXE);
			itemStack.setAmount(1);
			itemStack.addEnchantment(Enchantment.SILK_TOUCH, 1);
			itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			itemStack.setDurability(itemStack.getDurability());		
		
		ItemMeta itemMeta = itemStack.getItemMeta();		
			itemMeta.setDisplayName("§2§lBeispiel-Axt");
			itemMeta.setLore(Arrays.asList("§aDas dient nur", "§aals Beispiel"));
			itemMeta.setUnbreakable(true);
			itemMeta.addEnchant(Enchantment.DIG_SPEED, 5, false);
			itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
			itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		
		itemStack.setItemMeta(itemMeta);
			
		itemsYml.getConfig().addDefault("examplepool.exampleaxe", itemStack);
		itemsYml.copyDefaults(true);
		itemsYml.save();
		
		configYml.getConfig().addDefault("examplepool.exampleaxe", 1);
		configYml.copyDefaults(true);
		configYml.save();
	}
	
	private void addDefaultLootboxExample() {
		String identifier = "§l§b§1§r";
		String message = "§6Du hast eine §eVotebox§6 geöffnet!";
		List<String> items = new java.util.ArrayList<>(Arrays.asList("examplepool.exampleaxe"));
		
		lootboxesYml.getConfig().addDefault("examplebox.identifier", identifier);
		lootboxesYml.getConfig().addDefault("examplebox.message", message);
		lootboxesYml.getConfig().addDefault("examplebox.items", items);
		
		lootboxesYml.copyDefaults(true);
		lootboxesYml.save();
	}
	
	private void loadLootboxes() {
		YamlConfiguration lootboxesCfg = lootboxesYml.getConfig();
		
		for (String box : lootboxesCfg.getKeys(false)) {
			// Create a config section from lootbox keyname
			ConfigurationSection lootboxCfg = lootboxesCfg.getConfigurationSection(box);
			
			// Get info from lootbox config
			String identifier = lootboxCfg.getString("identifier", "");
			String message = lootboxCfg.getString("message", "");
			List<String> items = lootboxCfg.getStringList("items");

			// Prefix needed as id
			if (identifier.isEmpty()) {
				log.info("Skipping lootbox " + box + ". Prefix is empty");
				continue;
			}
			
			// Continue with next box if prefix already exists
			if (lootboxes.containsKey(identifier)) {
				log.info("Skipping lootbox " + box + ". Key already exists");
				continue;
			}
			
			// Check if lootbox has items
			if (items.isEmpty()) {
				log.info("Skipping lootbox " + box + ". It has no items");
				continue;
			}
			
			// Create new lootbox after sanity checks
			lootboxes.put(identifier, new Lootbox(this, identifier, message, items));
		}
	}
	
	private void loadItems() {
		YamlConfiguration itemsCfg = itemsYml.getConfig();
		YamlConfiguration configCfg = configYml.getConfig();
				
		// Deserialize item stack into hashmap
		for (String pool : itemsCfg.getKeys(false)) {

			// Get pool name
			ConfigurationSection poolCfg = itemsCfg.getConfigurationSection(pool);
			
			// Create pool in hashmap
			items.put(pool, new HashMap<String, LootboxItem>()); 
			
			// Fill pool with items
			for (String itemKey : poolCfg.getKeys(false)) {
				int weight = configCfg.getInt(pool + "." + itemKey, 1);
				LootboxItem item = new LootboxItem(poolCfg.getItemStack(itemKey), weight);
				items.get(pool).put(itemKey, item);
			}
		}
	}
		
	public LootifyLogger getLootifyLogger() { return this.log; }
	public Map<String, Lootbox> getLootboxes() { return this.lootboxes; }
	public Map<String, Map<String, LootboxItem>> getItems() { return this.items; }
}
