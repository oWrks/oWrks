<%@ page import="owrks.user.UserMessage" %>
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
        <g:set var="entityName" value="${message(code: 'userMessage.label', default: 'UserMessage')}" />
        <title><g:message code="default.userMessage.title" /></title>
        <g:javascript>
           	$(document).ready(function(){
           		$("#receiverName").autocomplete({
	         		source: '${createLink(action: 'loadUsers')}' 
	       		});
           	})
       </g:javascript>
    </head>
    <body>
        <div class="box messageCreate"> 
            <h2><g:message code="userMessage.create.h2" /></h2>
            
            <g:render template="../error/beanErrors" model="[ entity: userMessageInstance ]" />
            
            <g:form action="save" >
				
				<g:if test="${receiverName}" >
					<input type="hidden" name="receiverName" value="${receiverName}" />
				</g:if>
				<g:else>
					<form:fieldWrapper name="receiverName">
	               		<g:textField name="receiverName" id="receiverName" value="${userMessageInstance?.receiver?.username}" />
					</form:fieldWrapper>
				</g:else>
				
				<form:fieldWrapper name="message">
	               	<g:textArea name="message" value="${userMessageInstance?.message}" />
				</form:fieldWrapper>
				<div class="clear"></div>
				
                <g:submitButton name="create" class="save button" value="${message(code: 'userMessage.button.create.label', default: 'Senden')}" />
               
            </g:form>
        </div>
    </body>
</html>