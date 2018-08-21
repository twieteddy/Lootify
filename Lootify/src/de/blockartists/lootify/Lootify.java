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
	private LootifyConfig lootboxesYml = null;
	private ItemsConfig itemsConfig = null;
	
	private Map<String, Lootbox> lootboxes = null;
	
	@Override
	public void onEnable() {
		log = new LootifyLogger(this.getName());
		
		configYml = new LootifyConfig(this, "config.yml");
		lootboxesYml = new LootifyConfig(this, "lootboxes.yml");
		itemsConfig = new ItemsConfig(this);
		
		lootboxes = new HashMap<>();
				
		addItemExample();
		addLootboxExample();
		loadLootboxes();
		
		log.info(lootboxes.size() + " lootboxes added");
		
		getServer().getPluginManager().registerEvents(new LootifyListener(this), this);
		getCommand("lootify").setExecutor(new LootifyCommandExecutor(this));
	}
	
	@Override public void onDisable() {}
	
	// Temporary
	private void addItemExample() {
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
			
		
		itemsConfig.createItem("examplepool.exampleaxe", itemStack, 10);
		itemsConfig.createItem("examplepool.exampleaxe2", itemStack, 15);
		itemsConfig.createItem("examplepool.exampleaxe3", itemStack, 20);
		itemsConfig.createItem("rootaxe", itemStack, 1);
		
		itemsConfig.deleteItem("examplepool.exampleaxe3");
	}
	
	
	// Temporary
	private void addLootboxExample() {
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

			// Make sure that identifier is not empty
			if (identifier.isEmpty()) {
				log.info("Skipping lootbox " + box + ". Identifier is empty");
				continue;
			}
			
			// Continue with next box if index already exists
			if (lootboxes.containsKey(identifier)) {
				log.info("Skipping lootbox " + box + ". Identifier already exists");
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
		
	public LootifyLogger getLootifyLogger() { return this.log; }
	public LootifyConfig getConfigYml() { return this.configYml; }
	public LootifyConfig getLootboxesYml() { return this.lootboxesYml; }
	public ItemsConfig getItemsConfig() { return this.itemsConfig; }
	public Map<String, Lootbox> getLootboxes() { return this.lootboxes; }
}
