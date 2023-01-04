package io.github.bycubed7.corecubes.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.commands.uses.Arg;

public class PrefixAction extends Action {

	public PrefixAction(String prefix, CubePlugin _plugin) {
		super(prefix, _plugin);
	}

	@Override
	protected void setupArguments(List<ActionUse> arguments) {
		for (Action action : plugin.actions) {
			if (equals(action)) continue;
			
			List<ActionUse> actionArguments = new ArrayList<ActionUse>();
			action.setupArguments(actionArguments);
			
			for (ActionUse actionArgumentsUse : actionArguments) {
				ActionUse newActionUse = new ActionUse(actionArgumentsUse);
				newActionUse.add(0, new Arg("command", action.name));
				
				arguments.add(newActionUse);
				//Debug.log(newActionUse.toString());
			}
		}
	}

	@Override
	protected Execution approved(Player player, Map<String, String> args) {
		String targetCommand = args.get("command");
		
		// If we're just trying to run the command
		if (targetCommand != null) {
			for (Action action : plugin.actions) {
				if (action.name == targetCommand) 
					return action.approved(player, args);
			}
		}
		
		return Execution.NONE;
	}

	@Override
	protected String execute(Player player, Map<String, String> args) {
		String targetCommand = args.get("command");
		
		// If we're just trying to run the command
		if (targetCommand != null) {
			for (Action action : plugin.actions) {
				if (action.name.equalsIgnoreCase(targetCommand))
					return action.execute(player, args);
			}
		}
		
		return null;
	}
}
