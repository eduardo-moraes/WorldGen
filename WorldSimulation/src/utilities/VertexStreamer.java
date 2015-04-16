package utilities;

import java.util.LinkedHashSet;
import java.util.Set;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import world.World;
import datamaps.ElevationMap;
import datastructures.geometry.BoundingBox;
import datastructures.graphics.AdaptiveMesh;
import datastructures.graphics.Mesh;
import datastructures.graphics.Triangle;
import datastructures.graphics.Vertex;
import datastructures.graphics.Mesh.MeshMode;
import datastructures.viewing.Camera;
import datastructures.viewing.FlatView;

public class VertexStreamer {
	
	private class VertexUpdater extends Thread {
		//---Object Data
		int mStart = 0;
		int mEnd = 0;
		int mCurrent = 0;
		boolean mIsActive = false;
		boolean mNeedsRun = false;
		static final int DEFAULT_RANGE = 50;

		// ---Constructors
		public VertexUpdater(int startIndex, int endIndex) {
			//
			this.mStart = startIndex;
			this.mEnd = endIndex;
			this.mCurrent = startIndex;
			// 
			this.mIsActive = false;
			this.mNeedsRun = true;
		}

		// ---Methods

		@Override
		public void run() {
			mIsActive = true;
			int yieldCounter = 0;
			int runsBeforeYield = 10;
			while (mIsActive) {
				if (mNeedsRun) {
					mNeedsRun = false;
					mCurrent = mStart;
					while (mCurrent < mEnd) {
						update();
						++mCurrent;
						if (++yieldCounter >= runsBeforeYield) {
							yieldCounter = 0;
							yield();
						}
					}
				}
				else yield();
			}
		}
		
		public void poke() { this.mNeedsRun = true; }
		
		private void update() {
			// Get the vertex that will be updated
			Vertex vert = mVerts[mCurrent];
			
			// Get the vertex position in world space
			double vertX = vert.x() * mView.xRadius() + mView.x();
			double vertY = vert.y() * mView.yRadius() + mView.y();
			// Get the average elevation at this area
			double vertZ = mWorld.getElevation(vertX, vertY, mDXview, mDYview);
			// Set the vertex's position to the new elevation level
			vert.setPosition(vert.x(), vert.y(), vertZ);
			// Set the color based on the elevation level
			//if (vertZ > waterLevel) vert.setColor(Color.GREEN);
			//else vert.setColor(Color.BLUE);
		}
	}
	
	//---Object Data
	World mWorld;
	final BoundingBox mWorldBounds;
	
	static final int FIXED_PIXEL_SIZE = 3;
	final int mXverts, mYverts;
	final double mDXvert, mDYvert;
	Vertex[] mVerts;
	
	static final double VIEW_SPEED = 10.0;
	static final double ZOOM_SPEED = 0.01;
	FlatView mView;
	double mDXview, mDYview;
	
	AdaptiveMesh mMesh;
	boolean mNeedsUpdate;
	//VertexUpdater[] mVertUpdaters;
	
