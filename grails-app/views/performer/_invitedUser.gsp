<li id="rightUser${user?.id}">
			<img src="<g:createLink controller="image" action="profile" id="${user?.id}" />"  title="${user?.username}" width="30" style="vertical-align:middle" />
			<g:link controller="profile" action="index" id="${user?.id}">
				${user?.username} 
			</g:link>
			<g:form name="unInviteForm${user.id}">
				<g:hiddenField name="owoId" value="${owoInstance?.id}"></g:hiddenField>			
				<g:hiddenField name="inviteeId" value="${user.id}"></g:hiddenField>
				<g:submitToRemote url="[ controller: 'owoInvitation', action: 'unInviteUser']"
		            		onSuccess="moveToLeft(data,${user.id})" value="Ausladen" id="submitPost" class="button" before="" after="" />
		            	
			</g:form>
</li>