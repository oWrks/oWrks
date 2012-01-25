<g:javascript>
	function addStreamItem(response) {
		if(response.search('id="errors"') > 0) {
			$('div#posts').prepend(response);
		} else {
			// hide text
			if(!$('#sortableStream .streamItem').length) {
				$('p#noPostsYet').hide();
			}
			$('#sortableStream').prepend(response);
			$('#sortableStream .streamItem:first').hide().slideToggle();
		}
	}
	
	
	function increaseItemCount(e) {
		count = parseInt($("label[for='"+e+"'] span").text())+1;
		$("label[for='"+e+"'] span").text(count);
	}
	
	function decreaseItemCount(e) {
		count = parseInt($("label[for='"+e+"'] span").text())-1;
		$("label[for='"+e+"'] span").text((count < 0 ? '0' : count));
	}
	
	function inputValidate(e) {
		// input feld darf nicht leer sein
		//<![CDATA[
		if($('#' + e).val() != '' && $('#' + e).val() != $('#' + e).attr('title') && $('#' + e).val() != '<br>' ) {
			$('#' + e).css({ border: '1px solid #ccc'});
			return true;
		 } else {
		 	$('#' + e).css({ border: '1px solid red'});
		 	// TODO: show help texts
		 	//$('#' + e).val($('#' + e).attr('title')).addClass('inputHelp');
			return false;
		}
		//]]>
	}
	
	var niceEditor;
	function addTextEditor() {
		
		if(niceEditor) {
			niceEditor.removeInstance('stepTextContent');
			niceEditor = null;
		}
		
		niceEditor = new nicEditor({
			buttonList : ['fontSize','bold','italic','underline','strikethrough','subscript','superscript', 'ol', 'ul', 'forecolor', 'bgcolor',],
			iconsPath : '${resource(dir: 'images', file: 'nicEditorIcons.gif') }',
			maxHeight : 200,
			xhtml : true,
		}).panelInstance('stepTextContent');
	}
	
	
</g:javascript>

<jq:jquery>
		$('input#postContent').focus();
		
		videoCount = $('.stepVideo').length;
		$("label[for='showVideos'] span").text(videoCount);
		
		// init styles
		$('div#addMessage').show();
		$('#postNav li:first').addClass('active');
		
		$('ul#postNav li').click(function(){
			$('div.stepForm').hide().removeClass('active');
			var div = $(this).attr('id').replace('Link', '');
			$('div#' + div).show().addClass('active');
			
			// add active class to the nav
			$('ul#postNav li').removeClass('active');
			$(this).addClass('active');
			
			if(div == 'addText') {
				addTextEditor();
			}
		})

		// AJAX-UPLOADER
       var uploader = new qq.FileUploader({
			element: document.getElementById('stepUpload'),
       		action: '${createLink(controller: 'step', action: 'uploadAjax')}',
            multiple: true,
        	params: { stepId: $('input#currentStepId').val(), },
           	// ex. ['jpg', 'jpeg', 'png', 'gif'] or []
           	allowedExtensions: [],
           	// each file size limit in bytes
           	// this option isn't supported in all browsers
           	sizeLimit: 0, // max size   
           	minSizeLimit: 0, // min size
           	// set to true to output server response to console
           	debug: false,
           	onSubmit: function(id, fileName){},
			onProgress: function(id, fileName, loaded, total) {  
				$('#uploadStatus').html( '' + loaded + 'KB  von ' + total + 'KB bereits hochgeladen');
			},
			onComplete: function(id, fileName, responseJSON) { 
				$('#uploadResponse').append(responseJSON.message + "<br />");
				$('#uploadStatus').html("");
				jQuery.ajax({
					type:'POST', 
					url: '${createLink(controller: "stepAttachment", action: "renderAttachmentTemplate") }/' + responseJSON.attachmentId,
					success: function(data,textStatus){
						addStreamItem(data);
						increaseItemCount('showAttachments');
					},
					error: function(XMLHttpRequest,textStatus,errorThrown){}
				}); 
			},
			onCancel: function(id, fileName) {  
				$('#uploadResponse').append("Upload der Datei " + fileName + " wurde abgebrochen <br />");  
			},
			messages: {
			    // error messages, see qq.FileUploaderBasic for content            
			},
			showMessage: function(message){ 
				//alert(message);
				$('#uploadResponse').append(message); 
			} 
       });
       
       
       
       
        // FILTER
    	$('input.filterItem').not('.toggleSortable').change(function(){
    		var filterClass = $(this).attr('id').replace('Filter', '');
    		toggleFilter($(this), filterClass);
    	});
    	
    	function toggleFilter(e, cssClass) {
    		if(!e.is(':checked')) {
				$('div.' + cssClass).hide();
			} else { 
				$('div.' + cssClass).show(); 
			}
    	}
    	
    	// NICEDITOR - add nicEditor onclick
    	$('span.stepText.edit').live("click", function(){
    		//var myNicEditor = new nicEditor();
			//myNicEditor.addInstance('myInstance1');
			var o = $(this);
			var textId = $(this).attr('id').replace('textEdit_', '');
			jQuery.ajax({
				type:'POST',
				url: '${createLink(controller: "stepText", action: "renderTextEditTemplate") }/' + textId,
				success:function(data,textStatus){
					o.parent('.stepPostContent').children('div.wallItemContent').html(data);
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){}
			});
    	});
		
</jq:jquery>
