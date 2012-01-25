<%@ page import="owrks.admin.Feedback"%>
<jq:jquery>
	$('input#url').val(window.location);
</jq:jquery>
<div>
	<g:if test="${!success}">
		<p style="margin: 10px 0;">Fehler entdeckt? Oder Verbesserungsvorschlag? Lass es uns wissen!</p>
		<g:render template="../error/beanErrors" model="[ entity: feedbackInstance ]" />

		<g:formRemote name="feedback"
			url="[ controller: 'feedback', action: 'saveAjax', id: feedbackInstance?.id ]"
			method="post" controller="feedback" action="save"
			id="${feedbackInstance?.id}" update="fbContent" onLoading=""
			onLoaded="">

			<form:fieldWrapper name="topic" for="Betreff">
				<select id="topic" name="topic">
					<option value="Fehler / Bug">Fehler / Bug</option>
					<option value="Verbesserung">Verbesserung</option>
					<option value="Kommentar">Kommentar</option>
				</select>
			</form:fieldWrapper>

			<form:fieldWrapper name="message">
				<textarea name="message">${feedbackInstance?.message ?: ''}</textarea>
			</form:fieldWrapper>

			<form:fieldWrapper name="url">
				<g:textField name="url" value="${feedbackInstance?.url ?: request.forwardURI}" readonly="readonly" />
			</form:fieldWrapper>

			<div class="clear"></div>
			<g:submitButton name="create" class="save button right"
				value="${message(code: 'default.button.create.label', default: 'Create')}" />
			<div class="clear"></div>
		</g:formRemote>
	</g:if>
	<g:else>
		<p>Vielen Danke für deine Bemerkung!</p>
		<p>Wir werden die Anfrage in Kürze auswerten und uns ggfs. bei dir
			melden.</p>
	</g:else>
</div>
