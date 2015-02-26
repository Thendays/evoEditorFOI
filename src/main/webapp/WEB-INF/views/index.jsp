<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="false"%>
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
<script src="resources/js/js-webshim/minified/polyfiller.js"></script>
<script src="resources/js/modernizr.js"></script>

<script type="text/javascript"
	src="resources/js/jquery.fullscreen-0.4.2.min.js"></script>

</head>

<body>
	<c:set var="selected_item" scope="application" value="${gallery.findPageByID(selectedItemUUID)}"/>
	<div id="container">
		<header>
			<button id="add_slide">
				<img src="resources/images/plus.png" width="12px" />&nbsp;Add new page
			</button>
			<button id="add_subPage">
				<img src="resources/images/preview_eye.png" width="12px" />&nbsp;Add new subpage
			</button>
			<button id="undo">
				<img src="resources/images/undo.png" width="12px" />
			</button>
			<button id="redo">
				<img src="resources/images/redo.png" width="12px" />
			</button>

			<div id="header_right">
				<button id="import">Import</button>
				<button id="export">Export</button>
			</div>
		</header>
		<div id="main">

			<div id="left" class="gallery" data-id="<c:out value="${galleryID}"/>">
					<c:set var="parentPage" value="${galleryID}" scope="application"/>
					<jsp:include page="pageMenu.jsp"/>
			</div>

			<div id="center">
				<div id="center_main">
					<div id="center_main_image"></div>
					<div id="center_main_text">			
						<c:choose>
							<c:when test="${empty selected_item}"> 
