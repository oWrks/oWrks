<g:javascript>var textEditor;</g:javascript>
<jq:jquery>
	textEditor = new nicEditor({
			buttonList : ['fontSize','bold','italic','underline','strikethrough','subscript','superscript', 'ol', 'ul', 'forecolor', 'bgcolor'],
			iconsPath : '${resource(dir:'images', file:'nicEditorIcons.gif') }',
			maxHeight : 200,
			xhtml : true,
	}).panelInstance('stepTextEdit_${stepText.id}');
</jq:jquery>
<g:javascript>
	function updateTextEditField() {
		var content = textEditor.instanceById('stepTextEdit_${stepText.id}').getContent();
		$('textarea#stepTextEdit_${stepText.id}').val(content);
	}
	
	function unbindTextEditor() {
		textEditor.removeInstance('stepTextEdit');
	}
</g:javascript>
<g:formRemote name="stepEditor" 
	url="[ controller: 'stepText', action: 'updateAjax', id: stepText.id ]" 
	method="post" 
	controller="stepText" 
	action="updateAjax" 
	id="${stepText.id}"
	before="updateTextEditField();"
	onLoading="setLoadingImage('saveTextButton')"
	onLoaded="setLoadingImage(false)"
	onSuccess="\$('div#stepText_${stepText.id} div.wallItemContent').html(data)"
	class="stepEditor">


	<textarea name="content" id="stepTextEdit_${stepText.id}" style="width: 470px;">${stepText?.content ? stepText?.content : ''}</textarea>
	
	<div class="buttonWrapper" id="saveTextButton">
		<g:actionSubmit class="save button right" action="save" value="${message(code: 'default.button.save.label', default: 'Speichern')}" />
		<span class="button right" onclick="$('div#stepText_${stepText.id} div.wallItemContent').html('<%=stepText.content.encodeAsHTML() %>'); unbindTextEditor();">${message(code: 'default.button.cancel.label', default: 'Abbrechen')}</span>
	</div>
	<div class="clear"></div>

</g:formRemote>
