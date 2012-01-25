<div id="stepComment_${comment?.id}" class="streamItem stepComment">
	<div id="comment_${comment.id}">
		<app:renderUserPicture user="${comment?.creator}" class="userPicture" />
		<div class="stepCommentContent">
			<p class="stepPostCreator"><g:link mapping="profile" id="${comment?.creator?.id}">${comment?.creator?.username}</g:link> <app:niceDateformat date="${comment?.dateCreated}" />:</p>
			<div class="wallItemContent">
				${comment?.content}
			</div>
			<al:stepCommentDeleteLink comment="${comment}" />
			<span class="wall draggable">&nbsp;</span>
		</div>
		<div class="clear"></div>
	</div>
</div>