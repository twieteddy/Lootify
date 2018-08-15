package de.blockartists.lootify;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Lootbox {
	private String name = null; // Displayed in the upper left corner of lootbox inventory
	private String prefix = null; // Prefix for items used to identify lootbox. Should be unique
	private String message = null; // Message displayed to you after opening the lootbox
	private List<String> items = new ArrayList<>(); // A path like <category>.<item>. <item> can be ? for random
	
	public Lootbox(String prefix, String name, String message, List<String> items) {
		this.prefix = Lootify.replaceFormat(prefix);
		
		if (name != null && !name.isEmpty())
			this.name = Lootify.replaceFormat(name);
		
		if (message != null && !message.isEmpty())
			this.message = Lootify.replaceFormat(message);
		
		if (items != null && items.size() > 0) 
			this.items = items;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPrefix() {
		return this.prefix;
	} 
	
	public String getMessage() {
		return this.message;
	}
		
	public Inventory createInventory() {
		Inventory inventory = Bukkit.createInventory(null,  9, this.getName());
		
		
		return inventory;
	}
	
	
	
}
