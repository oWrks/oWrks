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
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div>
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>
             <table class="show">
                 <tbody>
                 <%  excludedProps = Event.allEvents.toList() << 'version'
                     allowedNames = domainClass.persistentProperties*.name << 'id' << 'dateCreated' << 'lastUpdated'
                     props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) }
                     Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
                     props.each { p -> %>
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></td>
                         <%  if (p.isEnum()) { %>
                         <td valign="top" class="value">\${${propertyName}?.${p.name}?.encodeAsHTML()}</td>
                         <%  } else if (p.oneToMany || p.manyToMany) { %>
                         <td valign="top" style="text-align: left;" class="value">
                             <ul>
                             <g:each in="\${${propertyName}.${p.name}}" var="${p.name[0]}">
                                 <li><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${p.name[0]}.id}">\${${p.name[0]}?.encodeAsHTML()}</g:link></li>
                             </g:each>
                             </ul>
                         </td>
                         <%  } else if (p.manyToOne || p.oneToOne) { %>
                         <td valign="top" class="value"><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${propertyName}?.${p.name}?.id}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link></td>
                         <%  } else if (p.type == Boolean.class || p.type == boolean.class) { %>
                         <td valign="top" class="value"><g:formatBoolean boolean="\${${propertyName}?.${p.name}}" /></td>
                         <%  } else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) { %>
                         <td valign="top" class="value"><g:formatDate date="\${${propertyName}?.${p.name}}" /></td>
                         <%  } else if(!p.type.isArray()) { %>
                         <td valign="top" class="value">\${${propertyName}?.${p.name}}</td>
                         <%  } %>
                     </tr>
                 <%  } %>
                 </tbody>
             </table>
        </div>
       	
	        <g:form>
	            <g:hiddenField name="id" value="\${${propertyName}?.id}" />
	            <g:actionSubmit class="edit button left" action="edit" value="\${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	            <g:actionSubmit class="delete" action="delete button right" value="\${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('\${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	        </g:form>
    </body>
</html>