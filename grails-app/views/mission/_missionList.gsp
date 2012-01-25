<g:render template="missionFilter" model="[ owoInstance: owoInstance, stepInstance: stepInstance, selectedMission: selectedMission ]" />
<h3>Missions:</h3>
<g:if test="${missionInstanceList}">
	<jq:jquery>
		$('span.addStep').click(function(){
			$(this).next('div.addMission').show();
		});
	</jq:jquery>
	<g:each in="${missionInstanceList}" status="i" var="${mission}">
		<g:render template="missionListItem" model="[ missionInstance: mission ]" />
	</g:each>
</g:if>
<g:else>
	<p>Es wurde bisher keine Mission erstellt.</p>
</g:else>