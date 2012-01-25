<g:if test="${message}">
	<div id="ajaxmessage" style="margin:10px 0;">
		<p>${message}</p>
	</div>
	<jq:jquery>$('div#ajaxmessage').effect("highlight", {}, 7000);</jq:jquery>
</g:if>

<div id="errorList">
	<g:render template="/error/beanErrors" model="[ entity: entity ]" />
</div>