package de.blockartists.lootify;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Lootbox {
	private Lootify lootify = null;
	private String identifier = null;
	private String message = null;
	private List<String> items = new ArrayList<>();
	
	
	public Lootbox(Lootify lootify, String identifier, String message, List<String> items) {
		this.lootify = lootify;
		this.identifier = identifier;
		this.message = message;
		this.items = items;
	}
	
	public String getIdentifier() { return this.identifier; } 
	public String getMessage() { return this.message; }
	
	public Inventory createInventory(String displayName) {
		Inventory inventory = Bukkit.createInventory(null,  9, displayName);
			
		for (String uri : items) {
			inventory.addItem(lootify.getItemManager().getItem(uri).getItemStack());
		}
		
		return inventory;
	}
	
	public void saveLootbox(String name) {
		lootify.getLootboxesYml().getConfig().set(name + ".identifier", identifier);
		lootify.getLootboxesYml().getConfig().set(name + ".message", message);
		lootify.getLootboxesYml().getConfig().set(name + ".items", items);
		lootify.getLootboxesYml().save(); 
	}
	
}