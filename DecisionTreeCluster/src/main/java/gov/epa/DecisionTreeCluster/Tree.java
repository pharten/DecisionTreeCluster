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

	private String[] header = null;
	Vector<String[]> trainingDataStrings = null;
	Vector<String[]> predictionDataStrings = null;
	
//	DoubleMatrix xPrediction = new DoubleMatrix(predictionDataStrings.size(), descriptorHeader.length);
//	DoubleMatrix yPrediction = new DoubleMatrix(predictionDataStrings.size(), 1);
//	buildMatrices(predictionDataStrings, xPrediction, yPrediction);
	
	public Tree() {
		super();

		try {
			trainingDataStrings = readCsvFile("LC50_training_set-2D.csv");
			predictionDataStrings = readCsvFile("LC50_prediction_set-2D.csv");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		header = trainingDataStrings.firstElement();
		String[] descriptorHeader = new String[header.length-2];
		for (int i=0; i<descriptorHeader.length; i++) descriptorHeader[i] = header[i+2];
		
//		DoubleMatrix xTraining = new DoubleMatrix(trainingDataStrings.size(), descriptorHeader.length);
//		DoubleMatrix yTraining = new DoubleMatrix(trainingDataStrings.size(), 1);
		buildSingleElementLeaves(descriptorHeader, trainingDataStrings);
//		buildMatrices(trainingDataStrings, xTraining, yTraining);
		
	}
	
	public Vector<String[]> readCsvFile(String filename) throws Exception {

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
	
	private void buildSingleElementLeaves(String[] dataStrings, Vector<String[]> trainingData) {
		
		String elem = null;
		String[] row = null;
		Node node = null;
		
		for (int j=1; j<trainingData.size(); j++) {
			node = new Node();
			row = trainingData.get(j);
			elem = row[1];
//			y.put(j-1, 0, Double.valueOf(elem).doubleValue());
			for (int i=2; i<row.length; i++) {
				elem = row[i];
//				x.put(j-1, i-2, Double.valueOf(elem).doubleValue());
			}
		}
		
	}

}
