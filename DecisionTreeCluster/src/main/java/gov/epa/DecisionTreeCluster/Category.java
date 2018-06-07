package gov.epa.DecisionTreeCluster;

import java.util.Vector;

public class Category {
	
	private String catName;
	private Vector<String> catValues = null;
	
	public Category(String catName) {
		super();
		this.catName = catName;
		this.catValues = new Vector<String>();
	}

	public String getCatName() {
		return catName;
	}

	public Vector<String> getCatValues() {
		return catValues;
	}

}
