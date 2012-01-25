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
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    	<div>
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="list">
            <h2><g:message code="default.list.label" args="[entityName]" default="${entityName}" /></h2>
            <table>
                <thead>
                    <tr>
                    
                        <g:sortableColumn property="id" title="${message(code: 'userMessage.id.label', default: 'Id')}" />
                        <g:sortableColumn property="message" title="${message(code: 'userMessage.message.label', default: 'Message')}" />
                        <th><g:message code="userMessage.sender.label" default="Sender" /></th>
                        <th><g:message code="userMessage.receiver.label" default="Receiver" /></th>
                        <g:sortableColumn property="dateCreated" title="${message(code: 'userMessage.dateCreated.label', default: 'Date Created')}" />
                        <g:sortableColumn property="lastUpdated" title="${message(code: 'userMessage.lastUpdated.label', default: 'Last Updated')}" />
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                <g:each in="${userMessageInstanceList}" status="i" var="userMessageInstance">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                    	
                        <td>${userMessageInstance.id}</td>
                        <td>${userMessageInstance.message}</td>
                        <td>${userMessageInstance.sender}</td>
                        <td>${userMessageInstance.receiver}</td>                    
                        <td><g:formatDate date="${userMessageInstance.dateCreated}" /></td>                    
                        <td><g:formatDate date="${userMessageInstance.lastUpdated}" /></td>
                        <td>
                        	<g:form id="${userMessageInstance.id}">
                        		<g:actionSubmit action="show" value="${message(code: 'default.list.button.show', default: 'show')}" />
                        		<g:actionSubmit action="edit" value="${message(code: 'default.list.button.edit', default: 'edit')}" />
                        		<g:actionSubmit action="delete" value="${message(code: 'default.list.button.delete', default: 'delete')}" />
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
    </body>
</html>