package de.blockartists.lootify;

import org.bukkit.inventory.ItemStack;

public class LootboxItem {
	private ItemStack itemStack;
	private int weight;
	
	public LootboxItem(ItemStack itemStack, int weight) {
		this.itemStack = itemStack;
		this.weight = weight;
	}
	
	public int getWeight() { return this.weight; }
	public ItemStack getItemStack() { return this.itemStack; }
}
