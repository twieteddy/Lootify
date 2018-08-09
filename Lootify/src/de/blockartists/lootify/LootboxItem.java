package de.blockartists.lootify;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LootboxItem {
	private String name;
	private String lore;
	private int amount;
	private Material material;
	private String category;
	
	public LootboxItem(String name, String lore, Material material, int amount, String category) {
		this.name = name; 
		this.lore = lore;
		this.material = material;
		this.amount = amount;
		this.category = category;
	}
	
	public ItemStack toItemStack() {
		ItemStack item = new ItemStack(this.material, this.amount);
		item.getItemMeta().setDisplayName(this.name);
		item.getItemMeta().setLore(Arrays.asList(lore));
		return item;
	}
	
	public String getCategory() {
		return this.category;
	}
	
}
