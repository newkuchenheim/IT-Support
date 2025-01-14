package de.newkuchenheim.ITSupport.bdo;

/**
* @author Sebastian Hansen
* 
* @createOn 16.02.2024
* 
*/
public class CostCentre implements Comparable<CostCentre>{
	private int number;
	private String label;
	private String label1;
	private String location;
	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the label1
	 */
	public String getLabel1() {
		return label1;
	}
	/**
	 * @param label1 the label1 to set
	 */
	public void setLabel1(String label1) {
		this.label1 = label1;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	public CostCentre() {
		number = -1;
		label = "";
		label1 = "";
		location = "";
	}
	@Override
	public int compareTo(CostCentre o) {
		return Integer.compare(this.number, o.getNumber());
	}
	@Override
	public String toString() {
		String str = "";
		if (number > 0) {
			str = number + " - " + label;
		}
		return str;
	}
}
