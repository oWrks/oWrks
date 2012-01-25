<g:render template="../error/beanErrors" model="[ entity: owoInstance ]" />

<g:javascript>
	function highlightMessage() {
		$('#emailMessage').effect('highlight', {}, 2000);
		console.log("highlightMessage called!")
		$('#invitefriends').val('');
		$('#invitefriends').blur()
	}
</g:javascript>

<g:form action="sendInvitation" id="${owoInstance?.id}">
	<table>
		<tbody>
			<tr>
				<td><g:message code="performer.invitationText.label" />
				</td>
				<td><g:textArea id="invitationtext" name="invitationtext" cols="40" rows="5" value="${owoInstance?.invitationText ?: message(code: 'owo.invitation.text')}" />
				</td>
			</tr>
			<tr>
				<td>
					<g:message code="performer.inviteFriends.label" />
				</td>
				<td>
					<g:textArea escapeHtml="true" style="height: 50px;" id="invitefriends" name="invitefriends" value="${message(code: 'user.invite.email')}"
					onfocus="if(this.value == '${message(code: 'user.invite.email')}') {this.value = '';}"
					onblur="if (this.value == '') {this.value = '${message(code: 'user.invite.email')}';}"
					cols="40" rows="3">${emails}</g:textArea><img src="${resource(dir: 'images', file: 'information.png')}" class="tooltip right" title="${message(code: 'user.invite.email.tooltip')}" />
				</td>
			</tr>
			</tbody>
	</table>
	<g:submitToRemote id="${owoInstance?.id}" action="sendInvitation" update="invitedEmails" class="button" value="Einladung senden" onSuccess="highlightMessage()"/>
	<div class="clear"></div>
</g:form>