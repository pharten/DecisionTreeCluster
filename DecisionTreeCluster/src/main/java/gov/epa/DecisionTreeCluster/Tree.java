package gov.epa.DecisionTreeCluster;

import java.io.FileReader;
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
	
	public Tree(String filename) {
		super();

		try {
			
			buildTreeFromSingleRecordNodes(filename);
			
//			normalizeAllProperties();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	private void buildTreeFromSingleRecordNodes(String filename) throws Exception {
		
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
			} else if (i==1) {
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
	
	private void normalizeAllProperties() throws Exception {

		Node node = null;
		while ((node=this.iterator().next())!=null) {
			Property[] singleRecord = node.getRecords().get(0);
			for (int i=0;i<singleRecord.length;i++) {
				
			}
		}
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
