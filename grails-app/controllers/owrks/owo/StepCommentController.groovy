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
import owrks.notification.StepNotification
import owrks.owo.Step;
import owrks.owo.StepComment;
import owrks.user.User

@Secured(['ROLE_USER'])	
class StepCommentController extends AController {

	def notificationService
	
    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [stepCommentInstanceList: StepComment.list(params), stepCommentInstanceTotal: StepComment.count()]
    }

    def create = {
        def stepCommentInstance = new StepComment()
        stepCommentInstance.properties = params
		stepCommentInstance.step = Step.get(params.stepId)
        return [stepCommentInstance: stepCommentInstance]
    }

    def save = {
        def stepCommentInstance = new StepComment(params)
		stepCommentInstance.creator = getCurrentUser()
        if (!stepCommentInstance.hasErrors() && stepCommentInstance.save()) {
            flash.message = "stepComment.created"
            flash.args = [stepCommentInstance.id]
            flash.defaultMessage = "StepComment ${stepCommentInstance.id} created"
            redirect(action: "show", id: stepCommentInstance.id)
        }
        else {
            render(view: "create", model: [stepCommentInstance: stepCommentInstance])
        }
    }
	
	def saveAjax = {
		def stepCommentInstance = new StepComment()
		def step = Step.get(params.id)
		def currentUser = getCurrentUser()
		stepCommentInstance.step = step
		stepCommentInstance.content = params.content
		stepCommentInstance.creator = currentUser
		String returnMessage = ""
		if (!stepCommentInstance.hasErrors() && stepCommentInstance.save()) {
			returnMessage = "Kommentar wurde erfolgreich gespeichert!"
			notificationService.notifyStepContentAdded(NotificationType.STEP_ADDED_COMMENT, step, currentUser)
		}
		else {
			return render(template: "/error/beanErrors", model: [ entity: stepCommentInstance ])
		}
		return render(template: "/step/stepComment", model: [ comment: stepCommentInstance ])
	}

    def show = {
        def stepCommentInstance = StepComment.get(params.id)
        if (!stepCommentInstance) {
            flash.message = "stepComment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepComment not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [stepCommentInstance: stepCommentInstance]
        }
    }

    def edit = {
        def stepCommentInstance = StepComment.get(params.id)
        if (!stepCommentInstance) {
            flash.message = "stepComment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepComment not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [stepCommentInstance: stepCommentInstance]
        }
    }

    def update = {
        def stepCommentInstance = StepComment.get(params.id)
        if (stepCommentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (stepCommentInstance.version > version) {
                    
                    stepCommentInstance.errors.rejectValue("version", "stepComment.optimistic.locking.failure", "Another user has updated this StepComment while you were editing")
                    render(view: "edit", model: [stepCommentInstance: stepCommentInstance])
                    return
                }
            }
            stepCommentInstance.properties = params
            if (!stepCommentInstance.hasErrors() && stepCommentInstance.save()) {
                flash.message = "stepComment.updated"
                flash.args = [params.id]
                flash.defaultMessage = "StepComment ${params.id} updated"
                redirect(action: "show", id: stepCommentInstance.id)
            }
            else {
                render(view: "edit", model: [stepCommentInstance: stepCommentInstance])
            }
        }
        else {
            flash.message = "stepComment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepComment not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }
	
	def deleteAjax = {
		def stepCommentInstance = StepComment.get(params.id)
		stepCommentInstance.delete()
		render(text: "Kommentar gelöscht", contentType: "text/plain")
	}

    def delete = {
        def stepCommentInstance = StepComment.get(params.id)
        if (stepCommentInstance) {
            try {
                stepCommentInstance.delete()
                flash.message = "stepComment.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "StepComment ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "stepComment.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "StepComment ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "stepComment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepComment not found with id ${params.id}"
            redirect(action: "list")
        }
    }
	
	
	
}
