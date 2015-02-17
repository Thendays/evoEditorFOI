package com.evolaris.editor.model.interfaces;

import java.util.ArrayList;

public interface IPageResourceRule {
	/**
	 * Set which resources can be added together on page.
	 * @param pageResourceList
	 * @param pageResourceName
	 */
	public void setResourceAsUsable(ArrayList<IPageResource> pageResourceList, String pageResourceName);

}
