package gov.epa.DecisionTreeCluster;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import com.opencsv.CSVReader;

public class Tree extends TreeSet<Node> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] dataNames = null;
	private Object[] dataTypes = null;
	private double[] stddev = null;
	private Property[] record = null;
	private Vector<Property[]> records = null;
	private Vector<Category> categories = new Vector<Category>();
	
	public Tree(ToxComp toxComp, String filename) {
		
		super(toxComp);  // sort nodes on tree by mean toxicity

		try {
			
			// sorting Nodes by Toxicity is done while creating this Tree
			createTree(filename);
			
			Node parentNode = this.first();
			double entropyReduction = splitTree(parentNode);
			System.out.println("Entropy reduction by split= "+entropyReduction);
			
			entropyReduction = splitTree(parentNode.getChild1());
			System.out.println("Entropy reduction by split= "+entropyReduction);
			
			entropyReduction = splitTree(parentNode.getChild2());
			System.out.println("Entropy reduction by split= "+entropyReduction);
			
			Iterator<Node> iter = this.iterator();
			while(iter.hasNext()) {
				Node node = iter.next();
				System.out.println("Toxicity = "+node.getToxicity()+", entropy = "+node.getEntropy()+", size = "+node.getRecords().size());
			}
			
//			int i = 0;
//			while (this.size()>2 && i++<2) {
//				clusterNodes();
//				System.out.println(this.size());
//			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
		
	}
	
	private void createTree(String filename) throws Exception {
		
		records = readCvsFile(filename);
		
		createSingleNodeTree(records);
		
//		createTreeOfSingleRecordNodes(records);
//		
//		stddev = getStandardDeviations(records);
//		
//		normalizeTree(stddev);
		
	}

	private Vector<Property[]> readCvsFile(String filename) throws IOException, Exception {
		double value;
		int intval;
		
		CSVReader csvReader = new CSVReader(new FileReader(filename));

		String[] line = csvReader.readNext();
		
		if (line==null) {
			csvReader.close();
			throw new Exception("First line of "+filename+" is null");
		}

		dataNames = new String[line.length];
		dataTypes = new Object[line.length];
		
		for (int i=0; i<dataNames.length; i++) {
			dataNames[i] = line[i];
			if (i==0) {				
				dataTypes[i] = Category.class;
				categories.add(new Category(dataNames[i]));
			} else if (i==dataNames.length) {
				dataTypes[i] = Integer.class;
			} else {
				dataTypes[i] = Double.class;
			}
		}
		
		records = new Vector<Property[]>();
		
		while ((line=csvReader.readNext())!=null) {
			record = new Property[dataNames.length];
			for (int i=0; i<record.length; i++) {
				if (line[i].equalsIgnoreCase("Null")) {
					record[i] = new Property();
				} else if (dataTypes[i] == Double.class) {
					value = Double.parseDouble(line[i]);
					record[i] = new Property(Double.valueOf(value));
				} else if (dataTypes[i] == Integer.class) {
					line[i] = line[i].replace('.', '0');
					intval = Integer.parseInt(line[i]);
					record[i] = new Property(Integer.valueOf(intval));
				} else if (dataTypes[i] == Category.class) {
					record[i] = new Property(categories, dataNames[i], line[i]);
				} else {
					csvReader.close();
					throw new Exception("Property datatype not found");
				}
			}
			records.add(record);
		}
		
		csvReader.close();
		
		return records;
	}
	
	private void createSingleNodeTree(Vector<Property[]> records) {
		this.add(new Node(this, null, records));
	}
	
	private double splitTree(Node parentNode) throws Exception {
		
		int bestPropertyIndex = splitOnBestProperty(parentNode);
		
		Node child1 = parentNode.getChild1();
		Node child2 = parentNode.getChild2();
		
		this.add(child1);
		this.add(child2);
		
		double entropy0 = parentNode.getEntropy();
		double entropy1 = child1.getEntropy();
		double entropy2 = child2.getEntropy();
		double avgEntropy = (child1.getRecords().size()*entropy1+child2.getRecords().size()*entropy2)/parentNode.getRecords().size();
		
		double entropyReduction = entropy0 - avgEntropy;
		
		return entropyReduction;
	}
	
	private int splitOnBestProperty(Node parentNode) throws Exception {
		double value1, value2;
		Property[] record1, record2;
		Node child1, child2;
		Node child1Best, child2Best;
		double entropy1, entropy2;
		Vector<Property[]> recordsL = null, recordsR = null;
		double avgEntropy, leastEntropy, parentEntropy;
		int bestPropertyIndex = 2;
		
		child1Best = null;
		child2Best = null;
		parentEntropy = parentNode.getEntropy();
		leastEntropy = parentEntropy;
		Vector<Property[]> records = parentNode.getRecords();
		record1 = records.firstElement();
		
		for (int j=1; j<record1.length; j++) {
			recordsL = new Vector<Property[]>();
			recordsR = new Vector<Property[]>();
			if (dataTypes[j]==Double.class) {
				value1 = (Double)record1[j].getPropWrap();
				for (int i=0; i<records.size(); i++) {
					record2 = records.get(i);
					value2 = (Double)record2[j].getPropWrap();
					if (value2<=value1) {
						recordsL.add(record2.clone());
					} else {
						recordsR.add(record2.clone());
					}
				}
			}
			child1 = new Node(this, parentNode, recordsL);
			child2 = new Node(this, parentNode, recordsR);
			entropy1 = child1.getEntropy();
			entropy2 = child2.getEntropy();
			avgEntropy = (recordsL.size()*entropy1+recordsR.size()*entropy2)/records.size();
			if (avgEntropy<leastEntropy) {
				leastEntropy = avgEntropy;
				child1Best = child1;
				child2Best = child2;
				bestPropertyIndex = j;
			}

		}
		
		if (leastEntropy>parentEntropy) {
			throw new Exception("Could not decrease Entropy at all!");
		}
		
		parentNode.setChild1(child1Best);
		parentNode.setChild2(child2Best);
		
		return bestPropertyIndex;
		
	}
	
	private void createTreeOfSingleRecordNodes(Vector<Property[]> records) {
		for (int i=0; i<records.size(); i++) {
			this.add(new Node(this, records.get(i)));
		}
	}

	private double[] getStandardDeviations(Vector<Property[]> records) throws Exception {
		
		Property[] record = records.firstElement();
		int length = record.length;
		double value;
		
		double[] avg = new double[length];
		double[] avgsq = new double[length];
		double[] count = new double[length];
		
		for (int j=0; j<records.size(); j++) {
			record = records.get(j);
			for (int i=0; i<avg.length; i++) {
				if (dataTypes[i] == Double.class) {
					value = (Double) record[i].getPropWrap();
					avg[i] += value;
					avgsq[i] += value*value;
					count[i] += 1.0;
				} else if (dataTypes[i] == Integer.class) {
					value = (Integer) record[i].getPropWrap();
					avg[i] += value;
					avgsq[i] += value*value;
					count[i] += 1.0;
				} else if (dataTypes[i] == Category.class) {
					// do nothing
				} else {
					throw new Exception("Property datatype not found");
				}
			}
		}
		
		// calculate standard deviations
		double[] stddev = new double[length];
		for (int i=0; i<stddev.length; i++) {
			stddev[i]=0.0;
			if (dataTypes[i] == Double.class || dataTypes[i] == Integer.class) {
				if (count[i]>1.0) {
					avg[i] = avg[i]/count[i];
					avgsq[i] = avgsq[i]/count[i];
					stddev[i] = Math.sqrt(avgsq[i]-avg[i]*avg[i]);
				}
			} else if (dataTypes[i] == Category.class) {
				// do nothing
			} else {
				throw new Exception("Property datatype not found");
			}
		}
		return stddev;
		
	}
	
	private void normalizeTree(double[] stddev) {
		Node node;
		double value;
		int intval;
		
		if (this.size()<2) return;

		Iterator<Node> iter = this.iterator();
		while (iter.hasNext()) {
			node = iter.next();
			records = node.getRecords();
			
			// there should not be more than 1 record for each node at this point;
			for (int i=0; i<records.size(); i++) {
				record = records.get(i);
				for (int j=2; j<stddev.length; j++) {
					if (stddev[j] != 0.0) {
						if (dataTypes[j]==Double.class) {
							value = (Double) record[j].getPropWrap();
							value = value/stddev[j];
							record[j].setPropWrap(Double.valueOf(value));
						} else if (dataTypes[j]==Integer.class) {
							intval = (int)((Integer)record[j].getPropWrap()/stddev[i]);
							record[j].setPropWrap(Integer.valueOf(intval));
						}
					}
				}
			}
			
		}
		
	}
	
	private void clusterNodes() throws Exception {
		double thisToxicity, prevToxicity;
		double toxDiff;
		Node thisNode, prevNode;

		int startSize = this.size();
		
		double minTox = this.first().getToxicity();
		double maxTox = this.last().getToxicity();
		double toxInc = (maxTox-minTox)/startSize;
		System.out.println(maxTox+", "+minTox+", "+toxInc);
		
		do {
			startSize = this.size();
			if (startSize<2) break;

			Iterator<Node> iter = this.iterator();
			prevNode = iter.next();
			prevToxicity = prevNode.getToxicity();

			while (iter.hasNext()) {

				thisNode = iter.next();
				thisToxicity = thisNode.getToxicity();
				toxDiff = thisToxicity-prevToxicity;
				if (toxDiff < 0.0) toxDiff = -toxDiff;  // take absolute value
				if (toxDiff < toxInc) { // add two Nodes together
					prevNode.addIn(toxInc, thisNode);
					prevToxicity = prevNode.getToxicity();
					iter.remove();  // remove thisNode at this location
				} else {
					if (prevNode.getToxInc() < toxInc) {
						prevNode.addIn(toxInc, null); // update when necessary
					}
					prevNode = thisNode;
					prevToxicity = thisToxicity;
				}
			}
			
			if (prevNode.getToxInc() < toxInc) {
				prevNode.addIn(toxInc, null); // update when necessary
			}
			
		} while (startSize != this.size());
		
		return;
	}
	
	public String[] getDataNames() {
		return dataNames;
	}

	public Object[] getDataTypes() {
		return dataTypes;
	}

	public Vector<Property[]> getRecords() {
		return records;
	}


}
