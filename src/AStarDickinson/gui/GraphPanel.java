package AStarDickinson.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;
import AStarDickinson.exec.AStarDickinson;
import AStarDickinson.render.GraphRenderer;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel implements ComponentListener,MouseListener {
	private GraphRenderer renderer;
	private boolean drawAllNodes;
	private boolean drawCandidatePaths;
	private boolean drawFinalPath;
	private boolean drawImage;
	private boolean drawLandmarks;
	private MapNode selectedNode;
	
	public GraphPanel(GraphRenderer renderer)
	{
		this.renderer = renderer;
		this.drawAllNodes = true;
		this.drawCandidatePaths = true;
		this.drawFinalPath = true;
		this.drawImage = true;
		this.drawLandmarks = true;
		
		this.setPreferredSize(new Dimension(renderer.getGraphWidth(),renderer.getGraphHeight()));
		if (AStarDickinson.NODE_MARKING) {
			this.addMouseListener(this);
		}
	}
	
	public boolean isDrawImage() {
		return drawImage;
	}

	public void setDrawImage(boolean drawImage) {
		this.drawImage = drawImage;
	}

	/**
	 * @param drawAllNodes the drawAllNodes to set
	 */
	public void setDrawAllNodes(boolean drawAllNodes) {
		this.drawAllNodes = drawAllNodes;
	}
	
	/**
	 * @return the drawAllNodes
	 */
	public boolean isDrawAllNodes() {
		return drawAllNodes;
	}

	/**
	 * @return the drawCandidatePaths
	 */
	public boolean isDrawCandidatePaths() {
		return drawCandidatePaths;
	}

	/**
	 * @param drawCandidatePaths the drawCandidatePaths to set
	 */
	public void setDrawCandidatePaths(boolean drawCandidatePaths) {
		this.drawCandidatePaths = drawCandidatePaths;
	}

	/**
	 * @return the drawFinalPath
	 */
	public boolean isDrawFinalPath() {
		return drawFinalPath;
	}

	/**
	 * @param drawFinalPath the drawFinalPath to set
	 */
	public void setDrawFinalPath(boolean drawFinalPath) {
		this.drawFinalPath = drawFinalPath;
	}
	
	/**
	 * @return the drawLandmarks
	 */
	public boolean isDrawLandmarks() {
		return drawLandmarks;
	}

	/**
	 * @param drawLandmarks the drawLandmarks to set
	 */
	public void setDrawLandmarks(boolean drawLandmarks) {
		this.drawLandmarks = drawLandmarks;
	}
	
	/**
	 * @return the renderer
	 */
	public GraphRenderer getRenderer() {
		return renderer;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		renderer.render(g, drawImage, drawAllNodes, drawLandmarks, drawCandidatePaths, drawFinalPath);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		double w = e.getComponent().getSize().getWidth();
		renderer.setResizeFactor(w / (double)renderer.getRawWidth());
		this.setPreferredSize(new Dimension(renderer.getGraphWidth(),renderer.getGraphHeight()));
		this.repaint();
	}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = (int)((double)e.getX() / renderer.getResizeFactor());
		int y = (int)((double)e.getY() / renderer.getResizeFactor());
		
		MapNode selected = this.selectNode(x, y);
		if (selectedNode != null && selected != null) {
			selectedNode.addEdge(selected);
			selected.addEdge(selectedNode);
			System.out.println("\tto " + selected);
		} else {
			MapNode oldSelected = selectedNode;
			selectedNode = selected;
			if (selectedNode == null && oldSelected == null) {
				String name = JOptionPane.showInputDialog("Name of (" + x + "," + y + ")?");
				if (name != null) {
					renderer.getNodes().add(new MapNode(name,x,y,false));
					this.repaint();
				}
			} else {
				System.out.println(selectedNode);
			}
		}
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	private MapNode selectNode(int x, int y) {
		for (MapNode node: renderer.getNodes()) {
			if (x >= node.getPoint().getX()-5 && x <= node.getPoint().getX()+5
			 && y >= node.getPoint().getY()-5 && y <= node.getPoint().getY()+5)
				return node;
		}
		return null;
	}
}
