package de.blockartists.lootify;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

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
		
		// Check if item is our lootbox and store it
		Lootbox lootbox = null;
		for (String key : plugin.getLootboxes().keySet()) {
			if (e.getItem().getItemMeta().getDisplayName().startsWith(key)) {
				lootbox = plugin.getLootboxes().get(key);
				break;
			}
		}
		
		// Return if no lootbox was found or action mismatches
		if (lootbox == null)
			return;
		
		// Cancel default routine
		e.setCancelled(true);
		
		if (e.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}
		
		// Play sound
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		
		// Display text message to player if it was set
		if (lootbox.getMessage() != null && lootbox.getMessage().length() > 0) {
			e.getPlayer().sendMessage(lootbox.getMessage());
		}
		
		// Open inventory of lootbox with random stuff
		e.getPlayer().openInventory(lootbox.createInventory());
		
		// Reduce amount by 1
		e.getItem().setAmount(e.getItem().getAmount()-1);
	}
}

	
