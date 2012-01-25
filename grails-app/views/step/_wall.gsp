<g:javascript>
	setCurrentStepId(${stepInstance.id});
</g:javascript>
<g:render template="wallHeader" />
<jq:jquery>

	// SORTABLE
	$("div#sortableStream").sortable({
		revert: true,
		opacity: 0.8,
		disabled: true,
		stop: function(event, ui) {
			var dataString = "";
			var dataArr = { 'stepComment': {}, 'stepText': {}, 'stepLink': {}, 'stepAttachment': {} };
			
			$(ui.item).parent('#sortableStream').children('div.streamItem').each(function(index){
				var Ausdruck = /(\w.+)_(.*)/;
			    Ausdruck.exec($(this).attr('id'));
			    var id = RegExp.$2;
			    var type = RegExp.$1;
				dataArr[type][id] = index+1;
			});
			
			
			jQuery.ajax({
				type:'POST',
				data: 'wallItems=' + JSON.stringify(dataArr), 
				url: '${createLink(controller: "step", action: "updateWallSequenceOrder") }',
				success:function(data,textStatus){
					//console.log(data);
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){}
			});
		}
	});
	
	// SORTABLE - toggle sortable
	$('input#toggleSortable').change(function(){
		var isDisabled = $('div#sortableStream').sortable('option', 'disabled');
		if(!isDisabled){
			$('#sortableStream').sortable('disable');
			$('span.draggable').hide();
			$("label[for=toggleSortable]").text('Reihenfolge der Beiträge ändern');
		} else {
			$('#sortableStream').sortable('enable');
			$('span.draggable').show();
			$("label[for=toggleSortable]").text('Reihenfolge der Beiträge ausschalten');
		}
	});
	//$('span.draggable').attr('title', 'Verschieb mich während du mich gedrückt haltest');
	//$('span.draggable').tipsy({live: true, trigger: 'manual', gravity:'e', fade: true});
	
	$('.tooltip').tipsy({gravity: 'n', live:true});	
