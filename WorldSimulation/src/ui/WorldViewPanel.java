package ui;

import utilities.graphics.Camera;
import world.World;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

public class WorldViewPanel extends GLJPanel implements GLEventListener {
	
	//---Object Data
	World mWorld;
	Camera mCamera;
	int mMode = 1; final int mModes = 2;
	double angle = 0;
	double rot = ROTATION_SPEED;
	static final double ROTATION_SPEED = 0.75;

	//---Constructors
	public WorldViewPanel(World world, Camera camera) {
		// 
		mWorld = world;
		mCamera = camera;
		
		// 
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setDepthBits(16);
		GLCanvas canvas = new GLCanvas(caps);
		
		// 
		this.addGLEventListener(this);
		this.add(canvas);
		
		// 
		FPSAnimator animator = new FPSAnimator(this, 30);
		animator.start();
	}

	//---Methods

	public void changeMode() {
		// 
		if (++mMode == mModes) mMode = 0;
	}
	
	private void update() {
		boolean needsRotating = true;
		if (needsRotating) {
			angle += rot;
			if (angle >= 45.0)
				rot = -ROTATION_SPEED;
			else if (angle <= -45.0)
				rot = ROTATION_SPEED;
		}
	}

	private void render(GLAutoDrawable drawable) {
		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();

		// Clear the screen
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		//
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		double scaleFactor = 0.01;
		//
		if (mMode == 0) {
			gl.glScaled(scaleFactor, scaleFactor, scaleFactor);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			mWorld.render(drawable, mCamera);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		} else {
			//
			//gl.glScaled(scaleFactor, scaleFactor, scaleFactor);
			//gl.glRotated(45, 0, 0, 1);
			//gl.glRotated(-45, 1, -1, 0);
			
			//gl.glRotated(angle, 1, 1, 1);
			
			/*
			gl.glColor3d(0.0, 0.0, 1.0);
			gl.glBegin(GL2.GL_POLYGON);
			gl.glVertex3d(-1, -1, -0.01);
			gl.glVertex3d(-1, 1, -0.01);
			gl.glVertex3d(1, 1, -0.01);
			gl.glVertex3d(1, -1, -0.01);
			gl.glEnd();
			*/
			
			gl.glColor3d(1, 0, 0);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
			mWorld.render(drawable, mCamera);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		}
		//
		gl.glPopMatrix();

		

		//
		//gl.glMatrixMode(GL2.GL_MODELVIEW);
		//gl.glPushMatrix();
		//gl.glLoadIdentity();
		//gl.glRotated(angle, 0, 1, 0);

		//mWorld.render(drawable);

		//gl.glPopMatrix();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// Update and render
		update();
		render(drawable);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// float light_ambient[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float light_diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		// float light_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		// float light_position[] = { 1.0f, 1.0f, 1.0f, 0.0f };

		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();
		// Set clear color and clear depth
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(0.0f);
		// Set lights
		// gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light_ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_diffuse, 0);
		// gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_specular, 0);
		// gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);
		// Enable lighting
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		// Enable depth bufer
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthMask(true);
		gl.glDepthFunc(GL2.GL_GEQUAL);
		gl.glDepthRange(0.0, 1.0);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub

	}
}
