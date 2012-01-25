<%@ page import="owrks.owo.Step"%>
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
	value="${message(code: 'step.label', default: 'Step')}" />
<title><g:message code="default.show.label" args="[entityName]" />
</title>
</head>
<body>
	<div class="box">
		<h2>
			Step:
			${stepInstance?.title}
			<al:stepEditLink step="${stepInstance}">edit</al:stepEditLink>
		</h2>

		<p>
			<g:message code="step.description.label" default="Beschreibung" />
			:
			${stepInstance?.description}

			<br />
			<g:message code="step.assignedTo.label" default="Verantwortlich" />
			:
			<g:link controller="user" action="show"
				id="${stepInstance?.assignedTo?.id}">
				${stepInstance?.assignedTo?.encodeAsHTML()}
			</g:link>

			<br />
			<g:message code="step.mission.label" default="Mission" />
			:
			<g:link controller="mission" action="show"
				id="${stepInstance?.mission?.id}">
				${stepInstance?.mission?.encodeAsHTML()}
			</g:link>
			
			<br />
			<g:message code="step.dueDate.label" default="Enddatum" />
			:
			<g:formatDate date="${stepInstance?.dueDate}" format="dd.MM.yyyy" />
		<p>
			<g:message code="step.lastUpdated.label" default="Letzte Änderung" />
			:
			<g:formatDate date="${stepInstance?.lastUpdated}" format="dd.MM.yyyy" />

			<br />
			<g:message code="step.published.label" default="Veröffentlicht" />
			:
			<g:formatBoolean boolean="${stepInstance?.published}" />
			<br />
			<g:message code="step.finished.label" default="Abgeschlossen" />
			:
			<g:formatBoolean boolean="${stepInstance?.finished}" />
		</p>

		<g:form>
			<g:link controller="mission" action="show" class="button"
				id="${stepInstance?.mission?.id}">zur Mission</g:link>
		</g:form>
	</div>
</body>
</html>