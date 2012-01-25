<div>
	<p><g:message code="step.mission.label" default="Mission" />: <g:link controller="owo" action="missions" id="${stepInstance?.mission?.owo.id}" params="[ missionId: stepInstance?.mission?.id ]">${stepInstance?.mission}</g:link></p>
	<p>Zugewiesen an <step:assignedTo assignedTo="${stepInstance.assignedTo}" /></p>
	<p>Endet am <g:formatDate date="${stepInstance.dueDate}" format="dd. MMMM yyyy" /> <small>(<app:niceDateformat date="${stepInstance.dueDate}" future="${true}" />)</small></p>

	<p><g:message code="step.lastUpdated.label" default="Letzte Änderung" /> <g:if test="${stepInstance.lastEditor}">von <g:link mapping="profile" id="${stepInstance.lastEditor?.id}">${stepInstance.lastEditor}</g:link></g:if></p>
	<p>Versionsnummer: ${stepInstance?.version}</p>
	<p><g:message code="step.finished.label" default="Abgeschlossen" />: <img src="${resource( dir: 'images', file: (stepInstance?.finished ? 'bullet_green.png' : 'bullet_red.png')) }" alt="" title="<g:formatBoolean boolean="${stepInstance?.finished}" />" style="padding:0;" /></p>
	<p><g:message code="step.published.label" default="Veröffentlicht" />: <img src="${resource( dir: 'images', file: (stepInstance?.published ? 'bullet_green.png' : 'bullet_red.png')) }" alt="" title="<g:formatBoolean boolean="${stepInstance?.published}" />" style="padding:0;" /></p>

	<div id="finishStep">
		<g:if test="${!stepInstance.finished && (stepInstance.assignedTo == currentUser || stepInstance.createdBy == currentUser)}">
			<g:formRemote name="stepComment" 
				url="[ controller: 'step', action: 'finishAjax', id: stepInstance?.id ]" 
				method="post" 
				controller="step" 
				action="finishAjax" 
				update="stepDescriptionContent"
				id="${stepInstance?.id}"
				onLoading="setLoadingImage('finishStepButtonWrapper')"
				onComplete="setLoadingImage(false); "
				onSuccess=""
				>
	            
            	<input type="hidden" name="finish" value="1" />
           		<div class="buttonWrapper" id="finishStepButtonWrapper">
           			<g:submitButton name="save" class="save" class="button" value="${message(code: 'button.step.finish', default: 'Step abschließen')}" />
           		</div>
			</g:formRemote>
		</g:if>
		<g:else>
			<g:if test="${stepInstance.published}">
				<p><g:message code="step.finished.and.published.description" /></p>
			</g:if>
			<g:else>
				<g:if test="${(stepInstance.assignedTo == currentUser || stepInstance.createdBy == currentUser)}">
					<p><g:message code="step.finished.not.published.description" /></p>
					<g:formRemote name="stepComment" 
						url="[ controller: 'step', action: 'publishAjax', id: stepInstance?.id ]" 
						method="post" 
						controller="step" 
						action="publishAjax" 
						update="stepDescriptionContent"
						id="${stepInstance?.id}"
						onLoading="setLoadingImage('publishStepButtonWrapper')"
						onComplete="setLoadingImage(false); "
						onSuccess=""
						>
			            
		            	<input type="hidden" name="publish" value="1" />
		           		<div class="buttonWrapper" id="publishStepButtonWrapper">
		           			<g:submitButton name="save" class="save" class="button" value="${message(code: 'button.step.publish', default: 'Step veröffentlichen')}" />
		           		</div>
					</g:formRemote>
				</g:if>
			</g:else>
		</g:else>
	</div>
</div>
