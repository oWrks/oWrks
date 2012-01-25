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

import javax.activation.MimetypesFileTypeMap

import org.grails.taggable.Tag
import org.grails.taggable.TagException

import owrks.controller.AController
import owrks.notification.MissionNotification
import owrks.notification.NotificationType
import owrks.notification.StepNotification
import owrks.owo.Mission;
import owrks.owo.Owo;
import owrks.owo.Performer;
import owrks.owo.Step;
import owrks.owo.StepAttachment;
import owrks.owo.StepComment;
import owrks.owo.StepLink;
import owrks.owo.StepText;
import owrks.user.User


@Secured(["IS_AUTHENTICATED_FULLY"])
class StepController extends AController {

	def owoService
	def stepService
	def uploadService
	def notificationService
	def emailService

	def index = { redirect(action: "list", params: params) }

	// all edits and saves redirect to same location
	def redirectToAfterEdit(long owoid) {
		redirect(controller:"owo", action: "missions", id: owoid)
	}

	// the delete, save and update actions only accept POST requests
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]


	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
		[stepInstanceList: Step.list(params), stepInstanceTotal: Step.count()]
	}

	def create = {
		def mission = Mission.get(params.id)
		def stepInstance = new Step(mission: mission)
		stepInstance.properties = params
		def performers = owoService.getConfirmedPerformers(mission.owo)
		return [stepInstance: stepInstance, performers: performers]
	}

	def createAjax = {
		def mission = Mission.get(params.id)
		if(!mission) {
			render(text: "Mission $params.id nicht gefunden!")
		} else {
			def stepInstance = new Step(mission: mission)
			render(template: "createAjax", model: [stepInstance: stepInstance, tags: ''])
		}
	}
	
	def loadStepTags = {
		def tags = []
		println(params.term)
		def terms = params.term.split(',')
		def term = terms.size() ? terms[terms.size() - 1].trim() : params.term
		if(term != "")
			tags = Tag.findAllByNameLike(term + "%")
		render tags*.name as JSON
	}

	def save = {
		// check for double submission
		withForm {

			def stepInstance = new Step()
			bindData(stepInstance, params, [ exclude: ['tags']])

			stepInstance.createdBy = getCurrentUser()

			if (!stepInstance.hasErrors() && stepInstance.save()) {
				log.info("Saving Step... and redirecting to Owo"+stepInstance.mission.owo.id)
				
				try {
					stepInstance.setTags(params.tags.tokenize(','))
				} catch(TagException e) {
					// TODO: handle this
					// this exception will be thrown e.g. if tag is empty (tag1, , tag2)
				}

				flash.message = "step.created"
				flash.args = stepInstance.id
				flash.defaultMessage = "Step ${stepInstance.id} created"

				notify(NotificationType.STEP_CREATED, stepInstance.createdBy, stepInstance)
				
				redirectToAfterEdit(stepInstance.mission.owo.id)
			}
			else {
				log.error ("Error saving step.")
				stepInstance.errors.each {  log.error (it) }
				render(view: "/mission/list", model: [stepInstance: stepInstance])
			}

		}.invalidToken {
			// bad request
			log.error "Stepform: Double submission! request ignored."
			flash.message = "step.doubleSubmitError"
			flash.defaultMessage = "Fehler: Form zweimal gesendet."

			redirectToAfterEdit(stepInstance.mission.owo.id)
		}
	}

	def saveAjax = {
		def stepInstance = new Step()
		bindData(stepInstance, params, [ exclude: ['tags']])
		stepInstance.createdBy = getCurrentUser()
		String message = ""
		if (!stepInstance.hasErrors() && stepInstance.save()) {
			message = "Step $stepInstance.title wurde gespeichert!"
			notify(NotificationType.STEP_CREATED,stepInstance.createdBy,stepInstance)
			try {
				stepInstance.setTags(params.tags.tokenize(','))
			} catch(TagException e) {
				// this exception will be thrown e.g. if tag is empty (tag1, , tag2)
				log.error("Error saving tags in owo: ${step.mission.owo.title}", e)
			}
			
			// mail
			emailService.sendNewStep(stepInstance)
			
			return render(template: "wall", model: [ stepInstance: stepInstance, message: message ])
		} else {
			message = "Step wurde nicht gespeichert!"
		}
		return render(template: "createAjax", model: [ stepInstance: stepInstance, message: message, tags: params.tags])
	}
	
	def finishAjax = {
		def step = params.id ? Step.get(params.id) : null
		step.finished = (params.finish as Boolean)
		step.save()
		return render(template: "/step/stepDescription", model: [ stepInstance: step ])
	}
	
	def publishAjax = {
		def step = params.id ? Step.get(params.id) : null
		step.published = (params.publish as Boolean)
		step.save()
		return render(template: "/step/stepDescription", model: [ stepInstance: step ])
	}
	
	def updateSequenceOrder = {
		def steps = JSON.parse(params.steps)
		steps.each { key, val ->
			Step s = Step.get(key)
			s.sequenceOrder = val
			s.save()
		}
		render "success"
	}

	def updateWallSequenceOrder = {
		def wallItems = JSON.parse(params.wallItems)
		wallItems.each { objType, objList ->
			objList.each { objId, sequenceOrder ->
				def o
				switch(objType) {
					case "stepAttachment":
						o = StepAttachment.get(objId)
						break;
					case "stepLink":
						o = StepLink.get(objId)
						break;
					case "stepComment":
						o = StepComment.get(objId)
						break;
					case "stepText":
						o = StepText.get(objId)
						break;
				}
				o.sequenceOrder = sequenceOrder
				o.save()
			}
		}
		render "success"
	}
	
	/*
	 * TODO: can be deleted
	 */
	/*def attachments = {
		def stepInstance = Step.get(params.id)
		def attachments = StepAttachment.findAllByStep(stepInstance)
		return render(template: "attachmentsAjax", model: [ stepInstance: stepInstance, attachmentList: attachments ])
	}*/
	
	/*
	* TODO: can be deleted
	*/
	/*def linkboard = {
		def stepInstance = Step.get(params.id)
		def links = StepLink.findAllByStep(stepInstance)
		return render(template: "linkboard", model: [ stepInstance: stepInstance ])
	}*/
	
	/*
	 * Ajax-Upload
	 * @see http://valums.com/ajax-upload/
	 */
	def uploadAjax = {
		def step = params.stepId ? Step.get(params.stepId) : null
		def currentUser = getCurrentUser()
		if(step && currentUser) {
			def filename = params.qqfile.replaceAll(" ", "")
			def targetFile = new File(step.getDataPath().toString() + File.separatorChar + filename)
			if(targetFile.exists())
				return render(text: [success:false, message: "Datei $filename existiert bereits"] as JSON, contentType:'text/json')

			// TODO: need some file-validation
			try {
				def uploadedFile = uploadService.upload(targetFile, request)
				MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap()
				
				StepAttachment attachment = new StepAttachment(
						filename: filename, 
						filesize: uploadedFile.size(), 
						step: step, 
						contentType: mimeTypesMap.getContentType(uploadedFile),
						uploader: currentUser)
				
				if (attachment.save(flush: true)){
					switch (attachment.contentType){
						case "image/jpeg":
							notificationService.notifyStepContentAdded(NotificationType.STEP_ADDED_IMAGE, step, currentUser)
							break
						default:
							notificationService.notifyStepContentAdded(NotificationType.STEP_ADDED_FILE, step, currentUser)
							break
					}
					return render(text: [success:true, message: "Upload von $filename erfolgreich", attachmentId: attachment.id ] as JSON, contentType:'text/json')
				}
				throw new Exception("unable to upload a file.")
			} catch (Exception e) {
				log.error("Failed to upload file.", e)
				return render(text: [success:false, message: "Es ist ein Fehler aufgetreten."] as JSON, contentType:'text/json')
			}
		}
		render(text: [success:false, message: "Kein Step gefunden!"] as JSON, contentType:'text/json')
	}
	
	def loadAttachmentListAjax = {
		def stepInstance = Step.get(params.id)
		def attachments = StepAttachment.findAllByStep(stepInstance)
		return render(template: "attachmentList", model: [ attachmentList: attachments ])
	}
	
	def updateDescription = {
		// TODO: ajax fallback function
	}
	
	
	def wall = {
		def stepInstance = Step.get(params.id)
		
		// get all post-items (attachments, links, texts)
		def texts = StepText.findAllByStep(stepInstance)
		def messages = StepComment.findAllByStep(stepInstance)
		def attachments = StepAttachment.findAllByStep(stepInstance)
		def links = StepLink.findAllByStep(stepInstance)
		def wallPosts = []
		wallPosts.addAll(texts)
		wallPosts.addAll(messages)
		wallPosts.addAll(attachments)
		wallPosts.addAll(links)
		// TODO: reverse doenst work???
		//wallPosts.sort { it.sequenceOrder }
		wallPosts.sort { obj1, obj2 -> obj1.sequenceOrder <=> obj2.sequenceOrder ?: obj2.dateCreated <=> obj1.dateCreated }
		//list.sort { map1, map2 -> map1.rowNum <=> map2.rowNum ?: map1.position <=> map2.position }
		
		
		// display counts in the nav
		def navCount = [ msgCount: 0, attachmentCount: 0, linkCount: 0, textCount: 0 ]
		if(stepInstance) {
			navCount.msgCount = StepComment.countByStep(stepInstance)
			navCount.attachmentCount = StepAttachment.countByStep(stepInstance)
			navCount.linkCount = StepLink.countByStep(stepInstance)
			navCount.textCount = StepText.countByStep(stepInstance)
		}
		
		return render(template: "wall", model: [ 
			stepInstance: stepInstance, 
			wallPosts: wallPosts, 
			navCount: navCount,
			currentUser: getCurrentUser()
		])
	}

	def show = {
		def stepInstance = Step.get(params.id)
		if (!stepInstance) {
			flash.message = "step.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "Step not found with id ${params.id}"
			redirect(action: "list")
		}
		else {
			return [stepInstance: stepInstance, owoInstance: stepInstance.mission.owo]
		}
	}

	def edit = {
		def stepInstance = Step.get(params.id)
		if (!stepInstance) {
			flash.message = "step.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "Step not found with id ${params.id}"
			log.error flash.defaultMessage
			// redirect 404?
		}
		else {
			Owo owoInstance = stepInstance.mission.owo
			def performers = Performer.createCriteria().list { eq("owo", owoInstance) }
			return [stepInstance: stepInstance, owoInstance: owoInstance, performers: performers]
		}
	}

	def editAjax = {
		def stepInstance = Step.get(params.id)
		if (!stepInstance) {
			render(text: "Step nicht gefunden!")
		}
		else {
			Owo owoInstance = stepInstance.mission.owo
			def performers = Performer.findAllByOwo(owoInstance)
			return render(template: "editAjax", model: [ stepInstance: stepInstance, performers: performers, tags: ''] )
		}
	}

	def update = {
		def stepInstance = Step.get(params.id)
		if (stepInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (stepInstance.version > version) {

					stepInstance.errors.rejectValue("version", "step.optimistic.locking.failure", "Another user has updated this Step while you were editing")
					log.error "Another user has updated this Step while you were editing"
					render(view: "edit", model: [stepInstance: stepInstance, owoInstance: stepInstance.mission.owo])
					return
				}
			}
			
			
			stepInstance.properties = params
			log.info("assignedToID: " + stepInstance.assignedTo.id)
			int stepID = stepInstance.id
			if (!stepInstance.hasErrors() && stepInstance.save()) {
				flash.message = "step.updated"
				flash.args = [stepID]
				flash.defaultMessage = "Step ${stepID} updated"
				log.info flash.defaultMessage
				
				// check if it was assigned to someone else
				Step tempstep = new Step()
				tempstep.properties = params
				
				Boolean assignedToSomeone = false
				
				log.info ("Zuweisung geändert? vorher "+tempstep.assignedTo +" nachher "+ stepInstance.assignedTo)
				
				if(tempstep.assignedTo != stepInstance.assignedTo){
					assignedToSomeone = true
				}
				if (assignedToSomeone){
					notify(NotificationType.STEP_ASSIGNED_TO,stepInstance.createdBy,stepInstance)
				} else {
					notify(NotificationType.STEP_UPDATED_DESCRIPTION,stepInstance.createdBy,stepInstance)
				}
				
				redirectToAfterEdit(stepInstance.mission.owo.id)
			}
			else {
				log.error "Step update: validation errors or unable to save step with id "+stepInstance.id
				render(view: "edit", model: [stepInstance: stepInstance, owoInstance: stepInstance.mission.owo])
			}
		}
		else {
			flash.message = "step.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "Step not found with id ${params.id}"
			redirect(action: "edit", id: params.id)
		}
	}

	def updateAjax = {
		def stepInstance = Step.get(params.id)
		def tempAssigned = stepInstance.assignedTo
		if (stepInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (stepInstance.version > version) {
					stepInstance.errors.rejectValue("version", "step.optimistic.locking.failure", "Another user has updated this Step while you were editing")
					log.error "Another user has updated this Step while you were editing"
					return render(template: "editAjax", model: [stepInstance: stepInstance])
				}
			}
			stepInstance.assignedTo = params.assignedTo.id ? User.get(params.assignedTo.id) : null
			params.remove("assignedTo.id")
			stepInstance.properties = params
			
			if(stepInstance.published && !stepInstance.finished)
				stepInstance.published = false
			String infoMessage = ""
			if (!stepInstance.hasErrors() && stepInstance.save()) {
				infoMessage = "Step $params.id wurde geändert"
				
				try {
					stepInstance.setTags(params.tags.tokenize(','))
				} catch(TagException e) {}
				
				log.info ("Zuweisung geändert? vorher "+tempAssigned +" nachher "+ stepInstance.assignedTo)
				
				if(tempAssigned != stepInstance.assignedTo){
					notify(NotificationType.STEP_ASSIGNED_TO,stepInstance.createdBy,stepInstance)
				} else {
					notify(NotificationType.STEP_UPDATED_DESCRIPTION,stepInstance.createdBy,stepInstance)
				}
				
				//return render(template: "wall", model: [stepInstance: stepInstance, message: infoMessage])
				forward(action: "wall", id: stepInstance.id)
			} else {
				infoMessage = "Step $params.id wurde nicht gespeichert"
				return render(template: "editAjax", model: [stepInstance: stepInstance, message: infoMessage, tags: params.tags])
			}
		}
		return render(text: "Step $params.id wurde nicht gefunden")
	}


	/*
	 * TODO: can be deleted
	 */
	/*def toggleFinishedFlag = {
		def stepInstance = Step.get(params.id)

		def finished = true
		if (stepInstance.finished)
			finished = false

		stepInstance.finished = finished
		log.info "Set finisehd to "+finished+" for step "+stepInstance.id

		if (!stepInstance.save()){
			log.error "Error setting finished flag."
			flash.message = "step.toggle.finished.error"
			flash.defaultMessage = "Error setting finished flag."
			flash.args = [params.id]
		} else {
			flash.message = "step.toggle.finished"
			flash.defaultMessage = "Set finished to "+finished+" for step "+stepInstance.id
			flash.args = [params.id]
			
			if (finished){
				notify(NotificationAction.STEP_COMPLETED,stepInstance.createdBy,stepInstance)
			} else {
				// TODO
				//notify(NotificationAction.STEP_UPDATED_DESCRIPTION,stepInstance.createdBy,stepInstance)
			}
			
		}

		redirect(controller: "owo", action:"missions", id:stepInstance.mission.owo.id)
	}*/

	/*
	 * TODO: can be deleted / wird momentan nicht per ajax durchgeführt sondern via save()
	 */
	/*def toggleFinishedFlagAjax = {
		def stepInstance = Step.get(params.id)

		def finished = true
		if (stepInstance.finished)
			finished = false

		stepInstance.finished = finished
		def infoMessage = "Set finisehd to "+finished+" for step "+stepInstance.id
		log.info infoMessage

		if (!stepInstance.save()){
			log.error "Error setting finished flag."
			flash.message = "step.toggle.finished.error"
			flash.defaultMessage = "Error setting finished flag."
			flash.args = [params.id]
		} else {
			flash.message = "step.toggle.finished"
			flash.defaultMessage = "Set finished to "+finished+" for step "+stepInstance.id
			flash.args = [params.id]
		}

		if (finished){
			
			notify(NotificationAction.STEP_COMPLETED,stepInstance.createdBy,stepInstance)
			render("Step \""+stepInstance.title+"\" fertig.")
			
		} else {
			render("Step als nicht fertig markiert.")
		}

	}*/

	def deleteAjax = {
		def stepInstance = Step.get(params.id)
		stepInstance.delete()
		render(text: "Step gelöscht", contentType: "text/plain")
	}
	
	def delete = {
		def stepInstance = Step.get(params.id)
		if (stepInstance) {
			try {
				def tempid = stepInstance.mission.owo.id // stepInstance should be null after del?
				stepInstance.delete()
				flash.message = "step.deleted"
				flash.args = [params.id]
				flash.defaultMessage = "Step ${params.id} deleted"
				redirectToAfterEdit(tempid)
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "step.not.deleted"
				flash.args = [params.id]
				flash.defaultMessage = "Step ${params.id} could not be deleted"
				redirect(action: "show", id: params.id)
			}
		}
		else {
			flash.message = "step.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "Step not found with id ${params.id}"
			redirect(controller: "mission", action: "show", id: stepInstance.mission.id)
		}
	}
	
	private void notify(NotificationType notificationAction, User currentUser, Step step){
		/* Notification */
			def notification = new StepNotification(action:notificationAction, actionByUser:currentUser,step:step)
			notificationService.notify(notification)
		/* End Notification */
	}
	
	private void notifyDelete(NotificationType notificationAction, User currentUser, Mission mission, String stepTitle){
		/* Notification */
			def notification = new MissionNotification(action:notificationAction, actionByUser:currentUser,mission:mission,info:stepTitle)
			notificationService.notify(notification)
		/* End Notification */
	}
}
