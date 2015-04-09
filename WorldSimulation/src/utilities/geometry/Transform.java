package utilities.geometry;

public class Transform {
	
	//---Object Data
	Point mPosition;
	double mXscale, mYscale, mZscale;

	//---Constructors
	public Transform() {
		// Create a transform at 0,0,0
		this(0.0, 0.0, 0.0);
	}
	
	public Transform(Transform other, double dx, double dy, double dz) {
		// 
		this(other.xPos()+dx, other.yPos()+dy, other.zPos()+dz);
	}
	
	public Transform(Transform first, Transform second) {
		// 
		this(first.xPos()-second.xPos(), first.yPos()-second.yPos(), first.zPos()-second.zPos());
	}
	
	public Transform(double x, double y, double z) {
		// Create point defining position using x,y,z
		mPosition = new Point(x, y, z);
		// Set scale factor in all dimensions to 1
		mXscale = 1.0;
		mYscale = 1.0;
		mZscale = 1.0;
	}

	//---Methods
	
	public double xPos() { return this.mPosition.getX(); }
	
	public double yPos() { return this.mPosition.getY(); }
	
	public double zPos() { return this.mPosition.getZ(); }
	
	public void translate(double dx, double dy, double dz) {
		this.mPosition = new Point(mPosition, dx, dy, dz);
	}
	
	public double xScale() {return this.mXscale; }
	
	public double yScale() {return this.mYscale; }
	
	public double zScale() {return this.mZscale; }
}
