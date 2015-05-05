package datastructures.graphics;

import datastructures.geometry.Point;
import datastructures.geometry.Vector3;

public class Vertex {
	
	//---Object Data
	Point mPosition;
	Vector3 mNormal;
	float[] mColor;
	
	//---Constructors
	public Vertex() {
		this(0, 0, 0, new float[] {1f,1f,1f});
	}
	
	public Vertex(double x, double y, double z) {
		this(x, y, z, new float[] {1f,1f,1f});
	}
	
	public Vertex(double x, double y, double z, float[] color) {
		this.mPosition = new Point(x, y, z);
		this.mNormal = new Vector3(0,0,1);
		this.mColor = color;
	}
	
	//---Methods
	
	public double x() { return this.mPosition.x; }
	
	public double y() { return this.mPosition.y; }
	
	public double z() { return this.mPosition.z; }
	
	public Point position() { return this.mPosition; }
	
	public void setPosition(double x, double y, double z) {
		this.mPosition = new Point(x, y, z);
	}
	
	public float[] color() { return this.mColor; }
	
	public void setColor(float[] color) {
		this.mColor = color;
	}
	
	public Vector3 normal() { return this.mNormal; }
	
	public void setNormal(Vector3 normal) { this.mNormal = normal; }
	
	@Override
	public boolean equals(Object o) {
		// If the object is null, return false
		if (o == null) return false;
		// If the object is actually a Vertex
		if (o instanceof Vertex) {
			// Cast it to a Vertex object
			Vertex other = (Vertex)o;
			// If both have the same position and normal, return true
			if (this.mPosition.equals(other.mPosition) && this.mNormal.equals(other.mNormal))
				return true;
			// Otherwise return false
			else return false;
		}
		// Otherwise return false
		else return false;
	}
	
	@Override
	public int hashCode() {
		// Calculate hash code using a unique prime times hash code of position and normal (cast to int)
		return (int)(this.mPosition.hashCode() + 7*this.mNormal.hashCode());
	}
}
