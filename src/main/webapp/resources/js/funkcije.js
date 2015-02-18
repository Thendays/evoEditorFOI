
function refreshPages() {
	$.ajax({
		url: 'refresh',
		success: function(data) {
			$("#left").html(data);
			attachEvents();
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

function addSubPage() {
	if (!$(".gallery:not(#left)").hasClass("selected")) {
		var pages = $("#left .selected");
		var uuid;
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
}

function deletePage() {
	var uuid = $(this).closest(".first, .second").attr("id");
	$.ajax({
		url: 'deletepage.html',
		data: {pageid: uuid},
		success: function(data) {
			refreshPages();
		}
	});
}

function movePage(e, direction) {
	var uuid = $(e).closest(".first, .second").attr("id");
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
		$("#resource").prop('disabled', 'disabled');
	} else {
		$("#resource").prop('disabled', false);
	}
	
	if (uuid !== null) {
		$.ajax({
			url: 'checkresource.html',
			data: {pageid: uuid},
			dataType: "json",
			success: function(data) {
				$("#resourceUsed").html(data.resourceUsed.charAt(0).toUpperCase() + data.resourceUsed.slice(1));
				$("#fileName").html(data.value);
				$("#resource option").filter(function() {
				    return this.text === (data.resourceUsed.charAt(0).toUpperCase() + data.resourceUsed.slice(1)); 
				}).attr('selected', true);
			}
		});
	}
}

function attachEvents() {
	$('.first, .second').each(function(i) { 
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
	
	$.ajax({
		url: 'changeresource',
		data: {pageid: uuid, resource: resource},
		success: function(data) {
			$("#resourceUsed").html(resource);
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