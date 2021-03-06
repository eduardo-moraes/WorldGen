package datamaps;

import java.util.LinkedHashSet;
import java.util.Set;

import datastructures.geometry.Vector3;
import datastructures.graphics.Mesh;
import datastructures.graphics.Triangle;
import datastructures.graphics.Vertex;

public class ElevationMap {
	
	//---Object Data
	public final int w, h;
	double[][] elevations;
	
	//---Constructors
	public ElevationMap(int width, int height) {
		this.w = width;
		this.h = height;
		this.elevations = new double[w][h];
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				this.setElev(0.0, x, y);
			}
		}
	}
	
	//---Methods
	
	public double getElev(int x, int y) {
		// If the location given is not within the map, return "Not-a-Number"
		if (x < 0 || y < 0 || x >= w || y >= h) return Double.NaN;
		// Otherwise, return the elevation for this coordinate
		else return elevations[x][y];
	}
	
	public void setElev(double value, int x, int y) {
		// If the location given is not within the map, return
		if (x < 0 || y < 0 || x >= w || y >= h) return;
		// Otherwise, set the elevation for this coordinate
		else elevations[x][y] = value;
	}
	
	public void normalize() {
		double max = Double.NEGATIVE_INFINITY;
		double min = Double.POSITIVE_INFINITY;
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				if (elevations[x][y] < min) min = elevations[x][y];
				if (elevations[x][y] > max) max = elevations[x][y];
			}
		}
		double range = (max-min);
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				elevations[x][y] -= min;
				elevations[x][y] /= range;
				elevations[x][y] *= 0.75;
			}
		}
	}
	
	public Mesh toMesh(double realWidth, double realHeight) {
		// Create vertex and triangle sets for our mesh
		Set<Vertex> vertSet = new LinkedHashSet<Vertex>();
		Set<Triangle> triSet = new LinkedHashSet<Triangle>();
		
		// Create temporary array to hold the vertices in the proper relative order
		Vertex[][] verts = new Vertex[w+1][h+1];
		
		// Calculate the left and top boundaries so the mesh is centered at 0,0
		double left = -(realWidth / 2.0);
		double top = realHeight / 2.0;
		// Calculate the step size between vertices in x and y
		double dx = realWidth / (double)(w);
		double dy = realHeight / (double)(h);
		
		// Iterate over each x,y position in the elevation map
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				// Find the real x,y position of the top-left vertex 
				double vX = left + (double)(x)*dx;
				double vY = top  - (double)(y)*dy;
				
				// Create array for local height values around this vertex
				double[][] localHeights = new double[2][2];
				// Copy local height values of 2x2 grid centered on current vertex
				localHeights[0][0] = elevations[(x == 0 ? (w-1) : x-1)][(y == 0 ? (h-1) : y-1)]; // UPPER-LEFT
				localHeights[1][0] = elevations[			x		  ][(y == 0 ? (h-1) : y-1)]; // UPPER-RIGHT
				localHeights[0][1] = elevations[(x == 0 ? (w-1) : x-1)][			y		  ]; // LOWER-LEFT
				localHeights[1][1] = elevations[			x		  ][			y		  ]; // LOWER-RIGHT
				
				// Average the four local height values to get the vertex height
				double vZ = (localHeights[0][0] + localHeights[1][0] + localHeights[0][1] + localHeights[1][1]) / 4.0;;
				
				// Create a new vertex at the calculated position
				verts[x][y] = new Vertex(vX, vY, vZ);
				// Add the vertex to the set
				vertSet.add(verts[x][y]);
			}
			// The bottom vertex in a column is a copy of the top vertex (with y negated)
			verts[x][h] = new Vertex(verts[x][0].x(), -(verts[x][0].y()), verts[x][0].z());
			// Add the vertex to the set
			vertSet.add(verts[x][h]);
		}
		// The right vertex in a row is a copy of the left vertex (with x negated)
		for (int y = 0; y < (h+1); ++y) {
			// Create the vertex and add it to the set
			verts[w][y] = new Vertex(-(verts[0][y].x()), verts[0][y].y(), verts[0][y].z());
			vertSet.add(verts[w][y]);
		}
		
		// Iterate over each x,y position in the elevation map
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				// Create 2 triangles forming a square using the 4 neighboring vertices
				Triangle temp1 = new Triangle(verts[x][y], verts[x][y+1], verts[x+1][y+1]);
				Triangle temp2 = new Triangle(verts[x][y], verts[x+1][y+1], verts[x+1][y]);
				// Add both triangles to the set
				triSet.add(temp1);
				triSet.add(temp2);
			}
		}
		
		// Create a mesh using the vertex and triangle sets we created
		Mesh mesh = new Mesh(vertSet, triSet);
		
		// Return that mesh
		return mesh;
	}
	
	public Mesh toMeshExact(double realWidth, double realHeight) {
		// Create vertex and triangle sets for our mesh
		Set<Vertex> vertSet = new LinkedHashSet<Vertex>();
		Set<Triangle> triSet = new LinkedHashSet<Triangle>();
		
		// Create temporary array to hold the vertices in the proper relative order
		Vertex[][] verts = new Vertex[w][h];
		
		// Calculate the left and top boundaries so the mesh is centered at 0,0
		double left = -(realWidth / 2.0);
		double top = realHeight / 2.0;
		// Calculate the step size between vertices in x and y
		double dx = realWidth / (double)(w);
		double dy = realHeight / (double)(h);
		
		// Iterate over each x,y position in the elevation map
		for (int x = 0; x < w; ++x) {
			for (int y = 0; y < h; ++y) {
				// Find the real x,y position of the top-left vertex 
				double vX = left + (double)(x)*dx;
				double vY = top  - (double)(y)*dy;
				// Use the elevation at this index as the height
				double vZ = elevations[x][y];
				
				// Create a new vertex at the calculated position
				verts[x][y] = new Vertex(vX, vY, vZ);
				// Add the vertex to the set
				vertSet.add(verts[x][y]);
			}
		}
		
		// Iterate over each x,y position in the elevation map (except last row/col)
		for (int x = 0; x < w-1; ++x) {
			for (int y = 0; y < h-1; ++y) {
				// Create 2 triangles forming a square using the 4 neighboring vertices
				Triangle temp1 = new Triangle(verts[x][y], verts[x][y+1], verts[x+1][y+1]);
				Triangle temp2 = new Triangle(verts[x][y], verts[x+1][y+1], verts[x+1][y]);
				// Add both triangles to the set
				triSet.add(temp1);
				triSet.add(temp2);
			}
		}
		
		// Create a mesh using the vertex and triangle sets we created
		Mesh mesh = new Mesh(vertSet, triSet);
		
		// Return that mesh
		return mesh;
	}
}
