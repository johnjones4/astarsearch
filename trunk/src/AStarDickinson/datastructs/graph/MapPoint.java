package AStarDickinson.datastructs.graph;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * MapPoint is similar to the JDK Point class except it does not permit
 * relocation of the coordinates and it provides XML serialization.
 * 
 * @author johnjones
 * 
 */
public class MapPoint {
	private final int y;
	private final int x;

	/**
	 * Construct a new MapPoint at the given location.
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	public MapPoint(int x, int y) {
		super();
		this.y = y;
		this.x = x;
	}

	/**
	 * Get the Y coordinate
	 * 
	 * @return the Y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Get the X coordinate
	 * 
	 * @return the X coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Serialize the object into XML.
	 * 
	 * @param doc
	 *            The document to which the XML will be appended to
	 * @return An XML element
	 */
	public Element xml(Document doc) {
		Element point = doc.createElement("point");

		Element x = doc.createElement("x");
		x.appendChild(doc.createTextNode(String.valueOf(this.x)));
		point.appendChild(x);

		Element y = doc.createElement("y");
		y.appendChild(doc.createTextNode(String.valueOf(this.y)));
		point.appendChild(y);

		return point;
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
		result = prime * result + x;
		result = prime * result + y;
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
		MapPoint other = (MapPoint) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
