package de.blockartists.lootify;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class LootifyLogger {
	private Logger log = Bukkit.getLogger(); 
	private String name;
	
	public LootifyLogger(String name) { 
		this.name = name;
	}
	
	public void info(String msg) {
		log.info("[" + this.name + "] " + msg);
	}
	
	public void severe(String msg) {
		log.severe("[" + this.name + "] " + msg);
	}
}
