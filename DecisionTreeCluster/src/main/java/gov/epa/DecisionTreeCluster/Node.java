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
	private Vector<Property[]> records = new Vector<Property[]>();

//	private int splitAttibuteIndex;
//	private Property splitAttributeValue;
	
	public Node(Tree allNodes) {
		super();
		this.allNodes = allNodes;
	}
	
	public Node(Tree allNodes, Node parent, Vector<Property[]> records) {
		super();
		this.allNodes = allNodes;
		this.parent = parent;
		this.records = records;
	}
	
	public Node(Tree allNodes, Property[] singleRecord) {
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

	public Vector<Property[]> getRecords() {
		return records;
	}

	public void setRecords(Vector<Property[]> records) {
		this.records = records;
	}

	public int compareTo(Node o) {
		return (this.hashCode()-o.hashCode());
	}
	
	@Override
	public String toString() {
		String total="";
		for (int i=0; i<records.size(); i++) {
			Property[] record = records.get(i);
			String endVal = ", ";
			for (int j=0; j<record.length; j++) {
				if (j==record.length-1) endVal = "\n";
				Property property = record[j];
				Object datatype = property.getDataType();
				if (datatype==double.class) {
					total += Double.toString(property.getdValue())+endVal;
				} else if (datatype==int.class) {
					total += Integer.toString(property.getiValue())+endVal;
				} else if (datatype==Category.class) {
					total += property.getcValue()+endVal;
				} else {
					try {
						throw new Exception("Error: Bad datatype");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
		return total;
	}

}
