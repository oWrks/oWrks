<jq:jquery>
$( ".sortable" ).sortable({
	revert: true,
	 opacity: 0.8,
	stop: function(event, ui) {
		var dataString = ""
		var dataArr = {};
		
		$(ui.item).parent('ul.sortable').children('li').each(function(index){
			var stepId = $(this).attr('id').replace("step_", "");
			dataArr[stepId] = index+1;
		});
		
		jQuery.ajax({
			type:'POST',
			data: 'steps=' + JSON.stringify(dataArr), 
			url: '${createLink(controller: "step", action: "updateSequenceOrder") }',
			success:function(data,textStatus){
				//console.log(data);
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){}
		});
	}
});
</jq:jquery>
<g:if test="${mission.steps}">
	<ul class="sortable stepList">
		<g:each in="${mission.steps}" status="j" var="${step}">
			<li id="step_${step.id}" class="step" title="[Erstellt von ${step.createdBy?.username}]">
				<div id="${'steps_' + step.id}">
					<div class="stepInfo ${step.finished ? 'finished' : ''}">
				
						<g:remoteLink controller="step" 
							action="wall" 
							id="${step.id}"
							onLoading="setLoading(true, 'Step wird geladen')"
							onLoaded="setLoading(false)"
							onSuccess="setCurrentStepId(${step.id}, 'true'); setAddress('step/wall/${step.id}');"
							update="missionContent"
							class="stepTitle">${step.title.size() > 140 ? (step.title[0..140] + '...') : step.title}</g:remoteLink>
						
						<span class="assignedTo">
							<g:link	controller="profile" id="${step?.assignedTo?.id}">zugewiesen an ${step?.assignedTo ? step?.assignedTo?.encodeAsHTML() : 'niemand'}</g:link> 
							<g:if test="${step?.dueDate}">- fällig am <g:formatDate date="${step?.dueDate}" format="dd.MM" /></g:if>
						</span>
					</div>
					
					<al:isPerformer owo="${step.mission.owo}">
						<div class="stepActions">
							<al:stepEditLink step="${step}" ajax="${true}">edit</al:stepEditLink>
							<al:stepDeleteLink step="${step}">delete</al:stepDeleteLink>
							<span class="draggable">&nbsp;</span>			
						</div>
					</al:isPerformer>
					<div class="clear"></div>
				</div>
			</li>
		</g:each>
	</ul>
</g:if>
<g:else>
	<p><g:remoteLink 
	controller="step" 
	action="createAjax" 
	id="${mission.id}"
	onLoading="setLoading(true, 'Formular wird geladen...')"
	onLoaded="setLoading(false)"
	onSuccess="setAddress('step/createAjax/${mission.id}');"
	update="missionContent"
	class="button">Neuer Step erstellen</g:remoteLink></p>
</g:else>