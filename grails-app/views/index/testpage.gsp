<%@ page import="owrks.user.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Welcome to oWrks</title>
        <meta name="layout" content="main" />
        <style type="text/css" media="screen">

        </style>
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
    </head>
    <body>
    	<div>
            <div class="panelBody" style="float:left;">
                <h1>Test Page by Lukas</h1>
                <ul style="list-style-type:none;">
                    <li>App version: <g:meta name="app.version"></g:meta></li>
                    <li>Grails version: <g:meta name="app.grails.version"></g:meta></li>
                    <li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>
                    <li>JVM version: ${System.getProperty('java.version')}</li>
                    <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
                    <li>Domains: ${grailsApplication.domainClasses.size()}</li>
                    <li>Services: ${grailsApplication.serviceClasses.size()}</li>
                    <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
                </ul>
                <h1>Installed Plugins</h1>
                <ul style="list-style-type:none;">
                    <g:set var="pluginManager"
                           value="${applicationContext.getBean('pluginManager')}"></g:set>

                    <g:each var="plugin" in="${pluginManager.allPlugins}">
                        <li>${plugin.name} - ${plugin.version}</li>
                    </g:each>

                </ul>
            </div>
	        <div style="float:left;margin-left:100px;">
	            <h1>Welcome to oWrks</h1>
	            <p>... running!</p>
	            <div id="controllerList" class="dialog">
	                <h2>Available Controllers:</h2>
	                <ul>
	                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
	                        <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
	                    </g:each>
	                </ul>
	            </div>
	        </div>
	        <div style="clear:both;"></div>
		</div>
    </body>
</html>