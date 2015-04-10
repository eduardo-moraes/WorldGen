package utilities;
import java.util.LinkedHashSet;
import java.util.Set;

import datamaps.ElevationMap;
import utilities.graphics.Mesh;
import utilities.graphics.Triangle;
import utilities.graphics.Vertex;
import utilities.graphics.Mesh.MeshMode;

public class VertexStreamer {
	
	//---Object Data
	

	//---Constructors
	public VertexStreamer() {
		
	}

	//---Methods
	
	public static Mesh buildMeshMax(ElevationMap elevs) {
		int w = elevs.getWidth();
		int h = elevs.getHeight();
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
		final int FIXED_PIXEL_SIZE = 10;
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
				
				verts[x][y] = new Vertex(vertPosX, -vertPosY, vertPosZ);
				vertSet.add(verts[x][y]);
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
		
		return new Mesh(vertSet, triSet, MeshMode.TOPLEFT);
	}
	
	public int getH(double x, double y, double fSize) {
		double radius = fSize / 2.0;
		double gX = x - 0.5;
		double gY = y - 0.5;
		int discreteRadius = (int)Math.ceil(x + radius);
		int top = (int)gY;
		
		return 0;
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
