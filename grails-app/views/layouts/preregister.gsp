<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>oWrks - <g:layoutTitle default="oWrks" />
</title>
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
<link rel="stylesheet" type="text/css"
	href="${resource(dir: 'css', file: 'main.css')}" />
<link rel="shortcut icon"
	href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
<g:javascript library="jquery" plugin="jquery" />
<jqui:resources />
<g:javascript library="application" />
<app:controllerStyle />
<g:layoutHead />
<script src="${resource(dir: 'js', file: 'slides.min.jquery.js')}"
	type="text/javascript"></script>
<script type="text/javascript">
			$(document).ready(function(){
	    		// Slider
	    		$('#slides').slides({
	    			preload: true,
	    			preloadImage: '../images/loading.gif',
	    			generatePagination: true,
	    			play: 5000,
	    			pause: 2500,
	    			hoverPause: true,
	    		});	
			});					   
	    		
	    </script>
	   
</head>
<body>
	<div id="wrapper">
		<div id="header">
			<div id="logo">
				<img src="${resource(dir:'images',file:'oWrks-logo.png')}"
					alt="oWrks" />
			</div>
			<div class="right">
				<a href="http://oWrks.posterous.com" target="_blank"><img
					src="${resource(dir:'images',file:'rss.png')}" width="30"
					alt="oWrks abbonnieren" />
				</a> <a href="http://www.facebook.com/oWrks" target="_blank"><img
					src="${resource(dir:'images',file:'facebook.png')}" width="30"
					alt="oWrks auf Facebook" />
				</a> <a href="http://twitter.com/oWrks" target="_blank"><img
					src="${resource(dir:'images',file:'twitter.png')}" width="30"
					alt="Folge uns auf Twitter" />
				</a>
			</div>
		</div>

		<div class="clear"></div>

		<div id="content">
			<g:layoutBody />
		</div>
		<div class="clear"></div>

		<div id="footer">
			&copy; 2011 oWrks -
			<g:link mapping="impressum">
				<g:message code="page.imprint" default="Imprint" />
			</g:link> 
			- <g:link url="http://blog.owrks.com/" target="_blank">Blog</g:link>
			<%-- <app:languageChooser />--%>
		</div>

	</div>
</body>
</html>