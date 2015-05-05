package world;

import java.util.LinkedHashSet;
import java.util.Set;

import proceduralgeneration.ElevationMapGenerator;
import utilities.Utilities;
import datamaps.ElevationMap;
import datastructures.graphics.Mesh;
import datastructures.graphics.Triangle;
import datastructures.graphics.Vertex;

public class Region {

	//---Object Data
	double w, h;
	ElevationMap mElevMap;
	Mesh mMesh;
	Region[][] neighbors;

	// ---Constructors
	public Region(ElevationMap map) {
		this.mElevMap = map;
		this.mMesh = map.toMesh(map.w, map.h);
		this.neighbors = new Region[3][3];
		this.neighbors[1][1] = this;
	}
	
	public Region(double width, double height) {
		this.w = width;
		this.h = height;
		
		ElevationMap seed = ElevationMapGenerator.getBlank(3);
		seed.setElev(10.0, 1, 1);
		mElevMap = ElevationMapGenerator.SquareSquare(seed, 5);
		mMesh = mElevMap.toMesh(w, h);
	}
	
	//---Methods
	
	public ElevationMap getElevationMap() { return this.mElevMap; }
	
	public Mesh getMesh() { return this.mMesh; }
	
	public void resetMesh() {
		int w = mElevMap.w;
		int h = mElevMap.h;
		double realWidth = w;
		double realHeight = h;
		
		// Create vertex and triangle sets for our mesh
		Set<Vertex> vertSet = new LinkedHashSet<Vertex>();
		Set<Triangle> triSet = new LinkedHashSet<Triangle>();

		// Create temporary array to hold the vertices in the proper relative order
		Vertex[][] verts = new Vertex[w + 1][h + 1];

		// Calculate the left and top boundaries so the mesh is centered at 0,0
		double left = -(realWidth / 2.0);
		double top = realHeight / 2.0;
		// Calculate the step size between vertices in x and y
		double dx = realWidth / (double) (w);
		double dy = realHeight / (double) (h);

		// Iterate over each x,y position in the vertex array
		for (int x = 0; x < w+1; ++x) {
			for (int y = 0; y < h+1; ++y) {
				// Find the real x,y position of the vertex
				double vX = left + (double) (x) * dx;
				double vY = top - (double) (y) * dy;

				// Create array for local height values around this vertex
				double[][] localHeights = new double[2][2];
				
				// Copy local height values of 2x2 grid centered on current vertex
				if (x == 0) {
					if (y == 0) {
						localHeights[0][0] = neighbors[0][0].getElevationMap().getElev(neighbors[0][0].getElevationMap().w-1, neighbors[0][0].getElevationMap().h-1); // UPPER-LEFT
						localHeights[0][1] = neighbors[0][1].getElevationMap().getElev(neighbors[0][1].getElevationMap().w-1, y); // LOWER-LEFT
						localHeights[1][0] = neighbors[1][0].getElevationMap().getElev(x, neighbors[1][0].getElevationMap().h-1); // UPPER-RIGHT
						localHeights[1][1] = mElevMap.getElev(x, y); // LOWER-RIGHT
					}
					else if (y == h) {
						localHeights[0][0] = neighbors[0][1].getElevationMap().getElev(neighbors[0][1].getElevationMap().w-1, y-1); // UPPER-LEFT
						localHeights[0][1] = neighbors[0][2].getElevationMap().getElev(neighbors[0][2].getElevationMap().w-1, 0); // LOWER-LEFT
						localHeights[1][0] = mElevMap.getElev(x, y);  // UPPER-RIGHT
						localHeights[1][1] = neighbors[1][2].getElevationMap().getElev(x, 0); // LOWER-RIGHT
					}
					else {
						localHeights[0][0] = neighbors[0][1].getElevationMap().getElev(neighbors[0][1].getElevationMap().w-1, y-1); // UPPER-LEFT
						localHeights[0][1] = neighbors[0][1].getElevationMap().getElev(neighbors[0][1].getElevationMap().w-1, y); // LOWER-LEFT
						localHeights[1][0] = mElevMap.getElev(x, y-1); // UPPER-RIGHT
						localHeights[1][1] = mElevMap.getElev(x, y); // LOWER-RIGHT
					}
				}
				else if (x == w) {
					if (y == 0) {
						localHeights[0][0] = neighbors[1][0].getElevationMap().getElev(x-1, neighbors[1][0].getElevationMap().h-1); // UPPER-LEFT
						localHeights[0][1] = mElevMap.getElev(x-1, y); // LOWER-LEFT
						localHeights[1][0] = neighbors[2][0].getElevationMap().getElev(0, neighbors[2][0].getElevationMap().h-1); // UPPER-RIGHT
						localHeights[1][1] = neighbors[2][1].getElevationMap().getElev(0, y); // LOWER-RIGHT
					}
					else if (y == h) {
						localHeights[0][0] = mElevMap.getElev(x-1, y-1); // UPPER-LEFT
						localHeights[0][1] = neighbors[1][2].getElevationMap().getElev(neighbors[1][2].getElevationMap().w-1, 0); // LOWER-LEFT
						localHeights[1][0] = neighbors[2][1].getElevationMap().getElev(0, neighbors[2][1].getElevationMap().h-1); // UPPER-RIGHT
						localHeights[1][1] = neighbors[2][2].getElevationMap().getElev(0, 0); // LOWER-RIGHT
					}
					else {
						localHeights[0][0] = mElevMap.getElev(x-1, y-1); // UPPER-LEFT
						localHeights[0][1] = mElevMap.getElev(x-1, y); // LOWER-LEFT
						localHeights[1][0] = neighbors[2][1].getElevationMap().getElev(0, y-1); // UPPER-RIGHT
						localHeights[1][1] = neighbors[2][1].getElevationMap().getElev(0, y); // LOWER-RIGHT
					}
				}
				else {
					if (y == 0) {
						localHeights[0][0] = neighbors[1][0].getElevationMap().getElev(x-1, neighbors[1][0].getElevationMap().h-1); // UPPER-LEFT
						localHeights[0][1] = mElevMap.getElev(x-1, y); // LOWER-LEFT
						localHeights[1][0] = neighbors[1][0].getElevationMap().getElev(x, neighbors[1][0].getElevationMap().h-1); // UPPER-RIGHT
						localHeights[1][1] = mElevMap.getElev(x, y); // LOWER-RIGHT
					}
					else if (y == h) {
						localHeights[0][0] = mElevMap.getElev(x-1, y-1); // UPPER-LEFT
						localHeights[0][1] = neighbors[1][2].getElevationMap().getElev(x-1, 0); // LOWER-LEFT
						localHeights[1][0] = mElevMap.getElev(x, y-1); // UPPER-RIGHT
						localHeights[1][1] = neighbors[1][2].getElevationMap().getElev(x, 0); // LOWER-RIGHT
					}
					else {
						localHeights[0][0] = mElevMap.getElev(x-1, y-1); // UPPER-LEFT
						localHeights[0][1] = mElevMap.getElev(x-1, y); // LOWER-LEFT
						localHeights[1][0] = mElevMap.getElev(x, y-1); // UPPER-RIGHT
						localHeights[1][1] = mElevMap.getElev(x, y); // LOWER-RIGHT
					}
				}
				
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
		this.mMesh = new Mesh(vertSet, triSet);
	}
	
	private void setNeighbors(Region[][] regions, int x, int y) {
		// 
		int left   = Utilities.indexWrap(x-1, regions.length);
		int right  = Utilities.indexWrap(x+1, regions.length);
		int top    = Utilities.indexWrap(y-1, regions[0].length);
		int bottom = Utilities.indexWrap(y+1, regions[0].length);
		// 
		this.neighbors[0][0] = regions[left][top]; // TOP-LEFT
		this.neighbors[1][0] = regions[x][top]; // TOP
		this.neighbors[2][0] = regions[right][top]; // TOP-RIGHT
		// 
		this.neighbors[0][1] = regions[left][y]; // LEFT
		this.neighbors[1][1] = regions[x][y]; // CENTER
		this.neighbors[2][1] = regions[right][y]; // RIGHT
		// 
		this.neighbors[0][2] = regions[left][bottom]; // BOTTOM-LEFT
		this.neighbors[1][2] = regions[x][bottom]; // BOTTOM
		this.neighbors[2][2] = regions[right][bottom]; // BOTTOM-RIGHT
	}
	
	//---Static Methods
	
	public static Region[][] splitMap(ElevationMap map) {
		int width = map.w;
		int height = map.h;
		
		int regionSize = 10;
		int xRegions = width / regionSize;
		int yRegions = height / regionSize;
		
		Region[][] temp = new Region[xRegions][yRegions];
		for (int x = 0; x < xRegions; ++x) {
			for (int y = 0; y < yRegions; ++y) {
				ElevationMap portion = ElevationMapGenerator.getBlank(regionSize);
				for (int px = 0; px < regionSize; ++px) {
					for (int py = 0; py < regionSize; ++py) {
						portion.setElev(map.getElev(x*regionSize+px, y*regionSize+py), px, py);
					}
				}
				temp[x][y] = new Region(portion);
			}
		}
		for (int x = 0; x < xRegions; ++x) {
			for (int y = 0; y < yRegions; ++y) {
				temp[x][y].setNeighbors(temp, x, y);
			}
		}
		for (int x = 0; x < xRegions; ++x) {
			for (int y = 0; y < yRegions; ++y) {
				temp[x][y].resetMesh();
			}
		}
		
		return temp;
	}
}
