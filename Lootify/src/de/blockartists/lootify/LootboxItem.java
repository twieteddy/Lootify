package de.blockartists.lootify;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LootboxItem {
	private String name;
	private List<String> lore;
	private String type;
	private int amount;
	private int weight;
	
	private LootboxItem() {
		// Default values
		this.name = null;
		this.lore = null;
		this.type = null;
		this.amount = 1;
		this.weight = 0;
	}
	
	public LootboxItem(String name, List<String> lore, String type, int amount, int weight) {
		this();
		
		// Override default values;
		if (name != null && !name.isEmpty())
			this.name = name;

		// Set lore if given
		if (lore != null && lore.size() > 0) {
			this.lore = lore;
		}
		
		// Set type
		if (type != null && !type.equalsIgnoreCase("air"))
			this.type = type;
		
		// Fix MIN and MAX stack size
		if (amount > 64)
			this.amount = 64;
		else if (amount < 1)
			this.amount = 1;
		else 
			this.amount = amount;
		
		// Set weight
		this.weight = weight;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	@Override
	public String toString() {
		return "Name: " + this.name 
				+ ", Lore: " + this.lore.toString() 
				+ ", Type: " + this.type 
				+ ", Amount: " + this.amount
				+ ", Weight: " + this.weight;
	}
	
	// Create ItemStack from this blueprint
	public ItemStack create() {
		if (this.type == null)
			return null;
		
		// Create item from material and get meta
		ItemStack item = new ItemStack(Material.getMaterial(type.toUpperCase()));
		ItemMeta meta = item.getItemMeta();
		
		// Set amount
		item.setAmount(this.amount);
		
		// Set optional name
		if (this.name != null && !this.name.isEmpty())
			meta.setDisplayName(this.name);
		
		// Set optional lore
		if (this.lore != null && this.lore.size() > 0)
			meta.setLore(this.lore);
		
		// Set new meta
		item.setItemMeta(meta);
		
		return item;
	}	
}
