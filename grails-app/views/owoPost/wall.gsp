<html>
	<head>
	<!--  
* This file is part of oWrks.
*
* oWrks is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
*  any later version.
*
* oWrks is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with oWrks.  If not, see <http://www.gnu.org/licenses/>.
-->
		<title><g:message code="owo.myOwos.title" default="Nachrichten" /></title>
		<meta name="layout" content="main" />
		<g:javascript>
			function addOwoPost(response) {
				// input wird validiert, daher kommen normalweise keine fehler zurück
				if(response.search("class=\"error\"") >= 0) {
					$(response).insertAfter("h1");
					$('div#errors').hide().slideToggle();
				} else {
					if($('#postList').children().size() > 0) {
						$(response).insertBefore('#postList .owoPost:first');
					} else {
						$('div#postList').append(response);
					}
					$('#postList .owoPost:first').hide().slideToggle();
					$('div#errors').remove();
				} 
			}
			
			function addOwoPostComment(cssId, response) {
				var insertPos = $('#c_' + cssId);
				$(response).insertBefore(insertPos);
				$(insertPos).prev('.postComment').hide().slideToggle();
			}
			
			function inputValidate(e) {
				// input feld darf nicht leer sein
				if($(e).parent().find('.validate').val()) {
					return true;
				 } else {
					return false;
				}
			}
			
			function validatePost(e) {
				// input feld darf nicht leer sein
				if($(e).parent().find('.validate').val()) {
					if($(e).parents('form').find('#attachment').val()) {
						//$(e).parents('form').attr( "enctype", "multipart/form-data" ).attr( "encoding", "multipart/form-data" ).submit();
						$(e).parents('form').submit();
					} else {
						return true;
					}
				 } else {
					return false;
				}
			}
			
			// deprecated: now in apllications.js (afterDeleteFadeOut())
			/*function afterDelete(selector) {
				$(selector).fadeOut('slow', function() {
					$(this).remove();
				});
			}*/
			
		</g:javascript>
		<jq:jquery>
			// jQuery Atmosphere Plugin, 
			// see: http://jfarcand.wordpress.com/2010/06/15/using-atmospheres-jquery-plug-in-to-build-applicationsupporting-both-websocket-and-comet/
			
			$("input#postContent").keypress(function(e) {
		        var k = e.keyCode || e.which;
		        if (k == 13) {
		          $('input#submitPost').click();
		          return false;
		        }
			});
			$('input.comment').live('keypress', function(e) {
		        var k = e.keyCode || e.which;
		        if (k == 13) {
		          $(this).next('input.submitComment').click();
		          return false;
		        }
			});
			
			// TODO: keypress zusammenfassen 			
			$('input.submitComment').live('keypress', function(e) {
		        var k = e.keyCode || e.which;
		        if (k == 13) {
		          $(this).click();
		          return false;
		        }
			}); 
			
			$('a.comment').live("click", function() {
				$(this).parents('.postContent').nextAll('.commentForm').slideToggle();
				$(this).parents('.postContent').nextAll('.commentForm').find('.validate').focus();
				return false;
			});
			
			$('p#addAttachment').click( function(){
				$(this).next('#attachmentWrapper').slideToggle();
			});
			
			$('input#postContent').focus();
			
		</jq:jquery>
	</head>
<body>
	<div class="box">
		<h1>oWo Pinnwand</h1>
		
		<g:render template="/error/beanErrors" model="[ entity: owoPostInstance ]" />
		
		<div id="posts">
			<div id="addPost">
				<g:form controller="owoPost" action="save" enctype="multipart/form-data" method="post">
		            <div id="postInput">
		            	<input type="hidden" name="owo.id" value="${owoInstance?.id}" />
		            	<input name="content" id="postContent" value="${owoPostInstance?.content}" class="validate" />
		           		<g:submitToRemote url="[ controller: 'owoPost', action: 'saveAjax']"
		            		onSuccess="addOwoPost(data); \$('input#postContent').val('');" value="Erstellen" id="submitPost" class="button" before="if( validatePost(this) ) {" after=" }; " />
		            	<div class="clear"></div>
		            </div>
		            <p id="addAttachment">Datei hinzufügen</p>
		            <div id="attachmentWrapper">
		            	<input type="file" name="filename" id="attachment" />
		            </div>
		             
		            <div class="clear"></div>
				</g:form>
				
			</div>
		
			<div id="postList">
				<g:each in="${owoInstance.posts}" var="post">
					<g:render template="/owo/owoPost" model="[ post: post, currentUser: currentUser ]" />
				</g:each>
			</div>
			
			<g:if test="${owoInstance?.posts?.size()}">	
				<div id="fileBox" class="box">
					<h2>Alle Dateien</h2>
					<owo:renderAllFiles owo="${owoInstance}" />
				</div>
			</g:if>
			<div class="clear"></div>
		</div>	
		
	</div>
</body>
</html>