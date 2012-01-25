<%@ page import="owrks.admin.Page" %>
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
        <g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div>
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>
             <table class="show">
                 <tbody>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="page.id.label" default="Id" /></td>
                         
                         <td valign="top" class="value">${pageInstance?.id}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="page.name.label" default="Name" /></td>
                         
                         <td valign="top" class="value">${pageInstance?.name}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="page.title.label" default="Title" /></td>
                         
                         <td valign="top" class="value">${pageInstance?.title}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="page.lastEditor.label" default="Last Editor" /></td>
                         
                         <td valign="top" class="value"><g:link controller="user" action="show" id="${pageInstance?.lastEditor?.id}">${pageInstance?.lastEditor?.encodeAsHTML()}</g:link></td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="page.content.label" default="Content" /></td>
                         
                         <td valign="top" class="value">${pageInstance?.content}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="page.creator.label" default="Creator" /></td>
                         
                         <td valign="top" class="value"><g:link controller="user" action="show" id="${pageInstance?.creator?.id}">${pageInstance?.creator?.encodeAsHTML()}</g:link></td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="page.dateCreated.label" default="Date Created" /></td>
                         
                         <td valign="top" class="value"><g:formatDate date="${pageInstance?.dateCreated}" /></td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="page.lastUpdated.label" default="Last Updated" /></td>
                         
                         <td valign="top" class="value"><g:formatDate date="${pageInstance?.lastUpdated}" /></td>
                         
                     </tr>
                 
                 </tbody>
             </table>
        </div>
       	<div class="buttons">
	        <g:form>
	            <g:hiddenField name="id" value="${pageInstance?.id}" />
	            <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	            <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	        </g:form>
	    </div>
    </body>
</html>