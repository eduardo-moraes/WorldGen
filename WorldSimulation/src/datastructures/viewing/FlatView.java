package datastructures.viewing;

import utilities.Utilities;
import datastructures.geometry.BoundingBox;
import datastructures.geometry.Point;

public class FlatView {
	
	//---Object Data
	final BoundingBox mViewBounds;
	Point mLocation;
	
	final double mMinXRadius, mMaxXRadius;
	final double mMinYRadius, mMaxYRadius;
	double mXRadius, mYRadius;

	//---Constructors
	public FlatView(BoundingBox viewedBounds) {
		// 
		double xMin = viewedBounds.xMin;
		double xMax = viewedBounds.xMax;
		double yMin = viewedBounds.yMin;
		double yMax = viewedBounds.yMax;
		double zMin = 0;
		double zMax = 1;
		this.mViewBounds = new BoundingBox(xMin, xMax, yMin, yMax, zMin, zMax);
		
		// 
		this.mLocation = new Point(viewedBounds.xMid, viewedBounds.yMid, zMax);
		
		// 
		double width = viewedBounds.xRange;
		double height = viewedBounds.yRange;
		this.mMinXRadius = 1;
		this.mMaxXRadius = width / 2.0;
		this.mMinYRadius = 1;
		this.mMaxYRadius = height / 2.0;
		// 
		this.mXRadius = (mMaxXRadius - mMinXRadius)*mLocation.z + mMinXRadius;
		this.mYRadius = (mMaxYRadius - mMinYRadius)*mLocation.z + mMinYRadius;
	}

	//---Methods
	
	public double x() { return this.mLocation.x; }
	
	public double y() { return this.mLocation.y; }
	
	public double z() { return this.mLocation.z; }
	
	public double xRadius() { return this.mXRadius; }
	
	public double yRadius() { return this.mYRadius; }
	
	public void moveView(double dx, double dy) {
		double newX = Utilities.clamp(mLocation.x+dx, mViewBounds.xMin, mViewBounds.xMax);
		double newY = Utilities.clamp(mLocation.y+dy, mViewBounds.yMin, mViewBounds.yMax);
		this.mLocation = new Point(newX, newY, mLocation.z);
	}
	
	public void zoomView(double dz) {
		double newZ = Utilities.clamp(mLocation.z+dz, mViewBounds.zMin, mViewBounds.zMax);
		this.mLocation = new Point(mLocation.x, mLocation.y, newZ);
		this.mXRadius = (mMaxXRadius - mMinXRadius)*mLocation.z + mMinXRadius;
		this.mYRadius = (mMaxYRadius - mMinYRadius)*mLocation.z + mMinYRadius;
	}
}
