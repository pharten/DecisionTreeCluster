package gov.epa.DecisionTreeCluster;

import java.lang.reflect.Member;
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
	
	public Node(Tree allNodes, Node child1, Node child2) {
		super();
		this.allNodes = allNodes;
		this.records.clear();
		Vector<Property[]> records1 = child1.getRecords();
		Vector<Property[]> records2 = child2.getRecords();
		this.records.addAll(records1);
		this.records.addAll(records2);
		double wght1 = ((double)records1.size())/records.size();
		double wght2 = ((double)records2.size())/records.size();
		
		// toxicity is calculated as a weighed sum of toxicity's of clusters
		this.mean = wght1*child1.getMean()+wght2*child2.getMean();
		
		// determine if entropy should be recalculated as a weighted sum of entropies, instead
		this.entropy = -wght1*Math.log(wght1)-wght2*Math.log(wght2);
//		calculateEntropy();
//		calculateMeanToxicityAndStd();
	}
	
	public Node(Tree allNodes, Property[] singleRecord) {
		super();
		this.allNodes = allNodes;
		this.entropy = 0.0;
		this.mean = ((Double)singleRecord[1].getPropWrap()).doubleValue();
		this.std = 0.0;
		this.records.clear();
		this.records.add(singleRecord);
	}

	boolean isLeaf() {
		return (child1==null && child2==null);
	}

	public double getMean() {
		return mean;
	}

	public double getStd() {
		return std;
	}

	public double getEntropy() {
		return entropy;
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

	public Node getChild1() {
		return child1;
	}

	public Node getChild2() {
		return child2;
	}

	public Vector<Property[]> getRecords() {
		return records;
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
				Object member = property.getPropWrap();
				if (member.getClass()==Double.class) {
					total += property.getPropWrap().toString()+endVal;
				} else if (member.getClass()==Integer.class) {
					total += property.getPropWrap().toString()+endVal;
				} else if (member.getClass()==String.class) {
					total += property.getPropWrap()+endVal;
					String memberName = property.getPropName();
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
