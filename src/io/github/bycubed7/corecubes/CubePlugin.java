package io.github.bycubed7.corecubes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.commands.Action;
import io.github.bycubed7.corecubes.commands.PrefixAction;
import io.github.bycubed7.corecubes.managers.Debug;

public abstract class CubePlugin extends JavaPlugin {

	public static CubePlugin instance;
	public List<String> banner;
	public List<Action> actions;

	//private PrefixAction prefixAction;
	
	@Override
	public final void onEnable() {
		instance = this;
		new Debug(this);
		
		banner = new ArrayList<String>();
		actions = new ArrayList<Action>();

		Debug.log("Reading Config..");
		//new ConfigManager(this, "CoreCube.yml");

		onBoot();

		Debug.log("Loading Managers..");
		onManagers();

		
		
		Debug.log("Loading Commands..");
		onCommands();
		
		new PrefixAction(getDescription().getPrefix(), this);
		
		for (Action action : actions)
			action.ready();
		
		
		
		Debug.log(ChatColor.GREEN + "Done!");
		Debug.banner(this);
		onReady();

		// Using a schedular so that it runs once ALL plugins are loaded.
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				onStart();
			}
		});
	}

	protected abstract void onBoot();

	protected abstract void onManagers();

	protected abstract void onCommands();

	protected abstract void onReady();

	protected abstract void onStart();

}
