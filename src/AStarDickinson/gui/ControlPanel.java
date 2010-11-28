package AStarDickinson.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import AStarDickinson.XMLExporter;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements ActionListener {
	private Vector destNodes;
	private ImagePanel imagePanel;
	private JCheckBox drawNodes;
	private JCheckBox drawCandidatePaths;
	private JCheckBox drawFinalPath;
	private JComboBox start;
	private JComboBox end;
	private JComboBox algorithm;
	private JButton search;
	private MapPath path;
	
	public ControlPanel(ImagePanel imagePanel,final Collection<MapNode> allnodes) {
		this.imagePanel = imagePanel;
		
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setDestinations(allnodes);
		this.setPreferredSize(new Dimension(300,300));
		
		this.drawNodes = new JCheckBox("All Nodes", imagePanel.isDrawAllNodes());
		this.drawCandidatePaths = new JCheckBox("Candidate Paths", imagePanel.isDrawAllNodes());
		this.drawFinalPath = new JCheckBox("Final Path", imagePanel.isDrawAllNodes());
		this.start = new JComboBox(this.destNodes);
		this.end = new JComboBox(this.destNodes);
		this.algorithm = new JComboBox(this.getAlgsVector());
		this.search = new JButton("Search");
		
		this.drawNodes.addActionListener(this);
		this.drawCandidatePaths.addActionListener(this);
		this.drawFinalPath.addActionListener(this);
		this.start.addActionListener(this);
		this.end.addActionListener(this);
		this.search.addActionListener(this);
		
		JPanel checks = new JPanel();
		checks.setLayout(new BoxLayout(checks,BoxLayout.Y_AXIS));
		checks.add(this.drawNodes);
		checks.add(this.drawCandidatePaths);
		checks.add(this.drawFinalPath);
		
		this.add(createLabeledPanel("Show: ",checks));
		this.add(createLabeledPanel("Start: ",start));
		this.add(createLabeledPanel("End: ",end));
		this.add(createLabeledPanel("Algorithm",algorithm));
		
		
		JPanel buttons = new JPanel();
		buttons.add(search);
		
		if (AStarDickinson.AStarDickinson.NODE_MARKING) {
			JButton button = new JButton("Save Node Data");
			buttons.add(button);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new XMLExporter().saveXML(allnodes,AStarDickinson.AStarDickinson.DEFAULT_DATA);
				}
			});
		}
		
		this.add(buttons);
	}
	
	private static JPanel createLabeledPanel(String label,JComponent c) {
		JPanel panel = new JPanel();
		panel.add(new JLabel(label));
		panel.add(c);
		return panel;
	}
	
	private void setDestinations(Collection<MapNode> allnodes) {
		TreeSet<MapNode> sorter = new TreeSet<MapNode>(); 
		for (MapNode node: allnodes)
			if (node.isDestination())
				sorter.add(node);
		destNodes = new Vector(sorter);
		destNodes.insertElementAt("Select a Location", 0);
	}
	
	private Vector getAlgsVector() {
		Vector algs = new Vector(PathFinder.getAvailableAlgorithms());
		algs.insertElementAt("Select an Algorithm", 0);
		return algs;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.drawNodes) {
			imagePanel.setDrawAllNodes(this.drawNodes.isSelected());
		} else if (e.getSource() == this.drawCandidatePaths) {
			imagePanel.setDrawCandidatePaths(this.drawCandidatePaths.isSelected());
		} else if (e.getSource() == this.drawFinalPath) {
			imagePanel.setDrawFinalPath(this.drawFinalPath.isSelected());
		} else if (e.getSource() == this.start || e.getSource() == this.end) {
			MapNode start = this.getSelectedNode(this.start);
			MapNode end = this.getSelectedNode(this.end);
			path = new MapPath(start,end);
			imagePanel.setPath(path);
			imagePanel.setCandidatePaths(null);
		} else if (e.getSource() == this.search) {
			PathFinder finder = this.getSelectedAlgorithm();
			if (finder != null) {
				AlgorithmReport report = finder.findPath(imagePanel,path.getStart(), path.getEnd());
			}
		}
		imagePanel.repaint();
	}
	
	private MapNode getSelectedNode(JComboBox box) {
		if (box.getSelectedIndex() > 0)
			return (MapNode)box.getSelectedItem();
		else
			return null;
	}
	
	private PathFinder getSelectedAlgorithm() {
		if (this.algorithm.getSelectedIndex() > 0)
			return (PathFinder)this.algorithm.getSelectedItem();
		else
			return null;
	}
}
