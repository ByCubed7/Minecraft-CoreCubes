package io.github.bycubed7.corecubes.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import io.github.bycubed7.corecubes.managers.Debug;
import io.github.bycubed7.corecubes.managers.Tell;
import io.github.bycubed7.corecubes.unit.Node;

public abstract class Action implements CommandExecutor, TabCompleter {

	// TODO: Move this to a language class of sorts
	protected static String responseNonPlayer = "A player must use this command.";
	protected static String responsePermission = "You do not have permission to use this command.";

	protected JavaPlugin plugin;

	protected String name;
	protected String prefix; // <-- Needs plugin name for permissions
	protected String description;

	protected Node<String> uses;

	public Action(String _name, JavaPlugin _plugin) {
		plugin = _plugin;
		name = _name;

		description = (String) plugin.getDescription().getCommands().get(name.toLowerCase()).get("description");
		uses = new Node<String>(name);

		PluginCommand command = plugin.getCommand(name);

		command.setExecutor(this);
		command.setTabCompleter(this);
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

	final public void addUse(String newUse) {
		
		//for (String sep : newUse.split(" ")) {
			//uses.
		//}
		
		uses.add(newUse);
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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

	@Override
	public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!command.getName().equalsIgnoreCase(name))
			return null;

		// Check the commandSender is a player
		if (!(sender instanceof Player)) {
			Debug.log(responseNonPlayer);
			return null;
		}

		Player player = (Player) sender;

		// Check the player has permission.
		if (!player.hasPermission(prefix + ".*") && !player.hasPermission(prefix + "." + name)) {
			Tell.player(player, responsePermission);
			return null;
		}
		
		// Is current command input valid?
		//if (false)
		// suggestions;

		// "PLAYER CHANT INT"
		
		// Suggest possible completions, eg player names, enchants etc.
		//for (String use : uses) {
		//	boolean valid = true;
		//	if (valid) suggestions.add(use);
		//}

		// Get the full input
		String input = args[0];
		for (int i = 1; i < args.length; i++) input = input + " " + args[i];
		//input = input.strip();
		
		
		// Get the expected inputs
		List<String> usages = tab(player, command, args);

		// Get the potential uses
		List<String> potential = new ArrayList<String>();
		for (String usage : usages) {
			if (usage.contains(input)) 
				//potential.add(usage.replace(input, "").strip().split(" ")[0]);
				potential.add(usage.split(" ")[StringUtils.countMatches(input, " ")]);
		}		
		
		return potential;
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

	abstract protected List<String> tab(Player player, Command command, String[] args);

}
