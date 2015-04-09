package utilities.graphics;

import java.util.Iterator;
import java.util.Set;

import utilities.arrays.Vector3;
import utilities.geometry.BoundingBox;
import utilities.geometry.Transform;

import com.jogamp.opengl.GLAutoDrawable;

public class Mesh {

	//---Object Data
	Set<Vertex> mVerts;
	Set<Triangle> mTris;
	MeshMode mDisplayMode;
	public enum MeshMode {
		CENTER,
		TOPLEFT
	}
	BoundingBox mBounds;
	
	//---Constructors
	public Mesh(Set<Vertex> verts, Set<Triangle> tris) {
		this(verts, tris, MeshMode.CENTER);
	}
	
	public Mesh(Set<Vertex> verts, Set<Triangle> tris, MeshMode mode) {
		this.mVerts = verts;
		this.mTris = tris;
		this.mDisplayMode = mode;
		calculateNormals();
		calculateBounds();
	}
	
	//---Methods
	
	private void calculateNormals() {
		// Convert vertex and triangle sets into arrays
		Vertex[] verts = mVerts.toArray(new Vertex[mVerts.size()]);
		Triangle[] tris = mTris.toArray(new Triangle[mTris.size()]);
		// Create accumulator for normal vectors and count of total neighboring normals
		Vector3[] normSums = new Vector3[verts.length];
		int[] normCount = new int[verts.length];
		// For each vertex
		for (int i = 0; i < verts.length; ++i) {
			// Initialize normal vector and counter to 0
			normSums[i] = new Vector3();
			normCount[i] = 0;
			// For each triangle
			for (int j = 0; j < tris.length; ++j) {
				// If the current vertex matches any vertex in the triangle
				if (verts[i] == tris[j].getFirst() || verts[i] == tris[j].getSecond() || verts[i] == tris[j].getThird()) {
					// Add the normal of the triangle and increment our counter
					normSums[i].add(tris[j].getNormal());
					normCount[i]++;
				}
			}
			// Divide the normal vector sum by the count to get the actual normal vector
			normSums[i].multiply(1.0 / (double)normCount[i]);
			// Set the normal vector for this vertex
			verts[i].setNormal(normSums[i]);
		}
	}
	
	private void calculateBounds() {
		// Initialize minimum bounds to maximum value (and vice-versa)
		double xMin = Double.MAX_VALUE;
		double xMax = Double.MIN_VALUE;
		double yMin = Double.MAX_VALUE;
		double yMax = Double.MIN_VALUE;
		double zMin = Double.MAX_VALUE;
		double zMax = Double.MIN_VALUE;
		
		// Get an iterator over the vertex set
		Iterator<Vertex> it = mVerts.iterator();
		
		// For each vertex
		while (it.hasNext()) {
			// Retrieve the next vertex
			Vertex next = it.next();
			// Check the bounds for each dimension
			if (next.getX() < xMin) xMin = next.getX();
			if (next.getX() > xMax) xMax = next.getX();
			if (next.getY() < yMin) yMin = next.getY();
			if (next.getY() > yMax) yMax = next.getY();
			if (next.getZ() < zMin) zMin = next.getZ();
			if (next.getZ() > zMax) zMax = next.getZ();
		}
		
		// Create the bounding box from these bounds
		this.mBounds = new BoundingBox(xMin, xMax, yMin, yMax, zMin, zMax);
	}
	
	public void render(GLAutoDrawable drawable, Transform transform) {
		// If there are no triangles, return
		if (mTris == null || mTris.isEmpty()) return;
		
		// 
		Transform modedTransform = transform;
		
		// 
		if (mDisplayMode == MeshMode.CENTER)
			modedTransform = new Transform(transform, -mBounds.xMid(), -mBounds.yMid(), -mBounds.zMid());
		
		// Tell each triangle to render itself to the given drawable
		for (Triangle tri : mTris) {
			tri.render(drawable, modedTransform);
		}
	}
}