</jq:jquery>
<div>
	<p><g:remoteLink controller="mission" 
					action="loadOwoMissionListAjax" 
					id="${stepInstance?.mission.owo.id}" 
					onLoading="setLoading()" 
					onSuccess="setLoading(false);setAddress('/missionList');" 
					update="missionContent"
					class="">&lsaquo; Zurück zur <b>Mission-Liste</b></g:remoteLink>
	<g:remoteLink controller="mission" 
					action="loadMissionListAjax" 
					id="${stepInstance?.mission.id}" 
					onLoading="setLoading()" 
					onSuccess="setLoading(false);setAddress('/mission/loadMissionListAjax/${stepInstance?.mission.id}');" 
					update="missionContent"
					class="" style="float:right;">Zur aktuellen Mission: <b>${stepInstance?.mission.title.size() > 30 ? (stepInstance?.mission.title[0..30] + '... ') : stepInstance?.mission.title} &rsaquo;</b></g:remoteLink>
		</p>
	<h1 style="margin-top:10px;"><span style="float:left">${stepInstance?.title}</span> <al:stepEditLink step="${stepInstance}" ajax="${true}">edit</al:stepEditLink></h1>
	<p style="clear:both;margin-bottom:25px;">${stepInstance?.description}</p>
			
	<g:render template="/error/beanErrors" model="[ entity: stepCommentInstance ]" />
		
	<div id="posts">
		<g:if test="${!stepInstance?.finished}">
			<div id="wallForm">
				<ul id="postNav">
					<li id="addMessageLink">Kommentar</li>
					<al:isPerformer owo="${stepInstance?.mission?.owo}">
						<li id="addTextLink">Text</li>
						<li id="addAttachmentLink">Datei</li>
						<li id="addVideoLink">Video</li>
						<li id="addLinkLink">Link</li>
					</al:isPerformer>
				</ul>
				<div class="clear"></div>
			
				<div id="addMessage" class="stepForm">
					<g:formRemote name="stepComment" 
						url="[ controller: 'stepComment', action: 'saveAjax', id: stepInstance?.id ]" 
						method="post" 
						controller="stepComment" 
						action="save" 
						id="${stepInstance?.id}"
						before="if( inputValidate('postMessageContent')) {" 
						after=" }; "
						onLoading="setLoadingImage('submitMessage')"
						onComplete="setLoadingImage(false); "
						onSuccess="addStreamItem(data); \$('#postMessageContent').val(''); increaseItemCount('showComments');"
						>
			            
			            <label for="content" style="display:block;">Deine Nachricht</label>
		            	<input type="text" name="content" id="postMessageContent" class="validate" style="height: 27px; overflow-y: hidden; width:470px;" value="${stepCommentInstance?.content}" title="${g.message(code: 'search.stepcomment.help')}" />
		           		<div class="buttonWrapper" id="submitMessage">
		           			<g:submitButton name="save" class="save" class="button right" value="${message(code: 'default.button.save.label', default: 'Speichern')}" />
		           		</div>
		            	<div class="clear"></div>
					</g:formRemote>
				</div>
				
				<al:isPerformer owo="${stepInstance?.mission?.owo}">
					<div id="addAttachment" class="stepForm">
						<div id="uploadWrapper">
							<div id="stepUpload">
								<noscript>
									<p>Please enable JavaScript to use file uploader.</p>
									<!-- TODO: <g:form controller="post" action="upload"
										method="post"
										enctype="multipart/form-data">
										<input type="file" name="qqfile" />
										<input type="submit" name="submit" value="submit" />
									</g:form> -->
								</noscript>
							</div>
							<div id="uploadStatus"></div>
							<div id="uploadResponse"></div>
						</div>
					</div>
					
					<div id="addLink" class="stepForm">
						<g:formRemote name="stepLink" 
							url="[ controller: 'stepLink', action: 'saveAjax', id: stepInstance?.id ]" 
							method="post" 
							controller="stepLink" 
							action="save" 
							id="${stepLinkInstance?.id}"
							before="if( inputValidate('linkUrl')) {" 
							after=" }; "
							onLoading="setLoadingImage('submitLink')"
							onComplete="setLoadingImage(false);"
							onSuccess="addStreamItem(data); \$('div#linkFields input[type=text]').val('');increaseItemCount('showLinks');">
				            
				            <div id="linkFields">
				            	<div style="float:left;">
				            		<label for="title">Titel</label>
				            		<input type="text" name="title" id="linkTitle" title="${g.message(code: 'search.steplink.title.help')}" value="${stepLinkInstance?.title}" style="width: 120px;" />
				            	</div>
				            	<div style="float:left;">
				            		<label for="title">URL</label>
				            		<input type="text" name="url" id="linkUrl" title="${g.message(code: 'search.steplink.url.help')}" value="${stepLinkInstance?.url}" style="width: 332px;" />
				            	</div>
				            	<div class="buttonWrapper" id="submitLink" style="clear:both; margin-top:10px;">
				           			<g:submitButton name="save" class="save" class="button right" value="${message(code: 'default.button.save.label', default: 'Speichern')}" />
				           		</div>
				           		<div class="clear"></div>
				            </div>
						</g:formRemote>
					</div>
					
					<div id="addVideo" class="stepForm">
						<g:formRemote name="stepLink" 
							url="[ controller: 'stepLink', action: 'saveAjax', id: stepInstance?.id ]" 
							method="post" 
							controller="stepLink" 
							action="save" 
							id="${stepLinkInstance?.id}"
							before="if( inputValidate('videoLinkUrl')) {" 
							after=" }; "
							onLoading="setLoadingImage('submitVideo')"
							onComplete="setLoadingImage(false);"
							onSuccess="addStreamItem(data); \$('div#linkFields input[type=text]').val('');increaseItemCount('showVideos');">
				            
				            <div id="linkFields">
				            	<label for="content" style="display:block;">Video URL</label>
				            	<input type="text" name="url" id="videoLinkUrl" title="${g.message(code: 'search.stepvideo.help')}" value="" style="width: 452px;" />
				            	<div class="buttonWrapper" id="submitVideo">
				           			<g:submitButton name="save" class="save" class="button right" value="${message(code: 'default.button.save.label', default: 'Speichern')}" />
				           		</div>
				           		<div class="clear"></div>
				            </div>
						</g:formRemote>
					</div>
					
					<div id="addText" class="stepForm">
						<g:formRemote name="stepEditor" 
							url="[ controller: 'stepText', action: 'saveAjax', id: stepInstance?.id ]" 
							method="post" 
							controller="stepText" 
							action="save" 
							id="${stepInstance.id}"
							before="\$('#stepTextContent').val(\$('.nicEdit-main').html()); if( inputValidate('stepTextContent')) {" 
							after=" }; "
							onLoading="setLoadingImage('saveTextButton')"
							onLoaded="setLoadingImage(false)"
							onSuccess="addStreamItem(data); \$('textarea#postContent').val(''); \$('.nicEdit-main').html(''); increaseItemCount('showTexts');"
							class="stepEditor">   
					            
					        <textarea name="content" id="stepTextContent" style="width: 470px;">${stepTextInstance?.content ?: ''}</textarea>
							
							<div class="buttonWrapper" id="saveTextButton">
								<g:actionSubmit class="save button right" action="save" value="${message(code: 'default.button.save.label', default: 'Speichern')}" />
							</div>
							<div class="clear"></div>
						</g:formRemote>
					</div>
				</al:isPerformer>
			</div>
		</g:if>
		<g:else>
			<p><strong>Step ist abgeschlossen und kann nicht mehr bearbeitet werden</strong></p>
		</g:else>
	
		<div id="streamContentWrapper">
			<div id="stepStream">
				<div id="sortableStream">
					<g:render template="stepWallPosts" model="[ wallPosts: wallPosts, stepInstance: stepInstance, currentUser: currentUser ]" />
				</div>
			</div>
			
			<div id="streamFilter">
				<h3>Filter</h3>
				<div class="filterContent">
					<div class="stepCommentFilter">
						<g:checkBox name="showComments" checked="true" id="stepCommentFilter" class="filterItem" />
						<label for="showComments">Kommentare (<span>${navCount?.msgCount ?: '0'}</span>)</label>
					</div>
					
					<div class="stepTextFilter">
						<g:checkBox name="showTexts" checked="true" id="stepTextFilter"  class="filterItem" />
						<label for="showTexts">Texte (<span>${navCount?.textCount ?: '0'}</span>)</label>
					</div>
					
					<div class="stepLinkFilter">
						<g:checkBox name="showLinks" checked="true" id="stepLinkFilter"  class="filterItem" />
						<label for="showLinks">Links (<span>${navCount?.linkCount ?: '0'}</span>)</label>
					</div>
					
					<div class="stepAttachmentFilter">
						<g:checkBox name="showAttachments" checked="true" id="stepAttachmentFilter"  class="filterItem" />
						<label for="showAttachments">Dateien (<span>${navCount?.attachmentCount ?: '0'}</span>)</label>
					</div>
					
					<div class="stepVideoFilter">
						<g:checkBox name="showVideos" checked="true" id="stepVideoFilter"  class="filterItem" />
						<label for="showVideos">Videos (<span>${navCount?.attachmentCount ?: '0'}</span>)</label>
					</div>
					
					<div style="margin-top:20px;">
						<input type="checkbox" name="toggleSortable" class="filterItem" id="toggleSortable" />
						<label for="toggleSortable" class="tooltip" title="Ist die Sortierug aktiviert, erscheint bei jedem Beitrag ein Symbol. Durch gedrückt halten und verschieben kannst du die Sortierung ändern.">Reihenfolge der Beiträge ändern</label>
					</div>
				</div>
			</div>
			
			<div id="stepDescription">
				<h3>Step-Info</h3>
				<div id="stepDescriptionContent">
					<g:render template="stepDescription" model="[ stepInstance: stepInstance, currentUser: currentUser ]" />
				</div>
			</div>
			
			<div class="clear"></div>
		</div>
		
		<div class="clear"></div>
	</div>			
</div>