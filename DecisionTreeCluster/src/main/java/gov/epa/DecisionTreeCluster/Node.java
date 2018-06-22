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
	private double toxicity;
	private double toxMax;
	private double toxMin;
	private double toxInc;
	private double std;
	private double entropy;
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
	
	public Node(double toxInc, Node child1, Node child2) {
		super();
		
		this.child1 = child1;
		this.child2 = child2;
		
		this.records.clear();
		this.records.addAll(child1.getRecords());
		this.records.addAll(child2.getRecords());
		
		this.toxInc = toxInc;
		this.toxMin = (child1.toxMax<child2.toxMin ? child1.toxMin : child2.toxMin);
		this.toxMax = (child1.toxMax>child2.toxMax ? child1.toxMax : child2.toxMax);
		
		// entropy (and toxicity) are calculated from the probability distribution
		calculateEntropy(getProbDist(toxMin, toxMax, toxInc));

	}
	
	public Node(Tree allNodes, Property[] singleRecord) {
		super();
		this.allNodes = allNodes;
		this.child1 = null;
		this.child2 = null;
		this.entropy = 0.0;
		this.toxicity = ((Double)singleRecord[1].getPropWrap()).doubleValue();
		this.toxMax = this.toxicity;
		this.toxMin = this.toxicity;
		this.toxInc = 0.0;
		this.std = 0.0;
		this.records.clear();
		this.records.add(singleRecord);
	}
	
	public void addIn(double toxInc, Node otherNode) {
		
		this.records.addAll(otherNode.records);
		
		this.toxInc = toxInc;
		if (otherNode.toxMin<this.toxMin) this.toxMin = otherNode.toxMin;
		if (otherNode.toxMax>this.toxMax) this.toxMax = otherNode.toxMax;
		
		// entropy (and toxicity) are calculated from the probability distribution
		calculateEntropy(getProbDist(toxMin, toxMax, toxInc));

	}
	
	private double[] getProbDist(double toxMin, double toxMax, double toxInc) {
		int index = 0;
		double tox, sumTox=0.0;
		int maxIndex = (int) ((toxMax-toxMin)/toxInc);
		double[] probDist = new double[maxIndex+1];
		double probInc = 1.0/records.size();

		for (int i=0; i<records.size(); i++) {
			tox = ((Double)(records.get(i)[1].getPropWrap())).doubleValue();
			sumTox += tox;
			index = (int) ((tox-toxMin)/toxInc);
			probDist[index]+=probInc;
		}
		this.toxicity = sumTox/records.size(); // set toxicity to avgTox;
		
		return probDist;
	}
	
	private void calculateEntropy(double probDist[]) {
		double prob = 0;
		double entropy = 0;
		
		for (int i=0; i<probDist.length; i++) {
			prob = probDist[i];
			if (prob > 0.0) {
				entropy -= prob * Math.log(prob);
			}
		}
		this.entropy = (entropy < 3.0e-16 ? 0.0: entropy);  // take care of round-off error
		
		return;
	}

	boolean isLeaf() {
		return (child1==null && child2==null);
	}

	public double getToxicity() {
		return toxicity;
	}

	public double getToxInc() {
		return toxInc;
	}

	public double getStd() {
		return std;
	}

	public double getEntropy() {
		return entropy;
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
