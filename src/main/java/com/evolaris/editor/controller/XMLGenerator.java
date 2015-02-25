package com.evolaris.editor.controller;

import com.evolaris.editor.model.interfaces.IGallery;
import com.evolaris.editor.model.interfaces.IPage;
import com.evolaris.editor.model.interfaces.IPageResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;


public class XMLGenerator {
	
	private static final String GALLERY_TAG_NAME = "gallery";
	private static final String PAGE_TAG_NAME = "page";

    //region PRIVATE FIELDS
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private Element documentationElement;
    private String file;
    private String indent;
    private IGallery gallery;
    //endregion

    //region CONSTRUCTORS
    public XMLGenerator() {
        try {
            // create empty XML object
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();

            // XML tree
            // create root element and insert it in document
            documentationElement = document.createElement("documentation");
            document.appendChild(documentationElement);
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    //region GETTERS & SETTERS
    public void setFile(String file) {
        this.file = file;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }
    
    //endregion

    //region PRIVATE METHODS
    private void appendGallery(IGallery gallery) {    	
    	this.gallery = gallery;
        Element galleryElement = document.createElement(GALLERY_TAG_NAME);
        
        for(String attributeName : gallery.getGalleryAttributeSet()){
        	String galleryAttributeValue = gallery.getGalleryAttribute(attributeName);
        	if(!galleryAttributeValue.isEmpty()){
        		galleryElement.setAttribute(attributeName, galleryAttributeValue);
        	}
        }      
        
        documentationElement.appendChild(galleryElement);

        appendPages(gallery.getChildPageList(gallery.getID()), galleryElement);
    }

    // append every page/subpage node to gallery node
    private void appendPages(ArrayList<IPage> pages, Element parentElement) {
        // iterate over every page in List
        for (IPage page : pages) {
            // create element document for page and set attributes
            Element pageElement = document.createElement(PAGE_TAG_NAME);
            
            for(String attributeName : page.getPageAttributeSet()){
            	String pageAttributeValue = page.getPageAttribute(attributeName);
            	if(!pageAttributeValue.isEmpty()){
            		pageElement.setAttribute(attributeName, pageAttributeValue);
            	}
            }
            
            for(IPageResource pageResource : page.getPageResources()){
            	if(pageResource.isUsed()){
            		//If path is NOT an empty string
            		Element element = document.createElement(pageResource.getName());
            		
            		Set<String> attributeSet = pageResource.getAttributeSet();
            		for(String attributeName : attributeSet){
            			String attributeValue = pageResource.getAttributeValue(attributeName);
            			if(!attributeValue.isEmpty()){
            				element.setAttribute(attributeName, attributeValue);
            			}
            		}
            		if(pageResource.canHaveContent()){
            			element.appendChild(document.createTextNode(pageResource.getContent()));
            		}
            		pageElement.appendChild(element);
            	}
            }

            // if page has subpages iterate through every one of them
            ArrayList<IPage> childPages = gallery.getChildPageList(page.getId());
            if (childPages.size() != 0) {
                appendPages(childPages, pageElement);
            }
            // if page don't have subpages append page node to parent node
            parentElement.appendChild(pageElement);
        }
    }
    //endregion

    //region PUBLIC METHODS
    public void generateXmlFile(IGallery gallery) {
        // create gallery node and page nodes for every page, subpage
        // and append that nodes to document object
        appendGallery(gallery);

        /*
        *****
        choose directory and file name
        *****
         */
        try {
            document.setXmlStandalone(true);
            File xmlFile = new File(file);
            xmlFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(xmlFile);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource dSource = new DOMSource(document);
            StreamResult sr = new StreamResult(outputStream);

            // set node indentation in xml
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indent);

            // add doctype to xml document
            DOMImplementation domImpl = document.getImplementation();
            DocumentType docType = domImpl.createDocumentType("doctype", "documentation", "documentation.dtd");
            t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());
            t.transform(dSource, sr);

            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    //endregion
}

