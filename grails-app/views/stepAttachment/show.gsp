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
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div>
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>
             <table class="show">
                 <tbody>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepAttachment.id.label" default="Id" /></td>
                         
                         <td valign="top" class="value">${stepAttachmentInstance?.id}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepAttachment.title.label" default="Title" /></td>
                         
                         <td valign="top" class="value">${stepAttachmentInstance?.title}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepAttachment.filename.label" default="Filename" /></td>
                         
                         <td valign="top" class="value">${stepAttachmentInstance?.filename}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepAttachment.contentType.label" default="Content Type" /></td>
                         
                         <td valign="top" class="value">${stepAttachmentInstance?.contentType}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepAttachment.filesize.label" default="Filesize" /></td>
                         
                         <td valign="top" class="value">${stepAttachmentInstance?.filesize}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepAttachment.dateCreated.label" default="Date Created" /></td>
                         
                         <td valign="top" class="value"><g:formatDate date="${stepAttachmentInstance?.dateCreated}" /></td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepAttachment.lastUpdated.label" default="Last Updated" /></td>
                         
                         <td valign="top" class="value"><g:formatDate date="${stepAttachmentInstance?.lastUpdated}" /></td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepAttachment.step.label" default="Step" /></td>
                         
                         <td valign="top" class="value"><g:link controller="step" action="show" id="${stepAttachmentInstance?.step?.id}">${stepAttachmentInstance?.step?.encodeAsHTML()}</g:link></td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepAttachment.uploader.label" default="Uploader" /></td>
                         
                         <td valign="top" class="value"><g:link controller="user" action="show" id="${stepAttachmentInstance?.uploader?.id}">${stepAttachmentInstance?.uploader?.encodeAsHTML()}</g:link></td>
                         
                     </tr>
                 
                 </tbody>
             </table>
        </div>
       	
	        <g:form>
	            <g:hiddenField name="id" value="${stepAttachmentInstance?.id}" />
	            <g:actionSubmit class="edit button left" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	            <g:actionSubmit class="delete" action="delete button right" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	        </g:form>
    </body>
</html>