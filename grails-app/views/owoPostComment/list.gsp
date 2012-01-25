<%@ page import="owrks.owo.OwoPostComment" %>
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
        <g:set var="entityName" value="${message(code: 'owoPostComment.label', default: 'OwoPostComment')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="box" style="border:none">
            <h2><g:message code="default.list.label" args="[entityName]" default="${entityName}" /></h2>
         </div>
         	
            <table>
                <thead>
                    <tr>
                    
                        <g:sortableColumn property="id" title="${message(code: 'owoPostComment.id.label', default: 'Id')}" />
                        <th><g:message code="owoPostComment.creator.label" default="Creator" /></th>
                        <g:sortableColumn property="content" title="${message(code: 'owoPostComment.content.label', default: 'Content')}" />
                        <g:sortableColumn property="dateCreated" title="${message(code: 'owoPostComment.dateCreated.label', default: 'Date Created')}" />
                        <g:sortableColumn property="lastUpdated" title="${message(code: 'owoPostComment.lastUpdated.label', default: 'Last Updated')}" />
                        <th><g:message code="owoPostComment.post.label" default="Post" /></th>
                        <th></th>
                    </tr>
                </thead>
                
                <g:each in="${owoPostCommentInstanceList}" status="i" var="owoPostCommentInstance">
                <div class="box owo-missions">
                    <ul class="${(i % 2) == 0 ? 'odd' : 'even'}" style="list-style-type: none;">
                    	
                        <li>${owoPostCommentInstance.id}</li>
                        <li>${owoPostCommentInstance.creator}</li>
                        <li>${owoPostCommentInstance.content}</li>                    
                        <li><g:formatDate date="${owoPostCommentInstance.dateCreated}" /></li>                    
                        <li><g:formatDate date="${owoPostCommentInstance.lastUpdated}" /></li>
                        <li>${owoPostCommentInstance.post}</li>
                        
                        <li>
                        	<g:form id="${owoPostCommentInstance.id}">
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
                <g:paginate total="${owoPostCommentInstanceTotal}" />
            </div>
    </body>
</html>