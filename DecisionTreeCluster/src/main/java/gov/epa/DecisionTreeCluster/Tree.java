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
	private Object[] record = null;
	private Vector<Object[]> records = null;
	
	public Tree(String filename) {
		super();

		try {
			
			buildTreeFromSingleElementNodes(filename);
			
//			predictionDataStrings = readCsvFile("LC50_prediction_set-2D.csv");
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
	
	private void buildTreeFromSingleElementNodes(String filename) throws Exception {
		
		CSVReader csvReader = new CSVReader(new FileReader(filename));
		
		String[] line = csvReader.readNext();
		
		if (line==null) throw new Exception("First line of "+filename+" is null");
		
		dataNames = new String[line.length];
		dataTypes = new Object[line.length];
		
		for (int i=0; i<dataNames.length; i++) {
			dataNames[i] = line[i];
			if (i==0) {				
				dataTypes[i] = String.class;
			} else if (i==1) {
				dataTypes[i] = Double.class;
			} else {
				dataTypes[i] = Double.class;
			}
		}
		
		while ((line=csvReader.readNext())!=null) {
			record = new Object[dataNames.length];
			for (int i=0; i<record.length; i++) {
				if (dataTypes[i] == Double.class) {				
					record[i] = Double.valueOf(line[i]);
				} else if (dataTypes[i] == Integer.class) {
					record[i] = Integer.valueOf(line[i]);
				} else {
					record[i] = line[i].toString();
				}
			}
			Node node = new Node(this, record);
			this.add(node);
		}
		
	}

	public String[] getDataNames() {
		return dataNames;
	}

	public Object[] getDataTypes() {
		return dataTypes;
	}

	public Vector<Object[]> getRecords() {
		return records;
	}


}
