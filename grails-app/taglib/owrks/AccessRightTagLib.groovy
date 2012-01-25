package owrks
/**
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
*/

import owrks.owo.Owo
import owrks.owo.OwoInvitation
import owrks.owo.OwoRequest
import owrks.owo.Performer
import owrks.user.User


class AccessRightTagLib {

	static namespace = "al"

	def owrksService

	/*
	 * Anfragen 
	 * (nicht teilnahme bestätigen!)
	 * */
	def owoJoinLink = { attrs, body ->
		
		User currentUser = owrksService.getCurrentUser()
		def owo = attrs.owo
		def performer = Performer.findByUserAndOwo(currentUser, owo)
		def owoRequest = OwoRequest.findByRequesterAndOwo(currentUser, owo)
		def owoInvitation = OwoInvitation.findByInviteeAndOwo(currentUser, owo)
		def cssClass = attrs.class ?: 'button right'
		if(owo.type != 'private' && owo.owner.email != currentUser?.email) {
			
			// wenn er perfomer ist, kurze info dass er mitmacht
			if (performer){
				out << "Du nimmst an diesem oWo teil."
			}
			// wenns ne einladung gibt kann der benutzer einfach bestätigen
			else if(owoInvitation) {
				def popup = !currentUser ? "dialog" : ""
				out << g.link(controller: "owoInvitation", action: "reject", id: owo.id, class: "button right $popup") { "ablehnen." }
				out << g.link(controller: "owoInvitation", action: "confirm", id: owo.id, class: "button right $popup") { "Einladung annehmen." }
				
			}
			// bei nem request wartet er noch auf bestätigung
			else if(owoRequest) {
				out << "Deine Anfrage wurde versendet."
			} 
			// keine einladung, keine anfrage aber das owo ist public: anfrage-button
			else if(owo.type == 'public') {
				def popup = !currentUser ? "dialog" : ""
				out << g.link(controller: "performer", action: "apply", id: owo.id, class: "$cssClass $popup") { "Anfragen" }
			}
			// community owo? dann kann er gleich teilnehmen
			else if(owo.type == 'community') {
				def popup = !currentUser ? "dialog" : ""
				out << g.link(controller: "owoRequest", action: "join", id: owo.id, class: "$cssClass $popup") { "Teilnehmen" }
			}
		}
		//else
		//	out << g.link(controller: "login", class: "button dialog") { "Join" }
	}
	
	/*
	* Einladung zu einem owo annehmen.
	* */
   def owoAcceptInvitationLink = { attrs, body ->
	   
	   def currentUser = owrksService.getCurrentUser()
	   def owo = attrs.owo
	   
	   def owoInvitation = OwoInvitation.findByInviteeAndOwo(currentUser, owo)
	   // todo invitation set to accept=false
	   def performer = Performer.findByUserAndOwo(currentUser, owo)
	   
	   if(!performer && owoInvitation && owo.type == 'public') {
		   
		   def popup = !currentUser ? "dialog" : ""
		   out << g.link(controller: "owoInvitation", action: "confirm", id: owo.id, class: "button right $popup") { body() }
	   }
   }

