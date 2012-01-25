<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><g:message code="dashboard.index.title" default="Mein AL" />
</title>
<meta name="layout" content="main" />
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
<jq:jquery>
			$(document).bind('reveal.facebox', function() {
		    	$("#receiverName").autocomplete({
	         		source: '${createLink(action: 'loadUsers')}' 
	       		});
		  	}); 
		</jq:jquery>
</head>

<body>
  <div class="box">
	<div class="col1">
		<h2 class="info-list-header">
			Dashboard für
			${currentUser}
		</h2>

		<p>Hier siehst du Deine persönliche oWrks-Übersicht. Du
			siehst, was gerade passiert und hast Zugriff auf häufige Funktionen.</p>

		<a name="user_notifications"></a>

		<h2 class="info-list-header">Benachrichtigungen für Dich</h2>
		<g:if test="${user_notifications.size()}">
			<ul>
				<g:each in="${user_notifications}" var="notification">
					<li><notify:notification notification="${notification}" />
					<li>
				</g:each>
			</ul>

		</g:if>
		<g:else>
			<p>Derzeit gibt es keine Benachrichtigungen für Dich.</p>
		</g:else>

		<a name="platformNotifications"></a>
	
		<h2 class="info-list-header">Aktivität auf oWrks</h2>

		<g:if test="${platformNotifications.size()}">
			<ul>
				<g:each in="${platformNotifications}" var="notification">
					<li><notify:notification notification="${notification}" />
					<li>
				</g:each>
			</ul>
		</g:if>
		<g:else>
			<p>Derzeit gibt es keine Aktivität auf der Plattform.</p>
		</g:else>
	</div>
	<div class="col2">
		<div class="box info-box">
			<h2 class="box-header">Quicklinks</h2>
			<p><g:link controller="owo" action="create" class="">oWo erstellen</g:link></p>
			<p>
				<input type="hidden" name="loadUsers" id="loadUsers"
					value="${createLink(action: 'loadUsers')}" />
				<g:link controller="userMessage" action="create" class="dialog">Nachricht erstellen</g:link>
			</p>
			<p><g:link controller="owo" action="list" class="">alle oWos ansehen</g:link></p>
		</div>
	</div>
	<div class="clear"></div>
  </div>
</body>
</html>