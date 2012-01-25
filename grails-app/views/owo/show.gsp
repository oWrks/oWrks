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
       		$(document).bind('reveal.facebox', function() {
		    	$( "#dueDate" ).datepicker({ dateFormat: 'dd.mm.yy' });
		  	}); 
		  	$(document).ready(function(){

				//Hide (Collapse) the toggle containers on load
				$(".toggle_container").show(); 
			
				//Switch the "Open" and "Close" state per click then slide up/down (depending on open/close state)
				$("h3.trigger").click(function(){
					$(this).toggleClass("active").next().slideToggle("slow");
					return false; //Prevent the browser jump to the link anchor
				});

			});
			
	</jq:jquery>
</head>
<body>
<div class="box">
	<div class="col1">
		<div class="owo-list-item">
			<h2 class="owo-list-header">oWo Info</h2>
			<h2 class="left">
				${owoInstance?.title}
			</h2>
			<div class="owoActions" style="margin-top: 13px;">
				<al:owoEditLink owo="${owoInstance}" />
				<al:owoDeleteButton owo="${owoInstance }" />
			</div>

			<div class="clear"></div>
			<div class="owo-info">
				<img src="<g:createLink controller="image" action="owo" id="${owoInstance.id}" />"
				alt="${owoInstance.title}" class="left" />

				<div class="right" style="width: 450px;">
					<p>Ziel:
					${owoInstance?.goal}
				</p>
				<p>
					<g:message code="owo.owner.username.label" default="Ersteller" />:
					${owoInstance?.owner.username}

					<br />
					
					<g:message code="owo.type.label" default="Type" />:
					${owoInstance?.type}

					<br />
					<g:message code="owo.workflowState.label" default="Phase" />:
					${owoInstance?.workflowState}


					<br />
					<g:message code="owo.startDate.label" default="oWo beginnt am" />:
					<g:formatDate date="${owoInstance?.startDate}" format="dd.MM.yyyy" />

					<br />
					<g:message code="owo.endDate.label" default="oWo endet am" />:
					<g:formatDate date="${owoInstance?.endDate}" format="dd.MM.yyyy" />
				</p>
				<p>Tags: 
					<g:if test="${owoInstance?.tags}">
						<g:each in="${owoInstance?.tags}" var="tag">
							<g:link controller="owo" action="tag" id="${tag.trim()}">${tag}</g:link>
						</g:each>
					</g:if>
				</p>

				<p>
					Twitter Hashtag:
					${owoInstance?.hashTag}

					<span style="float:right; font-weight:bold;">
						<al:owoJoinLink owo="${owoInstance}" />
					</span>
					<al:owoSwitchWorkflowState owo="${owoInstance}" />
					</p>
				<g:if test="${owoInstance?.type != 'private'}">
					<div class="right" style="margin: 10px -10px 0 0;">
						<g:render template="../likeButtons"></g:render>
					</div>
				</g:if>
				</div>
				<div class="clear"></div>
			</div>
			<div style="margin-top:10px; border-top:1px solid #E5E5E5;">
				<div>
					<h2 class="info-list-header">${owoInstance?.missions.size()} Missions in diesem oWo <span class="normal">( <g:link controller="owo" action="missions" id="${owoInstance?.id}" class="normal">Zur Mission-Liste</g:link> )</span></h2>
					<g:each in="${owoInstance.missions}" status="i" var="${mission}">

						<h3 class="trigger"><g:link controller="owo" action="missions" id="${mission.owo.id}" params="[ missionId: mission.id ]">${mission.title}</g:link></h3>
						<div class="toggle_container">
							<g:if test="${mission.steps}">
								<g:each in="${mission.steps}" var="step">
									<p><g:link controller="owo" action="missions" id="${step.mission.owo.id}" params="[ stepId: step.id ]" fragment="/step/wall/${step.id}">${step.title}</g:link> 
									<span class="assignedTo">zugewiesen an ${step.assignedTo ?: '-'} fällig am <g:formatDate date="${step.dueDate}" format="dd.MM.yyyy" /></span></p>
								</g:each>
							</g:if>
						</div>
					</g:each>
					<p>
						<al:missionCreateLink owo="${owoInstance}">Mission hinzufügen</al:missionCreateLink>
						<div class="clear"></div>
					</p>
				</div>
			</div>
			<div class="clear"></div>
			<g:if test="${notifications.size()}">
				<div style="margin-top:10px; border-top:1px solid #E5E5E5;">
					<div>				
						<h2 class="info-list-header">Letzte Aktivitäten in diesem oWo</h2>
							
						<ul>
						<g:each in="${notifications}" var="notification">
							<li>
							<notify:notification notification="${notification}"/>
							<li>
						</g:each>
						</ul>			
						
					</div>
				</div>
			</g:if>
			
			<div class="clear"></div>
		</div>
	</div>

	<div class="col2">
		<div class="box info-box">
			<g:if
				test="${owoInstance?.performers?.size() || owoInstance.owoRequests.size() || owoInstance.owoInvitations.size()}">
				<g:if test="${owoInstance.performers.size()}">
					<h2 class="box-header">Aktive Teilnehmer</h2>
					<ul>
						<g:each in="${owoInstance?.performers}" var="${performer}">
							<li><img
								src="<g:createLink controller="image" action="profile" id="${performer.user.id}" />"
								alt="${performer.user.username}"
								title="${performer.user.username}" width="30"
								style="vertical-align: middle" /> <g:link mapping="profile"
									id="${performer.user.id}" class="confirmed">
									${performer.user.username}
								</g:link> 
								<g:if test="${owoInstance.owner.id == performer.user.id || performer.isAdmin }">
									<span class="owoOwner">admin</span>
								</g:if> 
								<al:performerDeleteMe owo="${owoInstance}" performer="${performer}">austreten</al:performerDeleteMe> 
							</li>
						</g:each>
					</ul>
				</g:if>
				
				<g:if test="${(owoInstance.owner == currentUser)}">
					
					<g:if test="${pendingOwoRequests.size()}">
						<div id="RequestsOverviewBox">
								<h2 class="box-header">Anfragen </h2>	
								<g:render template="../performer/userList" model="['userList':pendingOwoRequests, 'userBox':'RequestsOverviewBox', 'showButton':'confirmRequestFromAnywhere']"></g:render>
						</div>
					</g:if>
					<g:if test="${userListInvitations.size()}">
						<div>
								<h2 class="box-header">Eingeladen</h2>
								<ul>
									<g:each in="${userListInvitations}" var="${invitedUser}">
										<li><img
											src="<g:createLink controller="image" action="profile" id="${invitedUser.id}" />"
											alt="${invitedUser.username}"
											title="${invitedUser.username}" width="30"
											style="vertical-align: middle" /> <g:link mapping="profile"
												id="${invitedUser.id}" class="confirmed">
												${invitedUser}
											</g:link>
										</li>
									</g:each>
								</ul>
						</div>
					</g:if>
				</g:if>
			</g:if>
			<g:else>
				<p>noch keine Teilnehmer</p>
			</g:else>
			<al:isPerformer owo="${owoInstance}">
				<p>
					<g:link controller="performer" action="list" id="${owoInstance?.id}"
						class="button">
						<g:message code="performer.list.label"
							default="Teilnehmer verwalten" />
					</g:link>
				</p>
			</al:isPerformer>
		</div>
	</div>
	<div class="clear"></div>
	</div>
</body>
</html>