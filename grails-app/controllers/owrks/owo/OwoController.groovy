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

import grails.converters.JSON
import grails.plugins.springsecurity.Secured

import org.apache.jasper.tagplugins.jstl.core.Redirect;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.grails.taggable.Tag
import org.grails.taggable.TagException

import owrks.controller.AController
import owrks.notification.Notification
import owrks.notification.NotificationType
import owrks.notification.OwoNotification
import owrks.owo.Owo;
import owrks.owo.Performer;
import owrks.user.User

@Secured(['IS_AUTHENTICATED_FULLY'])
class OwoController extends AController {

	def emailService
	def owoService
	def uploadService
	def performerService
	def notificationService
	def searchableService

	def index = { redirect(action: "list", params: params) }

	// the delete, save and update actions only accept POST requests
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def list = {
		def owos = []
		params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
		if(request.xhr) {
			switch(params.id) {
				case "1":
					owos = owoService.getAllOwos(params)
					break
				case "2":
					owos = owoService.getAllPublicOwos(params)
					break
				case "3":
					owos = owoService.getAllCommunityOwos(params)
					break
			}
			log.info(owos.size())
			return render(template: (params.listLayout == 'list' ? "owoListTable" : "owoListGrid"), model: [owoInstanceList: owos, owoInstanceTotal: owos.totalCount])
		} else {
			owos = owoService.getAllOwos(params)
			[owoInstanceList: owos, owoInstanceTotal: owos.totalCount, listLayout: params.listLayout ]
		}
	}

	def workspace = {
		def owo = Owo.get(params.id)
		[ owoInstance: owo ]
	}

	def create = {
		def owoInstance = new Owo()
		owoInstance.properties = params
		owoInstance.invitationText = "${message(code:'owo.invitation.text')}"
		return [owoInstance: owoInstance, tags: '']
	}

	def save = {
		withForm {
			def owoInstance = new Owo()
			def title = params.title.encodeAsHTML()
			bindData(owoInstance, params, [ exclude: ['tags', 'owner.id']])
			
			def currentUser = getCurrentUser()
			owoInstance.owner = currentUser
			
			// hibernate bug here, see: http://jira.grails.org/browse/GRAILS-7086
			// searchable causing the bug, stop mirroring
			searchableService.stopMirroring()
			if (owoInstance.save()) {
				searchableService.startMirroring()
				owoInstance.reindex()
				
				// add owner to performers
				owoInstance.addToPerformers(new Performer(user: currentUser, isAdmin: true)).save()
				
				try {
					owoInstance.setTags(params.tags.tokenize(','))
				} catch(TagException e) {
					// TODO: handle this
					// this exception will be thrown e.g. if tag is empty (tag1, , tag2)
				}
				int owoID = owoInstance.id
				flash.message = "owo.created"
				flash.args = [owoInstance.title]
				flash.defaultMessage = "Owo ${owoID} created"
				
				/* Notification */
				def notification = new OwoNotification(action:NotificationType.OWO_CREATED, actionByUser:currentUser,owo:owoInstance)
				notificationService.notify(notification)
				
				return redirect(controller: "performer", action: "wizzardInviteAgiler", id: owoID)
			} else {
				return render(view: "create", model: [owoInstance: owoInstance, tags: params.tags])
			}
		}.invalidToken {
			// bad request
			log.error "Owo create form: Double submission! request ignored."
			flash.message = "owo.doubleSubmitError"
			flash.defaultMessage = "Fehler: Form zweimal gesendet."

			// redirect zu myOwos
			return redirect(action: "myOwos")
		}
	}

	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def show = {
		Owo owoInstance = Owo.get(params.id)
		User currentUser = getCurrentUser()

		def userListInvitations = performerService.getUserListInvited(owoInstance)
		def pendingOwoRequests = performerService.getUserListOwoRequests(owoInstance)

		def notifications = []

		if (!owoInstance) {
			flash.message = "Owo wurde nicht gefunden"
			redirect(action: "list")
		}
		else {
			if (currentUser)
				notifications = notificationService.getNotificationsForOwo(owoInstance, 5)
			return [
				notifications:notifications,
				owoInstance: owoInstance,
				userListInvitations: userListInvitations,
				currentUser: currentUser,
				pendingOwoRequests:pendingOwoRequests]
		}
	}

	def edit = {
		def owoInstance = Owo.get(params.id)
		def reservedSlots = performerService.getUserListPerformers(owoInstance).size()+1 // +1 for creator
		if(owoInstance && owoInstance.owner.id == getCurrentUser().id) {
			if (!owoInstance) {
				//flash.message = "owo.not.found"
				//flash.args = [params.id]
				flash.message = "Owo nicht gefunden"
				redirect(action: "list")
			}
			else {
				return [owoInstance: owoInstance, reservedSlots: reservedSlots]
			}
		} else {
			flash.message = "Owo ${owoInstance.title} kann nicht bearbeitet werden."
			redirect(action: "list")
		}
	}

