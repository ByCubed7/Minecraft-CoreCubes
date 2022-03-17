package io.github.bycubed7.corecubes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.corecubes.managers.Debug;

public abstract class CubePlugin extends JavaPlugin {

	public static CubePlugin instance;
	public List<String> banner = new ArrayList<String>();

	@Override
	public final void onEnable() {
		instance = this;
		new Debug(this);

		Debug.log("Reading Config..");
		//new ConfigManager(this, "CoreCube.yml");

		onBoot();

		Debug.log("Loading Managers..");
		onManagers();

		Debug.log("Loading Listeners..");
		onListeners();

		Debug.log("Loading Commands..");
		onCommands();

		Debug.log(ChatColor.GREEN + "Done!");
		Debug.banner(this);
		onReady();

		// Using a schedular so that it runs once ALL plugins are loaded.
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				onStart();
			}
		});
	}

	protected abstract void onBoot();

	protected abstract void onManagers();

	protected abstract void onListeners();

	protected abstract void onCommands();

	protected abstract void onReady();

	protected abstract void onStart();

}
