<html>
	<head>
		<meta name="layout" content="main" />
		<title><g:message code="notFound.error" /></title>
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
	</head>
<body>
	<div class="box">
	
    <div class="error">
		<h2><g:message code="page.notFound.error.h2" /></h2>
		<p><g:message code="page.notFound.error.message" args="[ request.'javax.servlet.error.request_uri' ]" /></p>
	</div>
	<div>
		<p>Kein Grund zum Ärger. Vielleicht gibt es ja ein passendes oWo zu deinem Problem.</p>
		<g:render template="../index/owoTeaser" model="[ 'owos' : owos ]" />
	</div>
	</div>
</body>
</html>