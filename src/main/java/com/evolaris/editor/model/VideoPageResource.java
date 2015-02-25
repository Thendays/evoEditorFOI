package com.evolaris.editor.model;

import java.util.HashSet;
import java.util.Set;

import com.evolaris.editor.model.interfaces.IPageResource;

public class VideoPageResource implements IPageResource {
	
	private static final String DEFAULT_STRING = "";
	private static final String ATTRIBUTE_VALUE = "path";
	private static final boolean DEFAULT_BOOLEAN = false;
	private static final boolean CAN_HAVE_CONTENT = false;
	private static final String RESOURCE_NAME = "video";
	
	private String content;
	private String attributeName;
	private String attributeValue;
	private boolean isVisible;

	public VideoPageResource() {
		super();
		this.content = DEFAULT_STRING;
		this.attributeName = ATTRIBUTE_VALUE;
		this.attributeValue = DEFAULT_STRING;
		this.isVisible = DEFAULT_BOOLEAN;
	}

	@Override
	public String getName() {
		return RESOURCE_NAME;
	}
	
	@Override
	public void setName(String name) {	}

	@Override
	public String getContent() {
		return this.content;
	}

	@Override
	public Set<String> getAttributeSet() {
		HashSet<String> attributeSet = new HashSet<String>();
		attributeSet.add(ATTRIBUTE_VALUE);
		return attributeSet;
	}

	@Override
	public String getAttributeValue(String attribute) {
		return this.attributeValue;
	}

	@Override
	public boolean isUsed() {		
		return isVisible;
	}

	public boolean canHaveContent() {
		return CAN_HAVE_CONTENT;
	}

	@Override
	public void setAttribute(String attributeName, String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Override
	public void setCanHaveContent(boolean canHaveContent) {	}

	@Override
	public void setIsUsed(boolean isVisible) {
		this.isVisible = isVisible;

	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public IPageResource clone() {
		VideoPageResource vpr = new VideoPageResource();
		vpr.setIsUsed(isVisible);
		vpr.setContent(content);
		vpr.setAttribute(attributeName, attributeValue);
		
		return vpr;
	}
}
