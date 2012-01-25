<li id="${userBox}${user?.id}">
		<img src="<g:createLink controller="image" action="profile" id="${user?.id}" />"  title="${user?.username}" width="30" style="vertical-align:middle" />
		<g:link controller="profile" action="index" id="${user?.id}">
			${user?.username} 
		</g:link>
		<g:if test="${owoInstance.owner.id == user.id}">
			(Ersteller)
		</g:if>
		<g:if test="${params.action != 'wizzardInviteAgiler'}">(<g:link controller="userMessage" action="create"
						id="${user?.username}" class="dialog">Nachricht schreiben</g:link>)</g:if>
				
		<g:if test="${showButton == 'invite'}">
			<g:form name="userForm${user.id}">
			<g:hiddenField name="owoId" value="${owoInstance?.id}"></g:hiddenField>			
			<g:hiddenField name="inviteeId" value="${user.id}"></g:hiddenField>
			<g:submitToRemote url="[ controller: 'owoInvitation', action: 'inviteUser']"
	            		onSuccess="moveToRight(data,${user.id})" onFailure="" value="Einladen" id="submitPost" class="button" />
	            	
			</g:form>
		</g:if>
		
		<g:if test="${showButton == 'unInvite'}">
			<g:form name="userForm${user.id}">
				<g:hiddenField name="owoId" value="${owoInstance?.id}"></g:hiddenField>			
				<g:hiddenField name="inviteeId" value="${user.id}"></g:hiddenField>
				<g:submitToRemote url="[ controller: 'owoInvitation', action: 'unInviteUser']"
		            		onSuccess="moveToLeft(data,${user.id})" value="Ausladen" id="submitPost" class="button" />
		            	
			</g:form>
		</g:if>
		
		<g:if test="${showButton == 'deletePerformer' && owoInstance?.owner.id == currentUser.id && owoInstance?.owner.id != user.id}">
			<g:form name="userForm${user.id}">
				<g:hiddenField name="owo.id" value="${owoInstance?.id}"></g:hiddenField>			
				<g:hiddenField name="user.id" value="${user.id}"></g:hiddenField>
				<g:submitToRemote url="[ controller: 'performer', action: 'delete']"
		            		onSuccess="deletePerformer(data,${user.id})" value="Teilnehmer löschen" id="submitPost" class="button" before="" after="" />
		            	
			</g:form>
		</g:if>
		
		<g:if test="${showButton == 'confirmRequestAjax'}">
			<g:form name="userForm${user.id}">
				<g:hiddenField name="owoID" value="${owoInstance?.id}"></g:hiddenField>			
				<g:hiddenField name="userID" value="${user.id}"></g:hiddenField>
				<g:submitToRemote url="[ controller: 'owoRequest', action: 'confirmAjax']"
		            		onSuccess="moveToPerformerBox(data,${user.id})" value="Anfrage annehmen." id="submitPost" class="button" before="" after="" />
		            	
			</g:form>
			
			<g:form name="userForm${user.id}">
				<g:hiddenField name="owoID" value="${owoInstance?.id}"></g:hiddenField>			
				<g:hiddenField name="userID" value="${user.id}"></g:hiddenField>
				<g:submitToRemote url="[controller: 'owoRequest', action: 'declineAjax']" 
							onSuccess="declineRequest(data,${user.id})" value="ablehnen." id="submitPost" class="button" before="" after="" />
		            	
			</g:form>
		</g:if>
		
		<g:if test="${showButton=='confirmRequestFromAnywhere'}">
			<g:form name="userForm${user.id}" controller="owoRequest" action="confirm" >
				<g:hiddenField name="owoID" value="${owoInstance?.id}"></g:hiddenField>			
				<g:hiddenField name="userID" value="${user.id}"></g:hiddenField>
				<g:submitButton name="confirm" value="Anfrage annehmen." id="submitPost" class="button" />
		            	
			</g:form>
		</g:if>
		
		
</li>