<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en">
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
		<title><g:layoutTitle default="oWrks" /> - oWrks.com</title>
		<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
		<meta name="description" content="" />
		<meta name="keywords" content="Workshop, Online, Team, Group, Learn, Agile, Community" />
		<meta name="author" content="oWrks" />
		<meta name="revisit-after" content="2 weeks" />
		<meta name="robots" content="all" />
		<meta http-equiv="pragma" content="no-cache" />
		<link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'main.css')}" />
		<link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
		<g:javascript library="jquery" plugin="jquery" />
		<jqui:resources />
		<app:controllerStyle />
		
		<script src="${resource(dir: 'js/facebox', file: 'facebox.js')}" type="text/javascript"></script>
		<link href="${resource(dir: 'css/facebox', file: 'facebox.css')}" media="screen" rel="stylesheet" type="text/css"/>
		
		<script src="${resource(dir: 'js/pnotify-1.0.1', file: 'jquery.pnotify.min.js')}" type="text/javascript"></script>
		<link href="${resource(dir: 'css/pnotify-1.0.1', file: 'jquery.pnotify.default.css')}" media="screen" rel="stylesheet" type="text/css"/>
		<link href="${resource(dir: 'css/jquery-ui', file: 'jquery-ui-1.8.14.custom.css')}" media="screen" rel="stylesheet" type="text/css"/>
		
		
		<script src="${resource(dir: 'js/PeriodicalUpdater', file: 'jquery.periodicalupdater.js')}" type="text/javascript"></script>
		<script src="${resource(dir: 'js/PeriodicalUpdater', file: 'jquery.updater.js')}" type="text/javascript"></script>
		
		
		<script src="${resource(dir: 'js', file: 'slides.min.jquery.js')}" type="text/javascript"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.blockUI.js') }"></script>
		
		<script src="${resource(dir: 'js/tipsy', file: 'jquery.tipsy.js')}" type="text/javascript"></script>
		<link href="${resource(dir: 'css/tipsy', file: 'tipsy.css')}" media="screen" rel="stylesheet" type="text/css"/>
		
		<script type="text/javascript" src="${resource(dir:'js/',file:'jquery.labelify.js')}"></script>
		
		<script type="text/javascript">
			$(document).ready(function(){

				// labelify jQuery Plugin: see http://www.kryogenix.org/code/browser/labelify/
				// Activate help-texts for input-fields
				$("input:text").labelify({ labelledClass: "inputHelp" });
				
				//Login
	    		$("#login").click(function(){
	    			$("#login-box").slideToggle(200);
	    			$("#username").focus();

	    		});
	    		
	    		// Slider
	    		$('#slides').slides({
	    			preload: true,
	    			preloadImage: '${resource(dir:'/images',file:'loading.gif')}',
	    			generatePagination: true,
	    			play: 7000,
	    			pause: 5000,
	    			hoverPause: true,
	    		});	
	    		// tooltip
	    		$('.tooltip').tipsy({gravity: $.fn.tipsy.autoNS, live:true});

				
	
			});
	        var focusQueryInput = function() {
	            document.getElementById("q").focus();
	        }

	     	
	    </script>
	    <sec:ifLoggedIn>
	    	<script type="text/javascript">

	    		/*$.PeriodicalUpdater('/notification/checkForNotifications', { method: 'post', // method; get or post 
		    		data: '', // array of values to be passed to the page - e.g. {name: "John", greeting: "hello"} 
		    		minTimeout: 1000, // starting value for the timeout in milliseconds 
		    		maxTimeout: 50000, // maximum length of time between requests 
		    		multiplier: 3, // the amount to expand the timeout by if the response hasn't changed (up to maxTimeout) 
		    		type: 'text', // response type - text, xml, json, etc. See $.ajax config options 
		    		maxCalls: 0, // maximum number of calls. 0 = no limit. 
		    		autoStop: 500 // automatically stop requests after this many returns of the same data. 0 = disabled. 
		    	}, function(remoteData, success, xhr, handle) { // Process the new data (only called when there was a change) 
					if(success != 'notmodified') {
						$('#notification_icon').addClass('notify_pending');
						$('#notification_icon').html(remoteData);
						$('#notification_icon').effect('highlight', {}, 2000);
					}	
					
			    });	*/

	    		//$('#notification_icon').click(function(){
				//	alert('ok');
		    	//});
			    
	    		function showNotification(textdata){
	    			$('#notification_icon').removeClass('notify_pending');
					$('#notification_icon').html('!');

					// für den fall eines manuellen aufrufs.
					if (!textdata){
						textdata = 'Keine neuen Benachrichtigungen: <a href="/dashboard" title="Dashboard">Dashboard aufrufen</a>';
					}
					//alert(textdata);
					if(!$('div.ui-pnotify').is(':visible'))
						$.pnotify({pnotify_title:'Info',pnotify_text:textdata,pnotify_notice_icon: 'ui-icon ui-icon-mail-closed' });
				}
	    	
			</script>
	    </sec:ifLoggedIn>
	    <g:javascript library="application" />
	    <g:layoutHead />
	</head>
