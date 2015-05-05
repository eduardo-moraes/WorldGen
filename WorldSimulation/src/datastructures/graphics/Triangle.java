package datastructures.graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import datastructures.geometry.Vector3;

public class Triangle {
	
	//---Object Data
	Vertex v0, v1, v2;
	Vector3 mNormal;
	
	//---Constructors
	public Triangle(Vertex v0, Vertex v1, Vertex v2) {
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		
		Vector3 vect0 = new Vector3(v0.position(), v1.position());
		Vector3 vect1 = new Vector3(v0.position(), v2.position());
		this.mNormal = vect0.cross(vect1).normalized();
	}
	
	//---Methods
	
	public Vertex first() { return this.v0; }
	
	public Vertex second() { return this.v1; }
	
	public Vertex third() { return this.v2; }
	
	public Vector3 normal() { return this.mNormal; }
	
	public void render(GLAutoDrawable drawable) {
		// If any of the vertices are null, return
		if (v0 == null || v1 == null || v2 == null) return;
		
		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();
		
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
