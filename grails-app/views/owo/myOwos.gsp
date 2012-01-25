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
<title><g:message code="owo.myOwos.title" default="My oWos" />
</title>
<meta name="layout" content="main" />
</head>
<body>
  <div class="box">
	<div class="col1">
			<div>
				<h2 class="owo-list-header">Neues oWo: <g:link controller="owo" action="create" class="button normal">oWo erstellen</g:link></h2>
			</div>
			<g:if test="${owosCreatedByMe}">
				<h2 class="owo-list-header">
					Von mir erstellte oWos
				</h2>
				<g:each in="${owosCreatedByMe}" var="owo">
					<div class="owo-list-item">
						<img src="<g:createLink controller="image" action="owo" id="${owo.id }" />"
							alt="${owo.title}" class="small-icon" />
						<h2>
							<g:link controller="owo" id="${owo.id }" action="show">
								${ owo.title }
							</g:link>
						</h2>
	
						<g:set var="owoRequests" value="${owo.getPendingRequest()}"/>
						<!-- -1, weil man selber also owo owner nicht mit reingezählt werden sollte -->
						<g:set var="counter" value="${owoRequests.size()}" scope="page" />
	
						<g:if test="${counter > 0}">
							<g:set var="userlist"
								value="${owoRequests*.requester.username}"
								scope="page" />
							<g:link class="tooltip" title="Benutzer auf der Warteliste: ${userlist}"
								action="performer" id="${owo?.id}">
								${counter} User ${counter > 1 ? "warten" : "wartet"} auf deine Bestätigung.
							</g:link>
						</g:if>
					</div>
					<div class="clear" style="border-bottom: 1px solid #e5e5e5"></div>
				</g:each>
			</g:if>
			
			<div class="clear"></div>

			<g:if test="${activeOwos}">
				<h2 class="owo-list-header">An diesen oWos nehme ich teil</h2>
				<g:each in="${activeOwos}" var="owo">
					<div class="owo-list-item">
					<img src="<g:createLink controller="image" action="owo" id="${owo.id }" />"
						alt="${owo.title}" class="small-icon" />
						<h2>
							<g:link controller="owo" id="${owo?.id }" action="show">
								${ owo?.title }
							</g:link>
							<g:link controller="performer" id="${owo?.id}" action="deleteMe"
							class="button normal">Abmelden</g:link>

						</h2>
						
					</div>
					<div class="clear" style="border-bottom: 1px solid #e5e5e5"></div>
				</g:each>		
			</g:if>
			<div class="clear"></div>
			
			<g:if test="${myOwoInvitations}">
				<h2 class="owo-list-header">Ich wurde zu diesen oWos eingeladen</h2>
				<g:each in="${myOwoInvitations}" var="${owoInvitation}">
					<div class="owo-list-item">
					<img src="<g:createLink controller="image" action="owo" id="${owoInvitation.owo?.id }" />"
						alt="${owoInvitation.owo?.title}" class="small-icon" />
						<h2>
							<g:link controller="owo" id="${owoInvitation.owo?.id }"
								action="show">
								${ owoInvitation.owo.title }
							</g:link>
							<g:link controller="owoInvitation" id="${owoInvitation.owo?.id}"
							action="confirm" class="button normal">Einladung bestätigen</g:link>
						</h2>				
					</div>
					<div class="clear" style="border-bottom: 1px solid #e5e5e5"></div>
				</g:each>
			</g:if>
			<div class="clear"></div>
			<g:if test="${myOwoRequests}">
				<h2 class="owo-list-header">oWos auf der Warteliste</h2>
				<g:each in="${myOwoRequests}" var="${owoRequest}">
					<div class="owo-list-item">
					<img src="<g:createLink controller="image" action="owo" id="${owoRequest.owo?.id }" />"
						alt="${owoRequest.owo?.title}" class="small-icon" />
						<h2>
							<g:link controller="owo" id="${owoRequest.owo.id }" action="show">
								${ owoRequest.owo.title }
							</g:link>
							<g:link controller="performer" id="${owoRequest.owo.id}"
							action="declineOwoRequest" class="button normal">Abmelden</g:link>
						</h2>
					</div>
					<div class="clear" style="border-bottom: 1px solid #e5e5e5"></div>
				</g:each>
			</g:if>
			<div class="clear" style="margin-bottom: 20px;"></div>
	</div>

	<div class="col2">
		<div class="box info-box">
			<h2 class="box-header">oWos, die dich vielleicht interessieren?</h2>
			<g:render template="owoTeaser" model="[ 'owos': newestOwos ]"></g:render>
		</div>
	</div>
	<div class="clear"></div>
  </div>
</body>
</html>