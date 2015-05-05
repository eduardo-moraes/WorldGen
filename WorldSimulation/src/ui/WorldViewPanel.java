package ui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import utilities.VertexStreamer;
import world.World;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import datastructures.geometry.Point;
import datastructures.geometry.Vector3;
import datastructures.viewing.Camera;

public class WorldViewPanel extends GLJPanel implements GLEventListener, MouseListener, MouseMotionListener {
	
	//---Object Data
	WorldView mParent;
	World mWorld;
	Camera mCamera;
	VertexStreamer mVertStream;
	GLU glu;
	FPSAnimator animator;
	int mMode = 0; final int mModes = 2;
	ProjectionMode mProjectionMode;
	boolean mIsRotating;
	double mAngle = 0;
	double mRotationSpeed = ROTATION_SPEED;
	static final double ROTATION_SPEED = 1.0;
	boolean mZoomInDemo;
	boolean mZoomOutDemo;
	double mDemoStart;
	static final double DEMO_TIME = 15.0;

	//---Constructors
	public WorldViewPanel(World world, WorldView parent, int width, int height) {
		// 
		this.mParent = parent;
		this.mWorld = world;
		this.mCamera = new Camera(width, height);
		this.mVertStream = new VertexStreamer(world, mCamera);
		this.mProjectionMode = ProjectionMode.ORTHOGRAPHIC;
		this.mIsRotating = false;
		this.mZoomInDemo = false;
		this.mZoomOutDemo = false;
		
		// 
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setRedBits(8);
		caps.setGreenBits(8);
		caps.setBlueBits(8);
		caps.setDepthBits(16);
		GLCanvas canvas = new GLCanvas(caps);
		
		// 
		this.add(canvas);
		this.addGLEventListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
	}

	//---Methods
	
	public VertexStreamer getVertexStreamer() { return this.mVertStream; }
	
	public void changeProjection(ProjectionMode p) { this.mProjectionMode = p; }
	
	public void setRotation(boolean isRotating) { this.mIsRotating = isRotating; }
	
	public void startZoomInDemo() {
		if (mZoomInDemo || mZoomOutDemo) return;
		mZoomInDemo = true;
		mZoomOutDemo = false;
		mVertStream.maxZoomOut();
		mDemoStart = System.currentTimeMillis() / 1000.0;
	}
	
	public void startZoomOutDemo() {
		if (mZoomInDemo || mZoomOutDemo) return;
		mZoomOutDemo = true;
		mZoomInDemo = false;
		mVertStream.maxZoomIn();
		mDemoStart = System.currentTimeMillis() / 1000.0;
	}
	
	public void stopDemo() {
		mZoomInDemo = false;
		mZoomOutDemo = false;
	}
	
	private void update() {
		// 
		if (mZoomInDemo) {
			mVertStream.zoomIn();
			if ((System.currentTimeMillis() / 1000.0) - mDemoStart > DEMO_TIME)
				mZoomInDemo = false;
		}
		else if (mZoomOutDemo) {
			mVertStream.zoomOut();
			if ((System.currentTimeMillis() / 1000.0) - mDemoStart > DEMO_TIME)
				mZoomOutDemo = false;
		}
		mVertStream.globalUpdate();
		
		// 
		if (mIsRotating) {
			mAngle += mRotationSpeed;
			if (mAngle >= 45.0) mRotationSpeed = -ROTATION_SPEED;
			else if (mAngle <= -45.0) mRotationSpeed = ROTATION_SPEED;
		}
		else mAngle = 0.0;
	}

	private void render(GLAutoDrawable drawable) {
		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();

		// Clear the screen
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		// Switch to model view and push new matrix
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
			/* Draw axis lines
			gl.glBegin(gl.GL_LINES);
				gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT_AND_DIFFUSE, Color.get(Color.RED), 0);
				gl.glVertex3f(0, 0, 0);
				gl.glVertex3f(1, 0, 0);
				gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT_AND_DIFFUSE, Color.get(Color.GREEN), 0);
				gl.glVertex3f(0, 0, 0);
				gl.glVertex3f(0, 1, 0);
				gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT_AND_DIFFUSE, Color.get(Color.BLUE), 0);
				gl.glVertex3f(0, 0, 0);
				gl.glVertex3f(0, 0, 1);
			gl.glEnd();
			*/
			
			// 
			gl.glRotated(mAngle, 0, 1, 0);
			
			// 
			//gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
			
			mVertStream.display(drawable);
			
			//gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			
		// Pop matrix
		gl.glPopMatrix();
	}

	private void resetCamera(GL2 gl) {
		// Switch to projection matrix and load identity
		gl.glMatrixMode(gl.GL_PROJECTION);
		gl.glLoadIdentity();
		
		// Retrieve camera information
		double fov	  = mCamera.fov();
		Point eye	  = mCamera.position();
		Point at	  = mCamera.at();
		Vector3 up	  = mCamera.up();
		double aspect = mCamera.aspectRatio();
		double near	  = mCamera.near();
		double far	  = mCamera.far();
		// Set up projection matrix based on projection mode setting
		if (mProjectionMode == ProjectionMode.ORTHOGRAPHIC) gl.glOrtho(-1, 1, -1, 1, near, far);
		else if (mProjectionMode == ProjectionMode.PERSPECTIVE) glu.gluPerspective(fov, aspect, near, far);
		else gl.glOrtho(-1, 1, -1, 1, near, far);
		// Define viewing transformation
		glu.gluLookAt(eye.x, eye.y, eye.z, at.x, at.y, at.z, up.x, up.y, up.z);
		
		// Switch to model view matrix and load identity
		gl.glMatrixMode(gl.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// Update and render
		resetCamera(drawable.getGL().getGL2());
		update();
		render(drawable);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// Get OpenGL object
		GL2 gl = drawable.getGL().getGL2();
		
		// Set clear color and clear depth
		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glClearDepth(0f);
		
		// Enable lighting
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_NORMALIZE);
		
		// Set ambient light
		float lightAmbient[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lightAmbient, 0);
		
		// Set shading model
		gl.glShadeModel(gl.GL_SMOOTH);
		gl.glHint(gl.GL_PERSPECTIVE_CORRECTION_HINT, gl.GL_NICEST);
		
		// Set diffuse light
		float lightDiffuse[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		float lightPosition[] = { 0.0f, 0.0f, 4.0f, 1.0f };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
		
		// Enable depth buffer
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthMask(true);
		gl.glDepthFunc(GL2.GL_GEQUAL);
		gl.glDepthRange(0.0, 1.0);
		
		// Create new GLU object
		glu = new GLU();
		
		// Reset the camera using the current GL2 object
		resetCamera(gl);
		
		// Start an animator running at the camera's fps
		animator = new FPSAnimator(this, mCamera.fps());
		animator.start();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// AUTOMATICALLY RESIZED BY DEFAULT IMPLEMENTATION
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// 
		int x = e.getX();
		int y = e.getY();
		int w = this.getWidth();
		int h = this.getHeight();
		
		String text = "???";
		
		if (x < 0 || y < 0 || x >= w || y >= h) text = "NO SELECTION";
		else {
			double screenX = -1 + 2*((double)(x) / (double)(w-1));
			double screenY = 1 - 2*((double)(y) / (double)(h-1));
			text = mVertStream.getAreaName(screenX, screenY);
		}
		
		mParent.changeSelection(text);
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) {
		mParent.changeSelection("NO SELECTION");
	}
}