<!-- 							Nije odabrana nijedna stranica      -->
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${selected_item.getUsedResourceName().equals('text')}">
										 ---- Text.
									</c:when>
									<c:when test="${selected_item.getUsedResourceName().equals('image')}">
										---- Image.
									</c:when>
									<c:when test="${selected_item.getUsedResourceName().equals('video')}">
										---- Video.
									</c:when>
									<c:otherwise>
										NO resource found...
									</c:otherwise>
								</c:choose>
								<div id="description_preview">
									<div id="description_preview_label">Description:</div>
									<div id="description_preview_content">TEXT</div>
								</div>
								<div id="confirmation_text_preview">
									<div id="confirmation_text_preview_label">Confirmation text:</div>
									<div id="confirmation_text_preview_content">TEXT</div>
								</div>
							</c:otherwise>
						</c:choose>
						
						<div id="center_main_text_button">
							<button id="play">
								<img src="resources/images/play.png" width="30px" />
							</button>
						</div>
						<div id="center_center_text">
							<input type="text" name="instruction1"
								placeholder="Lorem ipsum dolor sit amet"><br /> <input
								type="text" name="instruction2"
								placeholder="Lorem ipsum dolor sit amet">
						</div>
						<div id="center_main_text_button">
							<button id="save">
								<img src="resources/images/save.png" width="30px" />
							</button>
						</div>
					</div>
				</div>
				<div id="center_main_info" style="margin-top: 90px;">
					<div class="first_half half table">
						<c:choose>
							<c:when test="${empty selected_item}">
								<!-- 	Nije odabrana stranica      -->
							</c:when>							
							<c:otherwise>
								<table id="attributes">
									<tr>
										<th class="first_col">Attribute</th>
										<th class="second_col">Value</th>
									</tr>
									<c:forEach var="attribute" items="${selected_item.getPageAttributeSet()}">
										<tr>
											<td class="first_col">
												<c:out value="${attribute}"></c:out>
											</td>											
											<td class="second_col">
												<c:out value="${selected_item.getPageAttribute(attribute)}"></c:out>
											</td>											
										</tr>
									</c:forEach>
	   							</table>								
   							 </c:otherwise>
						</c:choose>	
					</div>
					<div class="second_half half">
						<div class="half_text" style="float: left;">
							Resource<br />
						</div>
						<c:choose>
							<c:when test="${empty selected_item}">
								<c:set var="isDisabled" scope="request" value="disabled"/>
							</c:when>
							<c:otherwise>
								<c:set var="isDisabled" scope="request" value="dfdf"/>
							</c:otherwise>
						</c:choose>
						<select name="resource" id="resource" <c:out value="${isDisabled}"></c:out>>
							<c:choose>
								<c:when test="${empty selected_item}">
								<!-- 	Nije odabrana stranica      -->
								</c:when>						
								<c:otherwise>								
										<c:set var="first_resource" scope="application" value="${gallery.getPossiblePageResources(selected_item.getId()).get(0)}"/>
										<c:forEach var="resource" items="${gallery.getPossiblePageResources(selected_item.getId())}">
											<c:choose>
												<c:when test="${resource.getName().equals(first_resource.getName())}"> 
													<option value="<c:out value="${resource.getName()}"></c:out>" selected>
														<c:out value="${resource.getName()}"></c:out>
													</option>
												</c:when>
												<c:otherwise>
													<option value="<c:out value="${resource.getName()}"></c:out>">
														<c:out value="${resource.getName()}"></c:out>
													</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>									
								</c:otherwise>
							</c:choose>
						</select>					
						<br />
						<br />
						<form method="POST" action="" enctype="multipart/form-data"
							id="uploadFile">
							<input type="file" name="file" id="upload" <c:out value="${isDisabled}"></c:out>>
							<input type="text" name="page" id="page" hidden>
							<input type="submit" value="Upload" id="cloud_2" <c:out value="${isDisabled}"></c:out>>
						</form>
						<button id="add_attr" disabled>
							<img src="resources/images/save.png" width="13px" />
						</button>

					</div>
				</div>
			</div>

			<div id="right">
				<div class="right_input">
					<div class="right_input_text first_div">
						GALLERY<br />
					</div>
					<hr width="80%">
				</div>
				<div class="half table">
					<table>
						<tr>
							<th class="first_col">Attribute</th>
							<th class="second_col">Value</th>
						</tr>
						<tr>
							<td class="first_col">Gallery name</td>
							<td class="second_col" id="galleryName">
								<div contenteditable="true">
									<c:out value='${gallery.getGalleryAttribute("name")}' />
								</div>
							</td>
						</tr>
						<tr>
							<td class="first_col">QrCode</td>
							<td class="second_col" id="qrCode">
								<div contenteditable="true">
									<c:out value='${gallery.getGalleryAttribute("qrcode")}' />
								</div>
							</td>
						</tr>
					</table>
				</div>
				<div class="right_input" id="galleryName"></div>
				<div class="right_input" id="qrCode"></div>
				<div class="right_input">
					<div class="right_input_text">
						Upload QR Code<br />
					</div>
					<div class="two_divs">
						<div id="qr_code_preview">
							<img
								src="https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=<c:out value='${gallery.getGalleryAttribute("qrcode")}'/>"
								height="100%" />
						</div>
						<button id="qr_browse">Browse</button>
					</div>
				</div>
				<div class="right_input slider">
					<form action="#">
						<div class="form-row number-range-combi">
							<label for="transparency">Transparency</label><br /> <input
								name="transparency" id="transparency" type="number"
								value="<c:out value='${gallery.getGalleryAttribute("transparency")}'/>"
								min="0" max="256" data-number-stepfactor="1" /><br /> <input
								id="transparency_slider" type="range"
								value="<c:out value='${gallery.getGalleryAttribute("transparency")}'/>"
								max="256" data-range-stepfactor="1" />
						</div>
					</form>
				</div>
				<div class="right_input left_align" id="checkBoxes">
					<input id="repeat" type="checkbox" name="repeat" value="repeat"
						<c:out value='${gallery.getGalleryAttribute("repeat")}'/>>Repeat<br>
					<input id="showIndicator" type="checkbox" name="showIndicator"
						value="showIndicator"
						<c:out value='${gallery.getGalleryAttribute("showIndicator")}'/>>Show
					indicator<br>
				</div>
				<div id="saveGalleryAttributes"
					onclick="saveGalleryAttributes.call()">
					<button>
						<img src="resources/images/save.png" width="30px" />
					</button>
				</div>
			</div>
		</div>
	</div>

	<script>
	$(document).ready(function() {
		attachEvents();
	});
	
		$.webshim.setOptions('forms-ext', {
			replaceUI : 'auto',
			types : 'range',
			widgets : {
				"classes" : "hide-inputbtns",
				number : {
					calculateWidth : false
				}
			}
		});

		$.webshim.polyfill('forms forms-ext');

		$(function() {
			$('.number-range-combi').combineNumberRange();
		});

		$.fn.combineNumberRange = function() {
			return this.each(function() {
				var timer;
				var range = $('input[type="range"]', this);
				var number = $('input[type="number"]', this);

				function onRangeChange() {
					number.val(range.val());
				}

				function onNumberChange() {
					if (number.is(':valid')) {
						range.val(number.val());
					}
				}

				range.on('input change', function(e) {
					clearTimeout(timer);
					timer = setTimeout(onRangeChange, 0);
				});
				number.on('input change', function(e) {
					clearTimeout(timer);
					timer = setTimeout(onNumberChange, 0);
				});
			});
		};
	</script>

</body>

</html>
