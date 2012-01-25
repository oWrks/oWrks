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
<% import grails.persistence.Event %>
<% import org.codehaus.groovy.grails.plugins.PluginManagerHolder %>
<%=packageName%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.${domainClass.propertyName}.title" /></title>
    </head>
    <body>
        <!--  <div id="breadcrumb">
            <span class="menuButton"><a class="home" href="\${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>-->
        <div class="box">
            <h2><g:message code="${domainClass.propertyName}.headline" /></h2>
            
            <g:render template="../error/beanErrors" model="[ entity: ${propertyName} ]" />
            
            <g:form action="save" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
	             <%  excludedProps = Event.allEvents.toList() << 'version' << 'id' << 'dateCreated' << 'lastUpdated'
	                   persistentPropNames = domainClass.persistentProperties*.name
	                   props = domainClass.properties.findAll { persistentPropNames.contains(it.name) && !excludedProps.contains(it.name) }
	                   Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
	                   display = true
	                   boolean hasHibernate = PluginManagerHolder.pluginManager.hasGrailsPlugin('hibernate')
	                   props.each { p ->
	                       if (hasHibernate) {
	                           cp = domainClass.constrainedProperties[p.name]
	                           display = (cp?.display ?: true)
	                       }
	                       if (display) { %>
				
				<form:fieldWrapper name="${p.name}">
	               	${renderEditor(p)}
				</form:fieldWrapper>
				<%  }   } %>
				<div class="clear"></div>
                <g:submitButton name="create" class="save button right" value="\${message(code: 'default.button.create.label', default: 'Create')}" />
				<div class="clear"></div>
            </g:form>
        </div>
    </body>
</html>