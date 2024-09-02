package de.newkuchenheim.ITSupport.bdo.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class htmlElement for Object element of a html
 * @param typ String Typ of element
 * @param text String Text of element
 * @param value String value of element
 * @param parent String parent of this element
 * @param isParent boolean true if this elem has parent, else false.
 * @param children List<htlmElement> list od chirldren, if exists
 * @param attributes Map<String, String> attributes with value of this element  
 */
public record htmlElement(String page_name, String typ, String text, String value, String parent, boolean isParent, boolean isChildren, List<htmlElement> childList, Map<String, String> attributes) {
	
	public void setChildren(List<htmlElement> list) {
		if(list!= null && !list.isEmpty()) {
			list.forEach(e -> childList.add(e));
		}
	}
	
	public void addAttribut(String attribute_name, String attribute_value) {
		if(attribute_value != null && !attribute_value.isBlank())
			if(attribute_name != null && !attribute_name.isBlank())
				attributes.putIfAbsent(attribute_name, attribute_value);
	}
}
