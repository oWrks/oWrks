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
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div>
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>
             <table class="show">
                 <tbody>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepLink.id.label" default="Id" /></td>
                         
                         <td valign="top" class="value">${stepLinkInstance?.id}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepLink.creator.label" default="Creator" /></td>
                         
                         <td valign="top" class="value"><g:link controller="user" action="show" id="${stepLinkInstance?.creator?.id}">${stepLinkInstance?.creator?.encodeAsHTML()}</g:link></td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepLink.title.label" default="Title" /></td>
                         
                         <td valign="top" class="value">${stepLinkInstance?.title}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepLink.url.label" default="Url" /></td>
                         
                         <td valign="top" class="value">${stepLinkInstance?.url}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepLink.dateCreated.label" default="Date Created" /></td>
                         
                         <td valign="top" class="value"><g:formatDate date="${stepLinkInstance?.dateCreated}" /></td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepLink.lastUpdated.label" default="Last Updated" /></td>
                         
                         <td valign="top" class="value"><g:formatDate date="${stepLinkInstance?.lastUpdated}" /></td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="stepLink.step.label" default="Step" /></td>
                         
                         <td valign="top" class="value"><g:link controller="step" action="show" id="${stepLinkInstance?.step?.id}">${stepLinkInstance?.step?.encodeAsHTML()}</g:link></td>
                         
                     </tr>
                 
                 </tbody>
             </table>
        </div>
       	
	        <g:form>
	            <g:hiddenField name="id" value="${stepLinkInstance?.id}" />
	            <g:actionSubmit class="edit button left" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	            <g:actionSubmit class="delete" action="delete button right" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	        </g:form>
    </body>
</html>