package ui;

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

	//---Constructors
	public RegionPanel(World world) {
		// 
		this.mWorld = world;
		
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
		
		// 
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// Add each component to panel
		this.add(Box.createVerticalGlue());
		this.add(prevRegion);
		this.add(regionLabel);
		this.add(nextRegion);
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
