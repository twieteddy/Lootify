package de.blockartists.lootify;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Lootbox {
	private Lootify lootify = null;
	private String name = null; // Displayed in the upper left corner of lootbox inventory
	private String prefix = null; // Prefix for items used to identify lootbox. Should be unique
	private String message = null; // Message displayed to you after opening the lootbox
	private List<String> itemPath = new ArrayList<>(); // A path like <path>.<item>. <item> can be ? for random
	
	
	public Lootbox(Lootify lootify, String prefix, String message, List<String> items) {
		this.lootify = lootify;
		this.prefix = prefix;
		this.message = message;
		this.itemPath = items;
	}
	
	public String getName() { return this.name; }
	public String getPrefix() { return this.prefix; } 
	public String getMessage() { return this.message; }
		
	// TODO: Make it safe
	public Inventory createInventory(String displayName) {
		Inventory inventory = Bukkit.createInventory(null,  9, displayName);
				
		for (String itemKey : itemPath) {
			String [] path = itemKey.split("\\.");
			
			// Skip if path isn't splitted
			if (path.length != 2) { continue; }
			
			String pool = path[0];
			String name = path[1];
			
			if (name.equals("?")) {
				int maxWeight = lootify.getItems().get(pool).values().stream().mapToInt(LootboxItem::getWeight).sum(); 
				//lootify.getItems().get(pool).values().stream().mapToInt(LootboxItem::getWeight).sum();
				int random = ThreadLocalRandom.current().nextInt(maxWeight);
				int lifted = 0;
				
				Iterator<LootboxItem> iter = lootify.getItems().get(pool).values().iterator();
				while (iter.hasNext()) {
					LootboxItem item = iter.next();
					
					if ((random >= lifted) && (random < lifted + item.getWeight())) {
						inventory.addItem(item.getItemStack());
						break;
					} else {
						lifted += item.getWeight();
					}		
				}
			} else {
				inventory.addItem(this.lootify.getItems().get(pool).get(name).getItemStack());
			}
		}
		return inventory;
	}
}
