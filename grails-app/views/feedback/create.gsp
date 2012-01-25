

<%@ page import="owrks.admin.Feedback" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'feedback.label', default: 'Feedback')}" />
        <title><g:message code="default.feedback.title" /></title>
    </head>
    <body>
        <!--  <div id="breadcrumb">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>-->
        <div class="box">
            <h2><g:message code="feedback.headline" /></h2>
            
            <g:render template="../error/beanErrors" model="[ entity: feedbackInstance ]" />
            
            <g:form action="save" >
	             
				
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
                <g:submitButton name="create" class="save button right" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				<div class="clear"></div>
            </g:form>
        </div>
    </body>
</html>