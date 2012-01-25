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
		<script src="${resource(dir: 'js/uniform', file: 'jquery.uniform.js')}"
			type="text/javascript"></script>
		<link
			href="${resource(dir: 'css/uniform', file: 'uniform.default.css')}"
			media="screen" rel="stylesheet" type="text/css" />
			
		<g:javascript>
		    $(document).ready(function(){
		        $("select, input:checkbox").uniform();
		    });
		</g:javascript>
	</head>
	<body>
	<div class="box">
		
		<g:render template="requestsAndPerformers" model="['performerUserList':performerUserList, 'requestingUsers':requestingUsers, 'owoInstance': owoInstance]"></g:render>
		
		<div class="clear"></div>
		
		<g:render template="inviteAgilers" model="['notInvitedUsers':notInvitedUsers, 'invitedUsers':invitedUsers]"></g:render>
		
		<div class="clear"></div>
		
		
		<!-- EMAIL EINLADUNGEN -->
		<a name="inviteByEmail"></a>
		<div class="col50percent">
			<div class="box info-box">
				<h2 class="box-header">
					Lade Freunde per E-Mail zu diesem oWo ein
					</h2>
					<g:render template="inviteByEmail" model="['owoInstance': owoInstance]" />
				
			</div>
		</div>
		<div class="col50percent">
			<div class="box info-box">
				<h2 class="box-header">Einladungen per E-Mail</h2>
				<ul id="invitedEmails">
					<g:if test="${userInvitations.size() == 0}">
						Keine Teilnehmer per Email eingeladen.
					</g:if>
					<g:render template="invitedByEmail" model="['userInvitations': userInvitations]" />
				</ul>
			
			</div>
		</div>
		<div class="clear"></div>
		</div>
	</body>
</html>