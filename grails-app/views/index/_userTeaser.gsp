<div class="box">
	<ul>
		<g:each in="${users}" var="user">
			<li><img
				src="<g:createLink controller="image" action="profile" id="${user.id}" />"
				alt="${user.picture}" class="small-icon" title="" width="30"
				style="vertical-align:middle" /> <g:link controller="profile"
					id="${user.id }">
					${ user.username }
				</g:link></li>
			<div class="clear"></div>

		</g:each>
	</ul>
</div>