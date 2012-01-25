<%@ page import="owrks.owo.Owo"%>
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
		value="${message(code: 'owo.label', default: 'Owo')}" />
	<title><g:message code="default.create.label"
			args="[entityName]" />
	</title>

	</head>
	<body>
		<div class="col50percent">
			<div class="box info-box">
			<h2 class="box-header"><g:message code="owo.create.step3.label" args="[entityName]" default="Freunde per E-Mail einladen" /></h2>

			<g:render template="inviteByEmail" model="['owoInstance': owoInstance]" />
				
			</div>
		</div>
		<div class="col50percent">
			<div class="box info-box">
				<h2 class="box-header">per E-Mail</h2>
				<ul id="invitedEmails">
					<g:if test="${userInvitations.size() == 0}">
						Keine Teilnehmer per Email eingeladen.
					</g:if>
					<g:render template="invitedByEmail" model="['userInvitations': userInvitations]" />
				</ul>
			</div>
			<g:link controller="owo" action="show" id="${owoInstance?.id}" class="button right" style="margin-right: 20px;">Fertigstellen</g:link>		
		</div>
	</body>
</html>