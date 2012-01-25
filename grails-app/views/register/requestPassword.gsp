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
        <title><g:message code="user.request.password.title" default="Request new password"/></title>
    </head>
    <body>
    	<div class="box">
			<h2><g:message code="${controllerName}.${actionName}.headline" /></h2>
			
			<g:render template="../error/beanErrors" />
		   		   	
		   		<g:if test="${!success}">
			    	<g:form class="normal" action="requestPassword">
			    		
			    		<form:fieldWrapper name="username">
			            	<g:textField name="username" value="${username}" />
						</form:fieldWrapper>
			    		
			    		<form:fieldWrapper name="email">
			            	<g:textField name="email" value="${email}" />
						</form:fieldWrapper>
						
			    		<input type="submit" name="submit" class="button right" value="<g:message code="register.newPassword" />" />
			    		<div class="clear"></div>
			    	</g:form>
			    </g:if>
			    <g:else>
			    	<p><g:message code="user.password.successfull.requested" /></p>
			    </g:else>
		    </div>
    </body>
</html>
