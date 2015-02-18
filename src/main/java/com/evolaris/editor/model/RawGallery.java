package com.evolaris.editor.model;

import com.evolaris.editor.model.interfaces.IGallery;
import com.evolaris.editor.model.interfaces.IPage;
import com.evolaris.editor.model.interfaces.IPageResource;
import com.evolaris.editor.model.interfaces.IPageResourceRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * The class represents an undefined Gallery that will be used in the editor. It expects the
 * get the Attribute list and the detail of the pages it contains in its creation.
 * 
 * @author Tadija
 *
 */
public class RawGallery implements IGallery{

    private static final String DEFAULT_STRING = "";

    private UUID iD;    
    
    private HashMap<String, String> galleryAttributeMap;
    
    private ArrayList<IPage> pageList;
    private ArrayList<IPageResource> pageResourceList;
    private ArrayList<String> pageAttributeList;
    private IPageResourceRule pageResourceRule;
    
    private boolean galleryDefined;
    private IPage pageType;
    
    private RawGallery(ArrayList<String> galleryAttributeNames,
    		ArrayList<String> pageAttributeList,
    		ArrayList<IPageResource> pageResourceList,
    		IPageResourceRule pageResourceRule,
    		IPage pageType){
    	
    	this.iD = UUID.randomUUID();
    	
    	this.pageType = pageType;
    	this.pageList = new ArrayList<IPage>();
    	this.pageResourceRule = pageResourceRule;
    	
        this.pageResourceList = new ArrayList<IPageResource>();
        for(IPageResource pageResource : pageResourceList){
    		this.pageResourceList.add(pageResource.clone());
    	}
        
        this.pageAttributeList = new ArrayList<String>();
        for(String pageAttribute : pageAttributeList){
    		this.pageAttributeList.add(pageAttribute);
    	}
        
    	this.galleryAttributeMap = new HashMap<String, String>();
    	for(String attributeName : galleryAttributeNames){
        	this.galleryAttributeMap.put(attributeName, DEFAULT_STRING);
        }        
    }
    
    public RawGallery() {
    	galleryDefined = false;
	}
    
    @Override
    public RawGallery getInstance(ArrayList<String> galleryAttributeNames,
    		ArrayList<String> pageAttributeList,
    		ArrayList<IPageResource> pageResourceList,
    		IPageResourceRule pageResourceRule,
    		IPage pageType){
    	
    	galleryDefined = true;
    	
    	return new RawGallery(galleryAttributeNames, pageAttributeList,
    			pageResourceList, pageResourceRule, pageType);
    }

	/**
	 * Get current gallery id.
	 * @return
	 */
	public UUID getID(){
		return this.iD;
	}

	/**
	 * Get Children of parent with id parentID.
	 * @param parentID
	 * @return
	 */
	public ArrayList<IPage> getChildPageList(UUID parentID){
    	ArrayList<IPage> childList = new ArrayList<IPage>();
    	for(IPage page : pageList){
    		if(page.getParentID().equals(parentID)){
    			childList.add(page);
    		}
    	}
    	return childList;
    }

	@Override
	public boolean hasChildPages(UUID parentID) {
		boolean result = false;
		for(IPage page : pageList){
    		if(page.getParentID().equals(parentID)){
    			result = true;
    			break;
    		}
    	}
		return result;
	}

	/**
	 * Add blank page as child of page with id parentID.
	 * @param parentID
	 * @return
	 */
    public UUID addBlankPage(UUID parentID){    	
    	int orderNumber = getChildPageList(parentID).size();
    	IPage newPage = pageType.getInstance(parentID, orderNumber, 
    			getPossiblePageAttributeList(),
    			getPossiblePageResources(),
    			getPageResourceRule());
    	
    	pageList.add(newPage);
    	return newPage.getId();
    }

	/**
	 * Delete page with id iD.
	 * @param iD
	 */
    public void deletePage(UUID iD){
    	IPage page = findPageByID(iD);
    	UUID parentID = null;
    	
    	if(page != null){
    		parentID = page.getParentID();
    		removePageAndSubpages(page);
    		resetChildPagesOrderNumber(parentID);
    	}
    }

	/**
	 * Move page with id iD up in tree.
	 * @param iD
	 */
    public void increaseOrderNumber(UUID iD){
    	IPage pageToIncrement = findPageByID(iD);
    	if(pageToIncrement != null && pageToIncrement.getOrderNumber() != 0){
    		for(IPage page : pageList){
    			if(page.getParentID().equals(pageToIncrement.getParentID())){
    				if(page.getOrderNumber() == pageToIncrement.getOrderNumber() - 1){
    					page.decreaseOrderNumber();
    					pageToIncrement.increaseOrderNumber();
    					Collections.sort(pageList);
    					break;
    				}
    			}
    		}
    	}	
    }

