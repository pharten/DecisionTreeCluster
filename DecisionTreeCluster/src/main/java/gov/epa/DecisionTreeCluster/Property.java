package gov.epa.DecisionTreeCluster;

import java.util.Vector;

/**
 * 
 */

/**
 * @author PHARTEN
 *
 */
public class Property  {

	/**
	 * 
	 */
	private Object propWrap = null;
	private String propName = null;
	
	public Property() {
	}
	
	public Property(Object member) {
		this.propWrap = member;
	}
	
	public Property(Vector<Category> categories, String cName, String cValue) throws Exception {

		Boolean categoryNotFound = true;
		for (Category category: categories) {
			if (category.getCatName().equals(cName)) {
				categoryNotFound = false;
				Vector<String> catValues = category.getCatValues();
				if (!catValues.contains(cValue)) catValues.addElement(cValue);
				break;
			}
		}
		if (categoryNotFound) {
			throw new Exception("Error: Category "+cName+" not found.");
		}
		
		this.propName = cName;
		this.propWrap = cValue;
	}
	
	public Object getPropWrap() {
		return propWrap;
	}

	public String getPropName() {
		return propName;
	}

}
