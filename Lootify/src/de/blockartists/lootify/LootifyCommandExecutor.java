package de.blockartists.lootify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LootifyCommandExecutor implements CommandExecutor {
	private Lootify lootify = null;
	
	// TODO: Create config file for messages
	public static String MESSAGE_WRONG_INSTANCE = ChatColor.RED + "Nur Spieler dürfen den Befehl benutzen";
	public static String MESSAGE_NO_PERMISSION = ChatColor.RED + "Du hast keine Berechtigung";
	public static String MESSAGE_NOT_ENOUGH_ARGS = ChatColor.RED + "Nicht genug Argumente";
	public static String MESSAGE_RELOADED = ChatColor.GREEN + "Konfigurationsdateien neugeladen";
	public static String MESSAGE_ITEM_CREATED = ChatColor.GREEN + "Item wurde erstellt";  
	
	public LootifyCommandExecutor(Lootify lootify) {
		this.lootify = lootify;
	} 
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("lootify.admin")) {
			return false;
		}

		Iterator<String> arg = Arrays.asList(args).iterator();
		String command = arg.hasNext() ? arg.next() : "help";
		String selector = arg.hasNext() ? arg.next() : "";
		String name = arg.hasNext() ? arg.next() : "";		
		
		switch(command.toLowerCase()) {
		
		case "create":
			boolean createResult = false;
			if (name.isEmpty()) {
				return false;
			}
			if (selector.equalsIgnoreCase("item")) {
				int weight = arg.hasNext() ? Integer.parseInt(arg.next()) : 1;
				createResult = createItem(player, name, weight);
			}
			else if (selector.equalsIgnoreCase("lootbox")) {
				String identifier = arg.hasNext() ? arg.next() : null;
				String message = arg.hasNext() ? arg.next() : "";
				if (identifier == null) {
					return false;
				}
	
				createResult = createLootbox(player, name, identifier, message); // Name = identifier
			}
			return createResult;
			
			
			
		case "delete":
			if (name.isEmpty()) {
				return false;
			}
			boolean deleteResult = false;
			if (selector.equalsIgnoreCase("item"))
				deleteResult = deleteItem(player, name);
			else if (selector.equalsIgnoreCase("lootbox"))
				deleteResult = deleteLootbox(player, name);
			return deleteResult;
				
			
		case "show":
			if (name.isEmpty()) {
				return false;
			}
			boolean showResult = false;
			if (selector.equals("item")) {
				showResult = showItem(player, name);
			} else if (selector.equalsIgnoreCase("lootbox")) {
				showResult = showLootbox(player, name);
			}
			player.sendMessage("Noch nicht implementiert");
			return showResult;
			
			
			
		case "get":
			if (selector.equals("item") && !name.isEmpty()) {
				ItemStack item = lootify.getItemManager().getItem(name).getItemStack();
				if (item.getType() == Material.AIR) {
					player.sendMessage("Item nicht gefunden");
				} else {
					player.getInventory().addItem(item);
				}
				return true;
			}
			return false;
			
			
			
		case "list":
			player.sendMessage("Noch nicht implementiert");
			return false;
			
			
			
		default: displayHelp(player);
		}
		return true;
	}
	
	
	
	private boolean createItem(Player player, String name, int weight) {
		if (player == null || name == null)
			return false;
		return lootify.getItemManager().createItem(name, player.getInventory().getItemInMainHand(), weight);
	}
	
	
	
	private boolean deleteItem(Player player, String name) {
		if (player == null || name == null)
			return false;		
		return lootify.getItemManager().deleteItem(name);
	}
	
	
	
	private boolean createLootbox(Player player, String name, String identifier, String message) { 
		Lootbox lootbox = new Lootbox(lootify, identifier, message, new ArrayList<String>());
		lootbox.saveLootbox(name);
		return true;
	}
	
	private boolean deleteLootbox(Player player, String name) { return false; }	
	
	private boolean showItem(Player player, String name) { return false; }
	private boolean showLootbox(Player player, String name) { return false; }
	
	private void displayHelp(Player player) {
		String msg = "Hilfe wird bald nachgetragen";
		player.sendMessage(msg);
	}
	
}
