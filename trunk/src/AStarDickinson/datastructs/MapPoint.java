package AStarDickinson.datastructs;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MapPoint {
	private final int y;
	private final int x;
	
	public MapPoint(int x, int y) {
		super();
		this.y = y;
		this.x = x;
	}

	public int gety() {
		return y;
	}

	public int getx() {
		return x;
	}
	
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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
