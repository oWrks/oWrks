<g:each in="${steps}" var="step">
	<div class="box owo-list-item">
			<h3><g:link controller="step" id="${step?.id }" action="show">
				${ step.title }
			</g:link>
			</h3>
		<p>
			${ step.description }<br /> oWo:
			<g:link title="asfcs" controller="owo" action="show"
				params="${[id: step.mission.owo.id]}">
				${ step.mission.owo.title }
			</g:link>
			<br />
			<g:message code="default.responsable" default=Verantwortlich:" />
			<g:if test="${step.assignedTo}">
				<g:link controller="profile" id="${step.assignedTo.id}">
					${step.assignedTo.encodeAsHTML() }
				</g:link>
				<br />
			</g:if>
			<g:else>
				<g:message code="default.nobody" default="Jeder" />
			</g:else>
		</p>
		<div class="clear"></div>
	</div>
</g:each>