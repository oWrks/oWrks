<%@ page import="owrks.owo.Owo"%>
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
<g:set var="entityName"
	value="${message(code: 'owo.label', default: 'Owo')}" />
<title><g:message code="default.edit.label" args="[entityName]" />
</title>
<script src="${resource(dir: 'js/uniform', file: 'jquery.uniform.js')}"
	type="text/javascript"></script>
<link
	href="${resource(dir: 'css/uniform', file: 'uniform.default.css')}"
	media="screen" rel="stylesheet" type="text/css" />
<g:javascript>
	    $(document).ready(function(){
         	$('select#owoType').change(function(){
    			if( $('select#owoType option:selected').val() == 'community') {
    				$('div#maxPerson').hide(300);
    				$('select#maxPerson option:first').attr('selected', 'selected');
    			} else {
    				$('div#maxPerson').show(300);
    			}
    		});
	    	$("select, radio").uniform();
	           		
	    	$( "#startDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
	   		$( "#endDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
	   		
	   		
	   		$( ".picture").click(function(){
    				$(".picture").parent().removeClass("active");
    				$(this).parent().addClass("active");
    				$("#picture").val($(this).attr("alt"));
    		});
    			
			$("img[alt='${owoInstance?.picture}']").parent().addClass("active");
			
			if($("#owoType option:selected").val() == 'community'){
				$("#maxPerson").hide();
			}
			
			$("#tags").autocomplete({
         		source: '${createLink(action: 'loadTags')}',
         		select: function(event, ui) {
         			var str = $(this).val().substring(0, $(this).val().lastIndexOf(",") + 1);
         			//console.log("tags: " + str);
         			//console.log("new tag: " + ui.item.value);
         			if(str.lastIndexOf(ui.item.value) == -1) {
         				ui.item.value = (str + " " + ui.item.value);
         			} else {
         				ui.item.value = str;
         			}
         		}
       		});
       			    		
	    	$('textarea[maxlength]').keyup(function(){
				var max = parseInt($(this).attr('maxlength'));
				var text = $(this).val();
				var textlength = text.length;
				
				if(textlength > max){
					$(this).val(text.substr(0, max));
				}
			});
	     })
	     
     </g:javascript>
</head>
<body>
	<div class="box">
		<h2>
			<g:message code="default.edit.label" args="[entityName]" />
		</h2>

		<g:render template="../error/beanErrors"
			model="['entity': owoInstance]" />

		<g:form class="normal" action="update" id="${owoInstance?.id}">
			<owo:renderOwoIcons></owo:renderOwoIcons>
			<form:fieldWrapper name="title">
				<g:textArea name="title" cols="40" rows="2"
					value="${owoInstance?.title}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="goal">
				<g:textArea name="goal" cols="40" rows="3"
					value="${owoInstance?.goal}" maxlength="255"/>
			</form:fieldWrapper>

			<form:fieldWrapper name="invitationText">
				<g:textArea name="invitationText" cols="40" rows="3"
					value="${owoInstance?.invitationText}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="startDate">
				<input type="text" name="startDate" id="startDate"
					value="${(owoInstance?.startDate ? formatDate(format: 'dd.MM.yyyy', date: owoInstance?.startDate) : formatDate(format: 'dd.MM.yyyy', date: new Date()))}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="endDate">
				<input type="text" name="endDate" id="endDate"
					value="${(owoInstance?.endDate ? formatDate(format: 'dd.MM.yyyy', date: owoInstance?.endDate) : formatDate(format: 'dd.MM.yyyy', date: new Date()))}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="type">
				<g:select name="type" id="owoType"
					from="${Owo.constraints.type.inList}" value="${owoInstance?.type}" /> <img src="${resource(dir: 'images', file: 'information.png')}" class="tooltip" title="${message(code: 'owo.type.title')}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="maxPerson" id="maxPerson">
				<g:select name="maxPerson" from="${(reservedSlots ?: 2)..9}"
					value="${owoInstance?.maxPerson == 0 ? 0 : (owoInstance?.maxPerson ?: 7)}"
					noSelection="[ 0 : 'Choose partners']" />
			</form:fieldWrapper>

			<form:fieldWrapper name="hashTag">
				<g:textField name="hashTag" value="${owoInstance?.hashTag}" />
			</form:fieldWrapper>
			
			<form:fieldWrapper name="tags">
					<input id="tags" type="text" name="tags"
						value="${owoInstance?.tags ? owoInstance.tags.join(', ') : ''}" />
				</form:fieldWrapper>
			
			<g:hiddenField name="picture" id="picture"
				value="${owoInstance?.picture ?: 'noun-ball.png'}"></g:hiddenField>

			<div class="clear"></div>
			<input type="submit" class="button right" value="speichern" />
			<div class="clear"></div>
		</g:form>
		<div class="clear"></div>

	</div>
</body>
</html>