package datastructures;

public class Coordinate {

	//---Object Data
	public int x;
	public int y;
	
	//---Constructors
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//---Methods
	
	public double distanceTo(Coordinate other) {
		double dx = (double)(this.x - other.x);
		double dy = (double)(this.y - other.y);
		double distance = Math.sqrt(dx*dx + dy*dy);
		return distance;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o instanceof Coordinate) {
			Coordinate other = (Coordinate)o;
			if (other.x == this.x && other.y == this.y) return true;
			else return false;
		}
		else return false;
	}
	
	@Override
	public int hashCode() {
		return 31*x + y;
	}
}
