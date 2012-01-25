<%@ page import="owrks.owo.StepAttachment" %>
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
        <g:set var="entityName" value="${message(code: 'stepAttachment.label', default: 'StepAttachment')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="box" style="border:none">
            <h2><g:message code="default.list.label" args="[entityName]" default="${entityName}" /></h2>
         </div>
         	
            <table>
                <thead>
                    <tr>
                    
                        <g:sortableColumn property="id" title="${message(code: 'stepAttachment.id.label', default: 'Id')}" />
                        <g:sortableColumn property="title" title="${message(code: 'stepAttachment.title.label', default: 'Title')}" />
                        <g:sortableColumn property="filename" title="${message(code: 'stepAttachment.filename.label', default: 'Filename')}" />
                        <g:sortableColumn property="contentType" title="${message(code: 'stepAttachment.contentType.label', default: 'Content Type')}" />
                        <g:sortableColumn property="filesize" title="${message(code: 'stepAttachment.filesize.label', default: 'Filesize')}" />
                        <g:sortableColumn property="dateCreated" title="${message(code: 'stepAttachment.dateCreated.label', default: 'Date Created')}" />
                        <th></th>
                    </tr>
                </thead>
                
                <g:each in="${stepAttachmentInstanceList}" status="i" var="stepAttachmentInstance">
                <div class="box owo-missions">
                    <ul class="${(i % 2) == 0 ? 'odd' : 'even'}" style="list-style-type: none;">
                    	
                        <li>${stepAttachmentInstance.id}</li>
                        <li>${stepAttachmentInstance.title}</li>
                        <li>${stepAttachmentInstance.filename}</li>
                        <li>${stepAttachmentInstance.contentType}</li>
                        <li>${stepAttachmentInstance.filesize}</li>                    
                        <li><g:formatDate date="${stepAttachmentInstance.dateCreated}" /></li>
                        
                        <li>
                        	<g:form id="${stepAttachmentInstance.id}">
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
                <g:paginate total="${stepAttachmentInstanceTotal}" />
            </div>
    </body>
</html>