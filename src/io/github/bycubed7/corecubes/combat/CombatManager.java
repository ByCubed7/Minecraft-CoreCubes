package io.github.bycubed7.corecubes.combat;

import org.bukkit.Bukkit;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.events.PlayerDeathByEntityEvent;
import io.github.bycubed7.corecubes.events.PlayerDeathByEnviromentEvent;
import io.github.bycubed7.corecubes.managers.Manager;
import io.github.bycubed7.corecubes.unit.BiHashMap;

public class CombatManager extends Manager {

	// Player that died, the cause, and the player who's responsible
	static private BiHashMap<Player, DamageCause, Entity> causes;
	
	static {
		causes = new BiHashMap<Player, DamageCause, Entity>();
	}
	
	public CombatManager(CubePlugin _plugin) {
		super(_plugin, -1);
	}
	
	public void run() {}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Death Events

	@EventHandler
	public void TriggerPlayerDeathByEntityEvent(PlayerDeathEvent event) {		
		Player player = event.getEntity();

		// Get source
		EntityDamageEvent lastDamage = player.getLastDamageCause();
		DamageCause lastDamageCause = lastDamage.getCause();
		
		// Try to find cause
		Entity attacker = causes.get(player, lastDamageCause);
		causes.remove(player); // Found or not, clear the past
		
		
		// Create Event
		PlayerDeathEvent newEvent;
		if (attacker == null) newEvent = PlayerDeathByEnviromentEvent.fromPlayerDeath(event);
		else newEvent = PlayerDeathByEntityEvent.fromPlayerDeath(event, attacker);
		
		// Raise event
		Bukkit.getPluginManager().callEvent(newEvent);

		// Refect changes back
		event.setKeepInventory(newEvent.getKeepInventory());
		event.setKeepLevel(newEvent.getKeepLevel());
		event.setNewExp(newEvent.getNewExp());
		event.setNewLevel(newEvent.getNewLevel());
		event.setNewTotalExp(newEvent.getNewTotalExp());
		event.setDroppedExp(newEvent.getDroppedExp());
		event.setDeathMessage(newEvent.getDeathMessage());
	}
	
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Damage Events
	
	@EventHandler 
	public void OnPlayerDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		
		Player defender = (Player) event.getEntity();
		Entity attacker = event.getDamager();
		
		// Add to potential kills
		causes.put(defender, DamageCause.ENTITY_ATTACK, attacker);

		// Could be enchanted with fire aspect
		causes.put(defender, DamageCause.FIRE_TICK, attacker);

		// Could cause the player to fall to thier death
		causes.put(defender, DamageCause.FALL, attacker);
		
		// The hit could cause the player to fall into the void
		causes.put(defender, DamageCause.VOID, attacker);
		
		// Could knock the player into lava
		causes.put(defender, DamageCause.LAVA, attacker);
		
		// Could knock the player into fire?
		causes.put(defender, DamageCause.FIRE, attacker);	
	}

	@EventHandler
	public void OnEntityProjectileHitPlayer(ProjectileHitEvent event) {
		if (!(event.getHitEntity() instanceof Player)) return;
		if(!(event.getEntity().getShooter() instanceof Entity)) return;
		
		Player defender = (Player) event.getHitEntity();
		Entity attacker = (Entity) event.getEntity().getShooter();
		
		// Add to potential kills
		causes.put(defender, DamageCause.PROJECTILE, attacker);
		
		// Could be enchanted with flame
		causes.put(defender, DamageCause.FIRE_TICK, attacker);

		// Could cause the player to fall to thier death
		causes.put(defender, DamageCause.FALL, attacker);
		
		// Could cause the player to fall into the void
		causes.put(defender, DamageCause.VOID, attacker);
		
		// Could knock the player into lava
		causes.put(defender, DamageCause.LAVA, attacker);
		
		// Could knock the player into fire?
		causes.put(defender, DamageCause.FIRE,attacker);	
		
		// Could get via a trident via lightning
		causes.put(defender, DamageCause.LIGHTNING, attacker);	
		
		// Could be hit with a splash poition of harming
		//causes.put(playerHurt, DamageCause.MAGIC, new CauseOfDeath(playerDamaging, DamageCause.MAGIC));	
	}
	
	@EventHandler 
	public void OnPlayerDamageEnderCrystal(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof EnderCrystal)) return;
		if (!(event.getDamager() instanceof Player)) return;
		EnderCrystal enderCrystal = (EnderCrystal) event.getEntity();
		Player attacker = (Player) event.getDamager();
		
		for (Entity entity : enderCrystal.getNearbyEntities(10,10,10)) {
			if (!(entity instanceof Player)) continue;
			Player player = (Player) entity;
			// Add to potential kills
			causes.put(player, DamageCause.ENTITY_EXPLOSION, attacker);
		}
	}

	@EventHandler
	public void OnPlayerProjectileHitEnderCrystal(ProjectileHitEvent event) {
		// Check the projectile was from a player
		// Check the the projectile hit a player
		if(!(event.getEntity().getShooter() instanceof Player)) return;
		if (event.getHitEntity() == null) return;
		if (!(event.getHitEntity().getType().equals(EntityType.ENDER_CRYSTAL))) return;

		Player attacker = (Player) event.getEntity().getShooter();

		for (Entity entity : event.getHitEntity().getNearbyEntities(10,10,10)) {
			if (!(entity instanceof Player)) continue;
			Player player = (Player) entity;
			// Add to potential kills
			causes.put(player, DamageCause.ENTITY_EXPLOSION, attacker);
		}
	}
	
	@EventHandler
	public void OnPlayerLeave(PlayerQuitEvent event) {
		causes.remove(event.getPlayer());
	}

}
