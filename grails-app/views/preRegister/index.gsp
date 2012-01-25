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
		<title>Welcome to oWrks</title>
		<meta name="layout" content="preregister" />
	</head>
<body>
	<div class="box">
		<div class="col50percent">
	  		<img class="left" src="${resource(dir:'images',file:'slide-pic.png')}" alt="oWrks" width="350"/>
	  	</div>
	  	<div class="col50percent">
			<h2>Eure Arbeit im richtigen Flow</h2>
			<p>oWrks bietet eine agile Umgebung für Online-Workshops Eurer Projekt-, Lern- oder Arbeitsgruppen. Bald starten wir die Beta-Phase. Wer oWrks als Erstes testen möchte, kann hier seine E-Mail-Adresse hinterlegen.</p> 
			<g:form class="normal left" action="save">
				<input type="text" name="email" value="your@email.com"/>
				<input type="submit" name="submit" class="button" value="Ich mach mit!" />
			</g:form>
			<div class="clear"></div>
			<g:if test="${params.success}">
				<p class="normal">Danke für Dein Interesse!</p>
			</g:if>
			<g:else>
				<g:hasErrors bean="${entity}">
					<g:renderErrors bean="${entity}" />
				</g:hasErrors>
			</g:else>
		</div>
		<div class="clear"></div>
	</div>
</body>
</html>