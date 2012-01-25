<div class="postComment" id="comment_${comment?.id}">
	<app:renderUserPicture user="${comment?.creator}" class="userPicture" />
	<div class="commentContent">
		<p><strong><g:link mapping="profile" id="${comment?.creator?.id}">${comment?.creator?.username}</g:link></strong> ${comment?.content}</p>
		<p class="dateCreated"><app:niceDateformat date="${comment?.dateCreated}" /> <al:owoPostCommentDeleteLink comment="${comment}" /></p>
	</div>
	<div class="clear"></div>
</div>