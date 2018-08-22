package de.blockartists.lootify;

import org.bukkit.GameMode;
import org.bukkit.Sound;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class LootifyListener implements Listener {
	private Lootify lootify;
	
	public LootifyListener(Lootify lootify) {
		this.lootify = lootify;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		// Exit if item in our hand doesn't fit the requirements of a lootbox
		if (e.getItem() == null || !e.getItem().getItemMeta().hasDisplayName()) { 
			return; 
		} 
		
		// Check if item is our lootbox and store it
		Lootbox lootbox = null;
		for (String key : lootify.getLootboxes().keySet()) {
			if (e.getItem().getItemMeta().getDisplayName().startsWith(key)) {
				lootbox = lootify.getLootboxes().get(key);
				break;
			}
		}
		
		// Return if no lootbox was found or action mismatches
		if (lootbox == null) { 
			return; 
		}
		
		// Cancel if lootbox was found and continue if it was rightclicked in the air
		e.setCancelled(true);
		
		if (e.getAction() != Action.RIGHT_CLICK_AIR || e.getHand() != EquipmentSlot.HAND) { 
			return; 
		}	
		
		// Play sound
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		
		// Display text message to player if it was set
		if (lootbox.getMessage() != null && !lootbox.getMessage().isEmpty()) {
			e.getPlayer().sendMessage(lootbox.getMessage());
		}
		
		// Open inventory of lootbox
		e.getPlayer().openInventory(lootbox.createInventory(e.getItem().getItemMeta().getDisplayName()));
		
		// Reduce stack size by 1 if not in creative mode
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.getItem().setAmount(e.getItem().getAmount()-1);	
		}
	}
}

	
