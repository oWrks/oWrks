<%@ page import="owrks.owo.OwoPostComment" %>
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
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'owoPostComment.label', default: 'OwoPostComment')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
    	<!-- <div id="breadcrumb">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div> -->
    	<div class="box">
    	<g:render template="../error/beanErrors" model="[ entity: owoPostCommentInstance ]" />
        
        <h2><g:message code="default.edit.label" args="[entityName]" /></h2>
        
        <g:form id="${owoPostCommentInstance?.id}" >
             
						   
			<form:fieldWrapper name="creator">
               	<g:select name="creator.id" from="${owrks.user.User.list()}" optionKey="id" value="${owoPostCommentInstance?.creator?.id}"  />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="content">
               	<g:textField name="content" value="${owoPostCommentInstance?.content}" />
			</form:fieldWrapper>
						   
			<form:fieldWrapper name="post">
               	<g:select name="post.id" from="${owrks.owo.OwoPost.list()}" optionKey="id" value="${owoPostCommentInstance?.post?.id}"  />
			</form:fieldWrapper>
            
			<div class="clear"></div>
			<g:actionSubmit class="save button left" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
            <g:actionSubmit class="delete button right" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
			<div class="clear"></div>
		</g:form>
		</div>
    </body>
</html>