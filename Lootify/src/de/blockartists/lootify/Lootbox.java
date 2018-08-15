package de.blockartists.lootify;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Lootbox {
	private String name = null;
	private String prefix = null;
	private String message = null;
	private List<LootboxItem> items = new ArrayList<>();
	
	public Lootbox(String prefix, String name, String message) {
		this.prefix = prefix;
		
		if (name != null && !name.isEmpty())
			this.name = name;
		
		if (message != null && !message.isEmpty())
			this.message = message;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return this.prefix;
	} 
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public List<LootboxItem> getItems() {
		return this.items;
	}
	
	public void addItem(LootboxItem item) {
		this.items.add(item);
	}
	
	public Inventory createInventory() {
		Inventory inventory = Bukkit.createInventory(null,  9, this.getName());
		
		
		
		return inventory;
	}
}
