package io.github.bycubed7.corecubes.unit;

import java.io.Serializable;

import org.bukkit.Location;

public class Vector2Int implements Convertable, Serializable {
	private static final long serialVersionUID = 7228021146726359199L;

	public Integer x;
	public Integer y;

	public Vector2Int() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2Int(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vector2Int fromLocation(Location loc) {
		return new Vector2Int(loc.getBlockX(), loc.getBlockZ());
	}

	public void add(Vector3Int otherVector) {
		x += otherVector.x;
		y += otherVector.y;
	}

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;

		if (!(o instanceof Vector2Int))
			return false;

		Vector2Int c = (Vector2Int) o;

		if (Integer.compare(x, c.x) != 0)
			return false;
		if (Integer.compare(y, c.y) != 0)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return x.toString() + " " + y.toString();
	}

	@Override
	public void fromString(String string) {
		String[] dataSplit = string.split(" ");
		x = Integer.parseInt(dataSplit[0]);
		y = Integer.parseInt(dataSplit[1]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
}