	//---Constructors
	public VertexStreamer(World world, Camera camera) {
		// Store reference to world and define world bounds
		this.mWorld = world;
		this.mWorldBounds  = new BoundingBox(0, world.w, 0, world.h, 0, 0);
		
		// Calculate the number of vertices and distance between them in x and y
		this.mXverts = (camera.w() / FIXED_PIXEL_SIZE) + 1;
		this.mYverts = (camera.h() / FIXED_PIXEL_SIZE) + 1;
		this.mDXvert = 2.0 / (double)(mXverts-1);
		this.mDYvert = 2.0 / (double)(mYverts-1);
		
		// Create a flat view over the world and store distance between verts in world space
		this.mView = new FlatView(mWorldBounds);
		this.mDXview = mView.xRadius()*mDXvert;
		this.mDYview = mView.yRadius()*mDYvert;
		
		// Determine number of vertices and triangles needed
		int vertCount = mXverts*mYverts;
		int triCount = 2*(mXverts-1)*(mYverts-1);
		// Create array of vertices and triangles (represented by vertex indexes)
		mVerts = new Vertex[vertCount];
		int[][] tris   = new int[triCount][3];
		
		// Create index over linear vertex array
		int vertIndex = 0;
		// For each grid vertex position
		for (int x = 0; x < mXverts; ++x) {
			for (int y = 0; y < mYverts; ++y) {
				// Calculate the position of the vertex
				double vertX = -1.0 + x*mDXvert;
				double vertY = -1.0 + y*mDYvert;
				double vertZ = 0;
				// Create the vertex in the next linear index
				mVerts[vertIndex++] = new Vertex(vertX, vertY, vertZ);
			}
		}
		// Create index over linear triangle array
		int triIndex = 0;
		// For each grid triangle position
		for (int x = 0; x < mXverts-1; ++x) {
			for (int y = 0; y < mYverts-1; ++y) {
				// Calculate the linear indexes of the square vertices
				int topLeft		= x*(mYverts) + y;
				int topRight	= topLeft + mYverts;
				int bottomLeft	= topLeft + 1;
				int bottomRight = topRight + 1;
				// Create the first triangle
				tris[triIndex][0] = topLeft;
				tris[triIndex][1] = bottomLeft;
				tris[triIndex++][2] = bottomRight;
				// Create the second triangle
				tris[triIndex][0] = topLeft;
				tris[triIndex][1] = bottomRight;
				tris[triIndex++][2] = topRight;
			}
		}
		
		// Ensure that the right number of vertices and triangles were made
		if (vertCount != vertIndex || triCount != triIndex)
			throw new RuntimeException("VertexStreamer: Did not create the right number of vertices or triangles");
		
		// Create an adaptive mesh from these vertices and triangles
		this.mMesh = new AdaptiveMesh(mVerts, tris);
		this.mNeedsUpdate = true;
		/*
		// Calculate the number of vertex updaters needed
		int vertUpdateCount = (vertCount / VertexUpdater.DEFAULT_RANGE);
		if (vertCount % VertexUpdater.DEFAULT_RANGE > 0) ++vertUpdateCount;
		mVertUpdaters = new VertexUpdater[vertUpdateCount];
		// Divide the vertexes among the updaters
		for (int i = 0; i < vertUpdateCount; ++i) {
			int start = i * VertexUpdater.DEFAULT_RANGE;
			int end = (i + 1) * VertexUpdater.DEFAULT_RANGE;
			if (end > vertCount) end = vertCount;
			mVertUpdaters[i] = new VertexUpdater(start, end);
			//mVertUpdaters[i].start();
		}
		*/
	}

	//---Methods
	
	public String getAreaName(double x, double y) {
		if (x < -1 || x > 1 || y < -1 || y > 1) return "OUT OF BOUNDS";
		
		double worldX = x*mView.xRadius() + mView.x();
		double worldY = y*mView.yRadius() + mView.y();
		
		return mWorld.getAreaName(worldX, worldY, mDXview, mDYview);
	}
	
	public void viewLeft() {
		this.mView.moveView(-VIEW_SPEED, 0);
		this.mNeedsUpdate = true;
	}
	
	public void viewRight() {
		this.mView.moveView(VIEW_SPEED, 0);
		this.mNeedsUpdate = true;
	}
	
	public void viewUp() {
		this.mView.moveView(0, VIEW_SPEED);
		this.mNeedsUpdate = true;
	}
	
	public void viewDown() {
		this.mView.moveView(0, -VIEW_SPEED);
		this.mNeedsUpdate = true;
	}
	
	public void zoomIn() {
		this.mView.zoomView(-ZOOM_SPEED);
		this.mDXview = mView.xRadius()*mDXvert;
		this.mDYview = mView.yRadius()*mDYvert;
		this.mNeedsUpdate = true;
	}
	
	public void zoomOut() {
		this.mView.zoomView(ZOOM_SPEED);
		this.mDXview = mView.xRadius()*mDXvert;
		this.mDYview = mView.yRadius()*mDYvert;
		this.mNeedsUpdate = true;
	}
	
	public void maxZoomIn() {
		this.mView.zoomView(-2.0);
		this.mDXview = mView.xRadius()*mDXvert;
		this.mDYview = mView.yRadius()*mDYvert;
		this.mNeedsUpdate = true;
	}
	
	public void maxZoomOut() {
		this.mView.zoomView(2.0);
		this.mDXview = mView.xRadius()*mDXvert;
		this.mDYview = mView.yRadius()*mDYvert;
		this.mNeedsUpdate = true;
	}
	
	public void globalUpdate() {
		// 
		if (!mNeedsUpdate) return;
		// 
		mNeedsUpdate = false;
		
		// For each vertex
		for (int v = 0; v < mVerts.length; ++v) {
			// Get the vertex that will be updated
			Vertex vert = mVerts[v];
			// Get the vertex position in world space
			double vertX = vert.x()*mView.xRadius() + mView.x();
			double vertY = vert.y()*mView.yRadius() + mView.y();
			// Get the average elevation at this area
			double vertZ = mWorld.getElevation(vertX, vertY, mDXview, mDYview);
			// Set the vertex's position to the new elevation level
			vert.setPosition(vert.x(), vert.y(), vertZ);
			// Set the color based on the elevation level
			vert.setColor(mWorld.getColor(vertZ));
		}
		// 
		mMesh.globalUpdate();
	}
	
