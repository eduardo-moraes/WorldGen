package datastructures.graphics;

import java.util.Iterator;
import java.util.Set;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import datastructures.geometry.BoundingBox;
import datastructures.geometry.Vector3;

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
	
	public void calculateNormals() {
		// Convert vertex and triangle sets into arrays
		Vertex[] verts = mVerts.toArray(new Vertex[mVerts.size()]);
		Triangle[] tris = mTris.toArray(new Triangle[mTris.size()]);
		
		// Create accumulator for normal vectors and count of total neighboring normals
		Vector3[] normSums = new Vector3[verts.length];
		
		// For each vertex
		for (int i = 0; i < verts.length; ++i) {
			// Initialize normal vector
			normSums[i] = new Vector3();
			// For each triangle
			for (int j = 0; j < tris.length; ++j) {
				// If the current vertex matches any vertex in the triangle
				if (verts[i] == tris[j].first() || verts[i] == tris[j].second() || verts[i] == tris[j].third())
					// Add the normal of the triangle
					normSums[i] = normSums[i].add(tris[j].normal());
			}
			// Normalize the sum of vectors and set as vertex normal
			verts[i].setNormal(normSums[i].normalized());
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
			if (next.x() < xMin) xMin = next.x();
			if (next.x() > xMax) xMax = next.x();
			if (next.y() < yMin) yMin = next.y();
			if (next.y() > yMax) yMax = next.y();
			if (next.z() < zMin) zMin = next.z();
			if (next.z() > zMax) zMax = next.z();
		}
		
		// Create the bounding box from these bounds
		this.mBounds = new BoundingBox(xMin, xMax, yMin, yMax, zMin, zMax);
	}
	
	public void render(GLAutoDrawable drawable) {
		// If there are no triangles, return
		if (mTris == null || mTris.isEmpty()) return;
		
		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();
		
		// Initialize display offsets to 0
		double dx = 0; double dy = 0; double dz = 0;
		// If display mode is set to CENTER, set offsets to mesh midpoint
		if (mDisplayMode == MeshMode.CENTER)
		{ dx = mBounds.xMid; dy = mBounds.yMid; dz = mBounds.zMid; }
		
		// Push new matrix
		gl.glPushMatrix();
			// Translate using offsets
			gl.glTranslated(-dx, -dy, -dz);
			// Render each triangle
			for (Triangle tri : mTris) tri.render(drawable);
		// Pop matrix
		gl.glPopMatrix();
	}
}
