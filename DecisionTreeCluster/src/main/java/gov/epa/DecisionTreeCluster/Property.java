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
	private double dValue;
	private int iValue;
	private String cName;
	private String cValue;
	private Object datatype;

	public Property(double dValue) {
		this.dValue = dValue;
		this.datatype =  double.class;
	}
	
	public Property(int iValue) {
		this.iValue = iValue;
		this.datatype =  int.class;
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
		
		this.cName = cName;
		this.cValue = cValue;
		this.datatype =  Category.class;
	}
	
	public double getdValue() {
		return dValue;
	}

	public int getiValue() {
		return iValue;
	}

	public String getcName() {
		return cName;
	}

	public String getcValue() {
		return cValue;
	}

	public Object getDataType() {
		return datatype;
	}

}
