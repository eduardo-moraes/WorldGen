package utilities.graphics;

import utilities.geometry.Transform;

public class Camera {
	
	//---Object Data
	Transform mLocation;
	final double w, h;
	final double translationSpeed;

	//---Constructors
	public Camera(double width, double height) {
		// Set width and height to given values
		this.w = width;
		this.h = height;
		// Set location to 0,0,1
		this.mLocation = new Transform(0, 0, 1);
		// 
		this.translationSpeed = 1;
	}

	//---Methods
	
	public Transform position() {
		return mLocation;
	}
	
	public void moveLeft() {
		mLocation.translate(-translationSpeed, 0, 0);
	}
	
	public void moveRight() {
		mLocation.translate(translationSpeed, 0, 0);
	}
	
	public void moveUp() {
		mLocation.translate(0, translationSpeed, 0);
	}
	
	public void moveDown() {
		mLocation.translate(0, -translationSpeed, 0);
	}
}
