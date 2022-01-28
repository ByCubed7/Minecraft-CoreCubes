package io.github.bycubed7.corecubes.unit;

import java.io.Serializable;

public class Vector3Int implements Serializable {
	private static final long serialVersionUID = -5050345705306273983L;

	public int x;
	public int y;
	public int z;

	public Vector3Int(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void add(Vector3Int otherVector) {
		x += otherVector.x;
		y += otherVector.y;
		z += otherVector.z;
	}
}
