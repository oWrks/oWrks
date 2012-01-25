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
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
    	<!-- <div id="breadcrumb">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div> -->
    	<div class="box">
    	<g:render template="../error/beanErrors" model="[ entity: stepAttachmentInstance ]" />
        
        <h2><g:message code="default.edit.label" args="[entityName]" /></h2>
        
        <g:form id="${stepAttachmentInstance?.id}" >
             
						   
			<form:fieldWrapper name="title">
               	<g:textField name="title" value="${stepAttachmentInstance?.title}" />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="filename">
               	<g:textField name="filename" value="${stepAttachmentInstance?.filename}" />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="contentType">
               	<g:textField name="contentType" value="${stepAttachmentInstance?.contentType}" />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="filesize">
               	<g:textField name="filesize" value="${fieldValue(bean: entity, field: 'filesize')}" />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="step">
               	<g:select name="step.id" from="${owrks.owo.Step.list()}" optionKey="id" value="${stepAttachmentInstance?.step?.id}"  />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="uploader">
               	<g:select name="uploader.id" from="${owrks.user.User.list()}" optionKey="id" value="${stepAttachmentInstance?.uploader?.id}"  />
			</form:fieldWrapper>
            
			<div class="clear"></div>
			<g:actionSubmit class="save button left" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
            <g:actionSubmit class="delete button right" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
			<div class="clear"></div>
		</g:form>
		</div>
    </body>
</html>