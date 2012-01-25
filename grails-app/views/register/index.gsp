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
        <title><g:message code="default.register.title" default="Register"/></title>
    </head>
    <body>
    	<div class="box">
			<h2><g:message code="default.${controllerName}.headline" /></h2>
			
			<g:render template="../error/beanErrors" model="[ entity: entity ]" />
		   	
		    	<g:form controller="register" action="register">
		    		
		    		<form:fieldWrapper name="username">
		            	<g:textField  name="username" value="${entity?.username}" />
					</form:fieldWrapper>
					
					<form:fieldWrapper name="email">
		            	<g:textField  name="email" value="${entity?.email}" />
					</form:fieldWrapper>
		
					<form:fieldWrapper name="password">
		            	<g:passwordField  type="password" name="password" value="" />
					</form:fieldWrapper>
		
					<form:fieldWrapper name="password2">
		            	<g:passwordField  type="password" name="password2" value="" />
					</form:fieldWrapper>
		    		
		    		<div class="clear"></div>
					<input type="submit" name="submit" value="Sign Up" class="button right" />
					<div class="clear"></div>
		    		
		    	</g:form>
		    </div>
		<div class="clear"></div>
    </body>
</html>
