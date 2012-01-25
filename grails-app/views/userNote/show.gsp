<%@ page import="owrks.ple.UserNote" %>
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
        <g:set var="entityName" value="${message(code: 'userNote.label', default: 'UserNote')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <script type="text/javascript" src="${resource(dir:'js/niceEdit',file:'nicEdit.js')}"></script>
        <jq:jquery>
        	niceEditor = new nicEditor({
				buttonList : ['fontSize','bold','italic','underline','strikethrough','subscript','superscript', 'ol', 'ul', 'forecolor', 'bgcolor', 'hr'],
				iconsPath : '../../images/nicEditorIcons.gif',
				maxHeight : 500,
				xhtml : true,
			}).panelInstance('usernotes');
        </jq:jquery>
    </head>
    <body>
    	<div class="box">
	    	<g:render template="../error/beanErrors" model="[ entity: userNoteInstance ]" />
	        
	        <h2 class="info-list-header"><g:message code="usernote.h2" /></h2>
	        
	        <g:form class="usernotes-editor" id="${userNoteInstance?.id}" style="width:800px;" >
					
				<textarea name="text" id="usernotes" style="width: 100%;">${userNoteInstance?.text ?: ''}</textarea>		   


	            <div style="margin: 10px 0;"><g:actionSubmit class="save button" action="update" value="${message(code: 'userNote.button.update.label', default: 'Zwischenspeichern')}" /></div>
			</g:form>
		</div>
    </body>
</html>