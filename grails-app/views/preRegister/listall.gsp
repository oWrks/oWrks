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
        <title>Welcome to oWrks</title>
        <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'main.css')}"/>
        
        
    </head>
    <body>
    
    	<div style="width:800px;margin:0 auto;">
    		<div>
	    		<g:link controller="index"><img src="${resource(dir:'images',file:'owrks-logo.jpg')}" alt="oWrks" /></g:link>
	    	</div>
            <h1>oWrks - Live and let learn</h1>
            <g:each in="${users }" var="user">
            	<p>${user.email}</p>
            </g:each>          
        </div>
    </body>
</html>