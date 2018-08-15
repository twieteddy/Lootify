package de.blockartists.lootify;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LootifyListener implements Listener {
	private Lootify plugin;
	
	public LootifyListener(Lootify plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {	
		// Exit if item in our hand doesn't fit the requirements of a lootbox
		if (e.getItem() == null || !e.getItem().getItemMeta().hasDisplayName())
			return;
		
		// Check if item is our lootbox
		Lootbox lootbox = null;
		for (String key : plugin.getLootboxes().keySet()) {
			if (e.getItem().getItemMeta().getDisplayName().startsWith(key)) {
				lootbox = plugin.getLootboxes().get(key);
				break;
			}
		}
		
		// Return if no lootbox was found
		if (lootbox == null) {
			return;
		}
	
		// If our lootbox was rightclicked in the air, it opens up
		if (e.getAction() == Action.RIGHT_CLICK_AIR) {
			// Play sound
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
			if (lootbox.getMessage() != null) {
				e.getPlayer().sendMessage(lootbox.getMessage());
			}
			
			// Open inventory of lootbox with random stuff
			e.getPlayer().openInventory(lootbox.createInventory());
			
			// Reduce amount by 1
			e.getItem().setAmount(e.getItem().getAmount()-1);
		}
		// Prevent execution of the default routine
		e.setCancelled(true);
	}		
	
	
}

	
