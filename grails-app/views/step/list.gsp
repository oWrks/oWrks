<%@ page import="owrks.owo.Step" %>
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
        <g:set var="entityName" value="${message(code: 'step.label', default: 'Step')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="box">
            <h2><g:message code="default.list.label" args="[entityName]" default="${entityName}" /></h2>
            <table>
                <thead>
                    <tr>
                        <%--<g:sortableColumn property="id" title="${message(code: 'step.id.label', default: 'Id')}" />
                        --%>
                        <g:sortableColumn property="title" title="${message(code: 'step.title.label', default: 'Titel')}" />
                        <g:sortableColumn property="description" title="${message(code: 'step.description.label', default: 'Beschreibung')}" />
                        <g:sortableColumn property="missionOwo" title="${message(code: 'owo.label', default: 'oWo')}" />
                        <g:sortableColumn property="createdBy" title="${message(code: 'step.createdBy.label', default: 'Ersteller')}" />
                    </tr>
                </thead>
                <tbody>
                <g:each in="${stepInstanceList}" status="i" var="stepInstance">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                    	<%--
                        <td>${stepInstance.id}</td>
                        --%>
                        <td><g:link uri="/owo/missions/${stepInstance.mission.owo.id}/#/step/wall/${stepInstance.id}">${stepInstance.title}</g:link></td>
                        <td>${stepInstance.description}</td>                    
                        <td><g:link controller="owo" action="show" id="${stepInstance.mission.owo.id}">${stepInstance.mission.owo}</g:link></td>
                        <td>
                        	<g:if test="${stepInstance.createdBy}">
                        		<g:link controller="profile" id="${stepInstance.createdBy.id}">${stepInstance.createdBy}</g:link>
                        	</g:if>
                        </td>
                        	
                        <%--
                        <td>
                        	<g:link action="show" id="${stepInstance.id}"><g:message code="default.list.button.show" default="show" /></g:link>
                        	<g:link action="edit" id="${stepInstance.id}"><g:message code="default.list.button.edit" default="edit" /></g:link>
                        	<g:link action="delete" id="${stepInstance.id}"><g:message code="default.list.button.delete" default="delete" /></g:link>
                        </td>
                    --%></tr>
                </g:each>
                </tbody>
            </table>
            <div class="paginateButtons">
                <g:paginate total="${stepInstanceTotal}" />
            </div>
        </div>
    </body>
</html>