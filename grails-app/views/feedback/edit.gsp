

<%@ page import="owrks.admin.Feedback" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'feedback.label', default: 'Feedback')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
    	<!-- <div id="breadcrumb">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div> -->
    	<div class="box">
    	<g:render template="../error/beanErrors" model="[ entity: feedbackInstance ]" />
        
        <h2><g:message code="default.edit.label" args="[entityName]" /></h2>
        
        <g:form id="${feedbackInstance?.id}" >
             
						   
			<form:fieldWrapper name="user">
               	<g:select name="user.id" from="${owrks.user.User.list()}" optionKey="id" value="${feedbackInstance?.user?.id}" noSelection="['null': '']" />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="topic">
               	<g:textField name="topic" value="${feedbackInstance?.topic}" />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="message">
               	<g:textField name="message" value="${feedbackInstance?.message}" />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="url">
               	<g:textField name="url" value="${feedbackInstance?.url}" />
			</form:fieldWrapper>
            
			<div class="clear"></div>
			<g:actionSubmit class="save button left" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
            <g:actionSubmit class="delete button right" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
			<div class="clear"></div>
		</g:form>
		</div>
    </body>
</html>