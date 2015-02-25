
function refreshPages() {
	$.ajax({
		url: 'refresh',
		success: function(data) {
			$("#left").html(data);
			attachEvents();
			
			var galleryId = $("#left").attr("data-id");
			var pages = $("#left .pages");
			$.each(pages, function(idx, val) {
				if ($(this).attr("data-parentid") !== galleryId) {
					if (parseFloat($("#" + $(this).attr("data-parentid")).css("zoom")) <= 0.1)
						$(this).css("zoom", String(parseFloat($("#" + $(this).attr("data-parentid")).css("zoom")) - 0.01));
					else
						$(this).css("zoom", String(parseFloat($("#" + $(this).attr("data-parentid")).css("zoom")) - 0.1));
				}
			});
		}
	});
}

function addPage() {
	var pages = $("#left .selected");
	var uuid = $("#left").attr("data-id");
	$.each(pages, function(idx, val) {
		if ($(this).hasClass("selected"))
			uuid = $(this).attr("id");
	});
	
	$.ajax({
		url: 'addpage.html',
		data: {parentid: uuid},
		success: function(data) {
			refreshPages();
		}
	});
}

function deletePage() {
	var uuid = $(this).closest(".pages").attr("id");
	$.ajax({
		url: 'deletepage.html',
		data: {pageid: uuid},
		success: function(data) {
			refreshPages();
		}
	});
}

function movePage(e, direction) {
	var uuid = $(e).closest(".pages").attr("id");
	if (direction === null) {
		if (e.oldvalue > e.value) {
			for (var i = (e.oldvalue - e.value); i>0; i--) {
				$.ajax({
					url: 'pageup.html',
					data: {pageid: uuid},
					success: function(data) {
						refreshPages();
					}
				});
			}
		} else if (e.value > e.oldvalue) {
			for (var i = (e.value - e.oldvalue); i>0; i--) {
				$.ajax({
					url: 'pagedown.html',
					data: {pageid: uuid},
					success: function(data) {
						refreshPages();
					}
				});
			}
		}
	} else if(direction === "up") {
		$.ajax({
			url: 'pageup.html',
			data: {pageid: uuid},
			success: function(data) {
				refreshPages();
			}
		});
	} else if(direction === "down") {
		$.ajax({
			url: 'pagedown.html',
			data: {pageid: uuid},
			success: function(data) {
				refreshPages();
			}
		});
	}
}

/*fullscreen*/

$(function() {
	  
	  // open in fullscreen
	  $('#center_main #play').click(function() {
		  $('#center_main').fullscreen();
		  return false;
	  });
	   $('header #play').click(function() {
		  $('#center_main').fullscreen();
		  return false;
	  });

	  // exit fullscreen
	  $('#center_main .exitfullscreen').click(function() {
		  $.fullscreen.exit();
		  return false;
	  });

	  // document's event
	  $(document).bind('fscreenchange', function(e, state, elem) {
		  // if we currently in fullscreen mode
		  if ($.fullscreen.isFullScreen()) {
			  $('#center_main #play').hide();
		  } else {
			  $('#center_main #play').show();
		  }
	  });
});

function chngTransparency(e) {
	if ($(this).attr("id") === "transparency") {
		$("#transparency_slider").val($(this).val());
	} else if($(this).attr("id") === "transparency_slider") {
		$("#transparency").val($(this).val());
	}
}

function selectPage(e) {
	
	var pages = $("#left").find(".selected");
	var uuid = null;
	
	if ($("#left").hasClass("selected"))
		$("#left").removeClass("selected");
	
	$.each(pages, function(idx, val) {
		if ($(this).hasClass("selected"))
			$(this).removeClass("selected");
	});
	
	$(this).addClass("selected");
	
	if ($(this).attr("id") !== "left")
		uuid = $(this).attr("id");
	e.stopPropagation();
	
	if ($("#left").hasClass("selected")) {
		$('#resource option[value=""]').attr('selected','selected');
		$("#attributes").find("tr:gt(0)").remove();
		$("#resource").prop('disabled', 'disabled');
		$("#add_attr").prop('disabled', 'disabled');
		$("#uploadFile input").prop('disabled', 'disabled');
	} else {
		$("#resource").prop('disabled', false);
		$("#add_attr").prop('disabled', false);
		$("#uploadFile input").prop('disabled', false);
		$("#page").attr("value", uuid);
	}
	
	if (uuid !== null) {
		$.ajax({
			url: 'checkpageattributes.html',
			data: {pageid: uuid},
			dataType: "json",
			success: function(data) {
				console.log(data);
				$("#attributes").find("tr:gt(0)").remove();
				$.each(data.pageAttributes, function(k, v) {
					$("#attributes").append('<tr><td class="first_col">' + k 
							+ '</td><td class="second_col"><div contenteditable>' + v + '</td></tr>');
				});
				
				if (data.resourceUsed) {
					$.each(data.resourceUsed, function(k, v) {
						if (typeof v === "object") {
							$.each(data.resourceUsed.attributes, function(k, v) {
								if (k === "Content")
									$("#attributes").append('<tr class="resourceAttributes"><td class="first_col">' + k 
											+ '</td><td class="second_col"><div contenteditable>' + v + '</td></tr>');
								else
									$("#attributes").append('<tr><td class="first_col">' + k 
										+ '</td><td class="second_col">' + v + '</td></tr>');
							});
						} else {
							$("#attributes").append('<tr><td class="first_col">' + k 
									+ '</td><td class="second_col">' + v + '</td></tr>');
						}
					});
					$('#resource option[value="' + data.resourceUsed.Resource + '"]').attr('selected','selected');
				} else
					$('#resource option[value=""]').attr('selected','selected');
			}
		});
	}
}

