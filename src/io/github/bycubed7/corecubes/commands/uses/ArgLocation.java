package io.github.bycubed7.corecubes.commands.uses;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import io.github.bycubed7.corecubes.unit.Vector3Int;

public class ArgLocation extends Arg {
		
	protected ArgLocation(String _name, String _value) {
		super(_name, _value);
	}

	@Override
	public List<String> use(Arg argument, Player player) {
		ArrayList<String> node = new ArrayList<String>();

		node.add(Vector3Int.fromLocation(player.getLocation()).toString());
		
		return node;
	}

	@Override
	public boolean valid(String current, Arg argument, Player player) {
		return false;
	}
}
