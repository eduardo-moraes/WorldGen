package utilities.graphics;

import utilities.arrays.Vector3;
import utilities.geometry.Transform;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class Triangle {
	
	//---Object Data
	Vertex v0, v1, v2;
	Vector3 mNormal;
	
	//---Constructors
	public Triangle(Vertex v0, Vertex v1, Vertex v2) {
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		
		Vector3 vect0 = new Vector3(v0.getPosition(), v1.getPosition());
		Vector3 vect1 = new Vector3(v0.getPosition(), v2.getPosition());
		this.mNormal = vect0.cross(vect1);
	}
	
	//---Methods
	
	public Vector3 getNormal() { return this.mNormal; }
	
	public Vertex getFirst() { return this.v0; }
	
	public Vertex getSecond() { return this.v1; }
	
	public Vertex getThird() { return this.v2; }
	
	public void render(GLAutoDrawable drawable, Transform transform) {
		// If any of the vertices are null, return
		if (v0 == null || v1 == null || v2 == null) return;
		
		// Get the distance of the local frame from the origin
		double dx = transform.xPos();
		double dy = transform.yPos();
		double dz = transform.zPos();
		
		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();
		
		float[] colorBlue  = { 0.0f, 0.0f, 0.1f, 1.0f };
		float[] colorGreen = { 0.0f, 0.1f, 0.0f, 1.0f };
		
		// Draw a triangle using the three given vertices
		gl.glBegin(GL2.GL_TRIANGLES);
			// First vertex
			gl.glNormal3d(v0.getNormal().getX(), v0.getNormal().getY(), v0.getNormal().getZ());
			if (v0.getZ() < 5) gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, colorBlue, 0);
			else gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, colorGreen, 0);
			gl.glVertex3d(v0.getX()+dx, v0.getY()+dy, v0.getZ()+dz);
			// Second vertex
			gl.glNormal3d(v1.getNormal().getX(), v1.getNormal().getY(), v1.getNormal().getZ());
			if (v1.getZ() < 5) gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, colorBlue, 0);
			else gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, colorGreen, 0);
			gl.glVertex3d(v1.getX()+dx, v1.getY()+dy, v1.getZ()+dz);
			// Third vertex
			gl.glNormal3d(v2.getNormal().getX(), v2.getNormal().getY(), v2.getNormal().getZ());
			if (v2.getZ() < 5) gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, colorBlue, 0);
			else gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, colorGreen, 0);
			gl.glVertex3d(v2.getX()+dx, v2.getY()+dy, v2.getZ()+dz);
		gl.glEnd();
	}
}
