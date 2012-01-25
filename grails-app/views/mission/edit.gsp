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
		<g:set var="entityName" value="${message(code: 'mission.label', default: 'Mission')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
		<g:javascript>
		        $(document).ready(function(){		       		
		    		$( "#startDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
		    		$( "#endDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
		        });
		</g:javascript>
	</head>
	<body>
		<div class="box">
			<h2><g:message code="default.edit.label" args="[entityName]" /></h2>
	
			<g:render template="../error/beanErrors" model="[ entity: missionInstance ]" />
	
			<g:form id="${missionInstance?.id}">
				<form:fieldWrapper name="title">
					<g:textArea name="title" cols="40" rows="5"
						value="${missionInstance?.title}" />
				</form:fieldWrapper>
	
				<form:fieldWrapper name="description">
					<g:textField name="description"
						value="${missionInstance?.description}" />
				</form:fieldWrapper>
	
				<form:fieldWrapper name="startDate">
					<input type="text" name="startDate" id="startDate"
						value="${(missionInstance?.startDate ? formatDate(format: 'dd.MM.yyyy', date: missionInstance?.startDate) : formatDate(format: 'dd.MM.yyyy', date: new Date()))}" />
				</form:fieldWrapper>
	
				<form:fieldWrapper name="endDate">
					<input type="text" name="endDate" id="endDate"
						value="${(missionInstance?.endDate ? formatDate(format: 'dd.MM.yyyy', date: missionInstance?.endDate) : formatDate(format: 'dd.MM.yyyy', date: new Date()))}" />
				</form:fieldWrapper>				
				
				<%-- 
				<form:fieldWrapper name="steps">
					<ul>
						<g:each in="${missionInstance?.steps?}" var="s">
							<li><g:link controller="step" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
						</g:each>
					</ul>
					<g:link controller="step" action="create" params="['mission.id': entity?.id]">${message(code: 'default.add.label', args: [message(code: 'step.label', default: 'Step')])}</g:link>
				</form:fieldWrapper>--%>
				
				<div class="clear"></div>
				
				<g:actionSubmit class="save" class="button left" action="update"
					value="${message(code: 'default.button.update.label', default: 'Update')}" />
					
				<g:actionSubmit class="delete" class="button right" action="delete"
					value="${message(code: 'default.button.delete.label', default: 'Delete')}"
					onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					
				<div class="clear"></div>
			</g:form>
		</div>
	</body>
</html>