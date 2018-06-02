package gov.epa.DecisionTreeCluster;

import java.io.FileReader;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Vector;

import com.opencsv.CSVReader;

public class Tree extends TreeSet<Node> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] header = null;
	
	public Tree(String fileName) {
		super();

		try {
			
			Vector<String[]> trainingDataStrings = readCsvFile(fileName);
			this.setHeader(generateHeader(trainingDataStrings));
			buildTreeFromSingleElementNodes(trainingDataStrings);
			
//			predictionDataStrings = readCsvFile("LC50_prediction_set-2D.csv");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String[] generateHeader(Vector<String[]> trainingData) {
		
		String[] firstRecord = trainingData.firstElement();
		String[] header = new String[firstRecord.length-2];
		for (int i=0; i<header.length; i++) header[i] = firstRecord[i+2];
		
		return header;
		
	}
	
	private Vector<String[]> readCsvFile(String filename) throws Exception {

		CSVReader csvReader = null;
		Vector<String[]> records = new Vector<String[]>();
		
		try	{
			
			/* create a new CSVReader for the fileName */
			csvReader = new CSVReader(new FileReader(filename));
			
			String[] line = null;
			/* Loop over lines in the csv file */
			while ((line = csvReader.readNext())!=null) {
				
				records.add(line);
				
			}
			
			/* Close the writer. */
			csvReader.close();
			
		} catch(Exception ex)	{
				
			throw ex;
			
		}
		
		return records;

	}
	
	private void buildTreeFromSingleElementNodes(Vector<String[]> trainingData) {
		
		Node node = null;
		Comparator<? super Node> comp = this.comparator();
		
		for (int j=1; j<trainingData.size(); j++) {
			node = new Node(this, trainingData.get(j));
			this.add(node);
		}
		
	}

	public void setHeader(String[] header) {
		this.header = header;
	}
	
	public String[] getHeader() {
		return header;
	}

}
