package de.blockartists.lootify;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Lootbox {
	private String name;
	private String prefix;
	private String message;
	private List<ItemStack> items;

	public Lootbox() {}
	
	public Lootbox(String prefix, String name, String message) {
		this.prefix = prefix;
		this.name = name;
		this.message = message;
		this.items = null;
	}
	
	public Lootbox(String prefix, String name, String message, List<ItemStack> items) {
		this(prefix, name, message);
		this.items = items;
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
	
	public List<ItemStack> getItems() {
		return this.items;
	}
	
	public void addItem(ItemStack item) {
		this.items.add(item);
	}
	
	public void createInventory() {
		
	}
}
