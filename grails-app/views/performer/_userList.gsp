<ul id="${userBox}">
	<g:each in="${userList}" var="user">
		<p>${box}</p>
		<g:render template="/performer/user" model="['owoInstance':owoInstance,'user':user, 'userBox':userBox, 'showButton':showButton, 'currentUser': currentUser]"></g:render>
	</g:each>
</ul>