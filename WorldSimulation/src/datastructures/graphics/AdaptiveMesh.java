package datastructures.graphics;

import utilities.Utilities;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import datastructures.geometry.BoundingBox;
import datastructures.geometry.Point;
import datastructures.geometry.Vector3;

public class AdaptiveMesh {
	
	private class TriangleNormalizer extends Thread {
		 
		//---Object Data
		int mStart	 = 0;
		int mEnd	 = 0;
		int mCurrent = 0;
		boolean mIsActive = false;
		boolean mNeedsRun = false;
		static final int DEFAULT_RANGE = 50;
		
		//---Constructors
		public TriangleNormalizer(int startIndex, int endIndex) {
			// 
			this.mStart	  = startIndex;
			this.mEnd	  = endIndex;
			this.mCurrent = startIndex;
			// 
			this.mIsActive = false;
			this.mNeedsRun = true;
		}
		
		//---Methods
		
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
						calcTriNormal(mCurrent);
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
	}
	
	private class VertexNormalizer extends Thread {
		 
		//---Object Data
		int mStart	 = 0;
		int mEnd	 = 0;
		int mCurrent = 0;
		boolean mIsActive = false;
		boolean mNeedsRun = false;
		static final int DEFAULT_RANGE = 50;
		
		//---Constructors
		public VertexNormalizer(int startIndex, int endIndex) {
			// 
			this.mStart	  = startIndex;
			this.mEnd	  = endIndex;
			this.mCurrent = startIndex;
			// 
			this.mIsActive = false;
			this.mNeedsRun = true;
		}
		
		//---Methods
		
		@Override
		public void run() {
			mIsActive = true;
			int yieldCounter = 0;
			int runsBeforeYield = 10;
			while (mIsActive) {
				if (mNeedsRun) {
					mNeedsRun = false;
					update();
					/*
					mCurrent = mStart;
					while (mCurrent < mEnd) {
						calcVertNormal(mCurrent);
						++mCurrent;
						if (++yieldCounter >= runsBeforeYield) {
							yieldCounter = 0;
							yield();
						}
					}
					*/
				}
				else yield();
			}
		}
		
		public void poke() { this.mNeedsRun = true; }
		
		private void update() {
			// Reset vertex normals for this range
			for (int i = mStart; i < mEnd; ++i)
				mVertNorms[i] = new Vector3();
			
			// For each triangle
			for (int t = 0; t < mTriCount; ++t) {
				// Get each vertex index
				int first = mTris[t][0];
				int second = mTris[t][1];
				int third = mTris[t][2];
				// Any vertex within the range of this thread should accumulate the normal for this triangle
				if (first >= mStart && first < mEnd)
					mVertNorms[first] = mVertNorms[first].add(mTriNorms[t]);
				if (second >= mStart && second < mEnd)
					mVertNorms[second] = mVertNorms[second].add(mTriNorms[t]);
				if (third >= mStart && third < mEnd)
					mVertNorms[third] = mVertNorms[third].add(mTriNorms[t]);
			}
			
			// Normalize the vector and assign it to the array and vertex
			for (int i = mStart; i < mEnd; ++i) {
				mVertNorms[i] = mVertNorms[i].normalized();
				mVerts[i].setNormal(mVertNorms[i]);
			}
		}
	}
	
	//---Object Data
	int mVertCount		 = 0;
	int mTriCount		 = 0;
	Vertex[] mVerts		 = null;
	int[][] mTris		 = null;
	Vector3[] mVertNorms = null;
	Vector3[] mTriNorms  = null;
	BoundingBox mBounds  = null;
	//TriangleNormalizer[] mTriNormalizers = null;
	//VertexNormalizer[] mVertNormalizers  = null;
	
