<div id="emailMessage"><g:message code="${message}"/></div>
<g:each in="${userInvitations}" var="${user}">
	<li>
		${user.email}
	</li>
</g:each>