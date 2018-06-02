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
	private Tree allNodes=null;
	private Vector<String[]> records=null;
	private double mean;
	private double std;
	private double entropy;
	private double weight;
	private Node parent=null;
	private Node child1=null;
	private Node child2=null;
	private Vector<Attribute> attributes;
	private Vector<Attribute[]> records2;
	private int splitAttibuteIndex;
	private Attribute splitAttributeValue;
	
	public Node(Tree allNodes) {
		super();
		this.allNodes = allNodes;
	}
	
	public Node(Tree allNodes, Node parent, Vector<String[]> records) {
		super();
		this.allNodes = allNodes;
		this.parent = parent;		
	}
	
	public Node(Tree allNodes, String[] singleRecord) {
		super();
		this.allNodes = allNodes;
		this.records = new Vector<String[]>();
		records.add(singleRecord);
	}
	
	
	boolean isLeaf() {
		return (child1==null && child2==null);
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getStd() {
		return std;
	}

	public void setStd(double std) {
		this.std = std;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getChild1() {
		return child1;
	}

	public void setChild1(Node child1) {
		this.child1 = child1;
	}

	public Node getChild2() {
		return child2;
	}

	public void setChild2(Node child2) {
		this.child2 = child2;
	}

	public Vector<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Vector<Attribute> attributes) {
		this.attributes = attributes;
	}

}
