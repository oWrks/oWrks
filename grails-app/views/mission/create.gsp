<%@ page import="owrks.owo.Mission" %>
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
        <title><g:message code="default.mission.title" /></title>
        <script src="${resource(dir: 'js/uniform', file: 'jquery.uniform.js')}" type="text/javascript"></script>
		<link href="${resource(dir: 'css/uniform', file: 'uniform.default.css')}" media="screen" rel="stylesheet" type="text/css"/>
        <g:javascript>
           	$(document).ready(function(){
	       		$("select").uniform();       		
    			$( "#startDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
    			$( "#endDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
           	});
       </g:javascript>
    </head>
    <body>
        <div class="box"	>
            <h2><g:message code="mission.create.headline" /></h2>
            
            <g:render template="../error/beanErrors" model="[ entity: missionInstance ]" />
            
            <g:form class="normal" action="save" >
				<form:fieldWrapper name="title">
			        <g:textField name="title" value="${missionInstance?.title}" />
				</form:fieldWrapper>
				
				<form:fieldWrapper name="description">
					<g:textArea name="description" cols="40" rows="5" value="${missionInstance?.description}" />
				</form:fieldWrapper>
				
				<form:fieldWrapper name="startDate">
					<g:datePicker name="startDate" id="startDate" precision="day"/>
		        </form:fieldWrapper>
				
				<form:fieldWrapper name="endDate">
					<g:datePicker name="endDate" id="endDate" precision="day"/>
				</form:fieldWrapper>
				
				<g:hiddenField name="owo.id" value="${missionInstance?.owo.id}"/>
				<div class="clear"></div>
				<g:submitButton name="create" class="save" class="button right" value="${message(code: 'mission.create.button.label', default: 'Create Mission')}" />
            	<div class="clear"></div>
            </g:form>
        </div>
    </body>
</html>