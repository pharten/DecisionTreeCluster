package gov.epa.DecisionTreeCluster;

import java.util.HashSet;

public class Category extends HashSet {
	
	private String categoryName;
	
	public Category(String categoryName) {
		super();
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

}
