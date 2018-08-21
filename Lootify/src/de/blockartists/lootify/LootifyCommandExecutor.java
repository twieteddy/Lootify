package de.blockartists.lootify;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LootifyCommandExecutor implements CommandExecutor {
	private Lootify lootify = null;
	private LootifyLogger log = null;
	
	// TODO: Create config file for messages
	public static String MESSAGE_WRONG_INSTANCE = ChatColor.RED + "Nur Spieler dürfen den Befehl benutzen";
	public static String MESSAGE_NO_PERMISSION = ChatColor.RED + "Du hast keine Berechtigung";
	public static String MESSAGE_NOT_ENOUGH_ARGS = ChatColor.RED + "Nicht genug Argumente";
	
	public LootifyCommandExecutor(Lootify lootify) {
		this.lootify = lootify;
		this.log = lootify.getLootifyLogger();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Instance check
		if (!(sender instanceof Player)) {
			sender.sendMessage(MESSAGE_WRONG_INSTANCE);
			return false;
		}
		
		// Permission check
		if (!sender.hasPermission("lootify.admin")) {
			sender.sendMessage(MESSAGE_NO_PERMISSION);
			return false;
		}
		
		if (args.length < 3) {
			sender.sendMessage(MESSAGE_NOT_ENOUGH_ARGS);
			return false;
		}

		Player player = (Player)sender;
		
		/**
		 * args[0] = <item|lootbox<reload>
		 * args[1] = <list|create|delete>
		 * args[2] = <path.to.item>
		 * args[3] = [weight]
		 */
		
		if (args[0].equalsIgnoreCase("reload"));
		
		if (args[0].equalsIgnoreCase("item")) { 
			switch(args[1].toLowerCase()) {
			
			case "list":
				// TODO
				break;
				
			case "create":
				int weight = 1;
				if (args.length == 4)
					weight = Integer.parseInt(args[3]);
				lootify.getItemsConfig().createItem(args[2], player.getInventory().getItemInMainHand(), weight);
				break;
				
			case "delete":
				lootify.getItemsConfig().deleteItem(args[2]);
				break;
			}
		}
		
		if (args[0].equalsIgnoreCase("lootbox")) {
			
		}
		
		return true;
	}
}
