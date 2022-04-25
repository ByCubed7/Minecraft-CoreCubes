package io.github.bycubed7.corecubes.unit;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/** Target selectors are used in commands to select players and entities arbitrarily, 
 * without needing to specify an exact player name or a UUID. 
 * 
 * One or more entities can be selected with a target selector variable, and targets 
 * can be filtered from the selection based on certain criteria using the target 
 * selector arguments.
 * 
 * see {@linkplain https://minecraft.fandom.com/wiki/Target_selectors} 
 * @author ByCubed7
 */
public class Selector implements Convertable {
	public HashSet<Player> players;
	
	private Player owner;
	private Location location;
	
	public Selector(Player player) {
		owner = player;
		location = player.getLocation();
	}

	public Selector add(String string) {
		// Is the string a selector?
		if (string.contains("@")) addSelection(string);
		else addPlayer(string);
		return this;
	}
	
	public Selector addPlayer(Player player) {
		players.add(player);
		return this;
	}

	public Selector addPlayer(String string) {
		players.add(Bukkit.getPlayer(string));
		return this;
	}
	
	public Selector addSelection(String string) {
		HashSet<Player> playersToAdd = new HashSet<Player>();
		
		// ----------------------------------
		// - - First, deal with the selectors
		
		// Targets the entity (alive or not) that executed the command
		if (string.startsWith("@s")) {
			string.replace("@s", "");
			playersToAdd.add(owner);
		}
		
		// Targets the nearest player from the command's execution. 
		// CHECK: If there are multiple nearest players, caused by them being precisely 
		//   the same distance away, the player who most recently joined the server is selected.
		else if (string.startsWith("@p")) {
			string.replace("@p", "");
			Player closest = null;
			double lastDistance = Double.MAX_VALUE;
			for(Player p : location.getWorld().getPlayers()) {
			    if(owner.equals(p))
			        continue;
			 
			    double distance = location.distance(p.getLocation());
			    if(distance < lastDistance) {
			        lastDistance = distance;
			        closest = p;
			    }
			}
			playersToAdd.add(closest);
		}
		
		// Targets a random player.
		else if (string.startsWith("@r")) {
			string.replace("@r", "");
			Random rand = new Random();
			List<Player> players = location.getWorld().getPlayers();
			playersToAdd.add(players.get(rand.nextInt(players.size())));
		}
		
		// Targets every player
		else if (string.startsWith("@a")) {
			string.replace("@a", "");
			playersToAdd.addAll(location.getWorld().getPlayers());
		}
		
		// -----------------------------
		// TODO: - Now, deal with select arguments
		
		// [distance=10] — Target all players exactly ten blocks away.
		// [distance=8..16] — Target all players more than eight blocks, but less than 16 blocks away
		
		// [x=1,dx=4,y=2,dy=5,z=3,dz=6] — Select all players whose hitbox collides with the block region (1~5, 2~7, 3~9) 
		//     (or, mathematically speaking, the region that is {(x,y,z)∈R3|x∈[1.0,5.0),y∈[2.0,7.0),z∈[3.0,9.0)}).
		
		players.addAll(playersToAdd);
		return this;
	}
	
	public HashSet<Player> get() {
		return players;
	}

	@Override
	public void fromString(String string) {
		// TODO:
		//  public HashSet<Player> players;
		//  private Player owner;
		//  private Location location;		
	}
	
}
