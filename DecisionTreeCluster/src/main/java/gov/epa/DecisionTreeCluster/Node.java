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
	private double toxMax=0.0;
	private double toxMin=Double.MAX_VALUE;
	private double toxInc;
	private double[] toxPDF = null;
	private double entropy;
	private Node parent=null;
	private Node child1=null;
	private Node child2=null;
	private Vector<Property[]> records = new Vector<Property[]>();
	private int bestPropertyIndex = 0;  // best property to split on;
	private int bestRecordIndex = 0; // best record to split on;

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
		
		if (parent==null) {
			findToxMinMaxInc(records, 100);
		} else {
			this.toxMin = parent.getToxMin();
			this.toxMax = parent.getToxMax();
			this.toxInc = parent.getToxInc();
		}

		toxPDF = getProbDist(toxMin, toxMax, toxInc);
		
		// entropy (and toxicity) are calculated from the probability distribution
		this.entropy = calculateEntropy(toxPDF);
		
		return;
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
		this.entropy = calculateEntropy(getProbDist(toxMin, toxMax, toxInc));

	}
	
	public Node(Tree allNodes, Property[] singleRecord) {
		super();
		this.allNodes = allNodes;
		this.child1 = null;
		this.child2 = null;
		this.entropy = 0.0;
		this.toxicity = (Double)singleRecord[1].getPropWrap();
		this.toxMax = this.toxicity;
		this.toxMin = this.toxicity;
		this.toxInc = 0.0;
		this.records.clear();
		this.records.add(singleRecord);
	}
	
	public void addIn(double toxInc, Node otherNode) {
		
		this.toxInc = toxInc;
		
		if (otherNode != null) {
			this.records.addAll(otherNode.records);
			if (otherNode.toxMin<this.toxMin) this.toxMin = otherNode.toxMin;
			if (otherNode.toxMax>this.toxMax) this.toxMax = otherNode.toxMax;
		} else {
			// just (re)calculate entropy
		}
		
		// entropy (and toxicity) are calculated from the probability distribution
		this.entropy = calculateEntropy(getProbDist(toxMin, toxMax, toxInc));

	}
	
	private void findToxMinMaxInc(Vector<Property[]> records, int ninc) {
		double tox;
		for (int i=0; i<records.size(); i++) {
			tox = getToxicity(records.get(i));
			if (tox > toxMax) toxMax = tox;
			if (tox < toxMin) toxMin = tox;
		}
		toxInc = (toxMax-toxMin)/ninc;
	}
	
	private double[] getProbDist(double toxMin, double toxMax, double toxInc) {
		int index = 0;
		double tox, avgTox=0.0;
		int maxIndex = (int) ((toxMax-toxMin)/toxInc);
		double[] probDist = new double[maxIndex+1];
		double probInc = 1.0/records.size();

		for (int i=0; i<records.size(); i++) {
			tox = getToxicity(records.get(i));
			avgTox += tox;
			index = (int) ((tox-toxMin)/toxInc);
			probDist[index]+=probInc;
		}
		this.toxicity = avgTox/records.size(); // set toxicity to avgTox;
		
		return probDist;
	}

	private static double getToxicity(Property[] record) {
		double tox;
		tox = (Double) record[1].getPropWrap();
		return tox;
	}
	
	private static double calculateEntropy(double probDist[]) {
		double prob = 0;
		double entropy = 0;
		
		for (int i=0; i<probDist.length; i++) {
			prob = probDist[i];
			if (prob > 0.0) {
				entropy -= prob * Math.log(prob);
			}
		}
		if (entropy < 3.0e-16 ) entropy = 0.0;  // take care of round-off error
		
		return entropy;
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

	public double getToxMax() {
		return toxMax;
	}

	public double getToxMin() {
		return toxMin;
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

	public int getBestPropertyIndex() {
		return bestPropertyIndex;
	}
	
	public void setBestPropertyIndex(int bestPropertyIndex) {
		this.bestPropertyIndex = bestPropertyIndex;
	}

	public int getBestRecordIndex() {
		return bestRecordIndex;
	}

	public void setBestRecordIndex(int bestRecordIndex) {
		this.bestRecordIndex = bestRecordIndex;
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
