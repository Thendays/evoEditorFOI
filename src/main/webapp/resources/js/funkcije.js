$(function() {
	  $( "#slider" ).slider({
		  range: "min",
		  min: 0,
		  max: 256,
		  value: 256,
		  slide: function( event, ui ) {
			$( "#opacity" ).val( ui.value );
		  }
    	});
		$( "#opacity" ).val( $( "#slider" ).slider( "value" ) );
  });

function refreshPages() {
	var json;
	$.ajax({
		url: 'refresh',
		dataType: 'json',
		success: function(json) {
			$('#left').html(json.gallerySidebar);
			$('#galleryName').html(json.galleryName);
			$('#qrCode').html(json.qrCode);
			$('#transparency').html(json.transparency);
			$('#chk').html(json.chk);
			attachEvents();
		}
	});
}

function addPage() {
	var pages = $("#left .selected");
	var uuid = $(".gallery:not(#left)").attr("id");
	$.each(pages, function(idx, val) {
		if ($(this).hasClass("selected") && !$(this).hasClass("gallery"))
			uuid = $(this).attr("data-parentid");
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

function movePage() {
	var uuid = $(this).closest(".first, .second").attr("id");
	if (this.oldvalue > this.value) {
		for (var i = (this.oldvalue - this.value); i>0; i--) {
			$.ajax({
				url: 'pageup.html',
				data: {pageid: uuid},
				success: function(data) {
					refreshPages();
				}
			});
		}
	} else if (this.value > this.oldvalue) {
		for (var i = (this.value - this.oldvalue); i>0; i--) {
			$.ajax({
				url: 'pagedown.html',
				data: {pageid: uuid},
				success: function(data) {
					refreshPages();
				}
			});
		}
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

function selectPage(e) {
	
	var pages = $("#left").find(".selected");
	
	$.each(pages, function(idx, val) {
		if ($(this).hasClass("selected"))
			$(this).removeClass("selected");
	});
	
	$(this).addClass("selected");
	e.stopPropagation();
}

function attachEvents() {
	$('.first, .second').each(function(i) { 
		$.each($(this).find(":button"), function() {
			$(this).on("click", null, this, deletePage);
			});
		
		$.each($(this).find(":input"), function() {
			$(this).on("focus", function() {
				this.oldvalue = this.value;
			});
			
			$(this).on("change", null, this, movePage);
		});
	});
}