/*
 * Register AJAX-Events (onCreate/onComplete)
 * In this case a div (#spinner) will be shown
 */
/*var Ajax;
if (Ajax && (Ajax != null)) {
	Ajax.Responders.register({
	  onCreate: function() {
        if($('spinner') && Ajax.activeRequestCount>0)
          Effect.Appear('spinner',{duration:0.5,queue:'end'});
	  },
	  onComplete: function() {
        if($('spinner') && Ajax.activeRequestCount==0)
          Effect.Fade('spinner',{duration:0.5,queue:'end'});
	  }
	});
}*/

//global vars
var loading = false;
var defaultLoadingMessage = 'Bitte warten...';

// init
$(document).ready(function(){
	
	//highlight msgs
	$('div#flashmessage').effect("highlight", {color: "#8bc53e", mode:"hide"}, 15000);
	
	// facebox
	$('a.dialog').live("click", function(){
		setLoading();
	  	$.get($(this).attr("href"), function(data){
	  		data = $(data);
	  		setLoading(false);
	  		$(data).find('nav#breadcrumb').remove();
	  		showDialog($(data).find('#content'));
	  	});
	  	return false;
	});
});

// set spinner
$(document).mousemove(function(e){
	if(loading){
	    $("#spinner").css("top", e.pageY+10);
	    $("#spinner").css("left", e.pageX+20);
	}
});

// show a dialog with given content
function showDialog(content){
	$.facebox($(content).html());
}

// set page to loading
function setLoading(loading, loadingMessage){
	if(loading == null)
		loading = true;
	if(loading){
		message = (loadingMessage == null || loadingMessage == '') ? defaultLoadingMessage : loadingMessage;
		//<![CDATA[
		$.blockUI({message: '<h1>' + message + '</h1><img src="../../images/ajax-loader.gif" alt="loading" /><p><a href="#" onclick="\$.unblockUI();return false;">Abbrechen</a></p>'});
		//]]>
		//$.growlUI('',message);
	}else{
		//$("#spinner").fadeOut(1000);
		$.unblockUI();
	}
}

// fade out element after delete
// TODO: function should be loaded only for logged in users
function afterDeleteFadeOut(selector) {
	$(selector).fadeOut('slow', function() {
		$(this).remove();
	});
}