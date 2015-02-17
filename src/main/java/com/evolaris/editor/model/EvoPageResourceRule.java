package com.evolaris.editor.model;

import com.evolaris.editor.model.interfaces.IPageResource;
import com.evolaris.editor.model.interfaces.IPageResourceRule;

import java.util.ArrayList;

/**
 * Defines the rules on which page resources can be used by a single page. This <br/>
 * Only one resource can be used at the time.
 * 
 * @author Tadija
 *
 */
public class EvoPageResourceRule implements IPageResourceRule{
	
	/**
	 * Only one resource can be visible at the time.
	 * 
	 * @param pageResources List of all page resources
	 * @param pageResourceName The name of the visible resource
	 */
	@Override
	public void setResourceAsUsable(ArrayList<IPageResource> pageResourceList, String pageResourceName){
		for(IPageResource pageResource : pageResourceList){
			if(pageResource.getName().compareTo(pageResourceName) == 0){
				pageResource.setIsUsed(true);
			}else{
				pageResource.setIsUsed(false);
			}
		}
	}

}