	public void display(GLAutoDrawable drawable) {
		// If there is no mesh, return
		if (mMesh == null) return;

		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();
		
		// Render the mesh
		this.mMesh.render(drawable);
	}
	
	public static Mesh buildMeshMax(ElevationMap elevs) {
		int w = elevs.w;
		int h = elevs.h;
		int pw = w;
		int ph = h;
		double squareSize = 1.0;
		double realWidth  = squareSize*w;
		double realHeight = squareSize*h;
		
		// Create vertex and triangle sets for our mesh
		Set<Vertex> vertSet = new LinkedHashSet<Vertex>();
		Set<Triangle> triSet = new LinkedHashSet<Triangle>();

		// Create temporary array to hold the vertices in the proper relative order
		Vertex[][] verts = new Vertex[pw + 1][ph + 1];
		
		// Calculate the step size between vertices in x and y
		double dx = realWidth / (double) (w);
		double dy = realHeight / (double) (h);

		// Iterate over each x,y position in the vertex array
		for (int x = 0; x < verts.length; ++x) {
			for (int y = 0; y < verts[0].length; ++y) {
				// Find the real x,y position of the vertex
				double vX = (double)(x) * dx;
				double vY = -(double)(y) * dy;

				// Create array for local height values around this vertex
				double[][] localHeights = new double[2][2];
				
				// Copy local height values of 2x2 grid centered on current vertex
				localHeights[0][0] = elevs.getElev(Utilities.indexWrap(x-1, w), Utilities.indexWrap(y-1, h)); // UPPER-LEFT
				localHeights[0][1] = elevs.getElev(Utilities.indexWrap(x-1, w), Utilities.indexWrap(y  , h)); // LOWER-LEFT
				localHeights[1][0] = elevs.getElev(Utilities.indexWrap(x  , w), Utilities.indexWrap(y-1, h)); // UPPER-RIGHT
				localHeights[1][1] = elevs.getElev(Utilities.indexWrap(x  , w), Utilities.indexWrap(y  , h)); // LOWER-RIGHT
				
				// Average the four local height values to get the vertex height
				double vZ = (localHeights[0][0] + localHeights[1][0] + localHeights[0][1] + localHeights[1][1]) / 4.0;
				
				// Create a new vertex at the calculated position
				verts[x][y] = new Vertex(vX, vY, vZ);
				// Add the vertex to the set
				vertSet.add(verts[x][y]);
			}
		}
		// Iterate over each x,y position in the elevation map
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				// Create 2 triangles forming a square using the 4 neighboring vertices
				Triangle temp1 = new Triangle(verts[x][y], verts[x][y + 1], verts[x + 1][y + 1]);
				Triangle temp2 = new Triangle(verts[x][y], verts[x + 1][y + 1], verts[x + 1][y]);
				// Add both triangles to the set
				triSet.add(temp1);
				triSet.add(temp2);
			}
		}

		// Create a mesh using the vertex and triangle sets we created
		return new Mesh(vertSet, triSet, MeshMode.TOPLEFT);
	}
	
	public static Mesh buildMeshMin(ElevationMap elevs, double portionW, double portionH) {
		//
		int screenW = 200;
		int screenH = 200;
		double vertDX = 2.0 * ((double)(FIXED_PIXEL_SIZE) / (double)(screenW));
		double vertDY = 2.0 * ((double)(FIXED_PIXEL_SIZE) / (double)(screenH));
		int vertsX = (screenW / FIXED_PIXEL_SIZE) + 1;
		int vertsY = (screenH / FIXED_PIXEL_SIZE) + 1;
		double worldW = 1000;
		double worldH = 1000;
		//double portionW = 200;
		//double portionH = 200;
		double portionX = 35;
		double portionY = 35;
		double minFeatureSizeX = portionW * ((double)(FIXED_PIXEL_SIZE) / (double)(screenW));
		double minFeatureSizeY = portionH * ((double)(FIXED_PIXEL_SIZE) / (double)(screenW));
		double featureRadiusX = minFeatureSizeX / 2.0;
		double featureRadiusY = minFeatureSizeY / 2.0;
		
		Set<Vertex> vertSet = new LinkedHashSet<Vertex>();
		Set<Triangle> triSet = new LinkedHashSet<Triangle>();

		Vertex[][] verts = new Vertex[vertsX][vertsY];
		
		for (int x = 0; x < verts.length; ++x) {
			for (int y = 0; y < verts[0].length; ++y) {
				double vertPosX = x * minFeatureSizeX;
				double vertPosY = y * minFeatureSizeY;
				
				int leftIndex = (int)Math.floor(vertPosX + portionX - featureRadiusX);
				int rightIndex = (int)Math.ceil(vertPosX + portionX + featureRadiusX);
				int topIndex = (int)Math.floor(vertPosY + portionY - featureRadiusY);
				int bottomIndex = (int)Math.ceil(vertPosY + portionY + featureRadiusY);
				int neighborsX = rightIndex-leftIndex+1;
				int neighborsY = bottomIndex-topIndex+1;
				
				double[][] neighbors = new double[neighborsX][neighborsY];
				for (int i = 0; i < neighborsX; ++i) {
					for (int j = 0; j < neighborsY; ++j) {
						neighbors[i][j] = elevs.getElev(leftIndex+i, topIndex+j);
					}
				}
				double vertPosZ = 0.0;
				double sum = 0.0;
				for (int i = 0; i < neighborsX; ++i) {
					for (int j = 0; j < neighborsY; ++j) {
						double distX = (leftIndex+i+0.5);
						double distY = (topIndex+j+0.5);
						double inverseDist = 1 / (distX*distX + distY*distY);
						vertPosZ += neighbors[i][j]*inverseDist;
						sum += inverseDist;
					}
				}
				vertPosZ /= sum;
				
				//if (vertPosZ > 5) verts[x][y] = new Vertex(vertPosX, -vertPosY, vertPosZ, Color.GREEN);
				//else verts[x][y] = new Vertex(vertPosX, -vertPosY, vertPosZ, Color.BLUE);
				//vertSet.add(verts[x][y]);
			}
		}
		
		for (int x = 0; x < verts.length-1; ++x) {
			for (int y = 0; y < verts[0].length-1; ++y) {
				// Create 2 triangles forming a square using the 4 neighboring vertices
				Triangle temp1 = new Triangle(verts[x][y], verts[x][y + 1], verts[x + 1][y + 1]);
				Triangle temp2 = new Triangle(verts[x][y], verts[x + 1][y + 1], verts[x + 1][y]);
				// Add both triangles to the set
				triSet.add(temp1);
				triSet.add(temp2);
			}
		}
		
		return new Mesh(vertSet, triSet, MeshMode.CENTER);
	}
	
	public static Mesh buildMesh(ElevationMap elevs, double pw, double ph) {
		double px = 5;
		double py = 5;
		//double pw = 100;
		//double ph = 100;
		
		int vx = 5;
		int vy = 5;
		int sqSize = 5;
		
		Set<Vertex> vertSet = new LinkedHashSet<Vertex>();
		Set<Triangle> triSet = new LinkedHashSet<Triangle>();

		Vertex[][] verts = new Vertex[vx][vy];
		
		double realWidth  = pw / sqSize;
		double realHeight = ph / sqSize;
		double realLeft = -(realWidth / 2.0);
		double realTop  = realHeight/2.0;
		double realDx = realWidth / vx;
		double realDy = realHeight / vy;
		
		int left = (int)(px / sqSize);
		int top  = (int)(py / sqSize);
		int dx   = (int)(pw / vx / sqSize);
		int dy   = (int)(ph / vy / sqSize);

		// Iterate over each x,y position in the vertex array
		for (int x = 0; x < vx; ++x) {
			for (int y = 0; y < vy; ++y) {
				double xPos = realLeft + (double)(x)*realDx + 5;
				double yPos = realTop  - (double)(y)*realDy - 5;
				double zPos = elevs.getElev(left + x*dx, top + y*dy);
				System.out.println(zPos);
				verts[x][y] = new Vertex(xPos, yPos, zPos);
				vertSet.add(verts[x][y]);
			}
		}
		// 
		for (int x = 0; x < vx-1; ++x) {
			for (int y = 0; y < vy-1; ++y) {
				// Create 2 triangles forming a square using the 4 neighboring vertices
				Triangle temp1 = new Triangle(verts[x][y], verts[x][y + 1], verts[x + 1][y + 1]);
				Triangle temp2 = new Triangle(verts[x][y], verts[x + 1][y + 1], verts[x + 1][y]);
				// Add both triangles to the set
				triSet.add(temp1);
				triSet.add(temp2);
			}
		}
		
		// Create a mesh using the vertex and triangle sets we created
		return new Mesh(vertSet, triSet);
	}
}
