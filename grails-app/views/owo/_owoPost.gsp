<div class="owoPost" id="post_${post?.id}">
	<app:renderUserPicture user="${post?.creator}" class="userPicture" />
	<div class="postContent">
		<p><strong><g:link mapping="profile" id="${post?.creator?.id}">${post?.creator?.username}</g:link></strong></p>
		<al:owoPostDeleteLink post="${post}" />
		<p class="content">${post?.content}</p>
		<g:if test="${post?.attachment}">
			<div class="attachment">
				<owo:renderPostAttachment attachment="${post.attachment}" />
			</div>
		</g:if>
		<p>
			<app:niceDateformat date="${post?.dateCreated}" /> &#149; <g:link controller="owoPostComment" action="create" id="${post?.id}" class="comment">Kommentieren</g:link>
		</p>
	</div>
	
	<div class="clear"></div>
	
	<g:if test="${post.comments?.size()}">
		<div class="comments">
			<g:each in="${post.comments}" var="comment">
				<g:render template="/owo/owoComment" model="[ comment: comment ]" />
			</g:each>
		</div>
	</g:if>
	<div class="commentForm" id="c_${post?.id}">
		<app:renderUserPicture user="${currentUser}" class="userPicture" />
		<div class="commentContent">
			<g:form controller="owoPostComment" action="save">
	            
	            <input type="hidden" name="post.id" value="${post?.id}" />
	            
	            <input name="content" id="commentField_${post.id}" class="validate comment" value="${owoPostCommentInstance?.content}" />
	            
	            <g:submitToRemote url="[ controller: 'owoPostComment', action: 'saveAjax']"
		            onSuccess="addOwoPostComment(${post.id}, data); \$('input#commentField_${post.id}').val('');" 
		            value="Erstellen" 
		            class="button submitComment" 
		            before="if( inputValidate( this ) ){" 
		            after=" }; " />
	            
	            <div class="clear"></div>
			</g:form>
			<div class="clear"></div>
		</div>
		<div class="clear"></div>
	</div>
	<div class="clear"></div>
</div>