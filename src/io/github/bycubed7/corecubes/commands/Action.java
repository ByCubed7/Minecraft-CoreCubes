package io.github.bycubed7.corecubes.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.managers.Debug;
import io.github.bycubed7.corecubes.managers.Tell;


public abstract class Action implements CommandExecutor, TabCompleter {

	protected final String name;
	protected final String commandName;
	protected final String description;

	protected CubePlugin plugin;
	protected boolean requiresPlayer;
	
	protected String prefix; // <-- Needs plugin name for permissions
	
	private List<ActionUse> arguments;
	private PluginCommand command;
	
	public Action(String _name, CubePlugin _plugin) {
		name = _name;
		plugin = _plugin;

		commandName = name.toLowerCase();
		description = findDescription();
		
		arguments = new ArrayList<ActionUse>();
		requiresPlayer = true;
		
		prefix = plugin.getName();
		
		command = plugin.getCommand(name);
		if (command == null) {
			Debug.warn("Can't find command `" + name + "` in plugin! Has it been added to plugin.yml?");
			return;			
		}
		
		plugin.actions.add(this);
	}
	
	public final void ready() {
		// Set up the command execution
		if (command != null) {
			command.setExecutor(this);	
			command.setTabCompleter(this);			
		}

		setupArguments(arguments);
	}
	
	/**
	 * Gets the command description that is currently set in plugin.yml 
	 * @return The string used as a description, or a temperary error message.
	 */
	private final String findDescription() {
		Map<String, Map<String, Object>> commandDescriptions = plugin.getDescription().getCommands();
		// Check the commands exist
		if (commandDescriptions == null) {
			Debug.warn("Can't find the plugins command map!");
			return "Not found: See log";
		}
		// Check the command exists
		if (!commandDescriptions.containsKey(commandName)) {
			Debug.warn("Can't find command `"+name+"` in plugin command map!");
			return "Not found: See log";
		}
		Map<String, Object> descriptions = commandDescriptions.get(commandName);
		
		// Check the command description value exists
		if (!descriptions.containsKey("description")) {
			Debug.warn("Can't find the command `"+name+"` description value!");
			return "Not found: See log";
		}
		return (String) descriptions.get("description");
	}
	
	/**
	 * Returns whether the commandSender has permission from the config to send the command
	 * @param sender - The sender of the command
	 * @return Whether the commandSender has permission to use the command
	 */
	private final boolean senderHasPermission(CommandSender sender) {

		// If the commandSender is a player
		if (sender instanceof Player) {
			Player player = (Player) sender;

			// Check the player has permission.
			if (!player.hasPermission(prefix + ".*") && !player.hasPermission(prefix + "." + name)) {
				Tell.player(player, "You do not have permission to use this command.");
				return false;
			}
			return true;
		}

		// Is the command required to be ran from a player
		if (requiresPlayer) {
			Debug.warn("A player must use this command.");
			return false;				
		}
		
		return true;
	}
	
	// -- -- -- -- -- -- -- -- -- -- --
	// Gets
	
	final public String getName() {
		return name;
	}

	final public String getPrefix() {
		return prefix;
	}

	final public String getDescription() {
		return description;
	}
	
	// -- -- -- -- -- -- -- -- -- -- --
	// Events

	@Override
	public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!command.getName().equalsIgnoreCase(name))
			return true;
		
		boolean hasPermission = senderHasPermission(sender);
		if (!hasPermission) return true;
		
		Player player = (Player) sender;

		// Convert String Array to Argument Map
		Map<String, String> mapOut = getArgumentMap(args);
		
		if (mapOut == null) {
			Debug.error("Command `" + name + "` has no usages listed!");
			return false;
		}
		
		Execution actionError = approved(player, mapOut);
		if (!actionError.failed) {
			execute(player, mapOut);
			return false;
		}
		
		Tell.player(player, actionError.reason);
		return actionError.printUsage;
	}

	@Override
	public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!command.getName().equalsIgnoreCase(name))
			return null;

		boolean hasPermission = senderHasPermission(sender);
		if (!hasPermission) return null;
		
		Player player = (Player) sender;

		String input = joinArgs(args);

		List<ActionUse> usages = incompleteUses(input);
		
		// Pad out uses using usages#getAllUses
		List<String> uses = new ArrayList<String>();
		for (ActionUse usage : usages)
			uses.addAll(usage.getAllUses(player));

		// Get the potential uses
		List<String> potential = new ArrayList<String>();
		for (String use : uses) {
			if (use.startsWith(input))
				potential.add(use.split(" ")[StringUtils.countMatches(input, " ")]);
		}
		
		return potential;
	}
	
	//
	
	private final Map<String, String> getArgumentMap(String[] args) {
		String input = joinArgs(args);
		List<ActionUse> uses = completeUses(input);

		if (uses.size() == 0) return null;//new HashMap<String, String>();
		
		uses = uses.stream().sorted(Comparator.comparing(ActionUse::size)).collect(Collectors.toList());
		
		
		return uses.get(0).mapOut(input);
	}
	
	private final List<ActionUse> completeUses(String input) {
		List<ActionUse> usages = new ArrayList<ActionUse>();
		for (ActionUse use : arguments) if (use.isComplete(input)) usages.add(use);
		return usages;
	}
	
	private final List<ActionUse> incompleteUses(String input) {
		List<ActionUse> usages = new ArrayList<ActionUse>();
		for (ActionUse use : arguments) if (use.canComplete(input)) usages.add(use);
		return usages;
	}
	
	/**
	 * Combine all of the arguments to a single string
	 * @param args - the array of arguments sent to the command
	 * @return The joint command array.
	 */
	private final String joinArgs(String[] args) {
		if (args.length == 0) return "";
		
		String input = args[0];
		for (int i = 1; i < args.length; i++) input += " " + args[i];
		//input = input.strip();
		return input;
	}
	
	//

	abstract protected void setupArguments(List<ActionUse> arguments);

	/**
	 * Check to see whether the action can function, the player has permission and
	 * any other checks required.
	 *
	 * @param player - the player who's activating the command
	 * @param args   - the arguments the player has added into the command line
	 * @return Why the player can not run the command (if any)
	 * @see Action
	 */
	abstract protected Execution approved(Player player, Map<String, String> args);

	/**
	 * Execute the action / command.
	 *
	 * @param player - the player who's activating the command
	 * @param args   - the arguments the player has added into the command line
	 * @return Whether the command completed successfully
	 * @see Action
	 */
	abstract protected boolean execute(Player player, Map<String, String> args);
	
	


	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;

		if (!(o instanceof Action))
			return false;

		Action c = (Action) o;

		if (!name.equals(c.name))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