	def update = {
		def owoInstance = Owo.get(params.id)
		if (owoInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (owoInstance.version > version) {

					owoInstance.errors.rejectValue("version", "owo.optimistic.locking.failure", "Another user has updated this Owo while you were editing")
					render(view: "edit", model: [owoInstance: owoInstance])
					return
				}
			}
			owoInstance.properties = params
			if (!owoInstance.hasErrors() && owoInstance.save()) {
				try {
					owoInstance.setTags(params.tags.tokenize(','))
				} catch(TagException e) {
					// TODO: handle this
					// this exception will be thrown e.g. if tag is empty (tag1, , tag2)
				}
				flash.message = "owo.updated"
				flash.args = [params.id]
				flash.defaultMessage = "Owo ${params.id} updated"
				redirect(action: "show", id: owoInstance.id)
			}
			else {
				render(view: "edit", model: [owoInstance: owoInstance])
			}
		}
		else {
			flash.message = "owo.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "Owo not found with id ${params.id}"
			redirect(action: "edit", id: params.id)
		}
	}

	def delete = {
		def owoInstance = Owo.get(params.id)
		def user = getCurrentUser()
		def temptitle = owoInstance.title
		if (owoInstance && (owoInstance.owner.id == user.id || SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN'))) {
			try {
				String title = owoInstance.title
				owoInstance.delete()
				flash.message = "Owo wurde gelöscht"
				//flash.args = [title]
				//flash.defaultMessage = "Owo ${title} deleted"

				/* Notification */
				def notification = new Notification(action:NotificationType.OWO_DELETED, actionByUser:currentUser,info:temptitle)
				notificationService.notify(notification)
				redirect(action: "myOwos")
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "Owo konnte nicht gelöscht werden"
				//flash.args = [params.id]
				//flash.defaultMessage = "Owo ${params.id} could not be deleted"
				redirect(action: "show", id: params.id)
			}
		}
		else {
			flash.message = "owo.deletion.failed"
			redirect(action: "list")
		}
	}

	def myOwos = {
		params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)

		def myOwos = Owo.findAllByOwner(getCurrentUser())
		def performers = Performer.withCriteria {
			eq("user", getCurrentUser())
		}
		def currentUser = getCurrentUser()

		def owosCreatedByMe = owoService.getOwosCreatedBy(params,currentUser)

		def owosJoinedByMe = owoService.getOwosByPerformer(params,currentUser)

		// TODO: owo-invitations holen! service methode anlegen
		def owoInvitations = owoService.getOwoInvitationsByUser(currentUser)

		// TODO: owo-requests holen! service methode anlegen
		def owoRequests = owoService.getOwoRequestsByUser(currentUser)

		// max 5 owo-recommendations
		params.max = 5
		def newestOwos = owoService.getLatestOwosByUser(params, currentUser)

		[myOwos : myOwos, performers: performers,
					owosCreatedByMe: owosCreatedByMe,
					activeOwos: owosJoinedByMe,
					myOwoInvitations: owoInvitations,
					myOwoRequests: owoRequests,
					newestOwos: newestOwos]
	}

	def loadTags = {
		def tags = []
		def terms = params.term.split(',')
		def term = terms.size() ? terms[terms.size() - 1].trim() : params.term
		if(term != "")
			tags = Tag.findAllByNameLike(term + "%")
		render tags*.name as JSON
	}

	def loadOwosByTitle = {
		def terms = params.value.split(' ')
		def owos = []
		if(terms.size() > 1) {
			owos = Owo.createCriteria().list() {
				or {
					terms.each {
						ilike("title", "%" + it + "%")
					}
				}
				ne("type", "private")
				maxResults(10)
			}
		} else {
			owos = Owo.findAllByTitleIlike("%" + params.value + "%")
		}
		//def term = terms.size() ? terms[terms.size() - 1].trim() : params.term
		render(template: "owoList", model: [owos: owos])
	}

	def changeWorkflowState = {
		def state = params.workflowState

		def owoInstance = Owo.get(params.id)

		owoInstance.workflowState = state;

		if(owoInstance.save()){
			flash.message = "oWo Workflow Status geändert auf "+state

			/* Notification */
			def notification = new OwoNotification(action:NotificationType.OWO_WORKFLOWSTATE_CHANGED, actionByUser:currentUser, owo:owoInstance, info:owoInstance.workflowState)
			notificationService.notify(notification)


		}
		else {
			flash.message = "Fehler beim Speichern vom Workflow-Status"
			log.error "Fehler beim Speichern vom Workflow-Status "+state
			owoInstance.errors.each { log.error it }
		}
		redirect(action: show, id: owoInstance.id)
	}

	def tag = {
		def owos = Owo.findAllByTag(params.tag)
		render(view: "tag", model: [owos: owos, tag: params.tag])
	}

}