package io.github.bycubed7.corecubes.managers;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import io.github.bycubed7.corecubes.CubePlugin;

public abstract class Manager implements Listener, Runnable {
	
	final protected CubePlugin plugin;
	
	final protected int interaction = 20;

	public Manager(CubePlugin _plugin, int interaction) {
		plugin = _plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		
		if (interaction != -1)
			plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, interaction);
	}
}
