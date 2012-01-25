<%@ page import="owrks.user.User"%>
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
<g:set var="entityName"
	value="${message(code: 'user.label', default: 'User')}" />
<title><g:message code="default.show.label" args="[entityName]" />
</title>
</head>
<body>
	<div class="box">
		<h2>
			User:
			${userInstance?.username}
		</h2>

		<p>
			<g:message code="user.email.label" default="E-Mail" />
			:
			${userInstance?.email}

			<br />
			<g:message code="user.firstName.label" default="Vorname" />
			:
			${userInstance?.firstName} 
			
			<br />
			<g:message code="user.lastName.label" default="Nachname" />
			:
			${userInstance?.lastName}

	</div>
</body>
</html>