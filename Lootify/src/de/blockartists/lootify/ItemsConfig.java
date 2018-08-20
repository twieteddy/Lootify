package de.blockartists.lootify;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

// TODO: Find a better name
public class ItemsConfig {
	private Lootify lootify;
	private LootifyLogger log;
	
	private Map<String, LootboxItem> cache;
	
	private static String SUBDIRECTORY = "items";
	private static String FILE_EXTENSION = ".yml";
	
	public static enum CreateResult {
		SUCCESS,
		FILE_EXISTS,
		SAVE_FAILURE
	}
	
	public ItemsConfig(Lootify lootify) {
		this.lootify = lootify;
		this.log = lootify.getLootifyLogger();
		this.cache = new HashMap<>();
	}
	
	public LootboxItem getItem(String name) {

		StringBuilder filePath = new StringBuilder(
				lootify.getDataFolder().getPath()
				+ File.separator 
				+ SUBDIRECTORY 
				+ File.separator);
		String[] steps = name.split("\\.");
		
		// Build path
		// TODO: Work with weight
		for (int i = 0; i < steps.length; i++) {

			if (steps[i].equals("?")) {	// Replace ? with a random file or directory
				
				File file = new File(filePath.toString());
				// If question mark isn't in the last position, treat it as a directory
				if (i < steps.length-1) {
					// Has to be tested
					List<File> dirs = Arrays.asList(file.listFiles(File::isDirectory));
					File random = dirs.get(ThreadLocalRandom.current().nextInt(dirs.size()));
					filePath.append(random.getName());
				} else { // Else treat it was a file
					List<File> files = (List<File>) FileUtils.listFiles(
							file.getAbsoluteFile(),
							new WildcardFileFilter("*" + FILE_EXTENSION), 
							null);
					File random = files.get(ThreadLocalRandom.current().nextInt(files.size()));
					steps[i] = FilenameUtils.removeExtension(random.getName());
					filePath.append(steps[i]);
				}
				
			} else {
				filePath.append(steps[i]);
			}
			
			// Append file separator or file extension
			if (i < (steps.length - 1)) {
				filePath.append(File.separator);
			} else {
				filePath.append(FILE_EXTENSION); 
			}
		}
		
		// New item path after replaced ?
		String itemPath = String.join(".", steps);
		
		// Return cached item
		if (cache.containsKey(itemPath)) {
			System.out.println("Return cached " + itemPath);
			return cache.get(itemPath);
		}
		
		// Load from file if item is not cached
		File file = new File(filePath.toString());

		YamlConfiguration itemConfig = YamlConfiguration.loadConfiguration(file);
			ItemStack itemStack = itemConfig.getItemStack("item");
			int weight = itemConfig.getInt("weight");
		
		LootboxItem item = new LootboxItem(itemStack, weight);
		cache.put(itemPath, item);
		
		return item;
	}
	
	public CreateResult createItem(String name, ItemStack itemStack, int weight) {		
		File file = new File(lootify.getDataFolder().getPath() + File.separator 
				+ SUBDIRECTORY + File.separator
				+ name.replace(".", File.separator) + FILE_EXTENSION);
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
			return CreateResult.SAVE_FAILURE;
		}
		
		return CreateResult.SUCCESS;
	}
	
	// TODO: Make it safe
	public void deleteItem(String path) {
		path = path.replace(".", File.separator);
		
		File file = new File(lootify.getDataFolder().getPath() + File.separator 
				+ SUBDIRECTORY + File.separator 
				+ path + FILE_EXTENSION);				
		
		System.out.print("delete: " + file.getPath());
		
		if (file.exists()) {
			file.delete();
		}
	}
	
}
