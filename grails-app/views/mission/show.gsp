
<%@ page import="owrks.owo.Mission"%>
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
<g:set var="entityName"
	value="${message(code: 'mission.label', default: 'Mission')}" />
<title><g:message code="default.show.label" args="[entityName]" />
</title>
</head>
<body>
	<div class="box">
		<h2>
			Mission:
			${missionInstance?.title}
			<al:missionEditLink step="${missionInstance}">edit</al:missionEditLink>
		</h2>
		<p>
			<g:message code="mission.description.label" default="Beschreibung" />
			:
			${missionInstance?.description}

			<br />
			<g:message code="mission.creator.label" default="Ersteller" />
			:
			<g:link controller="user" action="show"
				id="${missionInstance?.creator?.id}">
				${missionInstance?.creator?.encodeAsHTML()}
			</g:link>
			
			<br />
			<g:message code="mission.startDate.label" default="Anfangsdatum" />
			:
			<g:formatDate date="${missionInstance?.startDate}" format="dd.MM.yyyy" />
			
			<br />
			<g:message code="mission.endDate.label" default="Enddatum" />
			:
			<g:formatDate date="${missionInstance?.endDate}" format="dd.MM.yyyy" />
		<p>
			<g:message code="mission.lastUpdated.label" default="Letzte Änderung" />
			:
			<g:formatDate date="${missionInstance?.lastUpdated}" format="dd.MM.yyyy" />

		</p>
		<ul style="list-style-type: none;">
			<g:render template="stepList" model="[mission: missionInstance]"></g:render>
			<li>
				<al:stepCreateLink mission="${mission}">Step hinzufügen</al:stepCreateLink>
			</li>
		</ul>
		<g:form>
			<g:link controller="owo" action="missions" class="button"
				id="${missionInstance?.owo?.id}">alle Missions</g:link>
		</g:form>

	</div>
</body>
</html>