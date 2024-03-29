package io.github.bycubed7.corecubes.unit;

import java.io.Serializable;

import org.bukkit.Location;

public class Vector3Int implements Convertable, Serializable {
	private static final long serialVersionUID = -5050345705306273983L;

	public Integer x;
	public Integer y;
	public Integer z;

	public Vector3Int() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector3Int(Integer x, Integer y, Integer z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vector3Int fromLocation(Location loc) {
		return new Vector3Int(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public void add(Vector3Int otherVector) {
		x += otherVector.x;
		y += otherVector.y;
		z += otherVector.z;
	}

	public void add(int x1, int y1, int z1) {
		x += x1;
		y += y1;
		z += z1;
	}

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;

		if (!(o instanceof Vector3Int))
			return false;

		Vector3Int c = (Vector3Int) o;

		if (Integer.compare(x, c.x) != 0)
			return false;
		if (Integer.compare(y, c.y) != 0)
			return false;
		if (Integer.compare(z, c.z) != 0)
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		return x.toString() +" "+ y.toString() +" "+ z.toString();
	}

	@Override
	public void fromString(String string) {
		String[] dataSplit = string.split(" ");
		x = Integer.parseInt(dataSplit[0]);
		y = Integer.parseInt(dataSplit[1]);
		z = Integer.parseInt(dataSplit[2]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}
}
