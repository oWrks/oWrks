<g:each in="${owos }" var="owo">
	<div class="box owo-list-item">
			<h2><g:link controller="owo" id="${owo.id }" action="show">
				${ owo.title }
			</g:link>
			</h2>

			<img src="<g:createLink controller="image" action="owo" id="${owo.id}" />"
				alt="${owo.title}" class="left" />
			<p>${ owo.goal }<br />
			Erstellt von:
			<g:link controller="profile" id="${owo.owner.id }">${owo.owner.username }</g:link><br /> 
			Freie Pl&auml;tze: <owo:freeSlots owo="${owo}" /> (${owo.type})<br />
			Phase: ${owo.workflowState}
			</p>
			<p class="right">
				<al:owoJoinLink owo="${owo}"/> 
			</p>
			<div class="clear"></div>
	</div>
</g:each>