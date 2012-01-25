<ul id="topnav">
	<li id="home"><g:link mapping="index"><img src="${resource(dir: 'images', file: 'home.png')}" alt="Startseite" title="Startseite" width="18" /></g:link>
	    			</li>
	<li><sec:ifLoggedIn>
			<g:link controller="profile" class="normal">
				<sec:loggedInUserInfo field="username" />
			</g:link> / <g:link controller="logout">Logout</g:link>
		</sec:ifLoggedIn> 
		<sec:ifNotLoggedIn>
			<a href="#" id="login">Login</a>
			<div id="login-box">
				<form action="${request.contextPath}/j_spring_security_check" method="post" id="loginForm" class="cssform">
					<label for="username">Username:</label> <input type="text" class="textfield" name="j_username" id="username" /> 
					<label for="password">Password:</label> <input type="password" class="textfield" name="j_password" id="password" /> 
					<!-- 
					<label for="remember_me">Remember me</label> <input type="checkbox" class="chk" name="${rememberMeParameter}" id="remember_me" <g:if test="${hasCookie}">checked="checked"</g:if> />  <br />
					 -->
					<input type="submit" name="submit" class="button" value="Login" />
				</form>
				<p><g:link controller="register" action="requestPassword" class="small">Passwort vergessen?</g:link></p>
			</div>
		</sec:ifNotLoggedIn>
	</li>
	
	<li>
		<sec:ifAllGranted roles="ROLE_ADMIN">
			<!-- TODO: nicht sichtbar in der beta-phase -->
			<sec:ifNotLoggedIn>
				<g:link controller="register">Sign Up</g:link>
			</sec:ifNotLoggedIn>
		</sec:ifAllGranted> 
		<sec:ifNotLoggedIn>
		&nbsp;
		</sec:ifNotLoggedIn>
		<sec:ifLoggedIn>
			<g:link controller="dashboard">Mein oWrks</g:link>
		</sec:ifLoggedIn>
	</li>
			
	<sec:ifLoggedIn>
		<li>
			<g:link controller="owo" action="list">Alle OWOs</g:link>		
		</li>
	</sec:ifLoggedIn>
	
	<sec:ifLoggedIn>
		<li>
			<g:remoteLink elementId="notification_icon" title="letzte Benachrichtigungen" class="tooltip" action="newUserNotifications" controller="notification" onSuccess="showNotification(data)">!</g:remoteLink>
		</li>
	</sec:ifLoggedIn>			
	
	<sec:ifLoggedIn>
		<li>
			<div class="search-box" id="searchbox">
				<g:form controller="search" method="post">
					<input type="text" size="20" class="search-field inputHelp" name="q" value="${params.q ?: ''}" id="searchField" <g:if test="${!params.q}">title="${g.message(code: 'search.input.help')}"</g:if> />
					<input type="submit" value="" class="search" />
				</g:form>
			</div>
		</li>
	</sec:ifLoggedIn>
</ul>
<div class="clear"></div>