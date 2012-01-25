<%@ page import="owrks.user.Profile"%>
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
<meta name="layout" content="main" />
<title><g:message code="profile.edit.title"
		default="Edit your Profile" />
</title>
<script src="${resource(dir: 'js/uniform', file: 'jquery.uniform.js')}"
	type="text/javascript"></script>
<link
	href="${resource(dir: 'css/uniform', file: 'uniform.default.css')}"
	media="screen" rel="stylesheet" type="text/css" />
<g:javascript>
           	$(document).ready(function(){
	       		
	       		$("select").uniform();
	       		$( "#birthday" ).datepicker({ dateFormat: 'dd.mm.yy' });
	       		
           	})
       </g:javascript>
</head>
<body>
	<div class="box">
		<h2>
			<g:message code="profile.headline"
				default="Manage your public profile" args="[ entity.username ]" />
		</h2>

		<g:render template="../error/beanErrors" />

		<g:form class="normal" action="save" method="post"
			enctype="multipart/form-data">

			<form:fieldWrapper name="username">
				<g:textField name="username" value="${entity?.username}"
					/>
			</form:fieldWrapper>

			<form:fieldWrapper name="firstName">
				<g:textField name="firstName" value="${entity?.firstName}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="lastName">
				<g:textField name="lastName" value="${entity?.lastName}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="email">
				<g:textField name="email" value="${entity?.email}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="picture">
				<input type="file" name="picture" id="picture" />
				<g:if test="${entity.picture}">
					<div style="float: right; width: 300px;">
						<p>
							<img
								src="<g:createLink controller="image" action="profile" id="${entity.id}" />"
								alt="${entity.picture}" title="" width="100" />

							<g:if test="${currentUser.id == entity.id}">

								<g:link action="deletePicture" class="button" id="${entity.id}"
									style="margin-top:-20px">
									<g:message code="profile.image.delete" default="delete image" />
								</g:link>

							</g:if>
						</p>
					</div>
				</g:if>
			</form:fieldWrapper>


			<form:fieldWrapper name="profile.gender">
				<g:select name="profile.gender"
					from="${Profile.constraints.gender.inList}"
					value="${entity?.profile?.gender}"
					noSelection="[ '': message(code: 'profile.gender.noselection', default:'Select your gender')]" />
			</form:fieldWrapper>

			<form:fieldWrapper name="birthday">
				<g:datePicker name="birthday" precision="day"
					value="${entity?.profile?.birthday}" default="none" noSelection="['':'--']"/>
			</form:fieldWrapper>

			<form:fieldWrapper name="profile.description">
				<g:textArea name="profile.description"
					value="${entity?.profile?.description}" rows="5" cols="40" />

			</form:fieldWrapper>

			<form:fieldWrapper name="profile.city">
				<g:textField name="profile.city" value="${entity?.profile?.city}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="profile.country">
				<g:countrySelect name="profile.country"
					value="${entity?.profile?.country}"
					noSelection="[ '': message(code: 'profile.country.noselection', default:'Select your country')]" />
			</form:fieldWrapper>

			<form:fieldWrapper name="profile.twitter">
				<g:textField name="profile.twitter"
					value="${entity?.profile?.twitter}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="profile.website">
				<g:textField name="profile.website"
					value="${entity?.profile?.website}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="profile.skype">
				<g:textField name="profile.skype" value="${entity?.profile?.skype}" />
			</form:fieldWrapper>

			<form:fieldWrapper name="profile.tags">
				<g:textField name="profile.tags"
					value="${entity?.profile?.tags?.join(', ')}" />
			</form:fieldWrapper>

			<div class="clear"></div>
			<br />
			<g:link mapping="changePassword" class="button left">Passwort ändern</g:link>
			<g:actionSubmit class="button right"
				value="${message(code: 'form.submit')}" action="save" />
			<div class="clear"></div>

		</g:form>

		<div class="clear"></div>
	</div>
</body>
</html>