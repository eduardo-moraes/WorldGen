package utilities.graphics;

import utilities.arrays.Vector3;
import utilities.geometry.Point;

public class Vertex {
	
	//---Object Data
	Point mPosition;
	//double x, y, z;
	Vector3 mNormal;
	
	//---Constructors
	public Vertex() {
		this(0, 0, 0);
	}
	
	public Vertex(double x, double y, double z) {
		this.mPosition = new Point(x, y, z);
		this.mNormal = new Vector3();
	}
	
	//---Methods
	
	public double getX() { return this.mPosition.getX(); }
	
	public double getY() { return this.mPosition.getY(); }
	
	public double getZ() { return this.mPosition.getZ(); }
	
	public Point getPosition() { return this.mPosition; }
	
	public void setNormal(Vector3 normal) { this.mNormal = normal; }
	
	public Vector3 getNormal() { return this.mNormal; }
	
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
