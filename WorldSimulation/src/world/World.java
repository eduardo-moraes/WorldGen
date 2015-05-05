package world;

import proceduralgeneration.ElevationMapGenerator;
import utilities.Utilities;
import utilities.VertexStreamer;
import utilities.geometry.Transform;
import utilities.graphics.Camera;
import utilities.graphics.Mesh;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import datamaps.ElevationMap;
import interfaces.Renderable;

public class World {
	
	//---Object Data
	double w, h;
	int mMode = 0;
	double resolution = 200;
	
	ElevationMap temp;
	Mesh mMesh;
	Transform mLocation;
	
	int xMax, yMax;
	int xSelect, ySelect;
	Region[][] mRegions;
	//---------------TEST ZONE
	int mW, mH;
	//---------------END TEST ZONE
	
	//---Constructors
	public World(double width, double height) {
		this.w = width;
		this.h = height;
		
		this.xSelect = 3;
		this.ySelect = 3;
		
		this.mLocation = new Transform();
		
		createNew();
		//this.mRegions = Region.splitMap(temp);
		//swapRegion();
		this.mMesh = VertexStreamer.buildMeshMin(temp, resolution, resolution);
		/*
		this.mMesh = mRegions[xSelect][ySelect].getMesh();
		this.mMesh = temp.toMesh(temp.getWidth(), temp.getHeight());
		*/
	}
	
	//---Methods
	
	public double getWidth() { return this.w; }
	
	public double getHeight() { return this.h; }
	
	private void createNew() {
		temp = ElevationMapGenerator.getBlank(3);
		temp.setElev(10.0, 1, 1);
		temp = ElevationMapGenerator.SquareSquare(temp, 7);
		ElevationMapGenerator.filter(temp, 3, 0);
		System.out.println(temp.getWidth() + " " + temp.getHeight());
		
		// DO NOT DISTURB
		this.xMax = 5;
		this.yMax = 5;
		this.mRegions = new Region[xMax][yMax];
		for (int x = 0; x < xMax; ++x)
			for (int y = 0; y < yMax; ++y)
				mRegions[x][y] = new Region(10.0, 10.0);
	}
	
	public void prevRegion() {
		// If moving left passes the left end of a row
		if (--xSelect < 0) {
			// Move to right end and decrement column
			xSelect = xMax-1;
			// If moving up passes the top of the column, move to bottom
			if (--ySelect < 0) ySelect = yMax-1;
		}
		// 
		swapRegion();
	}
	
	public void nextRegion() {
		// If moving right passes the right end of a row
		if (++xSelect >= xMax) {
			// Move to left end and increment column
			xSelect = 0;
			// If moving down passes the bottom of the column, move to top
			if (++ySelect >= yMax) ySelect = 0;
		}
		// 
		swapRegion();
	}
	
	public int getRegionX() { return this.xSelect; }
	
	public int getRegionY() { return this.ySelect; }
	
	public void swapRegion() {
		// 
		this.mMesh = mRegions[xSelect][ySelect].getMesh();
	}
	
	public void smoothMap() {
		resolution *= 2;
		this.mMesh = VertexStreamer.buildMeshMin(temp, resolution, resolution);
		/*
		ElevationMapGenerator.filter(mRegions[xSelect][ySelect].mElevMap, 3, 0);
		mRegions[xSelect][ySelect].resetMesh();
		mMesh = mRegions[xSelect][ySelect].getMesh();
		*/
	}
	
	public void sharpenMap() {
		resolution /= 2;
		this.mMesh = VertexStreamer.buildMeshMin(temp, resolution, resolution);
		/*
		ElevationMapGenerator.filter(mRegions[xSelect][ySelect].mElevMap, 3, 1);
		mRegions[xSelect][ySelect].resetMesh();
		mMesh = mRegions[xSelect][ySelect].getMesh();
		*/
	}
	
	public void edgeDetectMap() {
		ElevationMapGenerator.filter(mRegions[xSelect][ySelect].mElevMap, 3, 2);
		mRegions[xSelect][ySelect].resetMesh();
		mMesh = mRegions[xSelect][ySelect].getMesh();
	}
	
	public void render(GLAutoDrawable drawable, Camera camera) {
		// If there is no mesh, return
		if (mMesh == null) return;
		if (mLocation == null) return;
		
		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();
		
		// 
		Transform relativeLocation = new Transform(mLocation, camera.position());
		
		double scaleFactor = 1/resolution;
		gl.glScaled(scaleFactor, scaleFactor, scaleFactor);
		this.mMesh.render(drawable, relativeLocation);
		
		/*
		mRegions[xSelect][ySelect].getMesh().render(drawable);
		// LEFT
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
			gl.glTranslated(10, 0, 0);
			mRegions[Utilities.indexWrap(xSelect+1, xMax)][ySelect].getMesh().render(drawable);
		gl.glPopMatrix();
		// RIGHT
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
			gl.glTranslated(-10, 0, 0);
			mRegions[Utilities.indexWrap(xSelect-1, xMax)][ySelect].getMesh().render(drawable);
		gl.glPopMatrix();
		// TOP
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
			gl.glTranslated(0, 10, 0);
			mRegions[xSelect][Utilities.indexWrap(ySelect-1, yMax)].getMesh().render(drawable);
		gl.glPopMatrix();
		// BOTTOM
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
			gl.glTranslated(0, -10, 0);
			mRegions[xSelect][Utilities.indexWrap(ySelect+1, yMax)].getMesh().render(drawable);
		gl.glPopMatrix();
		// TOP-LEFT
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
			gl.glTranslated(10, 10, 0);
			mRegions[Utilities.indexWrap(xSelect+1, xMax)][Utilities.indexWrap(ySelect-1, yMax)].getMesh().render(drawable);
		gl.glPopMatrix();
		// TOP-RIGHT
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
			gl.glTranslated(-10, 10, 0);
			mRegions[Utilities.indexWrap(xSelect-1, xMax)][Utilities.indexWrap(ySelect-1, yMax)].getMesh().render(drawable);
		gl.glPopMatrix();
		// BOTTOM-
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
			gl.glTranslated(10, -10, 0);
			mRegions[Utilities.indexWrap(xSelect+1, xMax)][Utilities.indexWrap(ySelect+1, yMax)].getMesh().render(drawable);
		gl.glPopMatrix();
		//
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
			gl.glTranslated(-10, -10, 0);
			mRegions[Utilities.indexWrap(xSelect-1, xMax)][Utilities.indexWrap(ySelect+1, yMax)].getMesh().render(drawable);
		gl.glPopMatrix();
		*/
		
		/*
		// 
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		double scaleFactor = 0.1;
		// 
		if (mMode == 0) {
			gl.glScaled(scaleFactor, scaleFactor, scaleFactor);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			mMesh.render(drawable);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		}
		else {
			// 
			gl.glScaled(0.4, 0.4, 0.4);
			gl.glRotated(45, 0, 0, 1);
			gl.glRotated(-45, 1, -1, 0);
			
			gl.glColor3d(0.0, 0.0, 1.0);
			gl.glBegin(GL2.GL_POLYGON);
				gl.glVertex3d(-1, -1, -0.01);
				gl.glVertex3d(-1, 1, -0.01);
				gl.glVertex3d(1, 1, -0.01);
				gl.glVertex3d(1, -1, -0.01);
			gl.glEnd();
			
			gl.glColor3d(1, 0, 0);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
			mMesh.render(drawable);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		}
		// 
		gl.glPopMatrix();
		*/
	}
}
