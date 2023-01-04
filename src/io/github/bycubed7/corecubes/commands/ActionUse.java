package io.github.bycubed7.corecubes.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.bycubed7.corecubes.commands.uses.Arg;
import io.github.bycubed7.corecubes.managers.Debug;
import io.github.bycubed7.corecubes.unit.Vector3Int;

public class ActionUse {
	public ArrayList<Arg> contents;
	public int maxArgumentSize;
	
	public ActionUse() {
		contents = new ArrayList<Arg>();
	}
	
	// Copy constructor
	public ActionUse(ActionUse oldActionUse) {
		contents = new ArrayList<Arg>(oldActionUse.contents);
		maxArgumentSize = oldActionUse.maxArgumentSize;
	}
	
	public int size() {
		return maxArgumentSize;
	}
	
	public static ActionUse create() {
		return new ActionUse();
	}

	public ActionUse add(int index, Arg _argument) {
		maxArgumentSize += _argument.getSize();
		contents.add(index, _argument);
		return this;
	}

	public ActionUse add(Arg _argument) {
		maxArgumentSize += _argument.getSize();
		contents.add(_argument);
		return this;
	}

	public ActionUse add(String name, String value) {
		return add(new Arg(name, value));
	}
			
	// Return whether the string could be a valid command
	public boolean canComplete(String input) {
		int count = 0;
		if (!input.isBlank()) 
			count = StringUtils.countMatches(input, " ") + 1;
		
		// Is size above parameter amount
		if (count > maxArgumentSize) return false;
		
		// Check the varibles have been added
		String[] inputSplit = input.split(" ");
		ArrayList<Arg> arguments = new ArrayList<Arg>(contents);
		int index = 0;
		for (int i = 0; i < arguments.size(); i++) {
			Arg arg = arguments.get(i);
			if (arg.isVariable()) {
				//fullarg = args
				String[] args = Arrays.copyOfRange(inputSplit, index, arg.getSize());

				if (!arg.getValue().startsWith(String.join(" ", args)))
					return false;
			}
			index += arg.getSize();
		}
		
		return true;
	}

	/**
	 * Return whether the input is a completed AND valid command
	 * @param input - the command input.
	 * @return Whether the input is a completed AND valid command
	 */
	public boolean isComplete(String input) {
		int count = 0;
		if (!input.isBlank()) 
			count = StringUtils.countMatches(input, " ") + 1;
		
		// Is size the same parameter amount
		//Debug.log(""+count+" vs "+ maxArgumentSize);
		if (count != maxArgumentSize) return false;
		
		// Check the varibles have been added
		String[] inputSplit = input.split(" ");
		ArrayList<Arg> arguments = new ArrayList<Arg>(contents);
		int index = 0;
		for (int i = 0; i < arguments.size(); i++) {
			Arg arg = arguments.get(i);
			if (arg.isVariable()) {
				//fullarg = args
				String[] args = Arrays.copyOfRange(inputSplit, index, index + arg.getSize());

				if (!arg.getValue().equalsIgnoreCase(String.join(" ", args))) {
					Debug.log("arg.getValue().equalsIgnoreCase(String.join(\" \", args))");
					return false;					
				}
			}
			else {
				String[] args = Arrays.copyOfRange(inputSplit, index, index + arg.getSize());
				String value = String.join(" ", args);
				
				if (value.strip() == "") return false;
			}
			
			index += arg.getSize();
		}
		
		return true;
	}
	
	/**
	 * Converts the given string to a argument map.
	 * @param input
	 * @return
	 */
	public Map<String, String> mapOut(String input) {
		
		Map<String, String> argumentMap = new HashMap<String, String>();
		
		// Check the varibles have been added
		String[] inputSplit = input.split(" ");
		int index = 0;
		for (int i = 0; i < contents.size(); i++) {
			Arg arg = contents.get(i);
			int endIndex = arg.isRecurring() ? inputSplit.length : arg.getSize();
			
			//Debug.log("Mapping: " + arg.getName() + " - with size: " + endIndex);
			
			if (arg.isVariable()) { index += arg.getSize(); continue; }



			String[] argumentStrings = Arrays.copyOfRange(inputSplit, index, index + endIndex);
			String argumentString = String.join(" ", argumentStrings);
			argumentMap.put(arg.getName(), argumentString);

			if (arg.isRecurring()) break;
			
			index += arg.getSize();
		}

		//Debug.log(argumentMap.toString());
		
		return argumentMap;
	}

	public Arg getArgument(String name) {
		for (Arg arg : contents) {
			if (arg.getName().equals(name))
				return arg;
		}
		return null;
	}

	public List<String> getAllUses(Player player) {
		
		// Fill node map
		ArrayList<ArrayList<String>> nodes = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < contents.size(); i++) {
			nodes.add(new ArrayList<String>());
			nodes.get(i).addAll(getUse(i, player));
		}
		
		ArrayList<String> useStrings = new ArrayList<String>();
		if (nodes.size() == 0) return useStrings;

		// Ready list
		useStrings.addAll(nodes.get(0));
		
		// Map 2D list to list of strings
		for (int i = 1; i < nodes.size(); i++) {
			
			// Clone to allow alts
			ArrayList<String> oldStrings = new ArrayList<String>(useStrings);
			for (int j = 0; j < nodes.get(i).size() - 1; j++) {
				useStrings.addAll(oldStrings);
			}
	
			// Populate
			for (int index = 0; index < useStrings.size(); index++) {
				
				int offset = index / nodes.get(i).size();
				String oldValue = useStrings.get(index);
				String newValue = oldValue + " " + nodes.get(i).get((index + offset) % nodes.get(i).size());
				
				useStrings.set(index, newValue);
			}
			
		}
		
		return useStrings;
	}

	protected List<String> getUse(Arg argument, Player player) {
		ArrayList<String> node = new ArrayList<String>();

		String value = argument.getValue();
		
		if (value == "PLAYER") {
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				node.add(onlinePlayer.getDisplayName());
			}
		}
		
		else if (value == "LOCATION") {
			node.add(Vector3Int.fromLocation(player.getLocation()).toString());
		}
		
		else
			node.add(value);
		
		return node;
	}

	private List<String> getUse(int index, Player player) {
		return getUse(contents.get(index), player);
	}
	
	@Override
	public String toString() {
		String string = "ActionUse:";
		
		for (Arg content : contents) {
			string += " " + content.getName();
		}
		
		return string;
	}
}
