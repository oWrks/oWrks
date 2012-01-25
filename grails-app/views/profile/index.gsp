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
	<title><g:message code="profile.index.title" default="Your Profile" /></title>
</head>
<body>
	<div class="box">
		<h2 class="info-list-header">
			<g:message code="profile.headline" default="Public profile"
				args="[entity.username]" />
			<al:profileEditButton user="${entity}" class="edit" />
			<!-- TODO: implement al:accountDeleteButton -->
		</h2>

		<sec:ifLoggedIn>
			<g:if test="${currentUser?.id != entity.id}">
				<p>
					<g:link controller="userMessage" action="create"
						id="${entity?.username}" class="dialog">Nachricht schreiben</g:link>
				</p>
			</g:if>
		</sec:ifLoggedIn>

		<g:render template="../error/beanErrors" />

		<div>
			<div class="col2">
				<img
					src="<g:createLink controller="image" action="profile" id="${entity.id}" />"
					alt="${entity.picture}" title="" width="120" />
				<%--<p>
					<strong><g:message code="user.score" />: ${entity?.score}
					</strong>
				</p> --%>
			</div>

			<div class="col1">
				<table>
					<tbody>
						<tr>
							<td><g:message code="user.firstName" />
							</td>
							<td>
								${entity?.firstName}
							</td>
						</tr>
						<tr>
							<td><g:message code="user.lastName" />
							</td>
							<td>
								${entity?.lastName}
							</td>
						</tr>
						<tr>
							<td><g:message code="user.birthday" />
							</td>
							<td>
							<g:formatDate date="${entity?.profile?.birthday}"
									format="dd.MM.yyyy" />
							</td>
						</tr>
						<tr>
							<td><g:message code="user.gender" />
							</td>
							<td>
								${entity?.profile?.gender}
							</td>
						</tr>
						<tr>
							<td><g:message code="user.city" />
							</td>
							<td>
								${entity?.profile?.city}
							</td>
						</tr>
						<tr>
							<td><g:message code="user.country" />
							</td>
							<td><g:if test="${entity.profile.country}">
									<g:country code="${entity?.profile?.country}" />
								</g:if>
							</td>
						</tr>
						<tr>
							<td><g:message code="user.description" />
							</td>
							<td>
								${entity?.profile?.description}
							</td>
						</tr>
						<tr>
							<td><g:message code="user.tags" />
							</td>
							<td>
								${entity?.profile?.tags?.join(', ')}
							</td>
						</tr>

						<tr>
							<td><g:message code="user.twitter" />
							</td>
							<td>
								${entity?.profile?.twitter}
							</td>
						</tr>
						<tr>
							<td><g:message code="user.skype" />
							</td>
							<td>
								${entity?.profile?.skype}
							</td>
						</tr>
						<tr>
							<td><g:message code="user.website" />
							</td>
							<td><a href="${entity?.profile?.website}" target="_blank">
									${entity?.profile?.website}
							</a>
							</td>
						</tr>
						<%--<tr>
							<td><g:message code="user.lastLogin" />
							</td>
							<td><g:formatDate date="${entity?.lastLogin}"
									type="datetime" style="MEDIUM" timeStyle="SHORT" />
							</td>
						</tr> --%>
					</tbody>
				</table>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<div class="box">
		<h2 class="info-list-header">
			<g:message code="profile.owos" default="My oWos"
				args="[entity.username]" />
		</h2>
		<g:if test="${activeOwos}">
			<g:each in="${activeOwos}" var="owo">
				<div class="owo-list-item">
					<img src="<g:createLink controller="image" action="owo" id="${owo.id }" />"
						alt="${owo.title}" class="small-icon" />
					<h2>
						<g:link controller="owo" id="${owo?.id }" action="show">
							${ owo?.title }
						</g:link>
						<span class="normal"><al:owoJoinLink owo="${owo}" class="button small" /></span>
					</h2>
					<p>${owo?.type}-oWo, Phase: ${owo?.workflowState}, <owo:freeSlots owo="${owo}" /> freie Plätze
					</p>
				</div>
				<div class="clear" style="border-bottom: 1px solid #e5e5e5"></div>
			</g:each>
			<div class="clear" style="margin-bottom: 20px;"></div>
		</g:if>
		
	</div>
</body>
</html>