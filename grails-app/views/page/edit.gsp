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
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
    	<div>
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
    	
    	<g:render template="../error/beanErrors" model="[ entity: pageInstance ]" />
        
        <h2><g:message code="default.edit.label" args="[entityName]" /></h2>
        
        <g:form id="${pageInstance?.id}" >
						   
			<form:fieldWrapper name="name">
               	<g:textField name="name" value="${pageInstance?.name}" />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="title">
               	<g:textField name="title" value="${pageInstance?.title}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="content">
               	<g:textField name="content" value="${pageInstance?.content}" />
			</form:fieldWrapper>
						   
            <div class="buttons">
				<span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
			</div>
		</g:form>
    </body>
</html>