<g:if test="${wallPosts}">
	<g:each in="${wallPosts}" var="post">
		<step:renderWallPost post="${post}" />
	</g:each>
</g:if>
<g:else>
	<p id="noPostsYet">Keine Beiträge vorhanden</p>
</g:else>
