package de.blockartists.lootify;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;


public class LootifyListener implements Listener {
	private Lootify plugin;
	
	public LootifyListener(Lootify plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null) {
				ItemStack item = e.getItem();
				if (item.getType() == plugin.getLootboxItem()) {
					if (item.getItemMeta().hasDisplayName()) {
						if (item.getItemMeta().getDisplayName().startsWith(plugin.getLootboxPrefix())) {
							e.getPlayer().giveExpLevels(1);
							e.getPlayer().sendMessage("Du hast " + item.getItemMeta().getDisplayName() + ChatColor.RESET + " geöffnet und 1 Level erhalten!");
							e.setCancelled(true);
							return;
						}
					}
				} 
			}
		}
	}
}
	
