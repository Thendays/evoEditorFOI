package com.evolaris.editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import com.evolaris.editor.model.interfaces.IPage;
import com.evolaris.editor.model.interfaces.IPageResource;
import com.evolaris.editor.model.interfaces.IPageResourceRule;

public class RawPage implements IPage{

    public static final String DEFAULT_STRING = "";   
    
    private UUID iD;
    private UUID parentID;
    private int orderNumber;
    private boolean pageDefined;
    
    private HashMap<String, String> pageAttributeMap;
    private ArrayList<IPageResource> pageResourceList;
    private IPageResourceRule pageResourceRule;
    
    private RawPage(UUID parentiD, int orderNumber,
    		ArrayList<String> pageAttributeNames,
    		ArrayList<IPageResource> pageResourceList,
    		IPageResourceRule pageResourceRule){
    	
        this.iD = UUID.randomUUID();
        this.parentID = parentiD;
        this.orderNumber = orderNumber;
        this.pageResourceRule = pageResourceRule;        
        
        this.pageResourceList = new ArrayList<IPageResource>();       
        for(IPageResource pageResource : pageResourceList){
    		this.pageResourceList.add(pageResource.clone());
    	}
        
        this.pageAttributeMap = new HashMap<String, String>();
        for(String attributeName : pageAttributeNames){
        	this.pageAttributeMap.put(attributeName, DEFAULT_STRING);
        }
    }
    
    public RawPage() {
    	pageDefined = false;
	}
    
    public void setPageResourceList(ArrayList<IPageResource> pageResourceList) {
		this.pageResourceList = pageResourceList;
	}

	@Override
	public IPage getInstance(UUID parentiD, int orderNumber,
			ArrayList<String> pageAttributeNames,
			ArrayList<IPageResource> pageResourceList,
			IPageResourceRule pageResourceRule) {
    	
    	this.pageDefined = true;
		
		return new RawPage(parentiD, orderNumber,
				pageAttributeNames, pageResourceList,
				pageResourceRule);
	}
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IPage other = (IPage) obj;
        if (!this.iD.equals(other.getId())){
        	return false;
        }            
        return true;
    }
    
    @Override
    public int compareTo(IPage page) {
        return this.orderNumber - page.getOrderNumber();
    }
    
    public int getOrderNumber() {
		return this.orderNumber;
	}
    
    public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
    
    public void increaseOrderNumber(){
    	this.orderNumber--;
    }
    
    public void decreaseOrderNumber(){
    	this.orderNumber++;
    }
    
    public UUID getId() {
        return this.iD;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
    }
    
    public UUID getParentID() {
        return parentID;
    }

    public String getPageAttribute(String attributeName) {
        return this.pageAttributeMap.get(attributeName);
    }

    public void setPageAttribute(String attributeName, String attributeValue) {
        this.pageAttributeMap.put(attributeName, attributeValue);
    }  
    
    public Set<String> getPageAttributeSet(){
    	return pageAttributeMap.keySet();
    }
    
    public HashMap<String, String> getPageAttributeMap() {
		return pageAttributeMap;
	}

	public void setPageAttributeMap(HashMap<String, String> pageAttributeMap) {
		this.pageAttributeMap = pageAttributeMap;
	}

	public ArrayList<IPageResource> getPageResources(){
    	return this.pageResourceList;    	
    }    
    
    public void setPossibleResources(ArrayList<IPageResource> possiblePageResources){
    	this.pageResourceList = new ArrayList<IPageResource>();
    	for(IPageResource pageResource : possiblePageResources){
    		this.pageResourceList.add(pageResource.clone());
    	}
    }
    
    public IPageResourceRule getPageResourceRule() {
		return pageResourceRule;
	}

	public void setPageResourceRule(IPageResourceRule pageResourceRule) {
		this.pageResourceRule = pageResourceRule;
	}
    
    public void usePageResource(String pageResourceName){
    	this.pageResourceRule.setResourceAsUsable(pageResourceList, pageResourceName);
    }

    /**
     * Change value of resource attribute attributeName on current page to attributeValue.
     * @param resourceName
     * @param attributeName
     * @param attributeValue
     */
    public void editResourceAttribute(String resourceName, String attributeName, String attributeValue){
    	if(pageResourceList != null){
    		for(IPageResource pageResource : pageResourceList){
        		if(pageResource.getName().equalsIgnoreCase(resourceName)){
        			pageResource.setAttribute(attributeName, attributeValue);
        		}
        	}
    	}    	 	
    }

    /**
     * Change content of resource resourceName on current page to content.
     * @param resourceName
     * @param content
     */
    public void editResourceContent(String resourceName, String content){
    	if(pageResourceList != null){
    		for(IPageResource pageResource : pageResourceList){
        		if(pageResource.getName().equalsIgnoreCase(resourceName)){
        			pageResource.setContent(content);   			
        		}
        	}
    	}    	
    }

	@Override
	public boolean isPageDefined() {
		return this.pageDefined;
	}
	
	public void removeUsedResources() {
		for (IPageResource resource : this.getPageResources()) {
			resource.setIsUsed(false);
		}
	}

	@Override
	public String getUsedResourceName() {
		String resourceName = "";
		for (IPageResource resource : this.getPageResources()) {
			if(resource.getIsUsed()){
				resourceName = resource.getName();
				break;
			}
		}
		return resourceName;
	}
}

