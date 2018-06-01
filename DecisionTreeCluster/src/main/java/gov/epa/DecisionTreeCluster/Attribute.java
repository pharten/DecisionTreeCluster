package gov.epa.DecisionTreeCluster;

/**
 * 
 */

/**
 * @author PHARTEN
 *
 */
public class Attribute  {

	/**
	 * 
	 */
	private double dValue;
	private int iValue;
	private Category cValue;
	private String cName;
	
	public Attribute(double dValue) {
		this.dValue = dValue;
	}
	
	public Attribute(int iValue) {
		this.iValue = iValue;
	}
	
	public Attribute(String cName, Category cValue) {
		this.cName = cName;
		this.cValue = cValue;
	}

}
