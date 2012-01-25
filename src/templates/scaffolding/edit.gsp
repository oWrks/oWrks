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
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
    	<!-- <div id="breadcrumb">
            <span class="menuButton"><a class="home" href="\${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div> -->
    	<div class="box">
    	<g:render template="../error/beanErrors" model="[ entity: ${propertyName} ]" />
        
        <h2><g:message code="default.edit.label" args="[entityName]" /></h2>
        
        <g:form id="\${${propertyName}?.id}" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
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
			</form:fieldWrapper><%  }   } %>
            
			<div class="clear"></div>
			<g:actionSubmit class="save button left" action="update" value="\${message(code: 'default.button.update.label', default: 'Update')}" />
            <g:actionSubmit class="delete button right" action="delete" value="\${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('\${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
			<div class="clear"></div>
		</g:form>
		</div>
    </body>
</html>