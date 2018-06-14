package gov.epa.DecisionTreeCluster;

import java.io.Serializable;
import java.util.Comparator;

public class ToxComp implements Comparator<Node>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7059771763099059202L;

	/**
	 * 
	 */

	public ToxComp() {
		// TODO Auto-generated constructor stub
	}

	public int compare(Node node1, Node node2) {
		double diff = node1.getMean()-node2.getMean();
		if (diff < 0.0) {
			return -1;
		} else if (diff > 0.0) {
			return 1;
		}
		return 0;
	}

}
