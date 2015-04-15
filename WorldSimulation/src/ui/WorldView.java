package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import utilities.VertexStreamer;
import utilities.graphics.Camera;
import world.World;

public class WorldView implements ActionListener {

	//---Object Data
	JFrame mFrame;
	WorldViewPanel mViewPanel;
	RegionPanel mRegionPanel;
	World mWorld;
	double angle = 0; double rot = ROTATION_SPEED;
	static final double ROTATION_SPEED = 0.75;
	
	//---Constructors
	public WorldView(World world) {
		// 
		mWorld = world;
		
		// Create JFrame and set size, location, layout, and close operation
		mFrame = new JFrame("World View");
		mFrame.setSize(800, 800);
		mFrame.setLocation(150, 150);
		mFrame.setLayout(new BorderLayout());
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 
		mViewPanel = new WorldViewPanel(world, this, 650, 650);
		//mViewPanel.setPreferredSize(new Dimension(650, 650));
		mFrame.add(mViewPanel, BorderLayout.CENTER);
		
		mRegionPanel = new RegionPanel(world, 140, 790);
		//mRegionPanel.setPreferredSize(new Dimension(140, 790));
		mFrame.add(mRegionPanel, BorderLayout.EAST);
		// Create button panel and add to frame
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(650, 140));
		mFrame.add(buttonPanel, BorderLayout.SOUTH);
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
		//
		JButton zoomInButton = new JButton("+");
		zoomInButton.setActionCommand("zoomIn");
		zoomInButton.addActionListener(this);
		// 
		JButton zoomOutButton = new JButton("-");
		zoomOutButton.setActionCommand("zoomOut");
		zoomOutButton.addActionListener(this);
		// 
		JButton cameraModeButton = new JButton("Camera Mode");
		cameraModeButton.setActionCommand("cameraChange");
		cameraModeButton.addActionListener(this);
		
		// Add each button to panel
		buttonPanel.add(leftButton);
		buttonPanel.add(upButton);
		buttonPanel.add(downButton);
		buttonPanel.add(rightButton);
		buttonPanel.add(zoomInButton);
		buttonPanel.add(zoomOutButton);
		buttonPanel.add(cameraModeButton);
		
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
	
	public RegionPanel getRegionPanel() {
		return this.mRegionPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// 
		if ("cameraLeft".equals(e.getActionCommand()))
			mViewPanel.getVertexStreamer().viewLeft();
		// 
		else if ("cameraUp".equals(e.getActionCommand()))
			mViewPanel.getVertexStreamer().viewUp();
		// 
		else if ("cameraDown".equals(e.getActionCommand()))
			mViewPanel.getVertexStreamer().viewDown();
		// 
		else if ("cameraRight".equals(e.getActionCommand()))
			mViewPanel.getVertexStreamer().viewRight();
		// 
		else if ("zoomIn".equals(e.getActionCommand()))
			mViewPanel.getVertexStreamer().zoomIn();
		//
		else if ("zoomOut".equals(e.getActionCommand()))
			mViewPanel.getVertexStreamer().zoomOut();
		// 
		else if ("cameraChange".equals(e.getActionCommand()))
			mViewPanel.changeCamera();
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
