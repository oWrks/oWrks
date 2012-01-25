<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="de" xml:lang="de">
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
		<title>oWrks - <g:layoutTitle default="oWrks" /></title>
		<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
		<meta name="content-language" content="de" />
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
		<g:javascript library="application" />
		<app:controllerStyle />
		<g:layoutHead />
	</head>
<body>
	<div id="wrapper">
		<div id="header">
			<div id="logo">
				<g:link mapping="index">
					<img src="${resource(dir:'images',file:'oWrks-logo.png')}"
						alt="oWrks" />
				</g:link>
			</div>
		</div>
		
		<g:if test="${flash.message}">
			<div id="flashmessage">
				<p>Info: ${flash.defaultMessage} (${flash.message})</p>
			</div>
		</g:if>
		
		<div id="spinner" class="spinner" style="display: none;">
			<img src="${resource(dir:'images',file:'spinner.gif')}"
				alt="${message(code:'spinner.alt',default:'Loading...')}" />
		</div>
		
		<div id="content">
			<g:layoutBody />
		</div>
		
		<div id="footer">
			&copy; 2011 oWrks - <g:link mapping="impressum"><g:message code="page.imprint" default="Imprint" /></g:link>- <g:link mapping="about"><g:message code="page.about" default="About" /></g:link> <app:languageChooser />
		</div>
	</div>
</body>
</html>