
<%@ page import="owrks.user.User"%>
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
	value="${message(code: 'user.label', default: 'User')}" />
<title><g:message code="default.list.label" args="[entityName]" />
</title>
</head>
<body>
	<div class="box">
		<h2>
			<g:message code="default.list.label" args="[entityName]"
				default="${entityName}" />
		</h2>
		<table>
			<thead>
				<tr>
					<g:sortableColumn property="id" 
						title="${message(code: 'user.id.label', default: 'Id')}" />
					<g:sortableColumn property="username"
						title="${message(code: 'user.username.label', default: 'Username')}" />
					<th>${message(code: 'user.lastName.label', default: 'Name')}</th>
					<g:sortableColumn property="lastLogin"
						title="${message(code: 'user.firstName.label', default: 'Letzter Login')}" />
					<g:sortableColumn property="dateCreated"
						title="${message(code: 'user.dateCreated.label', default: 'Erstellt am')}" />
					<g:sortableColumn property="email"
						title="${message(code: 'user.email.label', default: 'E-Mail')}" />
					<th></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${userInstanceList}" status="i" var="userInstance">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

						<td>
							${userInstance.id}
						</td>

						<td><g:link controller="user" action="show"
								id="${userInstance.id}">
								${userInstance.username}
							</g:link>
						</td>
						<td>
							${userInstance.firstName} ${userInstance.lastName}
						</td>
						<td>
							<g:formatDate date="${userInstance.lastLogin}"
								format="hh:mm dd.MM.yyyy" />
						</td>
						<td><g:formatDate date="${userInstance.dateCreated}"
								format="dd.MM.yyyy" />
						</td>
						<td>
							${userInstance.email}
						</td>

						
                        <td>
                        	<g:form id="${userInstance.id}" action="delete" controller="user" style="width:auto !important;">
                        		<g:submitButton name="delete" value="${message(code: 'default.list.button.delete', default: 'delete')}"/>
                        	</g:form>
                        </td>
					</tr>
				</g:each>
			</tbody>
		</table>
		<div class="paginateButtons">
			<g:paginate total="${userInstanceTotal}" />
		</div>
	</div>
</body>
</html>