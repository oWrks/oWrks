<g:set var="videoId" value="${step.renderYoutubeVideoId(url: link.url)}" />
<div id="stepLink_${link?.id}" class="streamItem ${videoId ? 'stepVideo' : 'stepLink' }">
	<div id="link_${link?.id}">
		<app:renderUserPicture user="${link.creator}" class="userPicture" />
		<div class="stepPostContent">
			<p class="stepPostCreator"><g:link mapping="profile" id="${link?.creator?.id}">${link?.creator}</g:link> <app:niceDateformat date="${link?.dateCreated}" />:</p>
			<g:if test="${videoId}">
				<div class="wallItemContent">
					<iframe width="500" height="349" src="http://www.youtube.com/embed/${videoId}" frameborder="0">
					</iframe>
					<!-- <object width="480" height="385">
						<param name="movie" value="http://www.youtube.com/v/VIDEO-ID?fs=1&amp;hl=en_US&amp;PARAMETER"></param>
						<param name="allowFullScreen" value="true"></param>
						<param name="allowscriptaccess" value="always"></param>
						<embed src="http://www.youtube.com/v/VIDEO-ID?fs=1&amp;hl=en_US&amp;PARAMETER" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="480" height="385"></embed>
					</object>  -->
				</div>
			</g:if>
			<g:else>
				<div class="wallItemContent">
					<g:if test="${link.url.startsWith('http://') || link.url.startsWith('www.') || link.url.startsWith('https://')}">
						<a href="${link.url}" title="${link.title}">${link.url.size() > 80 ? link.url[0..80] : link.url}</a> ${link.title ? "($link.title)" : ''}
					</g:if>
					<g:else>
						${link.url.size() > 80 ? link.url[0..80] : link.url} ${link.title ? "($link.title)" : ''}
					</g:else>
				</div>		
			</g:else>
			<al:stepLinkDeleteLink link="${link}" />
			<span class="wall draggable">&nbsp;</span>
		</div>
		<div class="clear"></div>
	</div>
</div>