package de.blockartists.lootify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ItemManager {
	private Lootify lootify;
	private Map<String, LootboxItem> cache;
	
	private static String SUBDIRECTORY = "items";
	private static String FILE_EXTENSION = ".yml";
	
	
	
	public ItemManager(Lootify lootify) {
		this.lootify = lootify;
		this.cache = new HashMap<>();
	}
	
	
	
	private File getFileByUri(String uri) {
		String path = lootify.getDataFolder().getPath()
				+ File.separator 
				+ SUBDIRECTORY 
				+ File.separator
				+ uri.replace(".", File.separator)
				+ FILE_EXTENSION;
		
		return new File(path);
	}
	

	
	private LootboxItem getRandomItemFromPool(String uri) {
		String path = lootify.getDataFolder().getPath()
				+ File.separator
				+ SUBDIRECTORY
				+ File.separator
				+ uri.replace(".", File.separator);
			
		File file = new File(path);
		
		if (!file.exists()) {
			file.mkdirs();
		}
		
		List<File> files = (List<File>) FileUtils.listFiles(
				file,
				new WildcardFileFilter("*" + FILE_EXTENSION), 
				null);
	
		List<LootboxItem> items = new ArrayList<>();
		for (File f : files) {
			String newUri = uri + "." + FilenameUtils.removeExtension(f.getName());
			items.add(getItem(newUri));
		}

		int maxWeight = items.stream().mapToInt(LootboxItem::getWeight).sum();
		int random = ThreadLocalRandom.current().nextInt(maxWeight);
		int lifted = 0;
		
		LootboxItem randomItem = null;
		Iterator<LootboxItem> iter = items.iterator();
		while (iter.hasNext()) {
			LootboxItem item = iter.next(); 
			
			if ((random >= lifted) && (random < lifted + item.getWeight())) {
				randomItem = item;
				break;
			} else {
				lifted += item.getWeight();
			}		
		}
		
		return randomItem;
	}
	

	
	public LootboxItem getItem(String uri) {	
		String[] fragments = uri.split("\\.");
		String lastFragment = fragments[fragments.length - 1];
		
		if (lastFragment.equals("?")) {
			String poolUri = String.join(".", Arrays.copyOfRange(fragments, 0, fragments.length - 1));
			return getRandomItemFromPool(poolUri);
		}
		
		// Return cached item
		if (cache.containsKey(uri)) {
			return cache.get(uri);
		}
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(this.getFileByUri(uri));
		LootboxItem item = new LootboxItem(
			config.getItemStack("item", new ItemStack(Material.AIR)), 
			config.getInt("weight", 0));
	
		cache.put(uri, item);
		return item;		
	}
	
	
	
	public boolean createItem(String uri, ItemStack itemStack, int weight) {		
		File file = this.getFileByUri(uri);		
		File parent = file.getParentFile();
		
		if (!parent.exists() && !parent.mkdirs()) {
			throw new IllegalStateException("Couldn't create directory: " + parent);
		}
				
		YamlConfiguration itemConfig = YamlConfiguration.loadConfiguration(file);
		itemConfig.set("weight", weight);
		itemConfig.set("item", itemStack);

		try {
			itemConfig.save(file);
		} catch (IOException e) {
			lootify.getLogger().severe("Couldn't save " + file.getPath());
			return false;
		}
		return true;
	}
	
	
	
	public boolean deleteItem(String uri) {
		File file = this.getFileByUri(uri);				
		
		if (file != null && !file.exists()) {
			lootify.getLogger().info("File doesn't exist: " + file.getPath());
			return false;
		}
		
		if (!file.delete()) {
			lootify.getLogger().info("Can't delete file " + file.getPath());
			return false;
		}
		
		return true;
	}
	
	public void clearCache() {
		cache.clear();
	}
	
}