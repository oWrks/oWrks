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
<%=packageName%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    	<div>
            <span class="menuButton"><a class="home" href="\${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="list">
            <h2><g:message code="default.list.label" args="[entityName]" default="\${entityName}" /></h2>
            <table>
                <thead>
                    <tr>
                    <%  excludedProps = Event.allEvents.toList() << 'version'
                        allowedNames = domainClass.persistentProperties*.name << 'id' << 'dateCreated' << 'lastUpdated'
                        props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) && !Collection.isAssignableFrom(it.type) }
                        Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
                        props.eachWithIndex { p, i ->  if (i < 6) { if (p.isAssociation()) { %>
                        <th><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></th><%      } else { %>
                        <g:sortableColumn property="${p.name}" title="\${message(code: '${domainClass.propertyName}.${p.name}.label', default: '${p.naturalName}')}" /><%  }   }   } %>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                <g:each in="\${${propertyName}List}" status="i" var="${propertyName}">
                    <tr class="\${(i % 2) == 0 ? 'odd' : 'even'}">
                    	<%  props.eachWithIndex { p, i -> %><%     if (i < 6) { if (p.type == Boolean.class || p.type == boolean.class) { %>
                        <td><g:formatBoolean boolean="\${${propertyName}.${p.name}}" /></td><%          } else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) { %>                    
                        <td><g:formatDate date="\${${propertyName}.${p.name}}" /></td><%          } else { %>
                        <td>\${${propertyName}.${p.name}}</td><%  }   }   } %>
                        <td>
                        	<g:form id="\${${propertyName}.id}">
                        		<g:actionSubmit action="show" value="\${message(code: 'default.list.button.show', default: 'show')}" />
                        		<g:actionSubmit action="edit" value="\${message(code: 'default.list.button.edit', default: 'edit')}" />
                        		<g:actionSubmit action="delete" value="\${message(code: 'default.list.button.delete', default: 'delete')}" />
                        	</g:form>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <div class="paginateButtons">
                <g:paginate total="\${${propertyName}Total}" />
            </div>
        </div>
    </body>
</html>