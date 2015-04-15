package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import world.World;

public class RegionPanel extends JPanel implements ActionListener {

	//---Object Data
	World mWorld;
	JLabel regionLabel;
	JLabel selectionLabel;

	//---Constructors
	public RegionPanel(World world, int width, int height) {
		// 
		this.mWorld = world;
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		
		// 
		JButton prevRegion = new JButton("Previous");
		prevRegion.setAlignmentX(CENTER_ALIGNMENT);
		prevRegion.setActionCommand("previous");
		prevRegion.addActionListener(this);
		
		// 
		String currRegion = Integer.toString(mWorld.getRegionX()) + "," + Integer.toString(mWorld.getRegionY());
		this.regionLabel = new JLabel(currRegion, JLabel.CENTER);
		this.regionLabel.setAlignmentX(CENTER_ALIGNMENT);

		// 
		JButton nextRegion = new JButton("Next");
		nextRegion.setAlignmentX(CENTER_ALIGNMENT);
		nextRegion.setActionCommand("next");
		nextRegion.addActionListener(this);
		
		this.selectionLabel = new JLabel("NO SELECTION");
		this.selectionLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		// 
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// Add each component to panel
		this.add(Box.createVerticalGlue());
		//this.add(prevRegion);
		//this.add(regionLabel);
		//this.add(nextRegion);
		this.add(selectionLabel);
		this.add(Box.createVerticalGlue());
	}

	//---Methods
	
	private void prev() {
		// Tell world to change to previous region
		mWorld.prevRegion();
		// Reset the label text
		resetLabel();
	}
	
	private void next() {
		// Tell world to change to next region
		mWorld.nextRegion();
		// Reset the label text
		resetLabel();
	}
	
	private void resetLabel() {
		// 
		String currRegion = Integer.toString(mWorld.getRegionX()) + "," + Integer.toString(mWorld.getRegionY());
		regionLabel.setText(currRegion);
	}
	
	public void setSelection(String text) {
		this.selectionLabel.setMinimumSize(new Dimension(this.getWidth(), selectionLabel.getHeight()));
		this.selectionLabel.setPreferredSize(new Dimension(this.getWidth(), selectionLabel.getHeight()));
		this.selectionLabel.setText(text);
		this.validate();
		this.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// 
		if ("previous".equals(e.getActionCommand())) {
			prev();
		}
		// 
		else if ("next".equals(e.getActionCommand())) {
			next();
		}
	}
}
