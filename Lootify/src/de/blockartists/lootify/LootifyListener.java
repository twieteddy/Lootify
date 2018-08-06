package de.blockartists.lootify;

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
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.PHYSICAL){
			if (e.getItem() != null) {
				if (e.getItem().getType() == plugin.getLootboxItem()) {
					e.getPlayer().sendMessage(e.getItem().getItemMeta().getDisplayName());
				} else {
					e.getPlayer().sendMessage("0");
				}
			}
		}
	}
	
}
