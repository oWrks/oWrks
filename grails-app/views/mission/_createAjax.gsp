<%@ page import="owrks.owo.Mission" %>
<jq:jquery>
	$( "#startDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
	$( "#endDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
</jq:jquery>
<div>
	<h2><g:message code="mission.create.headline" /></h2>
	         
	<g:render template="/error/ajaxErrors" model="[ entity: missionInstance, message: message ]" />
	
	<g:formRemote name="missionEdit" 
		url="[ controller: 'mission', action: 'saveAjax', id: missionInstance?.id ]" 
		method="post" 
		controller="mission" 
		action="save" 
		id="${missionInstance?.id}"
		update="missionContent"
		onLoading="setLoading(true, 'Mission wird geändert...')"
		onSuccess="setLoading(false);">
	         
		<form:fieldWrapper name="title">
	        <g:textField name="title" value="${missionInstance?.title}" />
		</form:fieldWrapper>
		
		<form:fieldWrapper name="description">
			<g:textArea name="description" cols="40" rows="5" value="${missionInstance?.description}" />
		</form:fieldWrapper>
		
		<form:fieldWrapper name="startDate">
			<input type="text" name="startDate" id="startDate"
				value="${(missionInstance?.startDate ? formatDate(format: 'dd.MM.yyyy', date: missionInstance?.startDate) : formatDate(format: 'dd.MM.yyyy', date: new Date()))}" />
        </form:fieldWrapper>
		
		<form:fieldWrapper name="endDate">
			<input type="text" name="endDate" id="endDate"
				value="${(missionInstance?.endDate ? formatDate(format: 'dd.MM.yyyy', date: missionInstance?.endDate) : formatDate(format: 'dd.MM.yyyy', date: new Date()))}" />
		</form:fieldWrapper>
		
		<g:hiddenField name="owo.id" value="${missionInstance?.owo.id}"/>
	
		<div class="clear"></div>

		<g:submitButton name="create" class="save" class="button right" value="${message(code: 'mission.create.button.label', default: 'Create Mission')}" />
	
	   	<div class="clear"></div>
	</g:formRemote>
</div>
