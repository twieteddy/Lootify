package de.blockartists.lootify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Lootbox {
	private String name = null; // Displayed in the upper left corner of lootbox inventory
	private String prefix = null; // Prefix for items used to identify lootbox. Should be unique
	private String message = null; // Message displayed to you after opening the lootbox
	private List<String> items = new ArrayList<>(); // A path like <category>.<item>. <item> can be ? for random
	private static Map<String, Map<String, LootboxItem>> itemBlueprints = new HashMap<>();
	
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
	
	public static void addBlueprint(String pool, String name, LootboxItem item) {
		if (!itemBlueprints.containsKey(pool)) 
			itemBlueprints.put(pool, new HashMap<>());
		
		if (!itemBlueprints.get(pool).containsKey(name)) 
			itemBlueprints.get(pool).put(name, item);
		
	}
	
		
	public Inventory createInventory() {
		Inventory inventory = Bukkit.createInventory(null,  9, this.getName());
		
		/*
		for (String item : items) {
			String [] path = item.split("\\.");
			String pool = path[0];
			String name = path[1];
			
			if (name.equals("?")) {
				// Sum weight of all lootbox items in category
				int maxWeight = itemBlueprints.get(pool).values().stream().mapToInt(LootboxItem::getWeight).sum();
				int lifted = 0;
				int random = ThreadLocalRandom.current().nextInt(lifted, maxWeight);
				
				for (LootboxItem boxItem : itemBlueprints.get(pool).values()) {
					int weight = boxItem.getWeight();
					if (random >= lifted && random < lifted + weight) {
						inventory.addItem(boxItem.create());
						break;
					} else {
						lifted += weight;
					}
				}
			} else {
				inventory.addItem(itemBlueprints.get(pool).get(name).create());
			}
			
		}*/
		
		return inventory;
	}
	
	
	
}
