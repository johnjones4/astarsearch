package AStarDickinson.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.XMLExporter;
import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements ActionListener {
	@SuppressWarnings("rawtypes")
	private Vector destNodes;
	private GraphPanel imagePanel;
	private ReportPanel reportPanel;
	private JCheckBox drawImage;
	private JCheckBox drawNodes;
	private JCheckBox drawCandidatePaths;
	private JCheckBox drawFinalPath;
	private JCheckBox drawLandmarks;
	private JComboBox start;
	private JComboBox end;
	private JComboBox algorithm;
	private JButton search;
	private MapPath path;
	private List<MapNode> graph;
	
	public ControlPanel(GraphPanel imagePanel,ReportPanel reportPanel,List<MapNode> graph) {
		this.imagePanel = imagePanel;
		this.reportPanel = reportPanel;
		this.graph = graph;
		
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Controls"));
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setDestinations(this.graph);
		this.setPreferredSize(new Dimension(500,300));
		
		this.drawImage = new JCheckBox("Image", imagePanel.isDrawImage());
		this.drawNodes = new JCheckBox("All Nodes", imagePanel.isDrawAllNodes());
		this.drawCandidatePaths = new JCheckBox("Candidate Paths", imagePanel.isDrawAllNodes());
		this.drawLandmarks = new JCheckBox("Landmarks", imagePanel.isDrawLandmarks());
		this.drawFinalPath = new JCheckBox("Final Path", imagePanel.isDrawAllNodes());
		this.start = new JComboBox(this.destNodes);
		this.end = new JComboBox(this.destNodes);
		this.algorithm = new JComboBox(this.getAlgsVector());
		this.search = new JButton("Search");
		
		this.drawImage.addActionListener(this);
		this.drawNodes.addActionListener(this);
		this.drawCandidatePaths.addActionListener(this);
		this.drawFinalPath.addActionListener(this);
		this.drawLandmarks.addActionListener(this);
		this.start.addActionListener(this);
		this.end.addActionListener(this);
		this.search.addActionListener(this);
		
		JPanel checks = new JPanel();
		checks.setLayout(new BoxLayout(checks,BoxLayout.Y_AXIS));
		checks.add(this.drawImage);
		checks.add(this.drawNodes);
		checks.add(this.drawLandmarks);
		checks.add(this.drawCandidatePaths);
		checks.add(this.drawFinalPath);
		
		this.add(createLabeledPanel("Show: ",checks));
		this.add(createLabeledPanel("Start: ",start));
		this.add(createLabeledPanel("End: ",end));
		this.add(createLabeledPanel("Algorithm",algorithm));
		
		
		JPanel buttons = new JPanel();
		buttons.add(search);
		
		if (AStarDickinson.exec.AStarDickinson.NODE_MARKING) {
			JButton button = new JButton("Save Node Data");
			buttons.add(button);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new XMLExporter().saveXML(ControlPanel.this.graph,AStarDickinson.exec.AStarDickinson.DEFAULT_DATA);
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setDestinations(Collection<MapNode> allnodes) {
		TreeSet<MapNode> sorter = new TreeSet<MapNode>(); 
		for (MapNode node: allnodes)
			if (node.isDestination())
				sorter.add(node);
		destNodes = new Vector(sorter);
		destNodes.insertElementAt("Select a Location", 0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Vector getAlgsVector() {
		Vector algs = new Vector(PathFinder.getAvailableAlgorithms(this.graph).values());
		algs.insertElementAt("Select an Algorithm", 0);
		return algs;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.drawImage) {
			imagePanel.setDrawImage(this.drawImage.isSelected());
		} else if (e.getSource() == this.drawLandmarks) {
			imagePanel.setDrawLandmarks(this.drawLandmarks.isSelected());
		} else if (e.getSource() == this.drawNodes) {
			imagePanel.setDrawAllNodes(this.drawNodes.isSelected());
		} else if (e.getSource() == this.drawCandidatePaths) {
			imagePanel.setDrawCandidatePaths(this.drawCandidatePaths.isSelected());
		} else if (e.getSource() == this.drawFinalPath) {
			imagePanel.setDrawFinalPath(this.drawFinalPath.isSelected());
		} else if (e.getSource() == this.start || e.getSource() == this.end) {
			MapNode start = this.getSelectedNode(this.start);
			MapNode end = this.getSelectedNode(this.end);
			path = new MapPath(start,end);
			imagePanel.getRenderer().setPath(path);
			imagePanel.getRenderer().setTree(null);
			imagePanel.getRenderer().setLandmarks(null);
		} else if (e.getSource() == this.search) {
			imagePanel.getRenderer().setTree(null);
			imagePanel.getRenderer().setLandmarks(null);
			PathFinder finder = this.getSelectedAlgorithm();
			if (finder != null) {
				AlgorithmReport report = finder.findPath(new PathFinderDelegate() {
					@Override
					public void pathsWereUpdated() {
						imagePanel.repaint();
					}

					@Override
					public void setFinalPath(MapPath finalPath) {
						imagePanel.getRenderer().setPath(finalPath);
						imagePanel.repaint();
					}

					@Override
					public void setRootNodes(TreeNode[] nodes) {
						imagePanel.getRenderer().setTree(nodes);
						imagePanel.repaint();
					}

					@Override
					public void setLandmarks(Collection<Landmark> landmarks) {
						imagePanel.getRenderer().setLandmarks(landmarks);
						imagePanel.repaint();
					}

					
				},path.getStart(), path.getEnd());
				this.reportPanel.setAlgorithmReport(report);
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