	def owoEditLink = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		Performer performer = currentUser ? Performer.findByUser(currentUser) : null
		def owo = attrs.owo
		//log.info(performer.isAdmin)
		if(owo.owner.email == currentUser?.email || performer?.isAdmin)
			out << g.link(action: "edit", id: owo.id, class:"edit")  { "edit" }
	}

	def owoDeleteButton = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def owo = attrs.owo
		if(owo && currentUser?.id == owo.owner.id) {
			out << g.form(controller:"owo", action:"delete", id:"${owo?.id}", method:"post", class: "owoForm") { '<input type="submit" name="delete" value="oWo löschen" class="delete" onclick="return confirm(\'Sind Sie sicher?\');" />' }
		}
	}

	def owoInviteLink = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def owo = attrs.owo
		if(owo && currentUser?.id == owo.owner.id)
			out << g.link(controller:"performer", action:"invite", id:"${owo.id}", class:"button"){ "Teilnehmer einladen" }
	}
	def owoSwitchWorkflowState = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def owo = attrs.owo
		def states = Owo.constraints.workflowState.inList
		def currentStateId = states.lastIndexOf(owo.workflowState)
		
		def nextState = (currentStateId+1) <= states.size() ? states[currentStateId+1] : null
		if(nextState && owo && currentUser?.id == owo.owner.id) {
			out << g.link(controller:"owo", action:"changeWorkflowState", class:"changeWorkflow button right", id:"${owo.id}", params:[workflowState:nextState]) {states[currentStateId] + " abschließen"}
		}
	}

	def missionEditLink = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def mission = attrs.mission
		if(mission) {
			def p = Performer.createCriteria()
			def performer = p.get {
				eq("owo", mission.owo)
				eq("user", currentUser)
			}
			if(currentUser?.id == mission.creator?.id || performer) {
				if(attrs.ajax) {
					out << g.remoteLink(controller:"mission", action:"editAjax", id:"${mission.id}", onLoading:"setLoading()", onLoaded:"setLoading(false)", update:"missionContent", class: "edit") { body() }
				} else {
					out << g.link(controller:"mission", action:"edit", id:"${mission.id}", class:"edit") { body() }
				}
			}
		}
	}
	
	def missionCreateLink = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def owo = attrs.owo
		if(currentUser && owo && owrksService.isPerformer(currentUser, owo))
			//out << g.link(controller: "mission", action: "create", id: owo.id, class: "dialog button right") { body() }
			out << g.link(uri: "/owo/missions/${owo.id}#/mission/createAjax/${owo.id}", class: "button right") { body() }
	}

	def missionListLink = { attrs, body ->
		def mission = attrs.mission
		if(mission)
			out << g.link(controller:"mission", action:"list", id:"${owoInstance.id}", class:"small") { "all" }
	}
	
	/*
	 * Mission creator oder Owo owner 
	 * können eine Mission löschen
	 */
	def missionDeleteButton = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def mission = attrs.mission
		if(currentUser && mission && (currentUser.id == mission.creator.id || currentUser.id == mission.owo.owner.id))
			out << g.form( controller:"mission", action:"delete", id:"${mission.id}", class: "missionForm") { '<input type="submit" value="delete" class="delete" />' }
	}
	
	/*
	 * ajax function
	 */
	def missionDeleteLink = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def mission = attrs.mission
		if(currentUser && mission && (currentUser.id == mission.creator?.id || currentUser.id == mission.owo.owner.id))
			out << g.remoteLink(controller: "mission", action: "deleteAjax", id: mission.id, onSuccess: "afterDeleteFadeOut('div#mission_${mission.id}');", class: "deleteMission", title: "Mission löschen") { "" }
	}

	def stepEditLink = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def step = attrs.step
		if(currentUser && step && (currentUser.id == step.createdBy?.id || currentUser.id == step.assignedTo?.id)) {
			if(attrs.ajax) {
				out << g.remoteLink(controller:"step", 
					action:"editAjax", 
					id:"${step.id}", 
					onLoading:"setLoading(); ", 
					onSuccess:"setLoading(false);setAddress('step/editAjax/${step.id}');", 
					update:"missionContent", 
					class: "edit") { "edit" }
			} else {
				out << g.link( controller:"step", action:"edit", id:"${step.id}", class:"edit dialog") { "edit" }
			}
		}
	}
	
	def stepDeleteButton = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def step = attrs.step
		if(currentUser && step && (currentUser.id == step.createdBy?.id || currentUser.id == step.assignedTo?.id || currentUser.id == step.mission.owo.owner.id))
			out << g.form( controller:"step", action:"delete", id:"${step.id}", class: "stepForm") { '<input type="submit" value="delete" class="delete" />' }
	}
	
	/*
	 * ajax function
	 */
	def stepDeleteLink = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def step = attrs.step
		if(currentUser && step && (currentUser.id == step.createdBy?.id || currentUser.id == step.assignedTo?.id || currentUser.id == step.mission.owo.owner.id))
			//out << g.form( controller:"step", action:"delete", id:"${step.id}", class: "stepForm") { '<input type="submit" value="delete" class="delete" />' }
			out << g.remoteLink(controller: "step", action: "deleteAjax", id: step.id, onSuccess: "afterDeleteFadeOut('#step_${step.id}');", class: "deleteStep", title: "Step löschen") { "" }
	}

	def stepCreateLink = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def mission = attrs.mission
		if(currentUser && mission && owrksService.isPerformer(currentUser, mission.owo)){
			if(attrs.ajax) {
				out << g.remoteLink(controller:"step", 
					action:"createAjax", 
					id:"${mission.id}", 
					onLoading:"setLoading()", 
					onLoaded:"setLoading(false);setAddress('step/createAjax/${mission.id}')", 
					update:"missionContent", 
					class: "create step") { body() }
			} else {
				out << g.link(controller:"step", action:"create", id:"${mission.id}", class:"create step") { body() }
			}
		}
	}
	
	/*
	 * @param owo Owo
	 * @see owrksService.isPerformer()
	 */
	def isPerformer = { attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def owo = attrs.owo
		if(owrksService.isPerformer(currentUser, owo))
			out << body()
	}

	/*
	 * Angemeldete User kann sich aus einem Owo abmelden
	 * (außer er ist der owner)
	 */
	def performerDeleteMe = { attrs, body ->
		def owo = attrs.owo
		def currentUser = owrksService.getCurrentUser()
		def performer = attrs.performer
		if(currentUser && owo && performer && performer.user.id == currentUser.id && performer.owo.owner.id != currentUser.id) {
			out << "(" << g.link(controller:"performer", action: "deleteMe", id: "${owo.id}") { body() } << ")"
		}
	}
	
	/*
	 * Entweder Link zum bestätigen oder zum Abmelden
	 */
	def performerConfirmRequester = { attrs, body ->
		def owo = attrs.owo
		def currentUser = owrksService.getCurrentUser()
		def requester = attrs.requester
		if(owo && requester) {
			if(currentUser?.id == owo.owner.id)
				out << g.link(controller:"performer", action: "accept", id: "${owo.id}", params: ['requester': requester.id]) {"bestätigen"}
			else if(currentUser?.id == requester.id)
				out << "(" << g.link(controller:"performer", action: "declineOwoRequest", id: "${owo.id}") { "Anfrage zurückziehen" } << ")"
			else
				out << "(Bestätigung offen)"
		}
	}
	
	def owoPostDeleteLink = { attrs ->
		def currentUser = owrksService.getCurrentUser()
		def owoPost = attrs.post
		if(owoPost?.creator == currentUser) {
			out << g.remoteLink(controller: "owoPost", action: "ajaxDelete", id: owoPost.id, onSuccess: "afterDeleteFadeOut('div#post_$owoPost.id'); afterDeleteFadeOut('div#attachment_$owoPost.id')", class: "deletePost") { "" }
		}
	}
	
	def owoPostCommentDeleteLink = { attrs ->
		def currentUser = owrksService.getCurrentUser()
		def owoPostComment = attrs.comment
		if(owoPostComment?.creator == currentUser) {
			out << g.remoteLink(controller: "owoPostComment", action: "ajaxDelete", id: owoPostComment.id, onSuccess: "afterDeleteFadeOut('div#comment_$owoPostComment.id')") { "&#149; Löschen" }
		}
	}
	
	def stepCommentDeleteLink = { attrs ->
		def currentUser = owrksService.getCurrentUser()
		def stepComment = attrs.comment
		if(stepComment?.creator == currentUser) {
			out << g.remoteLink(controller: "stepComment", action: "deleteAjax", id: stepComment.id, onSuccess: "afterDeleteFadeOut('div#stepComment_$stepComment.id'); decreaseItemCount('showComments');", class: "deleteWallItem", title: "Kommentar löschen") { "" }
		}
	}
	
	def stepAttachmentDeleteLink = { attrs ->
		def currentUser = owrksService.getCurrentUser()
		def stepAttachment = attrs.attachment
		if(stepAttachment?.uploader == currentUser) {
			out << g.remoteLink(controller: "stepAttachment", action: "deleteAjax", id: stepAttachment.id, onSuccess: "afterDeleteFadeOut('div#stepAttachment_${stepAttachment.id}'); decreaseItemCount('showAttachments');", class: "deleteWallItem", title: "Datei löschen") { "" }
		}
	}
	
	def stepLinkDeleteLink = { attrs ->
		def currentUser = owrksService.getCurrentUser()
		def stepLink = attrs.link
		if(stepLink?.creator == currentUser) {
			out << g.remoteLink(controller: "stepLink", action: "deleteAjax", id: stepLink.id, onSuccess: "afterDeleteFadeOut('div#stepLink_$stepLink.id'); decreaseItemCount('showLinks');", class: "deleteWallItem", title: "Link löschen") { "" }
		}
	}
	
	def stepTextDeleteLink = { attrs ->
		def currentUser = owrksService.getCurrentUser()
		def stepText = attrs.stepText
		if(stepText?.creator == currentUser) {
			out << g.remoteLink(controller: "stepText", action: "deleteAjax", id: stepText.id, onSuccess: "afterDeleteFadeOut('div#stepText_${stepText.id}'); decreaseItemCount('showTexts');", class: "deleteWallItem", title: "Text löschen") { "" }
		}
	}

	def stepTextEditLink = { attrs ->
		def currentUser = owrksService.getCurrentUser()
		def stepText = attrs.stepText
		if(stepText?.creator == currentUser) {
			out << "<span id=\"textEdit_${stepText.id}\" class=\"stepText edit\">&nbsp;</span>"
		}
	}
	
	/**
	 * Renders an button to delete an users account
	 * User-Rights are checked, so loggedin user must equal the user to delete
	 * @attr user
	 */
	def accountDeleteButton = { attrs ->
		def currentUser = owrksService.getCurrentUser()
		def profileUser = attrs.user
		if(profileUser?.id == currentUser?.id) {
			out << g.link(controller: "user", action: "deleteUser", class: attrs.class) { "Account l&ouml;schen" }
		}
	}
	def profileEditButton = {  attrs, body ->
		def currentUser = owrksService.getCurrentUser()
		def profileUser = attrs.user
		if(profileUser?.id == currentUser?.id) {
			out << g.link(controller: "profile", action: "edit", class: attrs.class) { "edit" }
		}
	}
}