function attachEvents() {
	$('.pages').each(function(i) { 
		$.each($(this).find(":button.delete_1"), function() {
			$(this).on("click", null, this, deletePage);
			});
		
		$.each($(this).find(":button.up_1"), function() {
			$(this).click(function() {
				movePage(this, "up");
				});
			});
		
		$.each($(this).find(":button.down_1"), function() {
			$(this).click(function() {
				movePage(this, "down");
				});
			});
		
		$.each($(this).find(":input"), function() {
			$(this).on("focus", function() {
				this.oldvalue = this.value;
			});
			
			$(this).change(function() {
				movePage(this, null);
			});
		});
	});
}

$(document).on("change", "#resource", function() {
	var pages = $("#left .selected");
	var uuid = null;
	var resource = $(this).val();
	$.each(pages, function(idx, val) {
		if ($(this).hasClass("selected") && !$(this).hasClass("gallery"))
			uuid = $(this).attr("id");
	});
	
	var ajax = $.ajax({
		url: 'changeresource',
		data: {pageid: uuid, resource: resource},
		dataType: "json",
		success: function(data) {
			$.each(data.attributes, function(k, v) {
				$("#attributes .resourceAttributes").remove();
				if (k === "Content")
					$("#attributes").append('<tr class="resourceAttributes"><td class="first_col">' + k 
							+ '</td><td class="second_col"><div contenteditable>' + v + '</td></tr>');
				else
					$("#attributes").append('<tr class="resourceAttributes"><td class="first_col">' + k 
							+ '</td><td class="second_col resourceAttributes">' + v + '</td></tr>');
				});
			},
		error: function() {
			$("#attributes .resourceAttributes").remove();
		}
		});
	});

$(document).on("click", "#add_attr", function() {
	var pages = $("#left .selected");
	var uuid = null;
	$.each(pages, function(idx, val) {
		if ($(this).hasClass("selected") && !$(this).hasClass("gallery"))
			uuid = $(this).attr("id");
	});
	
	var parameters = {
			pageid: uuid
	};
	
	var key = null;
	var value = null;
	
	$("#attributes > tbody > tr:not(:first)").each(function() {
		key = $(this).find("td:nth-child(1)").html();
		if ($(this).find("td:nth-child(2) > div").length) {
			value = $(this).find("td:nth-child(2) > div").html();
		}
		else {
			value = $(this).find("td:nth-child(2)").html();
		}
		value = value.replace("\\", "\\\\");
		parameters[key] = value;
	});
	
	$.ajax({
		url: 'savepageattributes.html',
		data: parameters,
			   
		success: function(data) {
		}
	});
});

$(document).on("click", "#export", function() {
	$.ajax({
		url: 'export.html',
		success: function(data) {
		}
	});
});

function saveGalleryAttributes() {
	var repeat = null;
	var showIndicator = null;
	var qrCode = null;
	var galleryName = null;
	var transparency = null;
	
	if ($("#repeat").prop('checked')) {
		repeat = "checked";
	} else {
		repeat = "";
	}
	
	if ($("#showIndicator").prop('checked')) {
		showIndicator = "checked";
	} else {
		showIndicator = "";
	}
	
	qrCode = $("#qrCode").text();
	galleryName = $("#galleryName").text();
	transparency = $("#transparency").val();
	
	$.ajax({
		url: 'galleryattr.html',
		data: {name: galleryName, 
			   qrcode: qrCode, 
			   repeat: repeat, 
			   showindicator: showIndicator, 
			   transparency: transparency},
			   
		success: function(data) {
			refreshPages();
		}
	});
}