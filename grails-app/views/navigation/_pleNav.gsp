<%@ page import="owrks.user.UserMessage" %>
<g:set var="msgCount" value="${UserMessage.countByReceiverAndReadFlag(currentUser, false)}"/>
<nav:renderPleNav msgCount="${msgCount}" />

<sec:ifAllGranted roles="ROLE_ADMIN">
	<ul id="nav" style="float:left;">
		<li><g:link controller="user">Benutzer</g:link></li>
		<li><g:link controller="feedback">Feedback</g:link></li>
	</ul>
</sec:ifAllGranted>
<div class="clear"></div>