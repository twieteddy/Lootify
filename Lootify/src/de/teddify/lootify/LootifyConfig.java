package de.blockartists.lootify;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class LootifyConfig {
	private Lootify lootify = null;
	private YamlConfiguration config = null;
	private File file = null;
	private String filename = null;
	
	public LootifyConfig(Lootify lootify, String filename) {
		this.lootify = lootify;
		this.filename = filename;
		
		if (!lootify.getDataFolder().exists()) {
			lootify.getDataFolder().mkdir();
		}
		
		file = new File(lootify.getDataFolder(), filename);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				lootify.getLogger().severe("Couldn't create " + filename);
			}
		}		
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			lootify.getLogger().severe("Couldn't save " + filename);
		}
	}
	
	public void copyDefaults(boolean defaultValues) {config.options().copyDefaults(defaultValues);}
	public void reload() { config = YamlConfiguration.loadConfiguration(file); }
	public YamlConfiguration getConfig() { return this.config; }
}
