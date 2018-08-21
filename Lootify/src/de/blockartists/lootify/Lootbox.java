package de.blockartists.lootify;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Lootbox {
	private Lootify lootify = null;
	private String identifier = null; // Prefix for items used to identify lootbox. Should be unique
	private String message = null; // Message displayed to you after opening the lootbox
	private List<String> items = new ArrayList<>(); // A path like <path>.<item>. <item> can be ? for random
	
	
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
			inventory.addItem(lootify.getItemsConfig().getItem(uri).getItemStack());
		}

		return inventory;
	}
}

/*for (String itemKey : itemPath) {
String [] path = itemKey.split("\\.");

inventory.addItem(lootify.getItemsConfig().getItem(itemKey).getItemStack());
*/
// Skip if path isn't splitted
/*if (path.length != 2) { continue; }

String pool = path[0];
String name = path[1];

if (name.equals("?")) {
	int maxWeight = lootify.getItems().get(pool).values().stream().mapToInt(LootboxItem::getWeight).sum(); 
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
}*/