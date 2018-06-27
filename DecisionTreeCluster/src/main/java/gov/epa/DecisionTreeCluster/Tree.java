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
			
			// cluster nodes in a loop
			System.out.println(this.size());
			
			int i = 0;
			while (this.size()>2 && i++<2) {
				clusterNodes();
				System.out.println(this.size());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
		
	}
	
	private void createTree(String filename) throws Exception {
		
		records = readCvsFile(filename);
		
		createTreeOfSingleRecordNodes(records);
		
		stddev = getStandardDeviations(records);
		
		normalizeTree(stddev);
		
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
	
	private void createTreeOfSingleRecordNodes(Vector<Property[]> records) {
		for (int i=0; i<records.size(); i++) {
			this.add(new Node(this, this.records.get(i)));
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
