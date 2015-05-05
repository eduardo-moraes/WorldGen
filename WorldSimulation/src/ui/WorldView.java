package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import world.World;

public class WorldView {

	//---Object Data
	JFrame mFrame;
	WorldViewPanel mViewPanel;
	InfoPanel mInfoPanel;
	NavigationPanel mNavPanel;
	World mWorld;
	
	//---Constructors
	public WorldView(World world) {
		// 
		mWorld = world;
		
		// Create JFrame and set size, location, layout, and close operation
		mFrame = new JFrame("World View");
		mFrame.setSize(800, 700);
		mFrame.setLocation(150, 150);
		mFrame.setLayout(new BorderLayout());
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 
		mViewPanel = new WorldViewPanel(world, this, 650, 650);
		mFrame.add(mViewPanel, BorderLayout.CENTER);
		
		// 
		mInfoPanel = new InfoPanel(mViewPanel, 140, 650);
		mFrame.add(mInfoPanel, BorderLayout.EAST);
		
		// 
		mNavPanel = new NavigationPanel(mViewPanel, 790, 40);
		mFrame.add(mNavPanel, BorderLayout.SOUTH);
		
		// Set frame as visible
		mFrame.setVisible(true);
	}
	
	//---Methods
	
	public void changeSelection(String text) {
		mInfoPanel.changeSelection(text);
	}
}
