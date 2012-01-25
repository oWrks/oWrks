<div id="stepAttachment_${stepAttachment?.id}" class="streamItem stepAttachment">
	<div id="attachment_${stepAttachment.id}">
		<app:renderUserPicture user="${stepAttachment.uploader}" class="userPicture" />
		<div class="stepPostContent">
			<p class="stepPostCreator"><g:link mapping="profile" id="${stepAttachment.uploader?.id}">${stepAttachment.uploader}</g:link> <app:niceDateformat date="${stepAttachment.dateCreated}" />:</p>
			<div class="wallItemContent">
				<step:renderAttachmentImage attachment="${stepAttachment}" />
			</div>
			<al:stepAttachmentDeleteLink attachment="${stepAttachment}" />
			<span class="wall draggable">&nbsp;</span>
		</div>
		<div class="clear"></div>
	</div>
</div>