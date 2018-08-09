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
		
		e.getPlayer().sendMessage("0");
		
		// Exit if item in our hand doesn't fit the requirements of a lootbox
		if (e.getItem() == null
				|| !e.getItem().getItemMeta().hasDisplayName()
				|| !plugin.getLootboxes().stream().anyMatch(box -> box.getItemMaterial() == e.getItem().getType()))
			return;
	
		e.getPlayer().sendMessage("1");
		
		// Exit if item doesn't start with predefined prefix
		Lootbox lootbox = null;
		for (Lootbox boxFromCollection : plugin.getLootboxes()) {
			if (e.getItem().getItemMeta().getDisplayName().startsWith(boxFromCollection.getPrefix())) {
				lootbox = boxFromCollection;
				break;
			}
		} 
		
		e.getPlayer().sendMessage("2");
		
		if (lootbox == null) {
			return;
		}
		
		e.getPlayer().sendMessage("3");
		
		//if (!e.getItem().getItemMeta().getDisplayName().startsWith(plugin.getLootboxPrefix()))
		//	return;
	
		// If our lootbox was rightclicked, it opens up
		if (e.getAction() == Action.RIGHT_CLICK_AIR) {
			// Play sound
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
			// Create random items for lootbox inventory
			{
				Inventory inv = Bukkit.createInventory(null, 9, lootbox.getName());
				inv.setItem(4, new ItemStack(Material.DIAMOND, 3));
				
				// TODO: Create random inventory content
				e.getPlayer().openInventory(inv);
			}
			// Reduce amount by 1
			e.getItem().setAmount(e.getItem().getAmount()-1);
		}
		// Prevent execution of the default routine
		e.setCancelled(true);
		return;
	}		
	
	
}

	
