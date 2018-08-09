package de.blockartists.lootify;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LootboxItem {
	private String name;
	private List<String> lore;
	private int amount;
	private Material material;
	
	public LootboxItem(String name, List<String> lore, Material material, short amount) {
		this.name = name; 
		this.lore = lore;
		this.material = material;
		this.amount = amount;
	}
	
	public ItemStack toItemStack() {
		ItemStack item = new ItemStack(this.material, this.amount);
		item.getItemMeta().setDisplayName(this.name);
		item.getItemMeta().setLore(this.lore);
		return item;
	}
}
