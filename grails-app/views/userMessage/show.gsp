<%@ page import="owrks.user.UserMessage"%>
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
<g:set var="entityName"
	value="${message(code: 'userMessage.label', default: 'UserMessage')}" />
<title><g:message code="default.userMessage.title" />
</title>
<meta name="layout" content="main" />
<g:set var="receiver"
	value="${userMessageInstance.receiver.id == currentUser.id ? userMessageInstance.sender.id : userMessageInstance.receiver.id}" />
</head>
<body>
	<div class="box no-border">
		<h2>Nachricht</h2>
	
			<p><g:message code="userMessage.show.sender"
					args="[ userMessageInstance.sender.username ]" />
			</p>
			<p><g:message code="userMessage.show.dateCreated" args="[ userMessageInstance.dateCreated ]" /></p>
		
		<g:render template="../error/beanErrors" model="[ entity: userMessageInstance ]" />
		<div class="box message">
			
				${userMessageInstance?.message}
		
		</div>
		
			<g:if test="${userMessageInstance.sender.email != currentUser.email}">
				<p>
					<g:link controller="userMessage" action="create"
						id="${userMessageInstance.sender.username}" class="dialog button">
						<g:message code="default.list.button.reply" default="Antworten" />
					</g:link>
				</p>
			</g:if>
	</div>
</body>
</html>