package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.text.View;

public class InfoPanel extends JPanel implements ActionListener {
	// ---Object Data
	WorldViewPanel mView;
	ProjectionMode mProjectionMode;
	boolean mRotateActive;
	JCheckBox mRotationCheckBox;
	JLabel mSelectionLabel;

	// ---Constructors
	public InfoPanel(WorldViewPanel view, int width, int height) {
		//
		this.mView = view;
		this.mProjectionMode = ProjectionMode.ORTHOGRAPHIC;

		//
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		//
		JPanel projectionModePanel = new JPanel();
		projectionModePanel.setBorder(BorderFactory.createTitledBorder("Projection Mode"));
		//
		JRadioButton orthographicButton = new JRadioButton("Orthographic");
		projectionModePanel.add(orthographicButton);
		orthographicButton.setAlignmentX(LEFT_ALIGNMENT);
		orthographicButton.setSelected(true);
		orthographicButton.setActionCommand("orthographicProjection");
		orthographicButton.addActionListener(this);
		//
		JRadioButton perspectiveButton = new JRadioButton("Perspective");
		projectionModePanel.add(perspectiveButton);
		perspectiveButton.setAlignmentX(LEFT_ALIGNMENT);
		perspectiveButton.setSelected(false);
		perspectiveButton.setActionCommand("perspectiveProjection");
		perspectiveButton.addActionListener(this);
		//
		ButtonGroup projectionModesGroup = new ButtonGroup();
		projectionModesGroup.add(orthographicButton);
		projectionModesGroup.add(perspectiveButton);
		
		// 
		this.mRotateActive = false;
		JPanel rotationPanel = new JPanel();
		rotationPanel.setBorder(BorderFactory.createTitledBorder("Rotation"));
		// 
		this.mRotationCheckBox = new JCheckBox("Rotate?");
		rotationPanel.add(mRotationCheckBox);
		this.mRotationCheckBox.setSelected(false);
		this.mRotationCheckBox.setActionCommand("rotation");
		this.mRotationCheckBox.addActionListener(this);

		// 
		JPanel demoPanel = new JPanel();
		demoPanel.setBorder(BorderFactory.createTitledBorder("Demos"));
		// 
		JButton zoomInDemo = new JButton("Zoom-In");
		demoPanel.add(zoomInDemo);
		zoomInDemo.setAlignmentX(LEFT_ALIGNMENT);
		zoomInDemo.setActionCommand("zoomInDemo");
		zoomInDemo.addActionListener(this);
		// 
		JButton zoomOutDemo = new JButton("Zoom-Out");
		demoPanel.add(zoomOutDemo);
		zoomOutDemo.setAlignmentX(LEFT_ALIGNMENT);
		zoomOutDemo.setActionCommand("zoomOutDemo");
		zoomOutDemo.addActionListener(this);
		// 
		JButton stopDemo = new JButton("Stop");
		demoPanel.add(stopDemo);
		stopDemo.setAlignmentX(LEFT_ALIGNMENT);
		stopDemo.setActionCommand("stopDemo");
		stopDemo.addActionListener(this);
		
		//
		JPanel selectionPanel = new JPanel();
		selectionPanel.setBorder(BorderFactory.createTitledBorder("Area Info"));
		//
		this.mSelectionLabel = new JLabel("NO SELECTION");
		selectionPanel.add(mSelectionLabel);
		this.mSelectionLabel.setAlignmentX(CENTER_ALIGNMENT);

		//
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// Add each component to panel
		this.add(projectionModePanel);
		this.add(rotationPanel);
		this.add(demoPanel);
		this.add(Box.createVerticalGlue());
		this.add(selectionPanel);
	}

	// ---Methods

	public ProjectionMode getProjectionMode() {
		return this.mProjectionMode;
	}

	private void changeProjectionMode(ProjectionMode p) {
		this.mProjectionMode = p;
		this.mView.changeProjection(p);
	}
	
	private void changeRotation() {
		this.mRotateActive = mRotationCheckBox.isSelected();
		this.mView.setRotation(mRotateActive);
	}

	public void changeSelection(String text) {
		this.mSelectionLabel.setText(text);
		this.validate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//
		if ("orthographicProjection".equals(e.getActionCommand()))
			changeProjectionMode(ProjectionMode.ORTHOGRAPHIC);
		//
		else if ("perspectiveProjection".equals(e.getActionCommand()))
			changeProjectionMode(ProjectionMode.PERSPECTIVE);
		// 
		else if ("rotation".equals(e.getActionCommand()))
			changeRotation();
		// 
		else if ("zoomInDemo".equals(e.getActionCommand()))
			this.mView.startZoomInDemo();
		//
		else if ("zoomOutDemo".equals(e.getActionCommand()))
			this.mView.startZoomOutDemo();
		//
		else if ("stopDemo".equals(e.getActionCommand()))
			this.mView.stopDemo();
	}
}
