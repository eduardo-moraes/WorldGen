package world;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.crypto.MacSpi;
import javax.imageio.ImageIO;

import proceduralgeneration.ElevationMapGenerator;
import utilities.Utilities;
import utilities.VertexStreamer;
import utilities.graphics.Camera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import datamaps.ElevationMap;
import datastructures.graphics.Mesh;

public class World {
	
	//---Object Data
	public final double w, h;
	int mMode = 0;
	double resolution = 100;
	
	ElevationMap temp;
	Mesh mMesh;
	double mWaterLevel = 0.22;
	
	int xMax, yMax;
	int xSelect, ySelect;
	Region[][] mRegions;
	//---------------TEST ZONE
	int mW, mH;
	//---------------END TEST ZONE
	
	//---Constructors
	public World() {
		this.xSelect = 3;
		this.ySelect = 3;
		
		//createNew();
		//temp.normalize();
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("earth.jpg"));
		}
		catch(IOException e) {
			System.exit(0);
		}
		int tempW = img.getWidth() / 2;
		int tempH = img.getHeight();
		this.w = tempW+1.0;
		this.h = tempH+1.0;
		temp = new ElevationMap(tempW, tempH);
		for (int x = 0; x < tempW; ++x) {
			for (int y = 0; y < tempH; ++y) {
				int value = new Color(img.getRGB(x, y)).getRed();
				temp.setElev(value, x, tempH-1-y);
			}
		}
		temp.normalize();
		
		//this.mRegions = Region.splitMap(temp);
		//swapRegion();
		//this.mMesh = VertexStreamer.buildMeshMin(temp, resolution, resolution);
		/*
		this.mMesh = mRegions[xSelect][ySelect].getMesh();
		this.mMesh = temp.toMesh(temp.getWidth(), temp.getHeight());
		*/
	}
	
	//---Methods
	
	public double getWidth() { return this.w; }
	
	public double getHeight() { return this.h; }
	
	public double getElevation(double x, double y, double width, double height) {
		// 
		double worldX = Utilities.wrap(x, w);
		double worldY = Utilities.wrap(y, h);
		
		// 
		double maxSize = 5.0;
		// 
		double halfWidth  = (width > maxSize ? maxSize / 2.0 : width / 2.0);
		double halfHeight = (height > maxSize ? maxSize / 2.0 : height / 2.0);
		
		// 
		int leftIndex   = (int)Math.floor(worldX - halfWidth);
		int rightIndex  = (int)Math.ceil(worldX + halfWidth);
		int bottomIndex = (int)Math.floor(worldY - halfHeight);
		int topIndex    = (int)Math.ceil(worldY + halfHeight);
		
		// 
		int neighborsX = Math.abs(rightIndex-leftIndex)+1;
		int neighborsY = Math.abs(topIndex-bottomIndex)+1;
		// 
		double weightedValue = 0.0;
		int sum = 0;
		//double sum = 0.0;
		// 
		for (int i = 0; i < neighborsX; ++i) {
			for (int j = 0; j < neighborsY; ++j) {
				// Find the index for the next data value
				int dataX = Utilities.indexWrap(leftIndex+i, temp.w);
				int dataY = Utilities.indexWrap(bottomIndex+j, temp.h);
				// Find the location of that data value in the real world
				//double xLocation = (double)(leftIndex+i) + 0.5;
				//double yLocation = (double)(bottomIndex+j) + 0.5;
				// Calculate the inverse distance to that location
				//double inverseDistance = 1 / Utilities.distance(worldX, worldY, xLocation, yLocation);
				// Multiply the data at this index by the inverse distance and accumulate
				//weightedValue += temp.getElev(dataX, dataY)*inverseDistance;
				//sum += inverseDistance;
				weightedValue += temp.getElev(dataX, dataY);
				++sum;
			}
		}
		// Divide total elevation data by sum of weights
		weightedValue /= (double)sum;
		// Return the weighted average elevation
		return weightedValue;
	}
	
	public float[] getColor(double z) {
		float[] rgb = new float[] {0,0,0};
		
		double minBlue = 0.2;
		double maxBlue = 0.75;
		
		if (z <= mWaterLevel) {
			rgb[2] = (float)((maxBlue-minBlue)*(z / mWaterLevel) + minBlue);
			return rgb;
		}
		else rgb[2] = (float)minBlue;
		/*
		double blue0 = 0.00;
		double blue1 = 0.05;
		double blue2 = 0.142;
		double blue3 = 0.148;
		
		if (value >= blue0 && value < blue1) rgb[2] = (float)((value-blue0) / (blue1-blue0));
		else if (value >= blue1 && value < blue2) rgb[2] = 1;
		else if (value >= blue2 && value < blue3) rgb[2] = (float)((blue3-value) / (blue3-blue2));
		else rgb[2] = 0;
		*/
		
		double minGreen = 0.05;
		double maxGreen = 0.70;
		
		double green0 = mWaterLevel;
		double green1 = green0 + 0.001;
		double green2 = green1 + 0.3;
		
		if (z < green0) rgb[1] = 0;
		else if (z > green0 && z < green1) rgb[1] = (float)maxGreen;
		else if (z >= green1 && z < green2) rgb[1] = (float)((maxGreen-minGreen)*((green2-z) / (green2-green1)) + minGreen);
		else rgb[1] = (float)minGreen;
		
		double minRed = 0.20;
		double maxRed = 0.70;
		
		double red0 = mWaterLevel;
		double red1 = red0 + 0.001;
		double red2 = red1 + 0.4;
		
		if (z < red0) rgb[0] = 0;
		else if (z > red0 && z < red1) rgb[0] = (float)minRed;
		else if (z >= red1 && z < red2) rgb[0] = (float)((maxRed-minRed)*((z-red1) / (red2-red1)) + minRed);
		else rgb[0] = (float)maxRed;
		
		return rgb;
	}
	
	public String getAreaName(double x, double y, double width, double height) {
		double elevation = getElevation(x, y, width, height);
		if (elevation < mWaterLevel) return "Ocean";
		else if (elevation > mWaterLevel) return "Land";
		else return "Something?";
	}
	
	private void createNew() {
		temp = ElevationMapGenerator.getBlank(3);
		temp.setElev(10.0, 1, 1);
		temp = ElevationMapGenerator.SquareSquare(temp, 7);
		ElevationMapGenerator.filter(temp, 3, 0);
		System.out.println(temp.w + " " + temp.h);
		
		/* DO NOT DISTURB
		this.xMax = 5;
		this.yMax = 5;
		this.mRegions = new Region[xMax][yMax];
		for (int x = 0; x < xMax; ++x)
			for (int y = 0; y < yMax; ++y)
				mRegions[x][y] = new Region(10.0, 10.0);
		*/
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
		
		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();
		
		// 
		//Transform relativeLocation = new Transform(mLocation, camera.position());
		
		double scaleFactor = 1/resolution;
		gl.glScaled(scaleFactor, scaleFactor, scaleFactor);
		this.mMesh.render(drawable);
		
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