	//---Constructors
	public AdaptiveMesh(Vertex[] verts, int[][] tris) {
		// Check that the arrays provided are valid
		if (verts == null || tris == null)
			throw new RuntimeException("AdaptiveMesh: NULL array received; cannot build");
		if (tris[0].length != 3)
			throw new RuntimeException("AdaptiveMesh: Array size is incorrect; must be size 3");
		
		// Assign the given arrays to the vertex and triangle arrays
		this.mVerts = verts;
		this.mTris  = tris;
		
		// Set the vertex and triangle counts to the length of the arrays
		this.mVertCount = mVerts.length;
		this.mTriCount  = mTris.length;
		
		// Create the vertex normal array and initialize all to 0 vector
		mVertNorms = new Vector3[mVertCount]; 
		for (int v = 0; v < mVertCount; ++v)
			mVertNorms[v] = new Vector3();
		
		// Create the triangle normal array
		mTriNorms = new Vector3[mTriCount];
		// For each triangle
		for (int t = 0; t < mTriCount; ++t) {
			// Ensure that the vertex indices are valid
			if (  mTris[t][0] < 0 || mTris[t][0] >= mVertCount
			   || mTris[t][1] < 0 || mTris[t][1] >= mVertCount
			   || mTris[t][2] < 0 || mTris[t][2] >= mVertCount
			   || mTris[t][0] == mTris[t][1] || mTris[t][0] == mTris[t][2] || mTris[t][1] == mTris[t][2]
			   ) throw new RuntimeException("AdaptiveMesh: Invalid vertex index; cannot build");
			// Find its normal vector
			calcTriNormal(t);
			// Add the triangle normal to the vertex normal of each vertex index
			mVertNorms[mTris[t][0]] = mVertNorms[mTris[t][0]].add(mTriNorms[t]);
			mVertNorms[mTris[t][1]] = mVertNorms[mTris[t][1]].add(mTriNorms[t]);
			mVertNorms[mTris[t][2]] = mVertNorms[mTris[t][2]].add(mTriNorms[t]);
		}
		
		// Normalize each vertex normal and assign the normal to the vertex object
		for (int v = 0; v < mVertCount; ++v) {
			mVertNorms[v] = mVertNorms[v].normalized();
			mVerts[v].setNormal(mVertNorms[v]);
		}
		
		// Find the bounding box for this mesh
		calculateBounds();
		/*
		// Calculate the number of triangle normalizers needed
		int triNormCount = (mTriCount / TriangleNormalizer.DEFAULT_RANGE);
		if (mTriCount % TriangleNormalizer.DEFAULT_RANGE > 0) ++triNormCount;
		mTriNormalizers = new TriangleNormalizer[triNormCount];
		// Divide the triangles among the normalizers
		for (int i = 0; i < triNormCount; ++i) {
			int start = i*TriangleNormalizer.DEFAULT_RANGE;
			int end   = (i+1)*TriangleNormalizer.DEFAULT_RANGE;
			if (end > mTriCount) end = mTriCount;
			mTriNormalizers[i] = new TriangleNormalizer(start, end);
			//mTriNormalizers[i].start();
		}
		
		// Calculate the number of vertex normalizers needed
		int vertNormCount = (mVertCount / VertexNormalizer.DEFAULT_RANGE);
		if (mVertCount % VertexNormalizer.DEFAULT_RANGE > 0) ++vertNormCount;
		mVertNormalizers = new VertexNormalizer[vertNormCount];
		// Divide the vertexes among the normalizers
		for (int i = 0; i < vertNormCount; ++i) {
			int start = i*VertexNormalizer.DEFAULT_RANGE;
			int end   = (i+1)*VertexNormalizer.DEFAULT_RANGE;
			if (end > mVertCount) end = mVertCount;
			mVertNormalizers[i] = new VertexNormalizer(start, end);
			//mVertNormalizers[i].start();
		}
		*/
	}
	
	//---Methods
	
