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
import owrks.notification.OwoNotification
import owrks.notification.OwoRequestNotification
import owrks.owo.Owo;
import owrks.owo.OwoInvitation;
import owrks.owo.OwoRequest;
import owrks.owo.Performer;
import owrks.user.User
import owrks.user.UserInvitation


@Secured(['ROLE_USER'])
class PerformerController extends AController {

	def emailService
	def performerService
	def notificationService

	def index = { redirect(action: "list", params: params) }

	// the delete, save and update actions only accept POST requests
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
		Owo owoInstance = Owo.get(params.id)
		
		// check if user is performer
		if(!owrksService.isPerformer(getCurrentUser(), owoInstance)) {
			flash.message = "Du musst erst dem oWo beitreten, um die angeforderte Seiten aufrufen zu können."
			return forward(controller: "owo", action: "show", id: owoInstance.id)
		}
		
		def userInvitations = UserInvitation.findAllByOwo(owoInstance)
		def notInvitedUsers = null
		def invitedUsers = performerService.getUserListInvited(owoInstance)
		
		def	performerUserList = performerService.getUserListPerformers(owoInstance)
		def	requestingUsers = performerService.getUserListOwoRequests(owoInstance)
		
		[
			owoInstance: owoInstance,
			performerUserList: performerUserList,
			requestingUsers: requestingUsers,
			userInvitations: userInvitations,
			notInvitedUsers:notInvitedUsers,
			invitedUsers:invitedUsers,
		]
	}

	// DELETE a performer.
	// Owner is allowed or logged in user for himself
	// expected POST params: {performer.id}
	def delete = {

		def user = User.get(params.user.id)
		def owo = Owo.get(params.owo.id)
		
		Performer performerInstance = Performer.findByUserAndOwo(user,owo)

		if (performerInstance) {
			
			log.info "Trying to delete performer for the owo "+performerInstance.owo.title
			log.info "Who? " + getCurrentUser()
	
			// nur der Owner kann wen löschen oder man sich selbst
			if (performerInstance.owo.owner == getCurrentUser() || user == getCurrentUser()){
				try {
					def tempUserDeleted = performerInstance.user
					performerInstance.delete()
					flash.message = "Der Performer wurde gelöscht."
					flash.args = [params.id]
					flash.defaultMessage = "Performer ${params.id} deleted"
					//redirect(action: "list", id: performerInstance.owo.id)
					
					def owoInvitation = OwoInvitation.findByInviteeAndOwo(user,owo)
					def owoRequest = OwoRequest.findByRequesterAndOwo(user,owo)
					
					log.info("Deleting invites and requests: - invite:"+owoInvitation+ " - request:"+owoRequest)
					
					if(owoInvitation){
						try { owoInvitation.delete()
							log.info("1 invite removed.")
						} catch (Exception ex) { log.fatal("afterDelete failed: " + ex) }
					}
					
					if(owoRequest){
						try { owoRequest.delete()
							log.info("1 request removed.")
						} catch (Exception ex) { log.fatal("afterDelete failed: " + ex) }
					}
					
					log.info "Der Performer wurde gelöscht."
					/* Notification anlegen zur benachrichtigung */
					def orn = new OwoNotification(action:NotificationType.OWO_PERFORMER_DELETED, actionByUser:currentUser, affectedUser:tempUserDeleted,owo:owo)
					notificationService.notify(orn)
					
					return render("ok")
					
				}
				catch (org.springframework.dao.DataIntegrityViolationException e) {
					flash.message = "Fehler: Der Performer wurde nicht gelöscht"
					flash.args = [params.id]
					flash.defaultMessage = "Performer ${params.id} could not be deleted"
					render("error")
					log.info "Der Performer nicht wurde gelöscht."
				}
			} else {

				flash.message = "Nur der Owner kann Performer löschen."

				log.info "Nur der Owner kann Performer löschen."
				render("error:only admins.")
			}

		}
		else {
			flash.message = "Fehler: Performer nicht gefunden."
			flash.args = [params.id]
			flash.defaultMessage = "Performer not found with id ${params.id}"
			render("Performer not found")
			log.info "Der Performer wurde nicht gefunden."
		}

	}

	/*
	 * TODO: 
	 */
	def wizzardInviteAgiler = {
		def owoInstance = Owo.get(params.id)
		//def notInvitedUsers = performerService.getUserListNotYetInvited(owoInstance)
		def notInvitedUsers = null
		def invitedUsers = performerService.getUserListInvited(owoInstance)
		if (!owoInstance) {
			flash.message = "owo.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "Owo not found with id ${params.id}"
			return redirect(action: "list")
		}
		[owoInstance: owoInstance, notInvitedUsers: notInvitedUsers, invitedUsers: invitedUsers]
	}
	
	def wizzardInviteEmail = {
		log.info params
		Owo owoInstance = Owo.get(params.id)
		
		if (!owoInstance) {
			flash.message = "owo.not.found"
		}
		else{
			def userInvitations = UserInvitation.findAllByOwo(owoInstance)
			return [owoInstance: owoInstance,
				userInvitations: userInvitations]
		}
	}

	def sendInvitation = {
		if(params.id) {
			Owo owoInstance = Owo.get(params.id)
			String emails = params.invitefriends
			String invitationtext = params.invitationtext
			owoInstance.invitationText = invitationtext
			def emailMap = emails.tokenize(",; ")
			def userInvitations = []
			def owoInvitations = []
			
			// checkt die eingegeben emails
			boolean validEmails = true
			emailMap.each {
				String email = it.trim()
				if(!(email ==~ /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,4}/))
					validEmails = false
			}
			
			if(validEmails) {
				emailMap.each {
					Performer currentPerformer = Performer.findByUserAndOwo(getCurrentUser(), owoInstance)
					String email = it.trim()
					// check id email is not like ower owner and his own email
					if(email != owoInstance.owner.email && email != getCurrentUser().email) {
						// user with the email already exist?
						User user = User.findByEmail(email);
						if(user) {
							// performer with the user already exist?
							Performer performer = Performer.findByUserAndOwo(user, owoInstance)
							if(performer) {
								//flash.message += "$user wurde zum oWo: $owoInstance eingeladen<br />"
							} else {
								// neue owo-invitation
								OwoInvitation owoInvitation = new OwoInvitation(invitee: user, creator: currentPerformer, owo: owoInstance)
								if(owoInvitation.save()) {
									owoInstance.addToOwoInvitations(owoInvitation)
									owoInvitations.add(owoInvitation)
								} else {
									// TODO: user muss darüber informiert werden, das diese email bereits registriert und eingeladen ist
								}
							}
						} else {
							UserInvitation userInvitation = new UserInvitation(email: email, owo: owoInstance)
							if(userInvitation.save(flush: true)) {
								userInvitations.add(userInvitation)
							} else {
								//flash.message = "<br />Leider ist ein Fehler aufgetreten, bitte schick die Email(s) nochmal ab<br />"
								log.error userInvitation.errors
								return render(view: "invite", model: [owoInstance:owoInstance, emails: params.invitefriends])
							}
						}
					}
				}
				if(owoInstance.save(flush: true)) {
					log.info("Calling EmailService with " + owoInvitations.size() + " owoInvitations/emails")
					log.info(userInvitations.size() + " userInvitation/emails.")
					
					flash.message = "user.invited.byemail"
					return render(template: "invitedByEmail", model: [userInvitations: UserInvitation.findAllByOwo(owoInstance), message: "invitation.by.email.success"])
				} else {
					flash.message = "Leider ist ein Fehler aufgetreten, bitte schick die Emails nochmal ab"
					return render(template: "invitedByEmail", model: [userInvitations: UserInvitation.findAllByOwo(owoInstance), message: "invitation.by.email.not.success"])
				}
			} else {
				flash.message = "Emails are not valid! Please seperate Emails with , ; or  "
				flash.defaultMessage = "Emails are not valid"
				return render(template: "invitedByEmail", model: [userInvitations: UserInvitation.findAllByOwo(owoInstance), message: "invitation.by.email.not.success"])
			}
		}
	}

	@Deprecated
	def addPerformer = {
		Owo owoInstance = Owo.get(params.owoID)
		def user = User.get(params.userID)
		// TODO: what is when performer still exist with this user and owo?
		Performer performer = Performer.findByUserAndOwo(user,owoInstance) 
		
		if (performer){
			log.info "Trying to add a performer who already is active. request ignored."
			flash.message = "Der Benuter ist bereits ein aktiver Teilnehmer."
			request.status = 500
			return render("performer.already.active")
		}
			
		performer = new Performer(user:user)
		
		owoInstance.addToPerformers(performer);

		if (owoInstance.save()){
			log.info(performer.user.username + " added to performers.")
			//redirect(controller: "performer", action: "list", id: params.owoID)
			// Render the user to add him with ajax to the performer list
			render(template:"/performer/user",model:[user:performer.user,owoInstance:owoInstance,'userBox':'leftPerformerBox', 'showButton':'deletePerformer'])
		} else {
			log.error "Error saving oWo with new Performers."
			flash.message = "Fehler beim hinzufügen von Performern zum oWo."
			redirect(controller: "performer", action: "list", id: params.owoID)
		}
	}

	def addPerformers = {
		Owo owoInstance = Owo.get(params.id)
		params.user.each {
			def user = User.get(it)
			// TODO: what is when performer still exist with this user and owo?
			Performer performer = new Performer(user:user)
			owoInstance.addToPerformers(performer);
		}

		if (owoInstance.save()){
			redirect(controller: "performer", action: "list", id: params.id)
		} else {
			log.error "Error saving oWo with new Performers."
			flash.message = "Fehler beim hinzufügen von Performern zum oWo."
			redirect(controller: "performer", action: "list", id: params.id)
		}
	}

	/*
	 * Bei einem public oWo anfragen.
	 * oder community: Direktes erstellen eines Performers
	 * TODO sollte eigentlich im OwoRequest Controller sein diese methode.
	 */
	def apply = {
		Owo owoInstance = Owo.get(params.id)
		def currentUser = getCurrentUser()

		if(owoInstance.type == "community") {
			Performer performer = new Performer(user:currentUser)
			owoInstance.addToPerformers(performer)
			if(owoInstance.save()) {
				flash.message = "Bravo, du kannst direkt loslegen."
				
				/* Notification anlegen zur benachrichtigung */
				def orn = new OwoNotification(action:NotificationType.OWO_PERFORMER_JOINED, actionByUser:currentUser,owo:owoInstance)
				notificationService.notify(orn)
				
				return redirect(controller: "owo", action: "show", id: owoInstance.id)
				
			} else {
				flash.message = "Da ist was schiefgelaufen. Sorry."
			}
		} else if(owoInstance.type == "public") {

			OwoRequest owoRequest = new OwoRequest(requester: currentUser, owo: owoInstance)
			owoInstance.addToOwoRequests(owoRequest)

			if(owoInstance.save(flush:true)) {
				flash.message = "Anfrage versendet. Ein Admin muss diese noch bestätigen."
				
				/* Notification anlegen zur benachrichtigung */
				def orn = new OwoRequestNotification(action:NotificationType.OWO_REQUEST, actionByUser:currentUser,owoRequest:owoRequest)
				notificationService.notify(orn)
				
			} else {
				// requester hat sich vermutlich schon einmal anbgemeldet
				def errorCode = owoInstance.errors?.getFieldError("owoRequests.requester")?.code
				flash.message = g.message(code: errorCode)
			}
			
			return redirect(controller: "owo", action: "show", id: owoInstance.id)
			
		} else {
			flash.message = "Das ist kein öffentliches Owo, du kannst nicht anfragen."
			return redirect(url: request.getHeader('referer'))
		}
		redirect(controller: "owo", action: "myOwos")
	}

	// TODO: existiert diese Methode? Wenn ja anpassen an neues Datenmodell - habe ich in OwoRequestController geschoben
	@Deprecated
	def confirm = {

		Performer p = Performer.get(params.performer.id)
		log.info p.owo.owner
		log.info getCurrentUser()
		if (p.owo.owner == getCurrentUser()){

			if (p.ownerConfirmed){ // already confirmed?
				flash.message = p.user.username+" ist schon bestätigt."
			} else {
				p.ownerConfirmed = true
				p.save()
				flash.message = "Teilnahme von "+p.user.username+" bestätigt."
			}


		}
		redirect(url: request.getHeader('referer'))
	}

	// Siehe owoRequest
	@Deprecated
	def accept = {
		Owo owo = Owo.get(params.id)
		def requester = params.requester ? User.get(params.requester) : null
		def owoRequest = OwoRequest.findByOwoAndRequester(owo, requester)
		if (!owoRequest) {
			flash.message = "Schaut nicht so aus als hättest du schon angefragt?"
			redirect(url: request.getHeader('referer'))
		} else {
			owoRequest.accepted = true
			
			if (owoRequest.save())
				log.info("Anfrage von "+requester+" angenommen.")
			else 
				log.error("Fehler beim bestätigen der Anfrage.")
				
			def performer = new Performer(user: requester, owo: owo)
			performer.save()
		}
		redirect(controller: "owo", action: "show", id: owo.id)
	}

	
	/*
	* Owo-Anfrage wird vom owo-Admin bestätigt
	* und der User als neuen Performer erstellt
	*/
	def acceptAjax = {
	   Owo owo = Owo.get(params.id)
	   def requester = params.requester ? User.get(params.requester) : null
	   def owoRequest = OwoRequest.findByOwoAndRequester(owo, requester)
	   if (!owoRequest) {
		   flash.message = "Schaut nicht so aus als hättest du schon angefragt?"
		   redirect(url: request.getHeader('referer'))
	   } else {
		   owoRequest.accepted = true
		   
		   if (owoRequest.save()){
			   log.info("Anfrage von "+requester+" angenommen.")
			   /* Notification anlegen zur benachrichtigung */
			   def orn = new OwoRequestNotification(action:NotificationType.OWO_REQUEST_CONFIRMED, actionByUser:currentUser,owoRequest:owoRequest)
			   notificationService.notify(orn)
		   }
		   else
			   log.error("Fehler beim bestätigen der Anfrage.")
			   
		   def performer = new Performer(user: requester, owo: owo)
		   owo.addToPerformers(performer)
		   owo.save()
	   }
	   redirect(controller: "owo", action: "show", id: owo.id)
	}
	
	@Deprecated
	def confirmInvitation = {
		throw new DeprecationException("This function has moved to the right controller: OwoInvitationController.")
	}

	// wird im OwoRequestController gemacht.
	@Deprecated
	def declineOwoRequest = {
		def currentUser = getCurrentUser()
		def owo = Owo.get(params.id)
		def owoRequest = OwoRequest.findByRequesterAndOwo(currentUser, owo)

		if (!owoRequest.delete()){
			flash.message = "Deine Owo-Anfrage wurde zurückgezogen."
		} else {
			flash.message = "Deine Owo-Anfrage konnte nicht zurückgezogen werden."
		}

		redirect(controller: "owo", action: "myOwos")
	}

	/*
	 * Der angemeldete User meldet sich aus einem Owo ab
	 */
	def deleteMe = {
		def user = getCurrentUser()
		def owo = Owo.get(params.id)
		def performer = Performer.findByUserAndOwo(user, owo)

		def owoInvitation = OwoInvitation.findByInviteeAndOwo(user,owo)
		def owoRequest = OwoRequest.findByRequesterAndOwo(user,owo)
		
		log.info("Deleting invites and requests: - invite:"+owoInvitation+ " - request:"+owoRequest)
		
		if(owoInvitation){
			try { owoInvitation.delete()
				log.info("1 invite removed.")
				}
			catch (Exception ex) { log.fatal("afterDelete failed:"+ex)}
		}
		
		if(owoRequest){
			try { owoRequest.delete()
				log.info("1 request removed.")
				}
			catch (Exception ex) { log.fatal("afterDelete failed:"+ex)}
		}
		
		if (!performer.delete()){
			/* Notification anlegen zur benachrichtigung */
			def orn = new OwoNotification(action:NotificationType.OWO_PERFORMER_UNSUBSCRIBE, actionByUser:currentUser,owo:owo)
			notificationService.notify(orn)
			
			flash.message = "Du hast dich vom oWo abgemeldet."
		} else {
			flash.message = "Ich konnte dich nicht abmelden. hmmm..."
		}

		redirect(controller: "owo", action: "myOwos")
	}

	def loadUsersByUserName = {
		def currentUser = getCurrentUser()
		Owo owo = Owo.get(params.id)
		
		// need: max 10 users with given username AND not current user AND not invited user to the given owo, fuck yeah :)
		def invitedUsers = owo.performers*.user
		def userListNotYetInvited = []
		if(params.userName != "") {
			
			String filterByUsername = 	params.userName;
			userListNotYetInvited = performerService.getUserListNotYetInvited(owo,filterByUsername)
			
			// checking if we found something
			if (userListNotYetInvited.size()>0)
				render(template: "userList", model: [userList: userListNotYetInvited, owoInstance: owo,userBox:'leftInviteBox', showButton:'invite'])
			else {
				render("Sorry, nix gefunden. Vorschläge:")
				//userListNotYetInvited = performerService.getUserListNotYetInvited(owo)
				userListNotYetInvited = null
				render(template: "userList", model: [userList: userListNotYetInvited, owoInstance: owo,userBox:'leftInviteBox', showButton:'invite'])
			}
			
		} else {
			userListNotYetInvited = performerService.getUserListNotYetInvited(owo)
			return render("Gib einen Benutzernamen ein")
		}
		
		
	}
	
	def loadPerformersByUserName = {
		
		def currentUser = getCurrentUser()
		Owo owo = Owo.get(params.id)
		
		// list of performers with filter
		def invitedUsers = performerService.getUserListPerformers(owo)
		def userList = []
		String filterByUsername = 	params.userName;
		userList = performerService.getUserListPerformers(owo,filterByUsername)
		
		// checking if we found something else show all users
		if (userList.size()>0)
			render(template: "userList", model: [userList: userList, owoInstance: owo,userBox:'leftPerformerBox', showButton:'deletePerformer', currentUser: currentUser])
		else {
			return render("Kein Performer mit dem Benutzernamen gefunden.")
			//userList = performerService.getUserListPerformers(owo)
			//render(template: "userList", model: [userList: userList, owoInstance: owo,userBox:'leftPerformerBox', showButton:'deletePerformer'])
		}
			
	}
}
