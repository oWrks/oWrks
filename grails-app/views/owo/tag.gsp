<%@ page import="owrks.owo.Owo"%>
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
		<g:set var="entityName"
			value="${message(code: 'owo.label', default: 'Owo')}" />
		<title><g:message code="default.show.label" args="[entityName]" />
		</title>
		<jq:jquery>
				$(document).ready(function(){
					var dateObject = new Date();
				    var currentTabUniqueID =
				          dateObject.getFullYear() + '' +
				          dateObject.getMonth() + '' +
				          dateObject.getDate() + '' +
				          dateObject.getTime();
				     console.log(currentTabUniqueID);
				});
		</jq:jquery>
	</head>
	<body>
  		<div class="box">
			<g:if test="${owos.size() > 0}">
				<g:each in="${owos}" status="j" var="${owo}">
					<g:if test="${owo.type != 'private'}">
						<div class="owo-list-item">
								<h2><g:link controller="owo" id="${owo.id }" action="show">
									${ owo.title }
								</g:link>
								</h2>
					
								<p>Erstellt von: <g:link controller="profile" id="${owo.owner.id }">${owo.owner.username }</g:link><br /> 
								Freie Pl&auml;tze: <owo:freeSlots owo="${owo}" /> (${owo.type})<br />
								Phase: ${owo.workflowState}
								</p>
								<p class="right">
									<al:owoJoinLink owo="${owo}"/> 
								</p>
								<div class="clear"></div>
						</div>
					</g:if>
					<g:else>
						<g:render template="owoTagError"></g:render>
					</g:else>
				</g:each>
			</g:if>
			<g:else>
				<g:render template="owoTagError"></g:render>
			</g:else>
		</div>
	</body>
</html>