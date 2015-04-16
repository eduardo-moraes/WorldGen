package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class NavigationPanel extends JPanel implements ActionListener {
	
	// ---Object Data
	WorldViewPanel mView;

	// ---Constructors
	public NavigationPanel(WorldViewPanel view, int width, int height) {
		// 
		this.mView = view;

		//
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
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

		// Add each button to panel
		this.add(leftButton);
		this.add(upButton);
		this.add(downButton);
		this.add(rightButton);
		this.add(zoomInButton);
		this.add(zoomOutButton);
	}

	// ---Methods
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// 
		if ("cameraLeft".equals(e.getActionCommand()))
			mView.getVertexStreamer().viewLeft();
		// 
		else if ("cameraUp".equals(e.getActionCommand()))
			mView.getVertexStreamer().viewUp();
		// 
		else if ("cameraDown".equals(e.getActionCommand()))
			mView.getVertexStreamer().viewDown();
		// 
		else if ("cameraRight".equals(e.getActionCommand()))
			mView.getVertexStreamer().viewRight();
		// 
		else if ("zoomIn".equals(e.getActionCommand()))
			mView.getVertexStreamer().zoomIn();
		//
		else if ("zoomOut".equals(e.getActionCommand()))
			mView.getVertexStreamer().zoomOut();
	}
}
