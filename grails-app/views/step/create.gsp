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
        <title><g:message code="default.step.title" /></title>
		<g:javascript>
           	$(document).ready(function(){
			    $( "#dueDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
           	})
       </g:javascript>
    </head>
    <body>
        <g:render template="createEditor" model="[ stepInstance: stepInstance ]" />
    </body>
</html>