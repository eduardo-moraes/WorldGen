package datastructures.geometry;

public class BoundingBox {
	
	//---Object Data
	public final double xMin, xMid, xMax, xRange;
	public final double yMin, yMid, yMax, yRange;
	public final double zMin, zMid, zMax, zRange;

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
		// Calculate and set the ranges
		this.xRange = (xMax - xMin);
		this.yRange = (yMax - yMin);
		this.zRange = (zMax - zMin);
	}

	// ---Methods
}
