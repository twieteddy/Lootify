package de.blockartists.lootify;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LootifyCommandExecutor implements CommandExecutor {
	private Lootify lootify;
	
	public LootifyCommandExecutor(Lootify lootify) {
		this.lootify = lootify;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// if (sender.hasPermission("lootify")) { }
		return true;
	}
}
