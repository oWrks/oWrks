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
        <g:set var="entityName" value="${message(code: 'userMessage.label', default: 'Inbox')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
		<jq:jquery>
			$('a.dialog').click(function(){
				if($(this).parent().parent('tr').hasClass('notRead')) {
					var count = parseInt($('a[href=/inbox] span').text().replace('(', '').replace(')', '')) -1;
					$('a[href=/inbox] span').text("("+count+")");
					$(this).parent().parent('tr').removeClass('notRead');
				}
			});
			$(document).bind('reveal.facebox', function() {
		    	$("#receiverName").autocomplete({
	         		source: '${createLink(action: 'loadUsers')}' 
	       		});
		  	}); 
		</jq:jquery>
    </head>
    <body>
    <div class="box">
    <div class="col1">
        <div class="list">
            <h2 class="info-list-header"><g:message code="userMessage.inbox.label" default="Posteingang" /></h2>
            <table>
                <thead>
                    <tr>
                        <g:sortableColumn property="sender" title="${message(code: 'userMessage.sender.label', default: 'Von')}" />
                        <g:sortableColumn property="message" title="${message(code: 'userMessage.message.label', default: 'Nachricht')}" />
                        <g:sortableColumn property="dateCreated" title="${message(code: 'userMessage.dateCreated.label', default: 'Datum')}" />
                 
                    </tr>
                </thead>
                <tbody>
                <g:each in="${userMessageInstanceList}" status="i" var="userMessageInstance">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'} ${!userMessageInstance.readFlag ? 'notRead' : ''}">
                    	
                        <td><g:link mapping="profile" id="${userMessageInstance.sender.id}">${userMessageInstance.sender}</g:link></td>
                        <td>
                        	<g:link controller="userMessage" action="show" id="${userMessageInstance.id}" class="dialog">
	                        	<g:if test="${userMessageInstance.message.length() > 20}">
	                        		${userMessageInstance.message[0..20]}
	                        	</g:if>
	                        	<g:else>
	                        		${userMessageInstance.message}
	                        	</g:else>
                        	</g:link>
                        </td>
                        <td><g:formatDate date="${userMessageInstance.dateCreated}" type="datetime" style="LONG" timeStyle="SHORT"  /></td>                    
                        <td>
                        	<g:form id="${userMessageInstance.id}">
                        		<g:actionSubmit action="delete" class="delete" value="${message(code: 'default.list.button.delete', default: 'delete')}" />
                        	</g:form>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <div class="paginateButtons">
                <g:paginate total="${userMessageInstanceTotal}" />
            </div>
        </div>
        </div>
        <div class="col2">
        	<div class="box info-box">
        	<h2 class="box-header">Mailbox</h2>
        		<ul style="list-style-type: none;">
        			<li><g:link mapping="inbox">Posteingang</g:link></li>
        			<li><g:link mapping="outbox">Postausgang</g:link></li>
        			<li>
        				<input type="hidden" name="loadUsers" id="loadUsers" value="${createLink(action: 'loadUsers')}" />
        				<g:link controller="userMessage" action="create" class="dialog">Nachricht erstellen</g:link></li>
        		</ul>
        	</div>
        </div>
        <div class="clear"></div>
        </div>
        
    </body>
</html>