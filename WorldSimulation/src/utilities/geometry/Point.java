package utilities.geometry;

public class Point {

	//---Object Data
	double x, y, z;
	
	//---Constructors
	public Point() {
		this(0, 0, 0);
	}
	
	public Point(Point other, double dx, double dy, double dz) {
		this(other.x+dx, other.y+dy, other.z+dz);
	}

	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// ---Methods

	public double getX() { return this.x; }

	public double getY() { return this.y; }

	public double getZ() { return this.z; }

	@Override
	public boolean equals(Object o) {
		// If the object is null, return false
		if (o == null) return false;
		// If the object is actually a Point
		if (o instanceof Point) {
			// Cast it to a Point object
			Point other = (Point) o;
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
