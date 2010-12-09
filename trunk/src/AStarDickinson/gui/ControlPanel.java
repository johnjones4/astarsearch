package AStarDickinson.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
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

import AStarDickinson.XMLExporter;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.algs.implementations.StaticALTSearch;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements ActionListener {
	@SuppressWarnings("unchecked")
	private Vector destNodes;
	private ImagePanel imagePanel;
	private ReportPanel reportPanel;
	private JCheckBox drawImage;
	private JCheckBox drawNodes;
	private JCheckBox drawCandidatePaths;
	private JCheckBox drawFinalPath;
	private JComboBox start;
	private JComboBox end;
	private JComboBox algorithm;
	private JButton search;
	private MapPath path;
	
	public ControlPanel(ImagePanel imagePanel,ReportPanel reportPanel,final Collection<MapNode> allnodes) {
		this.imagePanel = imagePanel;
		this.reportPanel = reportPanel;
		
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Controls"));
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setDestinations(allnodes);
		this.setPreferredSize(new Dimension(350,300));
		
		this.drawImage = new JCheckBox("Image", imagePanel.isDrawImage());
		this.drawNodes = new JCheckBox("All Nodes", imagePanel.isDrawAllNodes());
		this.drawCandidatePaths = new JCheckBox("Candidate Paths", imagePanel.isDrawAllNodes());
		this.drawFinalPath = new JCheckBox("Final Path", imagePanel.isDrawAllNodes());
		this.start = new JComboBox(this.destNodes);
		this.end = new JComboBox(this.destNodes);
		this.algorithm = new JComboBox(this.getAlgsVector());
		this.search = new JButton("Search");
		
		this.drawImage.addActionListener(this);
		this.drawNodes.addActionListener(this);
		this.drawCandidatePaths.addActionListener(this);
		this.drawFinalPath.addActionListener(this);
		this.start.addActionListener(this);
		this.end.addActionListener(this);
		this.search.addActionListener(this);
		
		JPanel checks = new JPanel();
		checks.setLayout(new BoxLayout(checks,BoxLayout.Y_AXIS));
		checks.add(this.drawImage);
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
	
	@SuppressWarnings("unchecked")
	private void setDestinations(Collection<MapNode> allnodes) {
		TreeSet<MapNode> sorter = new TreeSet<MapNode>(); 
		for (MapNode node: allnodes)
			if (node.isDestination())
				sorter.add(node);
		destNodes = new Vector(sorter);
		destNodes.insertElementAt("Select a Location", 0);
	}
	
	@SuppressWarnings("unchecked")
	private Vector getAlgsVector() {
		Vector algs = new Vector(PathFinder.getAvailableAlgorithms().values());
		algs.insertElementAt("Select an Algorithm", 0);
		return algs;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.drawImage) {
			imagePanel.setDrawImage(this.drawImage.isSelected());
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
			imagePanel.setPath(path);
			imagePanel.setTree(null);
		} else if (e.getSource() == this.search) {
			PathFinder finder = this.getSelectedAlgorithm();
			if (finder != null) {
				if (finder instanceof StaticALTSearch)
					((StaticALTSearch)finder).precompute()
				AlgorithmReport report = finder.findPath(new PathFinderDelegate() {
					@Override
					public void pathsWereUpdated() {
						imagePanel.repaint();
					}

					@Override
					public void setFinalPath(MapPath finalPath) {
						imagePanel.setPath(finalPath);
						imagePanel.repaint();
					}

					@Override
					public void setRootNode(TreeNode node) {
						imagePanel.setTree(node);
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
