package AStarDickinson.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import AStarDickinson.algs.AlgorithmReport;

@SuppressWarnings("serial")
public class ReportPanel extends JPanel {
	private static final String NODES_CONSIDERED_TEXT = "Nodes Considered: ";
	private static final String NODES_IN_PATH_TEXT = "Nodes in Path: ";
	private static final String PATH_DISTANCE_TEXT = "Path Distance: ";
	private JLabel nodesConsidered;
	private JLabel nodesInPath;
	private JLabel pathDistance;
	
	public ReportPanel() {
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Algorithm Report"));
		
		this.nodesConsidered = new JLabel(NODES_CONSIDERED_TEXT);
		this.nodesInPath = new JLabel(NODES_IN_PATH_TEXT);
		this.pathDistance = new JLabel(PATH_DISTANCE_TEXT);
		
		this.add(this.nodesConsidered);
		this.add(this.nodesInPath);
		this.add(this.pathDistance);
	}
	
	public void setNodesConsidered(int i) {
		this.nodesConsidered.setText(NODES_CONSIDERED_TEXT + i);
	}
	
	public void setNodesInPath(int i) {
		this.nodesInPath.setText(NODES_IN_PATH_TEXT + i);
	}
	
	public void setPathDistance(double d) {
		this.pathDistance.setText(PATH_DISTANCE_TEXT + d);
	}
	
	public void setAlgorithmReport(AlgorithmReport rep) {
		this.setNodesConsidered(rep.getNumNodesConsidered());
		this.setNodesInPath(rep.getNumSolutionNodes());
		this.setPathDistance(rep.getFinalPath().getPathDistance());
	}
}
