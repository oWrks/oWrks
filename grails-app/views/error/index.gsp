<html>
<head>
<meta name="layout" content="main" />
<title><g:message code="default.error" args="[]" />
</title>
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
</head>
<body>

	<div class="box">
	
    <div class="error">
		<h2>
			<g:message code="default.error" args="[]" />
		</h2>
		<div>
			<h3>
				<g:message code="default.error.details" args="[]" />
			</h3>

			<strong>Error ${request.'javax.servlet.error.status_code'}:</strong>
			${request.'javax.servlet.error.message'}<br /> <strong>Servlet:</strong>
			${request.'javax.servlet.error.servlet_name'}<br /> <strong>URI:</strong>
			${request.'javax.servlet.error.request_uri'}<br />
			<g:if test="${exception}">
				<strong>Exception Message:</strong>
				${exception.message}
				<br />
				<strong>Caused by:</strong>
				${exception.cause?.message}
				<br />
				<strong>Class:</strong>
				${exception.className}
				<br />
				<strong>At Line:</strong> [${exception.lineNumber}] <br />
				<strong>Code Snippet:</strong>
				<br />
				<div class="snippet">
					<g:each var="cs" in="${exception.codeSnippet}">
						${cs?.encodeAsHTML()}<br />
					</g:each>
				</div>
			</g:if>
		</div>
		<g:if test="${exception}">
			<h3>Stack Trace</h3>
			<div class="stack">
				<pre>
					<g:each in="${exception.stackTraceLines}">
						${it.encodeAsHTML()}<br />
					</g:each>
				</pre>
			</div>
		</g:if>
		</div>
	</div>
</body>
</html>