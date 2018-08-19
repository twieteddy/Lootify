package de.blockartists.lootify;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LootboxItem extends ItemStack {
	private String name;
	private List<String> lore;
	private String type;
	private int amount;
	private int weight;

	public LootboxItem(String name, List<String> lore, String type, int amount, int weight) {
		this.name = (name == null) ?  "" : this.name;
		this.lore = (lore == null) ? new ArrayList<String>() : this.lore;
		this.type = (type == null) ? "AIR" : this.type;
		this.amount = (amount > 64 || amount < 1) ? 1 : amount;
		this.weight = (weight < 1) ? 1 : weight;
	}
	
	public int getWeight() { return this.weight; }
	
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
		Material material = Material.getMaterial(this.type);
		
		if (material == null || material == Material.AIR) 
			return null;
		
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
