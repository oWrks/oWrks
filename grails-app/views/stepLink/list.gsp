<%@ page import="owrks.owo.StepLink" %>
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
        <g:set var="entityName" value="${message(code: 'stepLink.label', default: 'StepLink')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="box" style="border:none">
            <h2><g:message code="default.list.label" args="[entityName]" default="${entityName}" /></h2>
         </div>
         	
            <table>
                <thead>
                    <tr>
                    
                        <g:sortableColumn property="id" title="${message(code: 'stepLink.id.label', default: 'Id')}" />
                        <th><g:message code="stepLink.creator.label" default="Creator" /></th>
                        <g:sortableColumn property="title" title="${message(code: 'stepLink.title.label', default: 'Title')}" />
                        <g:sortableColumn property="url" title="${message(code: 'stepLink.url.label', default: 'Url')}" />
                        <g:sortableColumn property="dateCreated" title="${message(code: 'stepLink.dateCreated.label', default: 'Date Created')}" />
                        <g:sortableColumn property="lastUpdated" title="${message(code: 'stepLink.lastUpdated.label', default: 'Last Updated')}" />
                        <th></th>
                    </tr>
                </thead>
                
                <g:each in="${stepLinkInstanceList}" status="i" var="stepLinkInstance">
                <div class="box owo-missions">
                    <ul class="${(i % 2) == 0 ? 'odd' : 'even'}" style="list-style-type: none;">
                    	
                        <li>${stepLinkInstance.id}</li>
                        <li>${stepLinkInstance.creator}</li>
                        <li>${stepLinkInstance.title}</li>
                        <li>${stepLinkInstance.url}</li>                    
                        <li><g:formatDate date="${stepLinkInstance.dateCreated}" /></li>                    
                        <li><g:formatDate date="${stepLinkInstance.lastUpdated}" /></li>
                        
                        <li>
                        	<g:form id="${stepLinkInstance.id}">
                        		<g:actionSubmit action="show" value="${message(code: 'default.list.button.show', default: 'show')}" />
                        		<g:actionSubmit action="edit" value="${message(code: 'default.list.button.edit', default: 'edit')}" />
                        		<g:actionSubmit action="delete" value="${message(code: 'default.list.button.delete', default: 'delete')}" />
                        	</g:form>
                        </li>    
                	</ul>
                   </div>
                </g:each>
            </table>
            <div class="box" style="border:none">
                <g:paginate total="${stepLinkInstanceTotal}" />
            </div>
    </body>
</html>