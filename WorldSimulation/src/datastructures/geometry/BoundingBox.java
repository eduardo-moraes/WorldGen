package datastructures.geometry;

public class BoundingBox {
	
	//---Object Data
	public final double xMin, xMid, xMax;
	public final double yMin, yMid, yMax;
	public final double zMin, zMid, zMax;

	// ---Constructors
	public BoundingBox(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
		// Set the bounds to the given values
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.zMin = zMin;
		this.zMax = zMax;
		// Calculate and set the midpoints
		this.xMid = (xMin + xMax) / 2.0;
		this.yMid = (yMin + yMax) / 2.0;
		this.zMid = (zMin + zMax) / 2.0;
	}

	// ---Methods
}
