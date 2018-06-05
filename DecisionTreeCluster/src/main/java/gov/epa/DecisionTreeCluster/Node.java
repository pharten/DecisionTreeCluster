package gov.epa.DecisionTreeCluster;

import java.util.Vector;

/**
 * 
 */

/**
 * @author PHARTEN
 *
 */
public class Node implements Comparable<Node> {

	/**
	 *
	 */
	private Tree allNodes=null;
	private double mean;
	private double std;
	private double entropy;
	private double weight;
	private Node parent=null;
	private Node child1=null;
	private Node child2=null;
	private Vector<Object[]> records = new Vector<Object[]>();

//	private int splitAttibuteIndex;
//	private Property splitAttributeValue;
	
	public Node(Tree allNodes) {
		super();
		this.allNodes = allNodes;
	}
	
	public Node(Tree allNodes, Node parent, Vector<Object[]> records) {
		super();
		this.allNodes = allNodes;
		this.parent = parent;
		this.records = records;
	}
	
	public Node(Tree allNodes, Object[] singleRecord) {
		super();
		this.allNodes = allNodes;
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

	public Vector<Object[]> getRecords() {
		return records;
	}

	public void setRecords(Vector<Object[]> records) {
		this.records = records;
	}

	public int compareTo(Node o) {
		return (this.hashCode()-o.hashCode());
	}
	
	@Override
	public String toString() {
		String total="";
		for (int i=0; i<records.size(); i++) {
			Object[] record = records.get(i);
			for (int j=0; j<record.length-1; j++) {
				total += record[j]+", ";
			}
			total+=record[record.length-1]+"\n";
		}
		return total;
	}

}
