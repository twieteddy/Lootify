package de.blockartists.lootify;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;
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
		
	public ItemsConfig(Lootify lootify) {
		this.lootify = lootify;
		this.log = lootify.getLootifyLogger();
		this.cache = new HashMap<>();
	}
	
	/**
	 * Returns a file resource by item uri (e.g. examplepool.exampleitem)
	 * @param name
	 * @return
	 */
	private File getFileByUri(String uri) {
		String path = lootify.getDataFolder().getPath()
				+ File.separator 
				+ SUBDIRECTORY 
				+ File.separator
				+ uri.replace(".", File.separator)
				+ FILE_EXTENSION;
		
		File file = new File(path);
		return (file.exists()) ? file : null;
	}

	/**
	 * Returns all regular *.yml files from directory by uri (e.g. examplepool.subpool)
	 * @param path
	 * @return
	 */
	private List<File> getFilesInDirectory(String path) {
		String uri = lootify.getDataFolder().getPath()
				+ File.separator 
				+ SUBDIRECTORY 
				+ File.separator
				+ path.replace(".", File.separator);			
		
		File file = new File(uri);
		List<File> files = (List<File>) FileUtils.listFiles(
				file, 
				new WildcardFileFilter("*" + FILE_EXTENSION), 
				null);
		
		return files;
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	private File getRandomDirectory(File file) {
		
		return new File("");
	}
	
	/**
	 * Returns a random file in directory
	 * @return
	 */
	private File getRandomFile() {
		
		return new File("");
	}
	
	/**
	 * Get LootboxItem by uri
	 * @param name
	 * @return
	 */
	public LootboxItem getItem(String uri) {
		// Return cached item
		if (cache.containsKey(uri)) {
			return cache.get(uri);
		}
		
		YamlConfiguration itemConfig = YamlConfiguration.loadConfiguration(this.getFileByUri(uri));
		
		LootboxItem item = new LootboxItem(
				itemConfig.getItemStack("item"), 
				itemConfig.getInt("weight"));
		
		// Cache file for later use
		cache.put(uri, item);
		
		return item;
	}
	
	
	/**
	 * Creates a new item
	 * @param uri
	 * @param itemStack
	 * @param weight
	 * @return
	 */
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
			log.severe("Couldn't save " + file.getPath());
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Deltes a file by uri
	 * @param itemUri
	 * @return
	 */
	public boolean deleteItem(String uri) {
		File file = this.getFileByUri(uri);				
		
		if (file != null && !file.exists()) {
			log.info("File doesn't exist: " + file.getPath());
			return false;
		}
		
		if (!file.delete()) {
			log.info("Can't delete file " + file.getPath());
			return false;
		}
		
		return true;
	}
	
}



/*
for (int i = 0; i < steps.length; i++) {

	if (steps[i].equals("?")) {	// Replace ? with a random file or directory
		
		File file = new File(uri.toString());
		// If question mark isn't in the last position, treat it as a directory
		if (i < steps.length-1) {
			// Has to be tested
			List<File> dirs = Arrays.asList(file.listFiles(File::isDirectory));
			File random = dirs.get(ThreadLocalRandom.current().nextInt(dirs.size()));
			uri.append(random.getName());
		} else { // Else treat it was a file
			List<File> files = (List<File>) FileUtils.listFiles(
					file.getAbsoluteFile(),
					new WildcardFileFilter("*" + FILE_EXTENSION), 
					null);
			File random = files.get(ThreadLocalRandom.current().nextInt(files.size()));
			steps[i] = FilenameUtils.removeExtension(random.getName());
			uri.append(steps[i]);
		}
		
	} else {
		uri.append(steps[i]);
	}
	
	
	// Append file separator or file extension
	if (i < (steps.length - 1)) {
		uri.append(File.separator);
	} else {
		uri.append(FILE_EXTENSION); 
	}
}*/