<body>
	<g:if test="${flash.message}">
		<div id="flashmessage">
			<p><g:message code="${flash.message}" args="${flash.args}" default="${flash.message}"/></p>			
		</div>
	</g:if>

	<div id="wrapper">
		<div id="header">
			<div id="logo">
				<g:link mapping="index">
					<img src="${resource(dir:'images',file:'oWrks-logo.png')}"
						alt="oWrks" />
				</g:link>
			</div>
			<g:render template="../navigation/startNav" />
		
		</div>
		
		<div id="spinner" class="spinner" style="display: none; position:absolute;z-index:120; left:50%;">
			<img src="${resource(dir:'images',file:'spinner.gif')}"
				alt="${message(code:'spinner.alt',default:'Loading...')}" />
		</div>
		<sec:ifLoggedIn>
			<g:if test="${params.controller=='dashboard' || (params.controller=='profile' && (params.action == 'index' || currentUser.id.toString() == params.id.toString()) )|| params.controller == 'userMessage'
			 || params.action == 'changePassword' || params.controller == 'userNote' || (params.controller=='owo' && params.action =='myOwos') || (params.controller=='notificationSettings')}">
				<g:render template="../navigation/pleNav" />
			</g:if>
		</sec:ifLoggedIn>

		<g:if test="${((params.controller=='owo' && params.action !='list') || (params.controller == 'mission') || 
		(params.controller == 'step' && params.action !='list') || params.controller == 'performer' || params.controller == 'owoPost') 
		&& params.action !='myOwos'}">	
			<g:render template="../navigation/owoNav2" model="[owoInstance:owoInstance]" />
		</g:if>
		
		<div id="content">
			<g:layoutBody />
		</div>
		<div id="footer">
			&copy; 2011 oWrks - <g:link mapping="impressum"><g:message code="page.imprint" default="Imprint" /></g:link> - <g:link url="http://blog.owrks.com/">Blog</g:link><%-- <app:languageChooser />--%>
		</div>
		<sec:ifLoggedIn>
			<!--<g:javascript>
				function showFeedback(){
					if($('div#fbContent').is(':visible')) 
						$('#feedback').css({ left: '-180px'});
					else
						$('#feedback').css({ left: '10px'});
					$('div#fbContent').slideToggle('300');
				}
			</g:javascript>
			<div id="feedback">
				  <p id="showFeedback">
					<a href="#" onclick="showFeedback(); return false;">Feedback</a>
				</p>
				<div class="" id="fbContent">
					<g:render template="/feedback/create" />
				</div>
			</div>
				 -->
			<g:javascript>
  				var uvOptions = {};
  				(function() {
    				var uv = document.createElement('script'); uv.type = 'text/javascript'; uv.async = true;
   					uv.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'widget.uservoice.com/L1oqmoqA5GAxKgwaSKwDcw.js';
    				var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(uv, s);
  				})();
			</g:javascript>
		</sec:ifLoggedIn>
	</div>
</body>
</html>