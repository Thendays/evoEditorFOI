package com.evolaris.editor.model.interfaces;

import java.util.Set;

public interface IPageResource {
	
	/**
	 * @return Name of the resource element
	 */
	public String getName();
	
	/**
	 * Sets the resource element name to the given attribute
	 */
	public void setName(String name);	
	
	/**
	 * 
	 * @return The list of all defined tags of the resource element
	 */
	public Set<String> getAttributeSet();
	
	/**
	 * Gets the attribute value of the given attribute name
	 * 
	 * @param attributeName name of the attribute of which the value should be returned
	 * @return
	 */
	public String getAttributeValue(String attributeName);
	
	/**
	 * Flag that indicates if the resource should be visible in the final XML
	 * @return
	 */
	public boolean isUsed();
	
	/**
	 * Defines if the resource should be visible in the final XML
	 */
	public void setIsUsed(boolean isUsed);
	
	/**
	 * Checks it the resource element can have content
	 * @return true if the elemet can have content
	 */
	public boolean canHaveContent();
	
	/**
	 * Sets the tag name and value of the resource element
	 */
	public void setAttribute(String attributeName, String attributeValue);	
	
	/**
	 * Defines if the Resource can have content.
	 * 
	 */
	public void setCanHaveContent(boolean canHaveContent);	
	
	/**
	 * Sets the content of the resource element. 
	 * 
	 */
	public void setContent(String content);
	
	/**
	 * 
	 * @return The content of the resource element
	 */
	public String getContent();
	
	/**
	 * Deep copy of a PageResource object. 
	 */
	public IPageResource clone();
}
