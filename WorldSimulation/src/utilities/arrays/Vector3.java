package utilities.arrays;

import utilities.geometry.Point;

public class Vector3 {

	//---Object Data
	double x, y, z;

	// ---Constructors
	public Vector3() {
		this(0, 0, 0);
	}
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(Point start, Point end) {
		this.x = (end.getX() - start.getX());
		this.y = (end.getY() - start.getY());
		this.z = (end.getZ() - start.getZ());
	}

	// ---Methods
	
	public double getX() { return this.x; }

	public double getY() { return this.y; }

	public double getZ() { return this.z; }
	
	public void add(Vector3 other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
	}
	
	public void multiply(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
	}
	
	public Vector3 cross(Vector3 other) {
		// 
		double newX, newY, newZ;
		
		// 
		newX = (this.y*other.z - other.y*this.z);
		newY = (this.z*other.x - other.z*this.x);
		newZ = (this.x*other.y - other.x*this.y);
		
		// 
		return new Vector3(newX, newY, newZ);
	}
	
	@Override
	public boolean equals(Object o) {
		// If the object is null, return false
		if (o == null) return false;
		// If the object is actually a Vector3
		if (o instanceof Vector3) {
			// Cast it to a Vector3 object
			Vector3 other = (Vector3) o;
			// If all three dimensions are within a small distance of each other, return true
			if (Math.abs(other.x - this.x) < 0.001
			 && Math.abs(other.y - this.y) < 0.001
			 && Math.abs(other.z - this.z) < 0.001)
				 return true;
			// Otherwise return false
			else return false;
		}
		// Otherwise return false
		else return false;
	}

	@Override
	public int hashCode() {
		// Calculate hash code using a unique prime times each dimension (cast to int)
		return (int) (47 * x + 31 * y + 19 * z);
	}
}
