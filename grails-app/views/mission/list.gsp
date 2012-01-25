<%@ page import="owrks.owo.Mission" %>
<html>
    <head>
    	<!--  
		* This file is part of oWrks.
		*
		* oWrks is free software: you can redistribute it and/or modify
		* it under the terms of the GNU General Public License as published by
		* the Free Software Foundation, either version 3 of the License, or
		*  any later version.
		*
		* oWrks is distributed in the hope that it will be useful,
		* but WITHOUT ANY WARRANTY; without even the implied warranty of
		* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
		* GNU General Public License for more details.
		*
		* You should have received a copy of the GNU General Public License
		* along with oWrks.  If not, see <http://www.gnu.org/licenses/>.
		-->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'mission.label', default: 'Mission')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        
       	<al:isPerformer owo="${owoInstance}">
	        <jq:jquery>
				// check onLoad if stepId is set
				if(!$('input#currentStepId').val()) {
					$('ul#postNav').hide();
				}
				
				$('#toggleMission span').click(function(){
					$('div#missionList').slideToggle(1000, function() {
					   if($('div#missionList').is(":visible")) {
							$('#toggleMission span').text('Mission-Liste ausblenden');
							// set smart-cookie ;)
							$.cookie('showMissionList', 'true');	   	
					   } else {
					   		$('#toggleMission span').text('Mission-Liste einblenden');
					   		$.cookie('showMissionList', 'false');
					   }
					});
				});
				
				// Cookie-onLoad stuff
				// hide missionList if cookie ist set
				if($.cookie('showMissionList') == 'false') {
					$('div#missionList').hide();
					$('#toggleMission span').text('Mission-Liste einblenden');
				}
				
				
				// jQuery-Address Plugin - Basic Setup
				// @see http://www.asual.com/jquery/address/docs/
				
				$('a.stepTitle').click(function(){
					$.address.value($(this).attr('href'));
				});
				
				
				// WALL FORM - submit posts
				$('ul#stepNav li a').live('click', function(){
					
					if(!$('input#currentStepId').val()) {
						alert('Bitte wähle einen Step aus');
						return false;
					}
					
					$('ul#stepNav li a').removeClass('active');
					$(this).addClass('active');	
					
					var href = $(this).attr('href') + '/' + $('input#currentStepId').val();
					
					// jQuery-Address
					$.address.value(href);
					
					setLoading(true, $(this).text() + ' wird geladen...');
					jQuery.ajax({
						type:'POST', 
						url: href,
						success: function(data,textStatus){
							setLoading(false);
							jQuery('#missionContent').html(data);
						},
						error: function(XMLHttpRequest,textStatus,errorThrown){}
					});
					
					return false;
				});
				
				$('ul#stepNav li a').address(function() {  
				    //return $(this).attr('href').replace(/^#/, '');  
				});
				
				// jQuery-Address Setup
				$.address.externalChange(function(event) {
				    //var path = event.value;
				    //console.log(event);
				    chooseAddress(event);
				}); 
				
				$.address.init(function(event) {
					if(event.path == '/') {
						setAddress('/missionList');
					}
				})
				
	        </jq:jquery>
	       <g:javascript>
	       		var isLoaded = 0;
	       		function chooseAddress(event) {
	       			var path = event.value;
	       			if(path == '/'){
						//$.address.value('missionList');
					} else if(path == '/missionList') {
						var uri = '${createLink(controller: "mission", action: "loadOwoMissionListAjax", id: owoInstance.id)}';
						if(isLoaded == 0) {
							isLoaded = 1;
						} else {
							loadTemplate(uri);
						}
					} else {
						// AUTOLOAD currentStep
						currentStepId = ${selectedStep?.id ?: 0};
						if(currentStepId) {
							setStep(currentStepId);
						} else if($.cookie('currentStepId')) {
							// loading via cookie currently disabled
							//setStep(parseInt($.cookie('currentStepId')));
						}
						loadTemplate(path); 
					}
	       		}
	       		function setAddress(uri) {
	       			$.address.value(uri);
	       		}
	       
	       		// überschreibt die vorhandene setLoading (application.js)
	       		var defaultLoadingMessage = 'Bitte warten...';
	       		function setLoading(loading, loadingMessage){
					if(loading == null)
						loading = true;
					if(loading){
						message = (loadingMessage == null || loadingMessage == '') ? defaultLoadingMessage : loadingMessage;
						//<![CDATA[
						$('#missionContent').html('<h1>' + message + '</h1><img src="${resource(dir: 'images', file: 'ajax-loader.gif')}" alt="loading" />');
						//]]>
						
						// show loading.gif
						$('div#content').bind('mousemove', function(event){
							$("#spinner").show().css("top", event.pageY+10).css("left", event.pageX+10);
						});
					}else{
						$("#spinner").fadeOut(1000);
						//$.unblockUI();
						$('div#content').unbind('mousemove');
					}
				}

				// load template via ajax // wird ausschließlich von jQuery-Address aufgerufen
	       		function loadTemplate(uri) {
	       			setLoading(true, 'Seite wird geladen...');
				    jQuery.ajax({
						type:'post', 
						url: uri,
						success: function(data,textStatus){
							$('#missionContent').html(data);
							$("input:text").labelify({ labelledClass: "inputHelp" });
							$.address.value(uri);
						},error: function(XMLHttpRequest,textStatus,errorThrown){}
					});
					setLoading(false);
	       		}
	       		
	       		// set currentStepId to hidden input-field
	       		function setCurrentStepId(id, doRefreshNavigation) {
	       			$('input#currentStepId').val(id);
	       			
	       			// mark step select option selected
	       			$('select#stepId option[value=' + id + ']').attr('selected', 'selected');
	       			
	       			// remember-step in cookie
	       			$.cookie('currentStepId', id);
	       			
	       			if(doRefreshNavigation == 'true') {
	       				// no action yet
	       			}
	       		}
	       		
	       		
	       		function setStep(stepId) {
	       			// set jQuery-Address info (ajax-url)
	       			$.address.value('/step/wall/' + stepId);
	       			
	       			setLoading(true, 'Step wird geladen...');
	       			jQuery.ajax({
						type:'POST', 
						url: '${createLink(controller: "step", action: "wall") }/' + stepId,
						success: function(data,textStatus){
							jQuery('#missionContent').html(data);
							setCurrentStepId(stepId);
						},
						error: function(XMLHttpRequest,textStatus,errorThrown){}
					});
					setLoading(false);
	       		}
	       		
	       		function refreshMissionList(missionId, stepId) {
	       			jQuery.ajax({
						type:'POST', 
						url: '${createLink(controller: "mission", action: "loadMissionListItemAjax") }/' + missionId,
						success: function(data,textStatus){
							jQuery('#missionList').html(data);
							if(stepId) {
								$('li#step_' + stepId).effect('highlight', {}, 6000);
							} else {
								$('ul.stepList li:last').effect('highlight', {}, 6000);
							}
						},
						error: function(XMLHttpRequest,textStatus,errorThrown){}
					});
	       		}
	       		

				// linkboard stuff
				function addStepLink(response) {
					$('#stepLinkList').prepend(response);
					$('#stepLinkList div.stepLink:first').hide().slideToggle();
				}
				
				var tempButtonContent;
				var loadingButtonId;
				function setLoadingImage(e) {
					//<![CDATA[
					loader = '<img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />';
					//]]>
					if(e != '' && e != false) {
						loadingButtonId = e;
						tempButtonContent = $('#' + e).html();
						$('#' + e).html(loader);
					} else {
						$('#' + loadingButtonId).html(tempButtonContent);
					}
				}
	       </g:javascript>

	       <script type="text/javascript" src="${resource(dir:'js/niceEdit',file:'nicEdit-compressed.js')}"></script>
	       
	       <script type="text/javascript" src="${resource(dir:'js/address',file:'jquery.address-1.4.min.js')}"></script>
		   <link rel="stylesheet" href="${resource(dir:'css',file:'uploader.css')}" type="text/css" media="screen" />
		   
		   <link rel="stylesheet" href="${resource(dir:'css',file:'wall.css')}" type="text/css" media="screen" />
	       
	       <script type="text/javascript" src="${resource(dir:'js/',file:'fileuploader.js')}"></script>
	       
	       <script type="text/javascript" src="${resource(dir:'js/',file:'jquery.cookie.js')}"></script>
	       
        </al:isPerformer>
    </head>
    <body>
		<div class="box">
	        <div class="col2-step">
	        	<div style="padding:10px 10px 10px 10px;">
	        		<input type="hidden" id="currentStepId" value="${selectedStep?.id}" />
					<div id="missionContent" class="">
	        			<g:render template="missionList" model="[ missionInstanceList: missionInstanceList, owoInstance: owoInstance, stepInstance: stepInstance, selectedMission: selectedMission ]"></g:render>
					</div>
	        	</div>
	        </div>
	        <div class="clear"></div>
		</div>
    </body>
</html>