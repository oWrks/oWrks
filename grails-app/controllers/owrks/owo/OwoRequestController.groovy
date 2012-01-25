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
import owrks.controller.AController;
import owrks.notification.NotificationType;
import owrks.notification.OwoNotification;
import owrks.notification.OwoRequestNotification
import owrks.owo.Owo;
import owrks.owo.OwoRequest;
import owrks.owo.Performer;
import owrks.user.User;

@Secured(['ROLE_USER'])	
class OwoRequestController extends AController {

	def notificationService 
	def emailService
	
    // the delete, save and update actions only accept POST requests
    //static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    /*
	 * A owoRequest is confirmed (added to performers)
	 */
	def confirm = {
		forward(action:"confirmAjax")
	}
	
	/*
	* A owoRequest is confirmed via ajax (added to performers)
	* RETURNS: a _user template
	*/
	def confirmAjax = {
		Owo owoInstance = Owo.get(params.owoID)
		def user = User.get(params.userID)
		def currentUser = getCurrentUser()
		
		// Find the request for the user and owo		
		def owoRequest = OwoRequest.findByRequesterAndOwo(user,owoInstance)
		
		if (!owoRequest){
			log.info "There is no request for this User. ignored."
			flash.message = "Der Benuter hat keine Anfrage gesendet."
			request.status = 500
			
			if(request.xhr) { 
				return render("user.did.not.request")
			} else {
				redirect(controller: "owo", action: "performer", id: params.owoID)
			}
			
			
		}
		
		// Find the performer for this user.id and owo.id
		// If found, the user is already performing and the request is ignored
		Performer performer = Performer.findByUserAndOwo(user,owoInstance)
		
		if (performer){
			log.info "Trying to add a performer who already is active. request ignored."
			flash.message = "Der Benuter ist bereits ein aktiver Teilnehmer."
			request.status = 500
			
			if(request.xhr) {
				return render("performer.already.active")
			} else {
				redirect(controller: "owo", action: "performer", id: params.owoID)
			}
			
		}
			
		// TODO make a transaction!
		
		// update data
		log.info(owoRequest)
		owoRequest.accepted = true
		owoRequest.requester = user
		owoRequest.acceptor = currentUser
		
		if (owoRequest.save()){
			log.info("owoRequest set to true and saved.")
			
			performer = new Performer(user:user, owo:owoInstance)
			owoInstance.addToPerformers(performer);
			
			if (owoInstance.save()){
				log.info(user.username + " added to performers.")
				
				/* Notification anlegen zur benachrichtigung */
				def orn = new OwoRequestNotification(action:NotificationType.OWO_REQUEST_CONFIRMED, actionByUser:currentUser,owoRequest:owoRequest)
				notificationService.notify(orn)
				
				//redirect(controller: "performer", action: "list", id: params.owoID)
				// Render the user to add him with ajax to the performer list
				
				log.info(user)
				log.info(owoInstance)
				
				if(request.xhr) {
					return render(template:"/performer/user",model:[user:user,owoInstance:owoInstance,currentUser:currentUser,'userBox':'leftPerformerBox', 'showButton':'deletePerformer'])
				} else {
					flash.message = user.username +" ist jetzt PerformerIn in diesem oWo :)"
					redirect(controller: "owo", action: "performer", id: params.owoID)
				}
				
			} else {
				log.error "Error saving oWo with new Performers."
				flash.message = "Fehler beim hinzufügen von Performern zum oWo."
				response.status = 500
				
				if(request.xhr) {
					return render("error.adding.performer")
				} else {
					redirect(controller: "owo", action: "performer", id: params.owoID)
				}
				
			}
			
		} else {
			log.error "Error saving owoRequest instance."
			
			owoRequest.errors.each { log.error it }
			
			flash.message = "Fehler beim bestätigen der Anfrage."
			response.status = 500
			
			if(request.xhr) {
				return render("error.confirming.request")
			} else {
				redirect(controller: "owo", action: "performer", id: params.owoID)
			}
			
		}
	}
	
	/*
	 * The request from a user is declined by an admin
	 */
	def declineAjax = {
		
		Owo owoInstance = Owo.get(params.owoID)
		def user = User.get(params.userID)
		def currentUser = getCurrentUser()
		
		// Find the request for the user and owo
		def owoRequest = OwoRequest.findByRequesterAndOwo(user,owoInstance)
		
		if (!owoRequest){
			log.info "There is no request for this User. ignored."
			flash.message = "Der Benuter hat keine Anfrage gesendet."
			request.status = 500
			return render("user.did.not.request")
		}
		
		// delete the request
		log.info("decline request:" + owoRequest)
		
		try {
			owoRequest.delete()
			log.info("owoRequest deleted.")
			
			/* Notification anlegen zur benachrichtigung */
			def orn = new OwoNotification(action:NotificationType.OWO_REQUEST_DECLINED, actionByUser:currentUser,owo:owoInstance)
			notificationService.notify(orn)
			
			return render("ok")
				
		} catch (Exception ex){
			log.error("Decline request failed:"+ex)
			response.status = 500
			return render("error.decline.request")
		}
	}
	
	/*
	 * Join a community oWo
	 * */
	def join = {
		Owo owo = Owo.get(params.id)
		def currentUser = getCurrentUser()

		def performer = Performer.findByOwoAndUser(owo, currentUser)

		if (!performer) {
			if(owo.type == "community") {
				performer = new Performer(user:currentUser)
				owo.addToPerformers(performer);
				flash.message = "Community-oWo! Du nimmst gleich teil und kannst loslegen :)"
				
				try {
					owo.save()
					/* Notification anlegen zur benachrichtigung */
					def orn = new OwoNotification( action: NotificationType.OWO_PERFORMER_JOINED, actionByUser: currentUser, owo: owo)
					notificationService.notify(orn)
				} catch (Exception ex){
					log.error("Performer could not be added. owo.save failed."+ex)
				}
				
				// email
				emailService.sendOwoJoined(owo, currentUser)
				
			} else {
				flash.message = "Nur ein Community oWo kann direkt gejoined werden. Da ist was schiefgelaufen - ruf mal Omi an und komm später wieder."
				return redirect(url: request.getHeader('referer'))
			}
		} else {
			flash.message = "Du hast schon angefragt. Aber doppelt hält besser."
			redirect(url: request.getHeader('referer'))
		}
		redirect(controller: "owo", action: "show", id: owo.id)
	}

}
