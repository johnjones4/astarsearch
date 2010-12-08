package AStarDickinson.datastructs.tree;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;

import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;

/**
 * This class represents a node in the search trees used by the path finding
 * algorithms.
 * 
 * @author johnjones
 * 
 */
public class TreeNode {
	private TreeNode parent;
	private Collection<TreeNode> children;
	private MapNode value;

	/**
	 * Construct a new node that has a parent and a value.
	 * 
	 * @param parent
	 *            The parent of this node
	 * @param value
	 *            The value of this node
	 */
	public TreeNode(TreeNode parent, MapNode value) {
		super();
		this.parent = parent;
		this.children = new LinkedList<TreeNode>();
		this.value = value;
	}

	/**
	 * @return the parent
	 */
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * @return the children
	 */
	public Collection<TreeNode> getChildren() {
		return children;
	}

	/**
	 * @return the value
	 */
	public MapNode getValue() {
		return value;
	}
	
	/**
	 * Check if the node given is an ancestor of this node or the same node
	 * 
	 * @param node Node to check
	 * @return True if they are the same node or if the given node is an ancestor of this node
	 */
	public boolean hasAncestor(TreeNode node) {
		if (this.equals(node))
			return true;
		else if (this.parent != null)
			return this.parent.hasAncestor(node);
		else
			return false;
	}

	/**
	 * Add a child to this node
	 * 
	 * @param node
	 *            The child to add
	 */
	public void addChild(TreeNode node) {
		if (this.hasAncestor(node))
			throw new IllegalArgumentException("Cannot add ancsestor node to graph");
		
		this.children.add(node);
	}

	/**
	 * This method builds a MapPath class by recursing up the tree so that the
	 * value at the root node is the first component in the path and this node's
	 * value is the last component in the path
	 * 
	 * @param path The path object to add path components to
	 */
	public void assembleInversePath(MapPath path) {
		if (this.parent != null)
			parent.assembleInversePath(path);
		path.addNode(this.value);
	}

	/**
	 * Get the number of descendants of this node
	 * @return The number of descendants
	 */
	public int getNumNodes() {
		int children = 1;
		for (TreeNode child : this.children) {
			children += child.getNumNodes();
		}
		return children;
	}

	/**
	 * Draw this node's value and edges to its childrens' values to model the graph that the values are based upon 
	 * 
	 * @param g The graphics context to draw to
	 * @param c The color to draw with
	 * @param reduction The resize factor
	 */
	public void drawMapNodes(Graphics g, Color c, double reduction) {
		this.value.draw(g, c, reduction);
		for (TreeNode child : this.children) {
			this.value.drawEdge(g, child.getValue(), c, reduction);
			child.drawMapNodes(g, c, reduction);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeNode other = (TreeNode) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
