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


<%@ page import="owrks.notification.NotificationSettings" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'notificationSettings.label', default: 'NotificationSettings')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
    	<!-- <div id="breadcrumb">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div> -->
    	<div class="box">
	    	
	    	<h2 class="info-list-header"><g:message code="settings.headline" default="Settings" /></h2>
	    	
	    	<g:render template="/error/beanErrors" model="[ entity: notificationSettings ]" />
	        
	        <h3>Email Notification-Settings</h3>
	        
	        <div class="rounded">
		        <g:form controller="notificationSettings" action="update" id="${notificationSettings?.id}" >
								   
					<form:fieldWrapper name="owoNotifier">
		               	<g:checkBox name="owoNotifier" value="${notificationSettings?.owoNotifier}" />
					</form:fieldWrapper>
					<p><i>Owo-Benachrichtigungen, z.b. neuer Performer nimmt teil...</i></p>
					
					<form:fieldWrapper name="missionNotifier">
		               	<g:checkBox name="missionNotifier" value="${notificationSettings?.missionNotifier}" />
					</form:fieldWrapper>
					<p><i>Mission-Benachrichtigungen, z.b. neue Mission erstellt...</i></p>			   
					
					<form:fieldWrapper name="stepNotifier">
		               	<g:checkBox name="stepNotifier" value="${notificationSettings?.stepNotifier}" />
					</form:fieldWrapper>
		            <p><i>Step-Benachrichtigungen, z.b. neuer Step erstellt, Step zugewiesen an mich...</i></p>
		            
					<div class="clear"></div>
					<g:submitButton name="save" class="save button right" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<div class="clear"></div>
				</g:form>
			</div>
		</div>
    </body>
</html>