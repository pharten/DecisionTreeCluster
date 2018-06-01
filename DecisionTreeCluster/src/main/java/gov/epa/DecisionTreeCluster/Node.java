package gov.epa.DecisionTreeCluster;

import java.util.Vector;

/**
 * 
 */

/**
 * @author PHARTEN
 *
 */
public class Node {

	/**
	 *
	 */
	private double mean;
	private double std;
	private double entropy;
	private double weight;
	private Node parent;
	private Node child1=null;
	private Node child2=null;
	private Vector<Attribute> attributes;
//	private Vector<Record> records;
	private int splitAttibuteIndex;
	private Attribute splitAttributeValue;
	
	public Node() {
		super();
	}
	
	public Node(Node parent) {
		super();
		this.parent = parent;		
	}
	
	boolean isLeaf() {
		return (child1==null && child2==null);
	}

}
