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
	<title>Login</title>
	<meta name="layout" content="main" />
	<app:controllerStyle />
	<script type="text/javascript">
	$(document).ready(function(){
		  $("#j_username").focus();
		});
    </script>
</head>
<body>
	<div class="box">
		<h2><g:message code="default.login.headline" default="Bitte logge dich ein" /></h2>
		<form action="${postUrl}" method="post" id="loginForm" class="cssform">
			<form:fieldWrapper name="username">
		       <g:textField name="j_username" id="j_username" value="" />
			</form:fieldWrapper>
			<form:fieldWrapper name="password">
		       	<g:passwordField name="j_password" id="password" value="" />
			</form:fieldWrapper>	
			<!-- 
			<form:fieldWrapper name="remember_me">
		        <input type="checkbox" class="chk" name="${rememberMeParameter}" id="remember_me"
				<g:if test="${hasCookie}">checked="checked"</g:if> />
			</form:fieldWrapper>
			 -->					
			<div class="clear"></div>	
			<input type="submit" value="Login" class="button right" />	
			<div class="clear"></div>	
					
		</form>
		<p><g:link controller="register" action="requestPassword">Passwort vergessen?</g:link> </p>
	</div>

	<sec:ifAllGranted roles="ROLE_ADMIN">
		<!-- TODO: nicht sichtbar in der beta-phase -->
		<div class="box">
			<h2><g:message code="default.signup.headline" default="Mach mit bei oWrks" /></h2>
			<g:render template="../error/beanErrors" model="[ entity: entity ]" />
	
			<g:form controller="register" action="register">
	
				<form:fieldWrapper name="username">
					<g:textField name="username" value="${entity?.username}" />
				</form:fieldWrapper>
	
				<form:fieldWrapper name="email">
					<g:textField name="email" value="${entity?.email}" />
				</form:fieldWrapper>
	
				<form:fieldWrapper name="password">
					<g:passwordField type="password" name="password" value="" />
				</form:fieldWrapper>
	
				<form:fieldWrapper name="password2">
					<g:passwordField type="password" name="password2" value="" />
				</form:fieldWrapper>
				<div class="clear"></div>
				<input type="submit" name="submit" value="Sign Up"
					class="button right" />
				<div class="clear"></div>
			</g:form>
		</div>
	</sec:ifAllGranted>
	<div class="clear"></div>
</body>
