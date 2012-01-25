<%@ page import="owrks.owo.OwoPost" %>
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
        <g:set var="entityName" value="${message(code: 'owoPost.label', default: 'OwoPost')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="box" style="border:none">
            <h2><g:message code="default.list.label" args="[entityName]" default="${entityName}" /></h2>
         </div>
         	
            <table>
                <thead>
                    <tr>
                    
                        <g:sortableColumn property="id" title="${message(code: 'owoPost.id.label', default: 'Id')}" />
                        <th><g:message code="owoPost.creator.label" default="Creator" /></th>
                        <g:sortableColumn property="content" title="${message(code: 'owoPost.content.label', default: 'Content')}" />
                        <g:sortableColumn property="dateCreated" title="${message(code: 'owoPost.dateCreated.label', default: 'Date Created')}" />
                        <g:sortableColumn property="lastUpdated" title="${message(code: 'owoPost.lastUpdated.label', default: 'Last Updated')}" />
                        <th><g:message code="owoPost.owo.label" default="Owo" /></th>
                        <th></th>
                    </tr>
                </thead>
                
                <g:each in="${owoPostInstanceList}" status="i" var="owoPostInstance">
                <div class="box owo-missions">
                    <ul class="${(i % 2) == 0 ? 'odd' : 'even'}" style="list-style-type: none;">
                    	
                        <li>${owoPostInstance.id}</li>
                        <li>${owoPostInstance.creator}</li>
                        <li>${owoPostInstance.content}</li>                    
                        <li><g:formatDate date="${owoPostInstance.dateCreated}" /></li>                    
                        <li><g:formatDate date="${owoPostInstance.lastUpdated}" /></li>
                        <li>${owoPostInstance.owo}</li>
                        
                        <li>
                        	<g:form id="${owoPostInstance.id}">
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
                <g:paginate total="${owoPostInstanceTotal}" />
            </div>
    </body>
</html>