package gov.epa.DecisionTreeCluster;

import java.io.FileReader;
import java.util.TreeSet;

import com.opencsv.CSVReader;

public class Tree extends TreeSet<Node> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] header = null;
//	private TreeSet<Node> treeSet = new TreeSet<Node>();
	
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
		
		String[] header = null;
		
		CSVReader csvReader = new CSVReader(new FileReader(filename));
		
		String[] line = csvReader.readNext();
		
		if (line==null) {
			throw new Exception("First line of "+filename+" is null");
		} else {
			header = new String[line.length-2];
			for (int i=0; i<header.length; i++) header[i] = line[i+2];
		}
		this.setHeader(header);
		
		Node node = null;
		while ((line=csvReader.readNext())!=null) {
			node = new Node(this, line);
			this.add(node);
		}
		
	}

	public String[] getHeader() {
		return header;
	}
	
	public void setHeader(String[] header) {
		this.header = header;
	}

//	public TreeSet<Node> getTreeSet() {
//		return treeSet;
//	}

}
