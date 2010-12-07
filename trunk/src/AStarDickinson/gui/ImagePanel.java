package AStarDickinson.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel implements ComponentListener,MouseListener {
	
	private Image image;
	private Collection<MapNode> nodes;
	private MapPath path;
	private Collection<MapPath> candidatePaths;
	private double resizeFactor;
	private MapNode selectedNode;
	
	private boolean drawAllNodes;
	private boolean drawCandidatePaths;
	private boolean drawFinalPath;
	private boolean drawImage;
	
	public ImagePanel(String pathToImage,Collection<MapNode> nodes) throws IOException {
		this(pathToImage,nodes,1);
	}
	
	public ImagePanel(String pathToImage,Collection<MapNode> nodes, double resizeFactor) throws IOException {
		this.image = ImageIO.read(new File(pathToImage));
		this.nodes = nodes;
		this.resizeFactor = resizeFactor;
		this.drawAllNodes = true;
		this.drawCandidatePaths = true;
		this.drawFinalPath = true;
		this.drawImage = true;
		
		this.setPreferredSize(new Dimension(this.getImageWidth(),this.getImageHeight()));
		
		if (AStarDickinson.AStarDickinson.NODE_MARKING) {
			this.addMouseListener(this);
		}
	}

	public MapPath getPath() {
		return path;
	}

	public void setPath(MapPath path) {
		this.path = path;
	}
	
	private int getImageWidth() {
		return (int)((double)image.getWidth(null) * this.resizeFactor);
	}
	
	private int getImageHeight() {
		return (int)((double)image.getHeight(null) * this.resizeFactor);
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
	 * @return the candidatePaths
	 */
	public Collection<MapPath> getCandidatePaths() {
		return candidatePaths;
	}

	/**
	 * @param candidatePaths the candidatePaths to set
	 */
	public void setCandidatePaths(Collection<MapPath> candidatePaths) {
		this.candidatePaths = candidatePaths;
	}

	@Override
	public void paint(Graphics g) {
		if (this.drawImage)
			g.drawImage(image, 0, 0, this.getImageWidth(), this.getImageHeight(), null);
		else {
			g.setColor(Color.black);
			g.fillRect(0, 0, this.getImageWidth(), this.getImageHeight());
		}
		
		if (this.isDrawAllNodes()) {
			for(MapNode node: nodes) {
				node.draw(g, Color.blue, this.resizeFactor);
				
				for(MapNode edgeNode: node.getEdges()) {
					node.drawEdge(g, edgeNode, Color.blue, this.resizeFactor);
				}
			}
		}
		
		if (this.isDrawCandidatePaths() && candidatePaths != null) {
			for(MapPath path: this.candidatePaths) {
				this.drawPath(path, Color.white, g);
			}
		}
		
		if (this.isDrawFinalPath() && path != null) {
			if (path.getStart() != null)
				path.getStart().draw(g, Color.red, this.resizeFactor);
			if (path.getEnd() != null)
				path.getEnd().draw(g, Color.red, this.resizeFactor);
			this.drawPath(path, Color.green, g);
		}
	}
	
	private void drawPath(MapPath path, Color c, Graphics g) {
		MapNode lastNode = null;
		for(MapNode node: path.getPath()) {
			node.draw(g, c, this.resizeFactor);
			if (lastNode != null)
				node.drawEdge(g, lastNode, c, this.resizeFactor);
			lastNode = node;
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		double w = e.getComponent().getSize().getWidth();
		this.resizeFactor = w / (double)image.getWidth(null);
		this.setPreferredSize(new Dimension(this.getImageWidth(),this.getImageHeight()));
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
		int x = (int)((double)e.getX() / this.resizeFactor);
		int y = (int)((double)e.getY() / this.resizeFactor);
		
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
					this.nodes.add(new MapNode(name,x,y,false));
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
		for (MapNode node: this.nodes) {
			if (x >= node.getPoint().getX()-5 && x <= node.getPoint().getX()+5
			 && y >= node.getPoint().getY()-5 && y <= node.getPoint().getY()+5)
				return node;
		}
		return null;
	}
}
