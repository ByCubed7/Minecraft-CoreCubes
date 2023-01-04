package io.github.bycubed7.corecubes.commands.uses;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import io.github.bycubed7.corecubes.unit.Vector3Int;

public class ArgInt extends Arg {
		
	protected ArgInt(String _name, String _value) {
		super(_name, _value);
	}

	@Override
	public List<String> use(Arg argument, Player player) {
		ArrayList<String> node = new ArrayList<String>();

		node.add("0");
		
		return node;
	}

	@Override
	public boolean valid(String current, Arg argument, Player player) {
		
		try{
			Integer.parseInt(current);
			return true;
		}
		catch (NumberFormatException e){
			//e.printStackTrace();
		}
		
		return false;
	}
}
