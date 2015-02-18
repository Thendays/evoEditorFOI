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
import org.springframework.web.bind.annotation.ResponseStatus;

import com.evolaris.editor.model.RawGallery;
import com.evolaris.editor.model.interfaces.IGallery;
import com.evolaris.editor.model.interfaces.IPage;

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
		model.addAttribute("galleryID", gallery.getID());
		model.addAttribute("parentPage", gallery.getID());
		
		return "pageMenu";
	}
	
//	private String appendSubpages(IPage subpage, String sPages) {
//		sPages += 
//				"<div class=\\\"second\\\" id=\\\"" + String.valueOf(subpage.getId()) + "\\\" onclick=\\\"selectPage.call(this, event)\\\" data-parentid=\\\"" + subpage.getParentID() + "\\\">" +
//						"<div class=\\\"delete_slide\\\">" +
//							"<button class=\\\"delete_1\\\">" + 
//								"<img src=\\\"resources/images/delete.png\\\" width=\\\"20px\\\"/>" + 
//							"</button>" +
//						"</div>" +
//						"<div class=\\\"move_slide\\\">" +
//							"<button class=\\\"up_1\\\"><img src=\\\"resources/images/up.png\\\" width=\\\"10px\\\" height=\\\"10\\\"/></button><br/>" +
//							"<button class=\\\"down_1\\\"><img src=\\\"resources/images/down.png\\\" width=\\\"10px\\\" height=\\\"10\\\"/></button>" +
//						"</div>" +
//						"<div class=\\\"slide_image\\\">" +
//							"<img src=\\\"resources/images/Placeholder.png\\\"/>" +
//							"<div class=\\\"slide_number\\\">" +
//								"<input type=\\\"number\\\" name=\\\"quantity\\\"" +
//								"min=\\\"1\\\" max=\\\"1000\\\"" +
//								"value=\\\"" + String.valueOf(subpage.getOrderNumber() + 1) + "\\\">" +
//							"</div>" +
//						"</div>" +
//				"</div>";
//		
//		if (gallery.getChildPageList(subpage.getId()).size() != 0) {
//			for (IPage childPage : gallery.getChildPageList(subpage.getId())) {
//				sPages = appendSubpages(childPage, sPages);
//			}
//		}
//
//		return sPages;
//	}
	
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
