
<%@ page import="owrks.admin.Feedback" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'feedback.label', default: 'Feedback')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="box" style="border:none">
            <h2><g:message code="default.list.label" args="[entityName]" default="${entityName}" /></h2>
         	
            <table>
                <thead>
                    <tr>
                        <g:sortableColumn property="id" title="${message(code: 'feedback.id.label', default: 'Id')}" />
                        <th><g:message code="feedback.user.label" default="User" /></th>
                        <th>Email</th>
                        <g:sortableColumn property="topic" title="${message(code: 'feedback.topic.label', default: 'Topic')}" />
                        <g:sortableColumn property="message" title="${message(code: 'feedback.message.label', default: 'Message')}" />
                        <g:sortableColumn property="url" title="${message(code: 'feedback.url.label', default: 'Url')}" />
                        <g:sortableColumn property="dateCreated" title="${message(code: 'feedback.dateCreated.label', default: 'Date Created')}" />
                        <th></th>
                    </tr>
                </thead>
                <tbody class="box owo-missions">
                <g:each in="${feedbackInstanceList}" status="i" var="feedbackInstance">
                
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" style="list-style-type: none;">
                    	
                        <td>${feedbackInstance.id}</td>
                        <td>${feedbackInstance.user}</td>
                        <td>${feedbackInstance.user.email}</td>
                        <td>${feedbackInstance.topic}</td>
                        <td>${feedbackInstance.message}</td>
                        <td>${feedbackInstance.url}</td>                    
                        <td><g:formatDate date="${feedbackInstance.dateCreated}" /></td>
                        
                        <td>
                        	<g:form id="${feedbackInstance.id}" style="width:auto !important;">
                        		<g:actionSubmit action="show" value="${message(code: 'default.list.button.show', default: 'show')}" />
                        		<g:actionSubmit action="delete" value="${message(code: 'default.list.button.delete', default: 'delete')}" />
                        	</g:form>
                        </td>    
                	</tr>
                </g:each>
                </tbody>
            </table>
            <div class="box" style="border:none">
                <g:paginate total="${feedbackInstanceTotal}" />
            </div>
		</div>
    </body>
</html>