<div class="box owo-missions" id="mission_${missionInstance.id}">
	<h3>
		<span style="float:left;">${missionInstance.title}</span>
			<al:missionEditLink mission="${missionInstance}" ajax="${true}">edit</al:missionEditLink>
			<al:missionDeleteLink mission="${missionInstance}" />
			<al:stepCreateLink mission="${missionInstance}" ajax="${true}">Step hinzufügen</al:stepCreateLink>
	</h3>
	<p style="clear:both;">
		<em>${missionInstance?.description } </em>
	</p>
	
	<p><step:stepInfo mission="${missionInstance}" /></p>
	<div class="toggle_container">
		<g:render template="/mission/stepList" model="[mission: missionInstance]" />
	</div>
</div>