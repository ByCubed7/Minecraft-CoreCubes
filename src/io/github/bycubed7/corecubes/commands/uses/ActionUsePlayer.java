package io.github.bycubed7.corecubes.commands.uses;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.bycubed7.corecubes.commands.ActionUse;

public class ActionUsePlayer extends ActionUse {
	@Override
	protected List<String> getUse(Arg argument, Player player) {
		ArrayList<String> node = new ArrayList<String>();
		
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			node.add(onlinePlayer.getDisplayName());
		}
		
		return node;
	}
}
