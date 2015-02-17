package com.evolaris.editor;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.evolaris.editor.model.JSONObj;
import com.evolaris.editor.model.RawGallery;
import com.evolaris.editor.model.RawPage;
import com.evolaris.editor.model.interfaces.IGallery;
import com.evolaris.editor.model.interfaces.IPage;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private IGallery gallery;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private ApplicationContext context;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		
		context = new ClassPathXmlApplicationContext("gallery.xml");
		gallery = context.getBean("gallery", RawGallery.class);
		return "index";
	}
	
	@RequestMapping(value = "/refresh", method = RequestMethod.GET, headers="Accept=*/*")
	public @ResponseBody
	String refreshPages() {
		
		JSONObj dynamicContent;
		
		dynamicContent = context.getBean("dynamicContent", JSONObj.class);
		
		ArrayList<IPage> pages = gallery.getChildPageList(gallery.getID());
		
		String gallerySidebar = "<div class=\\\"gallery selected\\\" id=\\\"" + gallery.getID() + "\\\" onclick=\\\"selectPage.call(this, event)\\\">";
		
		for (IPage page : pages) {
			if (page.getParentID().equals(gallery.getID())) {
				gallerySidebar += 
						"<div class=\\\"first\\\" id=\\\"" + String.valueOf(page.getId()) + "\\\" onclick=\\\"selectPage.call(this, event)\\\" data-parentid=\\\"" + page.getParentID() + "\\\">" +
								"<div class=\\\"delete_slide\\\">" +
									"<button class=\\\"delete_1\\\">" +
										"<img src=\\\"resources/images/delete.png\\\" width=\\\"20px\\\"/>" +
									"</button>" + 
								"</div>" +
								"<div class=\\\"move_slide\\\">" +
									"<button class=\\\"up_1\\\"><img src=\\\"resources/images/up.png\\\" width=\\\"10px\\\" height=\\\"10\\\"/></button><br/>" +
									"<button class=\\\"down_1\\\"><img src=\\\"resources/images/down.png\\\" width=\\\"10px\\\" height=\\\"10\\\"/></button>" +
								"</div>" +
								"<div class=\\\"slide_image\\\">" +
									"<img src=\\\"resources/images/Placeholder.png\\\"/>" +
									"<div class=\\\"slide_number\\\">" +
										"<input type=\\\" number\\\" name=\\\"quantity\\\"" +
										"min=\\\"1\\\" max=\\\"1000\\\"" +
										"value=\\\"" + String.valueOf(page.getOrderNumber() + 1) + "\\\">" +
									"</div>" + 
								"</div>" + 
						"</div>";
			}
			
			if (gallery.getChildPageList(page.getId()).size() != 0) {
				for (IPage subpage : gallery.getChildPageList(page.getId())) {
					gallerySidebar = appendSubpages(subpage, gallerySidebar);
				}
			}
		}
		gallerySidebar += "</div>";
		
		String galleryName = "<div class=\\\"right_input_text\\\">Name<br/>"
				+ "<input type=\\\"text\\\" name=\\\"name\\\" placeholder=\\\"Name\\\" "
				+ "value=\\\"" + gallery.getGalleryAttribute("name") + "\\\"></div>";
		
		String qrCode = "<div class=\\\"right_input_text\\\">QR Code<br/>"
				+ "<input type=\\\"text\\\" name=\\\"qr\\\" placeholder=\\\"QR Code\\\" "
				+ "value=\\\"" + gallery.getGalleryAttribute("qrcode") + "\\\"></div>";
		
		String transparency = "<input class=\\\"slider_value\\\" type=\\\"number\\\" "
				+ "id=\\\"opacity\\\" readonly value=\\\"" 
				+ gallery.getGalleryAttribute("transparency") + "\\\">";
		
		String chk = "<input type=\\\"checkbox\\\" name=\\\"repeat\\\" "
				+ "value=\\\"Repeat\\\" " + gallery.getGalleryAttribute("repeat") 
				+ ">Repeat<br><input type=\\\"checkbox\\\" name=\\\"show_indicator\\\" "
				+ "value=\\\"Show_indicator\\\" " + gallery.getGalleryAttribute("showIndicator") 
				+ ">Show indicator";
		
		dynamicContent.put("gallerySidebar", gallerySidebar);
		dynamicContent.put("galleryName", galleryName);
		dynamicContent.put("qrCode", qrCode);
		dynamicContent.put("transparency", transparency);
		dynamicContent.put("chk", chk);
		return dynamicContent.toString();
	}
	
	private String appendSubpages(IPage subpage, String sPages) {
		sPages += 
				"<div class=\\\"second\\\" id=\\\"" + String.valueOf(subpage.getId()) + "\\\" onclick=\\\"selectPage.call(this, event)\\\" data-parentid=\\\"" + subpage.getParentID() + "\\\">" +
						"<div class=\\\"delete_slide\\\">" +
							"<button class=\\\"delete_1\\\">" + 
								"<img src=\\\"resources/images/delete.png\\\" width=\\\"20px\\\"/>" + 
							"</button>" +
						"</div>" +
						"<div class=\\\"move_slide\\\">" +
							"<button class=\\\"up_1\\\"><img src=\\\"resources/images/up.png\\\" width=\\\"10px\\\" height=\\\"10\\\"/></button><br/>" +
							"<button class=\\\"down_1\\\"><img src=\\\"resources/images/down.png\\\" width=\\\"10px\\\" height=\\\"10\\\"/></button>" +
						"</div>" +
						"<div class=\\\"slide_image\\\">" +
							"<img src=\\\"resources/images/Placeholder.png\\\"/>" +
							"<div class=\\\"slide_number\\\">" +
								"<input type=\\\"number\\\" name=\\\"quantity\\\"" +
								"min=\\\"1\\\" max=\\\"1000\\\"" +
								"value=\\\"" + String.valueOf(subpage.getOrderNumber() + 1) + "\\\">" +
							"</div>" +
						"</div>" +
				"</div>";
		
		if (gallery.getChildPageList(subpage.getId()).size() != 0) {
			for (IPage childPage : gallery.getChildPageList(subpage.getId())) {
				sPages = appendSubpages(childPage, sPages);
			}
		}

		return sPages;
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
		gallery.changePageResourceToUsed(pageId, usedResource);
	}
}
