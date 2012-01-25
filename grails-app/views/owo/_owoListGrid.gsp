<g:if test="${owoInstanceList }">
	<g:each in="${owoInstanceList}" var="owo" status="i">
		<div class="owoGrid ${i % 2 ? 'even' : 'odd' }">
				<h2><g:link controller="owo" id="${owo.id }" action="show">${ owo.title }</g:link></h2>
				<img src="<g:createLink controller="image" action="owo" id="${owo.id}" />" alt="${owo.title}" class="left owoPicMiddle" />
				<p>${ owo.goal }<p>
				<p class="clear">Erstellt von:
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
	<div class="paginateButtons clear">
       <g:paginate total="${owoInstanceTotal}" params="[ listLayout: 'grid' ]" />
   </div>
</g:if>
<g:else>
	<p>Keine oWos gefunden.</p>
</g:else>