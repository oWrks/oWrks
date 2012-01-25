<g:each in="${owoInstanceList}" status="i" var="${owoInstance}">
    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
        <td>
        	<img src="<g:createLink controller="image" action="owo" id="${owoInstance.id }" />" alt="${owoInstance.title}" class="small-icon" width="40" />
		</td>
        <td>
        	<g:link controller="owo" action="show" id="${owoInstance.id}">
        		${owoInstance.title}
        	</g:link>
        </td>
        <td>${owoInstance.goal}</td>
        <td>
        	<g:link controller="profile" id="${owoInstance?.owner?.id}">
				${owoInstance.owner.username}</g:link>
			</td>
        <td><owo:freeSlots owo="${owoInstance}" /></td>
        <sec:ifAllGranted roles="ROLE_ADMIN">
			<td>
				<g:form id="${owoInstance.id}">
					<g:actionSubmit action="delete" value="${message(code: 'default.list.button.delete', default: 'delete')}" />
				</g:form>
			</td>
		</sec:ifAllGranted>
    </tr>
</g:each>