	/**
	 * Move page with id iD down in tree.
	 * @param iD
	 */
    public void decreaseOrderNumber(UUID iD){
    	IPage pageToDecrement = findPageByID(iD);
    	ArrayList<IPage> childPages = getChildPageList(pageToDecrement.getParentID());
    	if(pageToDecrement != null && pageToDecrement.getOrderNumber() != childPages.size() - 1){
    		for(IPage page : childPages){
				if(page.getOrderNumber() == pageToDecrement.getOrderNumber() + 1){
					page.increaseOrderNumber();
					pageToDecrement.decreaseOrderNumber();
					Collections.sort(pageList);
					break;
				}
    		}
    	}	
    }

	/**
	 * Reset child pages ordering when deleting page.
	 * @param parentID
	 */
    public void resetChildPagesOrderNumber(UUID parentID){
    	ArrayList<IPage> childPages = getChildPageList(parentID);
    	int i = 0;
    	for(IPage page : childPages){
    		page.setOrderNumber(i);
    		i++;
    	}
    }

	/**
	 * Change value of attribute attributeName to attributeValue of page pageID.
	 * @param pageID
	 * @param attributeName
	 * @param attributeValue
	 */
    public void editPageAttribute(UUID pageID, String attributeName, String attributeValue){
    	IPage page = findPageByID(pageID);
    	if(page != null){
    		page.setPageAttribute(attributeName, attributeValue);
    	}
    }

	/**
	 * Change resource resourceName to content on page pageID.
	 * @param pageID
	 * @param resourceName
	 * @param content
	 */
    public void editPageResourceContent(UUID pageID, String resourceName, String content){
    	IPage page = findPageByID(pageID);
    	if(page != null){
    		page.editResourceContent(resourceName, content);
    	}    	
    }

	/**
	 * Change value of attribute attributeName to attributeValue of resource resourceName on page pageID.
	 * @param pageID
	 * @param resourceName
	 * @param attributeName
	 * @param attributeValue
	 */
    public void editPageResourceAtribute(UUID pageID, String resourceName, String attributeName, String attributeValue){
    	IPage page = findPageByID(pageID);
    	if(page != null){
    		page.editResourceAttribute(resourceName, attributeName, attributeValue);
    	}    	
    }

	/**
	 * Change resource which is used on page pageID to resourceName.
	 * @param pageID
	 * @param resourceName
	 */
    public void changePageResourceToUsed(UUID pageID, String resourceName){
    	IPage page = findPageByID(pageID);
    	if(page != null){
    		page.usePageResource(resourceName);
    	}    	
    }

	/**
	 * Create new child of page pageID.
	 * @param pageID
	 */
    public void makePageSubpage(UUID pageID){    	
    	IPage page = findPageByID(pageID);
    	if(page != null){
    		ArrayList<IPage> neighborPages = getChildPageList(page.getParentID());
        	if(neighborPages.size() != 1 && page.getOrderNumber() != 0){
        		// if the page is not the first or the only page on a level
        		for(IPage neighborPage : neighborPages){
        			if(neighborPage.getOrderNumber() == page.getOrderNumber() - 1){
        				//if the neighbor page is above the edited page make it the parent
        				page.setParentID(neighborPage.getId());
        				break;
        			}
        		}
        	}
    	}// else page is not found    	
    }

	/**
	 * Return page with id iD.
	 * @param iD
	 * @return
	 */
    public IPage findPageByID(UUID iD){
    	IPage retPage = null;
    	for(IPage page : pageList){
    		if(page.getId().compareTo(iD) == 0){
    			retPage = page;
    			break;
    		}
    	}
    	return retPage;
    }

	/**
	 * Remove page and its children.
	 * @param page
	 */
	private void removePageAndSubpages(IPage page){    	
		ArrayList<IPage> childPages = getChildPageList(page.getId());
		if(childPages.size() != 0){
			for(IPage childPage : childPages){
				removePageAndSubpages(childPage);
			}
		}
		for (int i = 0; i<pageList.size(); i++) {
			if (pageList.get(i).getId().equals(page.getId()))
				pageList.remove(i);
		}
	}

	private ArrayList<IPageResource> getPossiblePageResources() {
		return this.pageResourceList;
	}
    
	private ArrayList<String> getPossiblePageAttributeList() {
		return this.pageAttributeList;
	}

	private IPageResourceRule getPageResourceRule() {
		return this.pageResourceRule;
	}

	public Set<String> getGalleryAttributeSet() {
		return this.galleryAttributeMap.keySet();
	}
	
	public String getGalleryAttribute(String attributeName) {
		return this.galleryAttributeMap.get(attributeName);
	}
	
	public void setGalleryAttribute(String attributeName, String attributeValue) {
		this.galleryAttributeMap.put(attributeName, attributeValue);
	}

	public void setGalleryAttributeMap(HashMap<String, String> galleryAttributeMap) {
		this.galleryAttributeMap = galleryAttributeMap;
	}
	
	@Override
	public boolean isGallerySet() {
		return this.galleryDefined;
	}

	public void setGalleryDefined(boolean galleryDefined) {
		this.galleryDefined = galleryDefined;
	}
}

