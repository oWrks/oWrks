<h3>Filter:</h3>
<div class="filter">
	<div class="filterItem">
		<label for="missionId">Mission:</label>
		<g:select name="missionId" 
			from="${owoInstance.missions}" 
			optionKey="id" 
			optionValue="title" 
			onchange="loadTemplate('/mission/loadMissionListAjax/' + this.options[this.selectedIndex].value + '?owoId=${owoInstance.id}')" 
			value="${selectedMission?.id}"
			noSelection="[ '': 'Alle Missionen anzeigen' ]" />
	</div>
	<div class="filterItem">
		<label for="stepId">Step:</label>
		<g:select name="stepId" 
			from="${owoInstance.getSteps()}" 
			optionKey="id" 
			optionValue="title" 
			onchange="setStep(this.options[this.selectedIndex].value);" 
			value="${stepInstance?.id}"
			noSelection="[ '': 'Step auswählen' ]" />
	</div>
	<div class="filterItem" style="padding-top:2px;">
		<g:remoteLink 
		controller="mission" 
		action="createAjax" 
		id="${owoInstance.id}"
		onLoading="setLoading(true, 'Formular wird geladen...')"
		onSuccess="setLoading(false); setAddress('/mission/createAjax/${owoInstance.id}')"
		update="missionContent"
		class="button right">Mission erstellen</g:remoteLink>
	</div>
</div>
<div class="clear"></div>