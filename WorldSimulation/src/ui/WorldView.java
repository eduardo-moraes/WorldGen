package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.*;

import utilities.graphics.Camera;
import world.World;

public class WorldView implements ActionListener {

	//---Object Data
	JFrame mFrame;
	WorldViewPanel mViewPanel;
	RegionPanel mRegionPanel;
	Camera mCamera;
	World mWorld;
	double angle = 0; double rot = ROTATION_SPEED;
	static final double ROTATION_SPEED = 0.75;
	
	//---Constructors
	public WorldView(World world) {
		// 
		mWorld = world;
		// 
		mCamera = new Camera(10, 10);
		
		// Create JFrame and set size, location, layout, and close operation
		mFrame = new JFrame("World View");
		mFrame.setSize(800, 800);
		mFrame.setLocation(150, 150);
		mFrame.setLayout(new BorderLayout());
		mFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// 
		mViewPanel = new WorldViewPanel(world, mCamera);
		mFrame.add(mViewPanel, BorderLayout.CENTER);
		
		//
		mRegionPanel = new RegionPanel(world);
		mFrame.add(mRegionPanel, BorderLayout.EAST);
		
		//
		JButton leftButton = new JButton("<-");
		leftButton.setActionCommand("cameraLeft");
		leftButton.addActionListener(this);
		//
		JButton upButton = new JButton("^");
		upButton.setActionCommand("cameraUp");
		upButton.addActionListener(this);
		//
		JButton downButton = new JButton("V");
		downButton.setActionCommand("cameraDown");
		downButton.addActionListener(this);
		//
		JButton rightButton = new JButton("->");
		rightButton.setActionCommand("cameraRight");
		rightButton.addActionListener(this);
		// Create button panel and add to frame
		JPanel buttonPanel = new JPanel();
		mFrame.add(buttonPanel, BorderLayout.SOUTH);
		// Add each button to panel
		buttonPanel.add(leftButton);
		buttonPanel.add(upButton);
		buttonPanel.add(downButton);
		buttonPanel.add(rightButton);
		
		/* 
		JButton changeViewButton = new JButton("Change View");
		changeViewButton.setActionCommand("changeView");
		changeViewButton.addActionListener(this);
		// 
		JButton smoothButton = new JButton("Smooth");
		smoothButton.setActionCommand("smooth");
		smoothButton.addActionListener(this);
		// 
		JButton sharpButton = new JButton("Sharpen");
		sharpButton.setActionCommand("sharpen");
		sharpButton.addActionListener(this);
		// 
		JButton edgeButton = new JButton("Edge Detect");
		edgeButton.setActionCommand("edge");
		edgeButton.addActionListener(this);
		
		// Create button panel and add to frame
		JPanel buttonPanel = new JPanel();
		mFrame.add(buttonPanel, BorderLayout.SOUTH);
		// Add each button to panel
		buttonPanel.add(changeViewButton);
		buttonPanel.add(smoothButton);
		buttonPanel.add(sharpButton);
		buttonPanel.add(edgeButton);
		*/
		
		// Set frame as visible
		mFrame.setVisible(true);
	}
	
	//---Methods
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// 
		if ("cameraLeft".equals(e.getActionCommand()))
			mCamera.moveLeft();
		// 
		else if ("cameraUp".equals(e.getActionCommand()))
			mCamera.moveUp();
		// 
		else if ("cameraDown".equals(e.getActionCommand()))
			mCamera.moveDown();
		// 
		else if ("cameraRight".equals(e.getActionCommand()))
			mCamera.moveRight();
		/*
		if ("changeView".equals(e.getActionCommand())) {
			mViewPanel.changeMode();
		}
		// 
		else if ("smooth".equals(e.getActionCommand())) {
			mWorld.smoothMap();
		}
		// 
		else if ("sharpen".equals(e.getActionCommand())) {
			mWorld.sharpenMap();
		}
		// 
		else if ("edge".equals(e.getActionCommand())) {
			mWorld.edgeDetectMap();
		}
		*/
	}
}
