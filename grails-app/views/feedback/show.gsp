
<%@ page import="owrks.admin.Feedback" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'feedback.label', default: 'Feedback')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div>
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>
             <table class="show">
                 <tbody>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="feedback.id.label" default="Id" /></td>
                         
                         <td valign="top" class="value">${feedbackInstance?.id}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="feedback.user.label" default="User" /></td>
                         
                         <td valign="top" class="value"><g:link controller="user" action="show" id="${feedbackInstance?.user?.id}">${feedbackInstance?.user?.encodeAsHTML()}</g:link></td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="feedback.topic.label" default="Topic" /></td>
                         
                         <td valign="top" class="value">${feedbackInstance?.topic}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="feedback.message.label" default="Message" /></td>
                         
                         <td valign="top" class="value">${feedbackInstance?.message}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="feedback.url.label" default="Url" /></td>
                         
                         <td valign="top" class="value">${feedbackInstance?.url}</td>
                         
                     </tr>
                 
                     <tr class="prop">
                         <td valign="top" class="name"><g:message code="feedback.dateCreated.label" default="Date Created" /></td>
                         
                         <td valign="top" class="value"><g:formatDate date="${feedbackInstance?.dateCreated}" /></td>
                         
                     </tr>
                 
                 </tbody>
             </table>
        </div>
       	
	        <g:form>
	            <g:hiddenField name="id" value="${feedbackInstance?.id}" />
	            <g:actionSubmit class="edit button left" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	            <g:actionSubmit class="delete" action="delete button right" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	        </g:form>
    </body>
</html>