<%@ page import="owrks.owo.Performer" %>
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
        <g:set var="entityName" value="${message(code: 'performer.label', default: 'Performer')}" />
        <title><g:message code="default.performer.title" /></title>
    </head>
    <body>
        <div>
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div>
            <h2><g:message code="performer.headline" /></h2>
            
            <g:render template="../error/beanErrors" model="[ entity: performerInstance ]" />
            
            <g:form action="save" >
				<form:fieldWrapper name="user">
	               	<g:select name="user.id" from="${owrks.user.User.list()}" optionKey="id" value="${performerInstance?.user.id}" noSelection="['null': '']" />
				</form:fieldWrapper>
				
				<form:fieldWrapper name="owo">
	               	<g:select name="owo.id" from="${owrks.owo.Owo.list()}" optionKey="id" value="${performerInstance?.owo?.id}"  />
				</form:fieldWrapper>
				
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>