<div id="stepText_${stepText?.id}" class="streamItem stepText">
	<div id="text_${stepText.id}">
		<app:renderUserPicture user="${stepText.creator}" class="userPicture" />
		<div class="stepPostContent">
			<p class="stepPostCreator"><g:link mapping="profile" id="${stepText.creator?.id}">${stepText.creator?.username}</g:link> <app:niceDateformat date="${stepText.dateCreated}" />:</p>
			<div class="wallItemContent">
				<%=stepText.content.decodeHTML()%>
			</div>
			<al:stepTextDeleteLink stepText="${stepText}" />
			<span class="wall draggable">&nbsp;</span>
			<al:stepTextEditLink stepText="${stepText}" />
		</div>
		<div class="clear"></div>
	</div>
</div>