	public void globalUpdate() {
		// Recalculate all triangle normals
		for (int t = 0; t < mTriCount; ++t)
			calcTriNormal(t);
		
		// Reset vertex normals to 0 vector
		for (int v = 0; v < mVertCount; ++v)
			mVertNorms[v] = new Vector3();
		
		// For each triangle
		for (int t = 0; t < mTriCount; ++t) {
			// Get each vertex index
			int first = mTris[t][0];
			int second = mTris[t][1];
			int third = mTris[t][2];
			// Add this triangle's normal to that of each vertex
			mVertNorms[first] = mVertNorms[first].add(mTriNorms[t]);
			mVertNorms[second] = mVertNorms[second].add(mTriNorms[t]);
			mVertNorms[third] = mVertNorms[third].add(mTriNorms[t]);
		}
		
		// Normalize each vector and assign it to the array and vertex
		for (int v = 0; v < mVertCount; ++v) {
			mVertNorms[v] = mVertNorms[v].normalized();
			mVerts[v].setNormal(mVertNorms[v]);
		}
	}
	
	private void calculateBounds() {
		// If the needed array doesn't exist, return
		if (mVerts == null) return;
		
		// Initialize minimum bounds to maximum value (and vice-versa)
		double xMin = Double.MAX_VALUE;
		double xMax = Double.MIN_VALUE;
		double yMin = Double.MAX_VALUE;
		double yMax = Double.MIN_VALUE;
		double zMin = Double.MAX_VALUE;
		double zMax = Double.MIN_VALUE;
		
		// For each vertex
		for (int v = 0; v < mVertCount; ++v) {
			// Get the vertex
			Vertex vert = mVerts[v];
			// Check the bounds for each dimension
			if (vert.x() < xMin) xMin = vert.x();
			if (vert.x() > xMax) xMax = vert.x();
			if (vert.y() < yMin) yMin = vert.y();
			if (vert.y() > yMax) yMax = vert.y();
			if (vert.z() < zMin) zMin = vert.z();
			if (vert.z() > zMax) zMax = vert.z();
		}
		
		// Create the bounding box from these bounds
		this.mBounds = new BoundingBox(xMin, xMax, yMin, yMax, zMin, zMax);
	}
	
	private void calcTriNormal(int triIndex) {
		// If the needed arrays don't exist, return
		if (mTris == null || mTriNorms == null) return;
		
		// Get the position of each vertex
		Point p0 = mVerts[mTris[triIndex][0]].position();
		Point p1 = mVerts[mTris[triIndex][1]].position();
		Point p2 = mVerts[mTris[triIndex][2]].position();
		
		// Create vectors from v0 to each other point
		Vector3 vect0 = new Vector3(p0, p1);
		Vector3 vect1 = new Vector3(p0, p2);
		// Use the normalized cross-product as the normal vector
		mTriNorms[triIndex] = vect0.cross(vect1).normalized();
	}
	
	public void render(GLAutoDrawable drawable) {
		// If the needed arrays don't exist, return
		if (mVerts == null || mTris == null || mVertNorms == null || mTriNorms == null) return;
		
		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();
		
		// For each triangle
		for (int t = 0; t < mTriCount; ++t) {
			// Get each vertex
			Vertex v0 = mVerts[mTris[t][0]];
			Vertex v1 = mVerts[mTris[t][1]];
			Vertex v2 = mVerts[mTris[t][2]];
			
			// Draw a triangle using the three given vertices
			gl.glBegin(GL2.GL_TRIANGLES);
				// First vertex
				gl.glNormal3d(v0.normal().x, v0.normal().y, v0.normal().z);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, v0.color(), 0);
				gl.glVertex3d(v0.x(), v0.y(), v0.z());
				
				// Second vertex
				gl.glNormal3d(v1.normal().x, v1.normal().y, v1.normal().z);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, v1.color(), 0);
				gl.glVertex3d(v1.x(), v1.y(), v1.z());
				
				// Third vertex
				gl.glNormal3d(v2.normal().x, v2.normal().y, v2.normal().z);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, v2.color(), 0);
				gl.glVertex3d(v2.x(), v2.y(), v2.z());
			gl.glEnd();
		}
	}
}
