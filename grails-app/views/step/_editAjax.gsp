<%@ page import="owrks.owo.Step" %>
<jq:jquery>
	$( "#dueDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
	
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
</jq:jquery>
<div>
    <h2><g:message code="default.step.edit.label" args="[ stepInstance?.title ]" /></h2>
    
    <g:render template="/error/ajaxErrors" model="[ entity: stepInstance, message: message ]" />
    
    <g:formRemote name="stepEdit" 
		url="[ controller: 'step', action: 'updateAjax', id: stepInstance?.id ]" 
		method="post" 
		controller="step" 
		action="update" 
		id="${stepInstance?.id}"
		update="missionContent"
		onLoading="setLoading(true, 'Step wird gespeichert...')"
		onLoaded="setLoading(false); refreshMissionList( ${stepInstance?.mission.id }, ${stepInstance?.id });">   
            
		<form:fieldWrapper name="title">
              	<g:textField name="title" value="${stepInstance?.title}" size="60" />
		</form:fieldWrapper>
		
		<form:fieldWrapper name="dueDate">
              			<input type="text" name="dueDate" id="dueDate"
				value="${(stepInstance?.dueDate ? formatDate(format: 'dd.MM.yyyy', date: stepInstance?.dueDate) : '')}" />
		</form:fieldWrapper>
		
		<form:fieldWrapper name="assignedTo.id">
              	<g:select name="assignedTo.id" 
              		from="${stepInstance?.mission?.owo*.performers.flatten()}" 
              		optionKey="userId" 
              		value="${stepInstance?.assignedTo?.id}" 
              		noSelection="['' : 'Performer auswählen']"  />
		</form:fieldWrapper>
		
		<form:fieldWrapper name="finished">
			<g:checkBox name="finished" value="${stepInstance?.finished}" checked="${stepInstance?.finished}" />
		</form:fieldWrapper>
		
		<form:fieldWrapper name="published">
           	<g:if test="${stepInstance.finished}">
           		<g:checkBox name="published" value="${stepInstance?.published}" checked="${stepInstance?.published}" />
           	</g:if>
           	<g:else>
           		<p>Der Step muss zuerst abgeschlossen werden, bevor er veröffentlicht werden kann.</p>
           	</g:else>
		</form:fieldWrapper>
		
		<form:fieldWrapper name="tags">
			<input id="tags" type="text" name="tags"
				value="${stepInstance?.tags ? stepInstance.tags.join(', ') : (tags ?: '')}" />
		</form:fieldWrapper>
		
		<g:hiddenField name="missionId" value="${stepInstance?.mission?.id}"  />
           
		<div class="clear"></div>
			<g:actionSubmit class="save button left" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
		<div class="clear"></div>
	</g:formRemote>
</div>
