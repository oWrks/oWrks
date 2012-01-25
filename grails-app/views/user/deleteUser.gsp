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
        <title><g:message code="user.account.delete.title" default="Delete your account"/></title>
    </head>
    <body>
    	<div class="box">
			<h2><g:message code="${controllerName}.${actionName}.headline" /></h2>
			
		   		<g:form controller="user" action="deleteUser" method="POST">
		   			<input type="submit" name="delete" value="Account löschen" class="button" />
		   			<input type="submit" name="never" value="Nein niemals" class="button" />
		   			</g:form>
		    </div>
    </body>
</html>
