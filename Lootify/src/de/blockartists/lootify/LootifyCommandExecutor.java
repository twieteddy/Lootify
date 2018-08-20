package de.blockartists.lootify;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LootifyCommandExecutor implements CommandExecutor {
	private Lootify lootify;
	private LootifyLogger log;
	
	// TODO: Create config file for messages
	public static String MESSAGE_WRONG_INSTANCE = ChatColor.RED + "Nur Spieler dürfen den Befehl benutzen";
	public static String MESSAGE_NO_PERMISSION = ChatColor.RED + "Du hast keine Berechtigung";
	
	public static String SNAPS_DIRECTORY = "snaps";
	
	public LootifyCommandExecutor(Lootify lootify) {
		this.lootify = lootify;
		this.log = lootify.getLootifyLogger();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Ist der Sender ein Spieler?
		if (!(sender instanceof Player)) {
			sender.sendMessage(MESSAGE_WRONG_INSTANCE);
			return false;
		}
		
		// Darf er das?
		if (!sender.hasPermission("lootify.admin")) {
			sender.sendMessage(MESSAGE_NO_PERMISSION);
			return false;
		}
		
		return true;
	}
}
