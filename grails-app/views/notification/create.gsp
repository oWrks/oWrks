<%@ page import="owrks.notification.Notification" %>
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
        <g:set var="entityName" value="${message(code: 'notification.label', default: 'Notification')}" />
        <title><g:message code="default.notification.title" /></title>
    </head>
    <body>
        <!--  <div id="breadcrumb">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>-->
        <div class="box">
            <h2><g:message code="notification.headline" /></h2>
            
            <g:render template="../error/beanErrors" model="[ entity: notificationInstance ]" />
            
            <g:form action="save" >
	             
				
				<form:fieldWrapper name="action">
	               	<g:textField name="action" value="${notificationInstance?.action}" />
				</form:fieldWrapper>
				
				
				<form:fieldWrapper name="actionByUser">
	               	<g:select name="actionByUser.id" from="${owrks.user.User.list()}" optionKey="id" value="${notificationInstance?.actionByUser?.id}"  />
				</form:fieldWrapper>
				
				
				<form:fieldWrapper name="affectedUser">
	               	<g:select name="affectedUser.id" from="${owrks.user.User.list()}" optionKey="id" value="${notificationInstance?.affectedUser?.id}"  />
				</form:fieldWrapper>
				
				
				<form:fieldWrapper name="mission">
	               	<g:select name="mission.id" from="${owrks.owo.Mission.list()}" optionKey="id" value="${notificationInstance?.mission?.id}"  />
				</form:fieldWrapper>
				
				
				<form:fieldWrapper name="owo">
	               	<g:select name="owo.id" from="${owrks.owo.Owo.list()}" optionKey="id" value="${notificationInstance?.owo?.id}"  />
				</form:fieldWrapper>
				
				
				<form:fieldWrapper name="sighted">
	               	<g:checkBox name="sighted" value="${notificationInstance?.sighted}" />
				</form:fieldWrapper>
				
				
				<form:fieldWrapper name="step">
	               	<g:select name="step.id" from="${owrks.owo.Step.list()}" optionKey="id" value="${notificationInstance?.step?.id}"  />
				</form:fieldWrapper>
				
				<div class="clear"></div>
                <g:submitButton name="create" class="save button right" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				<div class="clear"></div>
            </g:form>
        </div>
    </body>
</html>