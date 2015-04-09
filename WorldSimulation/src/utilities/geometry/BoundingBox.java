package utilities.geometry;

public class BoundingBox {
	
	//---Object Data
	private double xMin, xMax, yMin, yMax, zMin, zMax;

	// ---Constructors
	public BoundingBox(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
		// 
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.zMin = zMin;
		this.zMax = zMax;
	}

	// ---Methods
	
	public double xMin() { return this.xMin; }
	
	public double xMax() { return this.xMax; }
	
	public double xMid() { return (this.xMax + this.xMin) / 2.0; }
	
	public double yMin() { return this.yMin; }
	
	public double yMax() { return this.yMax; }
	
	public double yMid() { return (this.yMax + this.yMin) / 2.0; }
	
	public double zMin() { return this.zMin; }
	
	public double zMax() { return this.zMax; }
	
	public double zMid() { return (this.zMax + this.zMin) / 2.0; }
}
