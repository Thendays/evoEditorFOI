package com.evolaris.editor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.evolaris.editor.controller.XMLGenerator;
import com.evolaris.editor.model.RawGallery;
import com.evolaris.editor.model.RawPage;
import com.evolaris.editor.model.RawPageResource;
import com.evolaris.editor.model.VideoPageResource;
import com.evolaris.editor.model.interfaces.IGallery;
import com.evolaris.editor.model.interfaces.IPage;
import com.evolaris.editor.model.interfaces.IPageResource;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private IGallery gallery;
	
	private ApplicationContext context;
	private static final String DOCUMENT_TYPE_DEFINITION = "documentation.dtd";
	private static final int BUFFER_SIZE = 4096;
	
	private UUID selectedItemUUID;
	
	static Log log = LogFactory.getLog(HomeController.class.getName());
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		log.info("PRVO UÈITAVANJE");
		context = new ClassPathXmlApplicationContext("gallery.xml");
		gallery = context.getBean("gallery", RawGallery.class);
		selectedItemUUID = gallery.getID();
		model.addAttribute("gallery", gallery);
		model.addAttribute("galleryID", gallery.getID());
		model.addAttribute("selectedItemUUID", selectedItemUUID);
		return "index";
	}
	
	@RequestMapping(value = "/refr", method = RequestMethod.GET, headers="Accept=*/*")
	@ResponseStatus(value = HttpStatus.OK)
	public String refresh(Model model) {
		model.addAttribute("gallery", gallery);
		model.addAttribute("galleryID", gallery.getID());
		model.addAttribute("selectedItemUUID", this.selectedItemUUID);
		return "index";
	}
	
	@RequestMapping(value = "/pageselected", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void selectPage(@RequestParam("pageid") UUID pageId, Model model) {
		log.info("page ID = " + pageId);
		if(pageId != null){
			this.selectedItemUUID = pageId;
		}		
	}
	
	@RequestMapping(value = "/addpage", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void addPage(@RequestParam("parentid") UUID parentId) {		
		selectedItemUUID = gallery.addBlankPage(parentId);		
	}
	
	@RequestMapping(value = "/deletepage", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void deletePage(@RequestParam("pageid") UUID pageId) {
		gallery.deletePage(pageId);
	}
	
	@RequestMapping(value = "/pageup", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void pageUp(@RequestParam("pageid") UUID pageId) {
		gallery.increaseOrderNumber(pageId);
	}
	
	@RequestMapping(value = "/pagedown", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void pageDownl(@RequestParam("pageid") UUID pageId) {
		gallery.decreaseOrderNumber(pageId);
	}
	
	@RequestMapping(value = "/changeresource", method = RequestMethod.GET)
	@ResponseBody
	public void changeResource(@RequestParam("pageid") UUID pageId,
							   @RequestParam("resource") String usedResource) {
		if (usedResource != "") {
			gallery.changePageResourceToUsed(pageId, usedResource.toLowerCase());
		} 
	}
	
	@RequestMapping(value = "/galleryattr", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void changeGalleryAttributes(@RequestParam("name") String galleryName,
										@RequestParam("qrcode") String qrCode,
										@RequestParam("repeat") String repeat,
										@RequestParam("showindicator") String showIndicator,
										@RequestParam("transparency") String transparency) {
		
		gallery.setGalleryAttribute("name", galleryName);
		gallery.setGalleryAttribute("qrcode", qrCode);
		gallery.setGalleryAttribute("repeat", repeat);
		gallery.setGalleryAttribute("showIndicator", showIndicator);
		gallery.setGalleryAttribute("transparency", transparency);
	}
	
	@RequestMapping(value = "/checkpageattributes", method = RequestMethod.GET)
	@ResponseBody
	public void checkResourceUsed(@RequestParam("pageid") UUID pageId,
			Model model) {
		//model.addAttribute("usedResources", attributeValue)
	}
	
	@RequestMapping(value = "/savepageattributes", method = RequestMethod.GET)
	public @ResponseBody void savePageAttributes(@RequestParam Map<String, String> params) {
		
		HashMap<String, String> pageAttributes = ((RawPage)gallery.findPageByID(UUID.fromString(params.get("pageid")))).getPageAttributeMap();
		
		for (Map.Entry<String, String> attribute : pageAttributes.entrySet()) {
			for (Map.Entry<String, String> parameter : params.entrySet()) {
				if (parameter.getKey().equalsIgnoreCase(attribute.getKey())) {
					attribute.setValue(parameter.getValue());
				}
			}
			
			for (IPageResource res : gallery.findPageByID(UUID.fromString(params.get("pageid"))).getPageResources()) {
				if (res.getIsUsed()) {					
					HashMap<String, String> attributeMap = ((RawPageResource)res).getAttributeMap();
					
					for (Map.Entry<String, String> parameter : params.entrySet()) {
						if (parameter.getKey().equalsIgnoreCase("content")) 
							res.setContent(parameter.getValue());
							
						for (Map.Entry<String, String> resAttribute : attributeMap.entrySet()) {
							if (resAttribute.getKey().equalsIgnoreCase(parameter.getKey()))
								res.setAttribute(resAttribute.getKey(), parameter.getValue());
							}
						}
					}
				}
			}
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String uploadFileHandler(Model model, 
									@RequestParam("file") MultipartFile file,
									@RequestParam("page") UUID pageId) {
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				
				String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "resources");
                if (!dir.exists())
                    dir.mkdirs();
                String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'), file.getOriginalFilename().length());
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator 
                        + String.format("%s", RandomStringUtils.randomAlphanumeric(8) 
                        		+ ext));
                
                String fileName = serverFile.getAbsolutePath();
                fileName = (fileName.substring(fileName.indexOf("resources"), fileName.length())).replace("\\", "\\\\");
                
                for (IPageResource res : gallery.findPageByID(pageId).getPageResources()) {
                	if (res.getIsUsed()) {
                		res.setAttribute("Path", fileName);
                	}
                }
                
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
			} catch (Exception e) {
                System.out.println("You failed to upload " + file.getName() + " => " + e.getMessage());
            }
		} else {
            System.out.println("You failed to upload " + file.getName()
                    + " because the file was empty.");
        }
		model.addAttribute("gallery", gallery);
		model.addAttribute("galleryID", gallery.getID());
		return "index";
	}
	
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public @ResponseBody 
	void exportGallery(HttpServletResponse response) throws IOException {
		XMLGenerator generator = context.getBean("generator", XMLGenerator.class);
		generator.setFile(System.getProperty("catalina.home") + File.separator + gallery.getGalleryAttribute("name") + ".xml");
		generator.generateXmlFile(gallery);
		
		byte[] buffer = new byte[1024];
		
		try {
			FileOutputStream fos = new FileOutputStream(System.getProperty("catalina.home") + File.separator + gallery.getGalleryAttribute("name") + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			ZipEntry ze = new ZipEntry(gallery.getGalleryAttribute("name") + ".xml");
			zos.putNextEntry(ze);
			
			FileInputStream in = new FileInputStream(System.getProperty("catalina.home") + File.separator + gallery.getGalleryAttribute("name") + ".xml");
			
			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			
			in.close();
			zos.closeEntry();
			
			ze = new ZipEntry(DOCUMENT_TYPE_DEFINITION);
			zos.putNextEntry(ze);
			
			in = new FileInputStream(System.getProperty("catalina.home") 
					+ File.separator + "resources" 
					+ File.separator + DOCUMENT_TYPE_DEFINITION);
			
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			
			in.close();
			zos.closeEntry();
			
			for (IPage page : gallery.getChildPageList(gallery.getID())) {
				for (IPageResource res : page.getPageResources()) {
					if (res.getIsUsed()) {
						HashMap<String, String> attributeMap = ((RawPageResource)res).getAttributeMap();
						
						for (Map.Entry<String, String> attribute : attributeMap.entrySet()) {
							if (attribute.getKey().equalsIgnoreCase("path")) {
								ze = new ZipEntry(attribute.getValue().replace("\\\\", "\\"));
								zos.putNextEntry(ze);
								
								in = new FileInputStream(System.getProperty("catalina.home") + File.separator + attribute.getValue());
								
								while ((len = in.read(buffer)) > 0) {
									zos.write(buffer, 0, len);
								}
								
								in.close();
								zos.closeEntry();
							}
						}
					}
				}
			}
			
			zos.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		File file = new File(System.getProperty("catalina.home") + File.separator + gallery.getGalleryAttribute("name") + ".zip");
			
		FileInputStream in = new FileInputStream(file);
		try {
			response.setContentType("application/zip");
			response.setContentLength((int) file.length());
			response.setHeader("Content-Disposition", "attachment;filename=\"" + file.getAbsolutePath() + "\"");
			OutputStream out = response.getOutputStream();
			byte[] b = new byte[BUFFER_SIZE]; 
			int bytesRead = -1;
			
			while ((bytesRead = in.read(b)) != -1) {
				out.write(b, 0, bytesRead);
			}
			
			in.close();
			out.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
}
