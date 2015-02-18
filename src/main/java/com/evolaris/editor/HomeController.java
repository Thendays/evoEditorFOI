package com.evolaris.editor;

import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.evolaris.editor.model.RawGallery;
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
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		context = new ClassPathXmlApplicationContext("gallery.xml");
		gallery = context.getBean("gallery", RawGallery.class);
		model.addAttribute("gallery", gallery);
		model.addAttribute("galleryID", gallery.getID());
		return "index";
	}
	
	@RequestMapping(value = "/refresh", method = RequestMethod.GET, headers="Accept=*/*")
	//public @ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String refreshPages(Model model) {
		model.addAttribute("gallery", gallery);
		model.addAttribute("parentPage", gallery.getID());
		return "pageMenu";
	}
	
	@RequestMapping(value = "/addpage", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void addPage(@RequestParam("parentid") UUID parentId) {
		gallery.addBlankPage(parentId);
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
	@ResponseStatus(value = HttpStatus.OK)
	public void changeResource(@RequestParam("pageid") UUID pageId,
							   @RequestParam("resource") String usedResource) {
		gallery.changePageResourceToUsed(pageId, usedResource.toLowerCase());
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
	
	@RequestMapping(value = "/checkresource", method = RequestMethod.GET)
	@ResponseBody
	public String checkResourceUsed(@RequestParam("pageid") UUID pageId) {
		String resource = "";
		
		for (IPageResource res : gallery.findPageByID(pageId).getPageResources()) {
			if (res.getIsUsed()) {
				resource += "{\"resourceUsed\": \"" + res.getName() + 
						"\", \"value\": \"" + res.getAttributeValue("path") + "\"}";
			}
		}
		return resource;
	}
}
