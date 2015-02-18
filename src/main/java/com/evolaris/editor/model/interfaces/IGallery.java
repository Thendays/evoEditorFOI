package com.evolaris.editor.model.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * If a Class wants to represent a gallery that the editor will use it has to implement this Interface.
 * @author Tadija
 *
 */
public interface IGallery {
	
	public IGallery getInstance(ArrayList<String> galleryAttributeNames,
    		ArrayList<String> pageAttributeList,
    		ArrayList<IPageResource> pageResourceList,
    		IPageResourceRule pageResourceRule,
    		IPage pageType);
	
	/**
	 * Returns true if the Gallery and its Pages have been fully defined.
	 */
	public boolean isGallerySet();
	
	/**
	 * Returns all the children of the given parent. If a gallery ID is received, all pages 
	 * of the first level will be returned.
	 * 
	 * @param parentID
	 * @return
	 */
	public ArrayList<IPage> getChildPageList(UUID parentID);

	/**
	 * Add new blank page as child of parent parentID.
	 * If a gallery ID is received, add new page to first level
	 * @param parentID
	 * @return
	 */
    public UUID addBlankPage(UUID parentID);

	/**
	 * Delete page with id iD.
	 * @param iD
	 */
    public void deletePage(UUID iD);

	/**
	 * Move page up in tree.
	 * @param iD
	 */
    public void increaseOrderNumber(UUID iD);

	/**
	 * Move page down in tree.
	 * @param iD
	 */
    public void decreaseOrderNumber(UUID iD);

	/**
	 * Reset order numbers of children of page with id parentID when deleting pages.
	 * @param parentID
	 */
    public void resetChildPagesOrderNumber(UUID parentID);

	/**
	 * Change value of attribute attributeName to attributeValue of page pageID.
	 * @param pageID
	 * @param attributeName
	 * @param attributeValue
	 */
    public void editPageAttribute(UUID pageID, String attributeName,
    		String attributeValue);

	/**
	 * Change resource resourceName to content on page pageID.
	 * @param pageID
	 * @param resourceName
	 * @param content
	 */
    public void editPageResourceContent(UUID pageID, String resourceName,
    		String content);

	/**
	 * Change value of attribute attributeName to attributeValue of resource resourceName on page pageID.
	 * @param pageID
	 * @param resourceName
	 * @param attributeName
	 * @param attributeValue
	 */
    public void editPageResourceAtribute(UUID pageID, String resourceName,
    		String attributeName, String attributeValue);

	/**
	 * Change resource which is used on page pageID to resourceName.
	 * @param pageID
	 * @param resourceName
	 */
    public void changePageResourceToUsed(UUID pageID, String resourceName);

	/**
	 * Create new child of page pageID.
	 * @param pageID
	 */
    public void makePageSubpage(UUID pageID);

	/**
	 * Return page with id iD.
	 * @param iD
	 * @return
	 */
    public IPage findPageByID(UUID iD);

	/**
	 * Get id of current page.
	 * @return
	 */
    public UUID getID();

	/**
	 * Get all attributes of current gallery.
	 * @return
	 */
	public Set<String> getGalleryAttributeSet();

	/**
	 * Get attribute attributeName of current gallery.
	 * @param attributeName
	 * @return
	 */
	public String getGalleryAttribute(String attributeName);

	/**
	 * Set value of attribute attributeName to attributeValue of current gallery.
	 * @param attributeName
	 * @param attributeValue
	 */
	public void setGalleryAttribute(String attributeName, String attributeValue);

	/**
	 * Set attibute set to current gallery.
	 * @param galleryAttributeMap
	 */
	public void setGalleryAttributeMap(HashMap<String, String> galleryAttributeMap);	
}
