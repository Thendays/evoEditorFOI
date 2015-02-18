<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="false" %>
<!doctype html>
<html>

<head>
<meta charset="utf-8">
<title>EvoEditor</title>
<link rel="stylesheet" href="resources/dizajn.css">
<link rel="stylesheet" href="resources/jquery-ui.css">
<script src="resources/js/jquery-1.10.2.js"></script>
<script src="resources/js/jquery-ui.js"></script>
<script src="resources/js/funkcije.js"></script>

<script type="text/javascript" src="resources/js/jquery.fullscreen-0.4.2.min.js"></script>

</head>

<body onload="refreshPages();">

<div id="container">
	<header>
    	<button id="add_slide" onclick="addPage()"><img src="resources/images/plus.png" width="12px"/>&nbsp;Add new slide</button>
        <button id="add_instruction" onclick="addSubPage()"><img src="resources/images/plus.png" width="12px"/>&nbsp;Add extra instruction</button>
        <button id="play"><img src="resources/images/preview_eye.png" width="12px"/>&nbsp;Preview</button>
        <button id="undo"><img src="resources/images/undo.png" width="12px"/></button>
        <button id="redo"><img src="resources/images/redo.png" width="12px"/></button>
        
        <div id="header_right">
        	<button id="import">Import</button>
       		<button id="export">Export</button>
        </div>
    </header>
    <div id="main">
    
    	<div id="left" class="gallery" data-id="<c:out value="${galleryID}"/>" onclick="selectPage.call(this, event)">

        </div>
        
        <div id="center">
        	<div id="center_main">
            	<div id="center_main_image">
                <!--	<img src="resources/images/Placeholder.png"/>  -->
                	<object width="613" height="143">
                		<PARAM name="code" value="GalleryPreview.class">
                 	</object>
                </div>
                <div id="center_main_text">
                	<div id="center_main_text_button">
                    	<button id="play"><img src="resources/images/play.png" width="30px"/></button>
                    </div>
                    <div id="center_center_text">
                        <input type="text" name="instruction1" placeholder="Lorem ipsum dolor sit amet"><br/>
                        <input type="text" name="instruction2" placeholder="Lorem ipsum dolor sit amet">
                    </div>
                    <div id="center_main_text_button">
                    	<button id="save"><img src="resources/images/save.png" width="30px"/></button>
                    </div>
                </div>
            </div>
            <div id="center_main_info" style="margin-top: 90px;">
            	<div class="first_half half table">
                	<table>
                    	<tr>
                        	<th class="first_col">Resource</th>
                            <th class="second_col">Attribute</th>
                        </tr>
                    	<tr>
                        	<td class="first_col" id="resourceUsed"></td>
                            <td class="second_col" id="fileName"></td>
                        </tr>
                    </table>
                </div>
               
                <div class="second_half half">
                	<div class="half_text" style="float: left;">
                		Resource<br/>
                    </div>
                    <select name="resource" id="resource" disabled>
                    	<option id="image">Image</option>
                        <option id="video">Video</option>
                        <option id="confirmation">Text</option>
                    </select>
                    <br/><br/>
                    <div class="half_text">
                    	Attribute<br/>
                    </div>
                    <input class="short" type="text" name="attr" placeholder="Attribute" style="width: 100%;"><br/>
                    <button id="cloud_2"><img src="resources/images/cloud.png" width="13px"/></button>
                    <button id="folder_2"><img src="resources/images/folder.png" width="13px"/></button>
                    <button id="add_attr"><img src="resources/images/save.png" width="13px"/></button>
                    
                </div>
            </div>
        </div>
        
        <div id="right">
        	<div class="right_input">
            	<div class="right_input_text first_div">
                	GALLERY<br/>
                </div><hr width="80%">
            </div>
            <div class="half table">
                	<table>
                    	<tr>
                        	<th class="first_col">Resource</th>
                            <th class="second_col">Attribute</th>
                        </tr>
                    	<tr>
                        	<td class="first_col">Gallery name</td>
                            <td class="second_col" id="galleryName"><div contenteditable><c:out value='${gallery.getGalleryAttribute("name")}'/></td>
                        </tr>
                        <tr>
                            <td class="first_col">QrCode</td>
                            <td class="second_col" id="qrCode"><div contenteditable><c:out value='${gallery.getGalleryAttribute("qrcode")}'/></td>
                        </tr>
                    </table>
                </div>
            <div class="right_input" id="galleryName">
            </div>
            <div class="right_input" id="qrCode">
            </div>
            <div class="right_input">
            	<div class="right_input_text">
                	Upload QR Code<br/>
                </div>
                <div class="two_divs">
                	<div id="qr_code_preview">
                    	<img src="resources/images/Placeholder.png" height="100%"/>
                    </div>
                    <button id="qr_browse">Browse</button>
                </div>
            </div>
            <div class="right_input slider">
	            <label for="transp">Transparency</label>
	            <br/><input type="number" id="transparency" value="<c:out value='${gallery.getGalleryAttribute("transparency")}'/>" onchange="chngTransparency.call(this, event)"><br/>
				0<input type="range" id="transparency_slider" min="0" value="<c:out value='${gallery.getGalleryAttribute("transparency")}'/>" max="256" step="1" onchange="chngTransparency.call(this, event)">256
            </div>
            <div class="right_input left_align" id="checkBoxes">
            	<input id="repeat" type="checkbox" name="repeat" value="repeat" <c:out value='${gallery.getGalleryAttribute("repeat")}'/>>Repeat<br>
  				<input id="showIndicator" type="checkbox" name="showIndicator" value="showIndicator" <c:out value='${gallery.getGalleryAttribute("showIndicator")}'/>>Show indicator<br>
            </div>
            <div id="saveGalleryAttributes" onclick="saveGalleryAttributes.call()">
            		<button><img src="resources/images/save.png" width="30px"/></button>
            </div>
        </div>
    </div>
</div>
		
</body>

</html>
