package owrks.owo
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

import grails.plugins.springsecurity.Secured
import owrks.controller.AController
import owrks.notification.NotificationType
import owrks.notification.OwoInvitationNotification
import owrks.notification.OwoNotification
import owrks.user.User

@Secured(['ROLE_USER'])	
class OwoInvitationController extends AController {

	def notificationService 
	
	def delete = {
		OwoInvitation owoInvitation = OwoInvitation.get(params.owoInvitation.id)
		if (owoInvitation) {
			// nur der Owner kann löschen
			if (owoInvitation.owo.owner == getCurrentUser()){
				try {
					owoInvitation.delete()
					flash.message = "Die OwoInvitation wurde gelöscht."
					flash.args = [params.owoInvitation.id]
					flash.defaultMessage = "OwoInvitation ${params.owoInvitation.id} deleted"
					redirect(action: "list", id: owoInvitation.owo.id)

					log.info "Einladung gelöscht."
					
					// Benarchrichtigung?
					
				}
				catch (org.springframework.dao.DataIntegrityViolationException e) {
					flash.message = "Fehler: Die OwoInvitation wurde nicht gelöscht"
					flash.args = [params.owoInvitation.id]
					flash.defaultMessage = "OwoInvitation ${params.owoInvitation.id} could not be deleted"
					redirect(action: "show", id: params.id)

					log.info "Der Performer nicht wurde gelöscht."
				}
			} else {
				flash.message = "Nur der Owner kann Performer löschen."
				log.info "Nur der Owner kann Performer löschen."
				redirect(action: "list", id: owoInvitation.owo.id)

			}

		}
		else {
			flash.message = "Fehler: Performer nicht gefunden."
			flash.args = [params.id]
			flash.defaultMessage = "Performer not found with id ${params.id}"
			redirect(action: "list")
			log.info "Der Performer wurde nicht gefunden."
		}
	}
	
	// list all invitations
    def index = { 
		render("OwoInvitationsController:")
		OwoInvitation.list().each { 
			render("<br />")
			render(it.invitee.username + " was invited by "+it.creator.user.username+" to owo "+ it.owo.title)	
		}
		
	}
	
	def inviteUser = { 
		def owo = Owo.get(params.owoId)
		def invitee = User.get(params.inviteeId)
		def currentUser = getCurrentUser()
		def creator = Performer.findByUserAndOwo(currentUser,owo)
		
		if (!creator){
			return render(status: 403, text: "Der aktuelle Benutzer ist nicht der Creator/Admin und kann nicht bestätigen.")
		}
		
		OwoInvitation invitation = new OwoInvitation( creator: creator, owo: owo, invitee: invitee )
		/*Owo.withTransaction { status -> 
			invitee.addToOwoInvitations(invitation)
			owo.addToOwoInvitations(invitation)
			try {
				owo.save() 
				invitee.save()
			} catch(Exception e) {
				status.setRollbackOnly()
			}
		}*/
		if (!invitation.hasErrors() && invitation.save()){
			def notification = new OwoInvitationNotification(action:NotificationType.INVITE_CREATED, actionByUser: currentUser, owoInvitation: invitation)
			notificationService.notify(notification)
			return render(template:"/performer/user", model:[ user: invitee, owoInstance: owo, 'userBox': 'rightInviteBox', 'showButton': 'unInvite'])
		} 
		render(status: 503, text: "Error while saving invitation")
	}
	
	// unInvite a User
	def unInviteUser = {
		def owo = Owo.get(params.owoId)
		def invitee = User.get(params.inviteeId)
		if(!owo)
			return render(status: 500, text: "Error: no oWo to handle.")
			
		// TODO all admins should be able to delete invites?
		def creator = Performer.findByUserAndOwo(getCurrentUser(),owo)
		
		try {
			OwoInvitation invitation = OwoInvitation.createCriteria().get{
				eq("owo", owo)
				eq("invitee", invitee)
				eq("creator", creator)
			}
		
			def tempinvitee = invitation.invitee
			invitation.delete(flsuh: true)
			
			def notification = new OwoNotification( action: NotificationType.INVITE_UNINVITED, actionByUser: currentUser, affectedUser: tempinvitee )
			notificationService.notify(notification)
			return render(template:"/performer/user",model:[user:invitee,owoInstance:owo,'userBox':'leftInviteBox', 'showButton':'invite'])
		} catch (Exception ex) {
			log.error "Error shooting the user from list: "+ex
			return render(status: 500, text: "Error: User still invited.")
		}
	}
	
	
	/*
	* Eine Einladung wird vom Eingeladenen bestätigt
	* und er wird als Performer zu dem owo erstellt
	*/
	def confirm = {
		Owo owo = Owo.get(params.id)
		def currentUser = getCurrentUser()
		def owoInvitation = OwoInvitation.findByOwoAndInvitee(owo, currentUser)
	   
		log.info "User "+currentUser +" confirms Invite: "+owoInvitation
	   
		if (!owoInvitation) {
			flash.message = "Schaut nicht so aus als hättest du schon angefragt?"
		redirect(url: request.getHeader('referer'))
		} else {
			flash.message = "Schön, du nimmst an diesem Owo teil."
			owoInvitation.accepted = true
		   
			try {
				if (!owoInvitation.save())
			   		throw new Exception("owoInvitation.save() failed!")
				def performer = new Performer(user: currentUser, owo: owo)
				owo.addToPerformers(performer)
				if (!owo.save()){
					owo.errors.each {
						log.error it
					}
					throw new Exception("owo.save() failed!")
				}
			   
				/* Notification */
				def notification = new OwoInvitationNotification(action:NotificationType.INVITE_CONFIRMED, actionByUser:currentUser,owoInvitation:owoInvitation)
				notificationService.notify(notification)
			   	/* End Notification */
			} catch (Exception ex) {
				log.error "Error confirming owoNotification: "+ex
				flash.message = "Es ist leider ein Fehler aufgetreten, bitte Confirm noch einmal."
		   }
	   }
	   redirect(controller: "owo", action: "show", id: owo.id)
   }
	
	/*
	* Eine Einladung wird vom Eingeladenen abgelehnt
	*/
	def reject = {
	   Owo owo = Owo.get(params.id)
	   def currentUser = getCurrentUser()
	   def owoInvitation = OwoInvitation.findByOwoAndInvitee(owo, currentUser)
	   
	   log.info "User " + currentUser + " declines Invite: " + owoInvitation
	   
	   if (!owoInvitation) {
		   flash.message = "Schaut nicht so aus als wärst du eingeladen?"
		   redirect(url: request.getHeader('referer'))
	   } else {
		   flash.message = "Du hast die Einladung abgelehnt."
		   
		   try {
			   owoInvitation.delete()
			   	/* Notification */
			   		def notification = new OwoNotification(action:NotificationType.INVITE_REJECTED, actionByUser:currentUser,owo:owo)
				    notificationService.notify(notification)
		   		/* End Notification */
		   } catch (Exception ex){
		   		log.error("Could not delete owoInvitation.")
				owoInvitation.erros.each { log.error(it) }
		   }
		   
		   
	   }
	   redirect(controller: "owo", action: "show", id: owo.id)
   }
}