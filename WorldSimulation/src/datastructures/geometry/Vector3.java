package datastructures.geometry;


public class Vector3 {

	//---Object Data
	public final double x, y, z;
	public final double length;

	// ---Constructors
	public Vector3() {
		this(0, 0, 0);
	}
	
	public Vector3(Point start, Point end) {
		this((end.x - start.x), (end.y - start.y), (end.z - start.z));
	}
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.length = Math.sqrt((x*x) + (y*y) + (z*z));
	}

	// ---Methods
	
	public Vector3 add(Vector3 other) {
		// Add each dimension to get the new vector
		double newX = this.x + other.x;
		double newY = this.y + other.y;
		double newZ = this.z + other.z;
		return new Vector3(newX, newY, newZ);
	}
	
	public Vector3 multiply(double scalar) {
		// Multiply each dimension by the scalar
		double newX = this.x*scalar;
		double newY = this.y*scalar;
		double newZ = this.z*scalar;
		return new Vector3(newX, newY, newZ);
	}
	
	public Vector3 cross(Vector3 other) {
		// 
		double newX = (this.y*other.z - other.y*this.z);
		double newY = (this.z*other.x - other.z*this.x);
		double newZ = (this.x*other.y - other.x*this.y);
		return new Vector3(newX, newY, newZ);
	}
	
	public Vector3 normalized() {
		if (Math.abs(this.length-1.0) < 0.0001) return this;
		else return new Vector3(x/length, y/length, z/length);
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
	
	@Override
	public String toString() {
		return this.x + ", " + this.y + ", " + this.z;
	}
}
