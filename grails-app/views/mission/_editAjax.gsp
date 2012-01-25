<%@ page import="owrks.owo.Mission"%>
<jq:jquery>
	$( "#startDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
	$( "#endDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
</jq:jquery>
<div>
	<h3><g:message code="default.mission.edit.label" args="[ missionInstance?.title ]" /></h3>
	
	<g:render template="/error/ajaxErrors" model="[ entity: missionInstance, message: message ]" />

	<g:formRemote name="missionEdit" 
		url="[ controller: 'mission', action: 'updateAjax', id: missionInstance?.id ]" 
		method="post" 
		controller="mission" 
		action="update" 
		id="${missionInstance?.id}"
		update="missionContent"
		onLoading="setLoading(true, 'Mission wird geändert...')"
		onLoaded="setLoading(false)">
		
		<form:fieldWrapper name="title">
			<g:textArea name="title" cols="40" rows="5"
				value="${missionInstance?.title}" />
		</form:fieldWrapper>

		<form:fieldWrapper name="description">
			<g:textField name="description"
				value="${missionInstance?.description}" />
		</form:fieldWrapper>

		<form:fieldWrapper name="startDate">
			<input type="text" name="startDate" id="startDate"
				value="${(missionInstance?.startDate ? formatDate(format: 'dd.MM.yyyy', date: missionInstance?.startDate) : formatDate(format: 'dd.MM.yyyy', date: new Date()))}" />
		</form:fieldWrapper>

		<form:fieldWrapper name="endDate">
			<input type="text" name="endDate" id="endDate"
				value="${(missionInstance?.endDate ? formatDate(format: 'dd.MM.yyyy', date: missionInstance?.endDate) : formatDate(format: 'dd.MM.yyyy', date: new Date()))}" />
		</form:fieldWrapper>
		
		<div class="clear"></div>
		
		<g:actionSubmit class="save" class="button right" action="update"
			value="${message(code: 'default.button.update.label', default: 'Update')}" />
			
		<g:actionSubmit class="delete" class="button left" action="delete"
			value="${message(code: 'default.button.delete.label', default: 'Delete')}"
			onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
			
		<div class="clear"></div>
	</g:formRemote>
</div>