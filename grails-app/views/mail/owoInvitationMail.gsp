<%@ page contentType="text/html"%>
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
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><g:message code="mail.title.invitation" default="Welcome to oWo ${owo.title}" /></title>
	<g:render template="/mail/mainCssStyles" />

</head>
<body>
	<div id="content">
		<div class="box">
			<div id="logo">
				<g:link controller="register" absolute="true">
					<img
						src="${resource(dir:'images',file:'oWrks-logo.png', absolute: true)}"
						alt="oWrks" title="oWrks" />
				</g:link>
			</div>
			<h2>
				<g:message code="mail.welcome" default="Hello" />
				${owoInvitation.invitee.username},
			</h2>
			<p>
				<g:message code="mail.invitation.text"
					args="[owoInvitation.creator.user.username, owo.title]"
					default="Welcome to oWrks." />
				${owo.invitationText}
			</p>
			<p>
				<g:message code="mail.goodbye" default="Yours, oWrks-Team" />
			</p>
		</div>
	</div>
</body>
</html>