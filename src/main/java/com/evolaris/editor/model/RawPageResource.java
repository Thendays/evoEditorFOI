package com.evolaris.editor.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.evolaris.editor.model.interfaces.IPageResource;

public class RawPageResource implements IPageResource {
	
	private static final String DEFAULT_STRING = "";
	private static final boolean DEFAULT_BOOLEAN = false;
	
	private String name;
	private String content;
	private HashMap<String, String> attributeMap;
	private boolean isUsed;
	private boolean canHaveContent;
	
	public RawPageResource() {
		name = DEFAULT_STRING;
		content = DEFAULT_STRING;
		attributeMap = new HashMap<String, String>();
		isUsed = DEFAULT_BOOLEAN;
		canHaveContent = DEFAULT_BOOLEAN;
	}
	
	public HashMap<String, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(HashMap<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void setContent(String content) {
		if(canHaveContent){
			this.content = content;
		}		
	}

	@Override
	public Set<String> getAttributeSet() {
		return attributeMap.keySet();
	}
	
	@Override
	public String getAttributeValue(String attribute){
		return attributeMap.get(attribute);
	}
	
	@Override
	public void setAttribute(String attributeName, String attributeValue){
		attributeMap.put(attributeName, attributeValue);
	}

	/**
	 * Check if that resource can have content.
	 * @return
	 */
	@Override
	public boolean canHaveContent(){
		return this.canHaveContent;
	}

	/**
	 * Set that resource can or can't have content.
	 * @param canHaveContent
	 */
	@Override
	public void setCanHaveContent(boolean canHaveContent){
		this.canHaveContent = canHaveContent;
		if(canHaveContent == false){
			this.content = DEFAULT_STRING;
		}
	}
	
	/***
	 * Deep copy of a RawPageResource object. 
	 */
	@Override
	public RawPageResource clone(){
		RawPageResource pr = new RawPageResource();
		pr.setIsUsed(isUsed);
		pr.setContent(content);
		pr.setName(name);
		pr.setCanHaveContent(canHaveContent);

		//String is immutable?
		for(Map.Entry<String, String> entry : attributeMap.entrySet()){
			pr.setAttribute(new String(entry.getKey()), new String(entry.getValue()));
		}		
		return pr;
	}

	@Override
	public boolean getIsUsed() {
		return isUsed;
	}

	@Override
	public void setIsUsed(boolean isVisible) {
		this.isUsed = isVisible;
	}
}
