package datastructures.viewing;

import datastructures.geometry.Point;
import datastructures.geometry.Vector3;

public class Camera {
	
	//---Object Data
	final int mW, mH;
	final double mAspectRatio;
	final double mFov;
	static final double DEFAULT_FOV = 90;
	final int mFps;
	static final int DEFAULT_FPS = 30;
	final double mZnear, mZfar;
	static final double DEFAULT_NEAR = 0.01;
	static final double DEFAULT_FAR = 100;
	Point mPosition;
	Point mAt;
	Vector3 mUp;
	final double translationSpeed;

	//---Constructors
	public Camera(int width, int height) {
		// Setup default camera at POSITION (0, 0, 1), looking AT the origin, with UP along y-axis
		this(width, height, new Point(0, 0, 1), new Point(), new Vector3(0, 1, 0));
	}
	
	public Camera(int width, int height, Point position, Point at, Vector3 up) {
		// Set width and height; calculate aspect ratio
		this.mW = width;
		this.mH = height;
		this.mAspectRatio = (double)(width) / (double)(height);
		
		// Set field of view and frames per second to default values
		this.mFov = DEFAULT_FOV;
		this.mFps = DEFAULT_FPS;
		
		// 
		this.mZnear = DEFAULT_NEAR;
		this.mZfar = DEFAULT_FAR;
		
		// Set position, at, and up to given values
		this.mPosition = position;
		this.mAt = at;
		this.mUp = up;
		
		// 
		this.translationSpeed = 1;
	}

	//---Methods
	
	public int w() { return this.mW; }
	
	public int h() { return this.mH; }
	
	public double aspectRatio() { return this.mAspectRatio; }
	
	public double fov() { return this.mFov; }
	
	public int fps() { return this.mFps; }
	
	public double near() { return this.mZnear; }
	
	public double far() { return this.mZfar; }
	
	public Point position() { return this.mPosition; }
	
	public Point at() { return this.mAt; }
	
	public Vector3 up() { return this.mUp; }
	
	public void moveLeft() {
		//mLocation.translate(-translationSpeed, 0, 0);
	}
	
	public void moveRight() {
		//mLocation.translate(translationSpeed, 0, 0);
	}
	
	public void moveUp() {
		//mLocation.translate(0, translationSpeed, 0);
	}
	
	public void moveDown() {
		//mLocation.translate(0, -translationSpeed, 0);
	}
}
