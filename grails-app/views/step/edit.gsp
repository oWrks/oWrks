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
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:javascript>
           	$(document).ready(function(){
    			$( "#dueDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
           	})
       </g:javascript>
    </head>
    <body>
    	<div class="box">
	    	<g:render template="../error/beanErrors" model="[ entity: stepInstance ]" />
	        
	        <h2><g:message code="default.edit.label" args="[entityName]" /></h2>
	        
	        <g:form action="update" id="${stepInstance?.id}" >
	             
				<form:fieldWrapper name="title">
	               	<g:textField name="title" value="${stepInstance?.title}" size="60" />
				</form:fieldWrapper>
				<form:fieldWrapper name="dueDate">
	               			<input type="text" name="dueDate" id="dueDate"
						value="${(stepInstance?.dueDate ? formatDate(format: 'dd.MM.yyyy', date: stepInstance?.dueDate) : formatDate(format: 'dd.MM.yyyy', date: new Date()))}" />
				</form:fieldWrapper>
				<form:fieldWrapper name="assignedTo">
	               	<g:select name="assignedTo.id" from="${performers?.user}" optionKey="id" value="${stepInstance?.assignedTo?.id}" noSelection="['' : 'Performer auswählen']"  />
				</form:fieldWrapper>
				<form:fieldWrapper name="published">
	               	<g:checkBox name="published" value="${stepInstance?.published}" />
				</form:fieldWrapper>
				<form:fieldWrapper name="finished">
	               	<g:checkBox name="finished" value="${stepInstance?.finished}" />
				</form:fieldWrapper>
	            
				<div class="clear"></div>
				<g:actionSubmit class="save button left" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				<div class="clear"></div>
			</g:form>
		</div>
    </body>
</html>