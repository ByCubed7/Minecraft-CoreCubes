package io.github.bycubed7.corecubes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.managers.Debug;
import io.github.bycubed7.corecubes.managers.Tell;

public abstract class Action implements CommandExecutor {

	// TODO: Move this to a language class of sorts
	protected static String responseNonPlayer = "A player must use this command.";
	protected static String responsePermission = "You do not have permission to use this command.";

	protected JavaPlugin plugin;

	protected String name;
	protected String prefix;
	protected String description;

	public Action(String _name, JavaPlugin _plugin) {
		plugin = _plugin;
		prefix = plugin.getDescription().getPrefix();
		name = _name;
		description = (String) plugin.getDescription().getCommands().get(name.toLowerCase()).get("description");

		plugin.getCommand(name).setExecutor(this);
	}

	final public String getName() {
		return name;
	}

	final public String getPrefix() {
		return prefix;
	}

	final public String getDescription() {
		return description;
	}

	@Override
	final public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!command.getName().equalsIgnoreCase(name))
			return true;

		// Check the commandSender is a player
		if (!(sender instanceof Player)) {
			Debug.log(responseNonPlayer);
			return true;
		}

		Player player = (Player) sender;

		// Check the player has permission.
		if (!player.hasPermission(prefix + ".*") && !player.hasPermission(prefix + "." + name)) {
			Tell.player(player, responsePermission);
			return true;
		}

		ActionFailed actionIsApproved = approved(player, args);

		switch (actionIsApproved) {
		case ARGUMENTLENGTH:
			Tell.player(player, "Invalid agrument count");
			return false;
		case USAGE:
			Tell.player(player, "Invalid command usage");
			return false;
		case NOPERMISSION:
			Tell.player(player, responsePermission);
			return true;
		case OTHER:
			return true;
		case NONE:
		default:
			execute(player, args);
			return true;
		}
		// return true;
	}

	/**
	 * Check to see whether the action can function, the player has permission and
	 * any other checks required.
	 *
	 * @param player - the player who's activating the command
	 * @param args   - the arguments the player has added into the command line
	 * @return Why player can not run the command (if any)
	 * @see Action
	 */
	abstract protected ActionFailed approved(Player player, String[] args);

	/**
	 * Execute the action / command.
	 *
	 * @param player - the player who's activating the command
	 * @param args   - the arguments the player has added into the command line
	 * @return Whether the command completed successfully
	 * @see Action
	 */
	abstract protected boolean execute(Player player, String[] args);

}
