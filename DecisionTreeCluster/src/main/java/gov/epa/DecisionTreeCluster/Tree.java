package gov.epa.DecisionTreeCluster;

import java.io.FileReader;
import java.util.Comparator;
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
	private Property[] record = null;
	private Vector<Property[]> records = null;
	private Vector<Category> categories = new Vector<Category>();
	
	public Tree(ToxComp toxComp, String filename) {
		
		super(toxComp);  // sort nodes on tree by mean toxicity

		try {
			
			// sorting Nodes by Toxicity is done while creating this Tree
			createTreeOfSingleRecordNodes(filename);
			
			// cluster nodes in a loop
			System.out.println(this.size());
			
			clusterNodes(this.size()/2);
			System.out.println(this.size());
			
			clusterNodes(this.size()/2);
			System.out.println(this.size());
			
			clusterNodes(this.size()/2);
			System.out.println(this.size());
			
			clusterNodes(this.size()/2);
			System.out.println(this.size());
			
			clusterNodes(this.size()/2);
			System.out.println(this.size());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
		
	}

//	private String[] generateHeader(Vector<String[]> trainingData) {
//		
//		String[] firstRecord = trainingData.firstElement();
//		String[] header = new String[firstRecord.length-2];
//		for (int i=0; i<header.length; i++) header[i] = firstRecord[i+2];
//		
//		return header;
//		
//	}
	
//	private Vector<String[]> readCsvFile(String filename) throws Exception {
//
//		CSVReader csvReader = null;
//		Vector<String[]> records = new Vector<String[]>();
//		
//		try	{
//			
//			/* create a new CSVReader for the fileName */
//			csvReader = new CSVReader(new FileReader(filename));
//			
//			String[] line = null;
//			/* Loop over lines in the csv file */
//			while ((line = csvReader.readNext())!=null) {
//				
//				records.add(line);
//				
//			}
//			
//			/* Close the writer. */
//			csvReader.close();
//			
//		} catch(Exception ex)	{
//				
//			throw ex;
//			
//		}
//		
//		return records;
//
//	}
	
	private void createTreeOfSingleRecordNodes(String filename) throws Exception {
		
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
		
		while ((line=csvReader.readNext())!=null) {
			record = new Property[dataNames.length];
			for (int i=0; i<record.length; i++) {
				if (line[i].equalsIgnoreCase("Null")) {
					record[i] = new Property();
				} else if (dataTypes[i] == Double.class) {				
					record[i] = new Property(Double.parseDouble(line[i]));
				} else if (dataTypes[i] == Integer.class) {
					line[i] = line[i].replace('.', '0');
					record[i] = new Property(Integer.parseInt(line[i]));
				} else if (dataTypes[i] == Category.class) {
					record[i] = new Property(categories, dataNames[i], line[i]);
				} else {
					csvReader.close();
					throw new Exception("Property datatype not found");
				}
			}
			Node node = new Node(this, record);
			this.add(node);
		}
		
		csvReader.close();
		
	}
	
	private void clusterNodes(int ntimes) throws Exception {
		double thisToxicity, prevToxicity;
		double toxDiff, minToxDiff;
		Node thisNode, prevNode, node1, node2;

		for (int i=0; i<ntimes; i++) {
			
			if (this.size()<2) break;

			Iterator<Node> iter = this.iterator();
			node1 = iter.next();
			prevToxicity = node1.getMean();
			node2 = iter.next();
			thisToxicity = node2.getMean();
			minToxDiff = node2.getMean() - node1.getMean();

			prevNode = node2;
			prevToxicity = thisToxicity;

			while (iter.hasNext()) {
				thisNode = iter.next();
				thisToxicity = thisNode.getMean();
				toxDiff = thisToxicity - prevToxicity;
				if (toxDiff < minToxDiff) {
					minToxDiff = toxDiff;
					node1 = prevNode;
					node2 = thisNode;
				}
				prevNode = thisNode;
				prevToxicity = thisToxicity;
			}

			Node parentNode = new Node(this, node1, node2);
			this.remove(node1);
			this.remove(node2);
			this.add(parentNode);

		}
	
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
