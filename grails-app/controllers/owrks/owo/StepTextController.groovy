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
import owrks.owo.Step;
import owrks.owo.StepText;

@Secured(['ROLE_USER'])	
class StepTextController extends AController {

	def notificationService
	
    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [stepTextInstanceList: StepText.list(params), stepTextInstanceTotal: StepText.count()]
    }
	
	def renderTextEditTemplate = {
		def step = StepText.get(params.id)
		return render(template: "/step/stepTextEdit", model: [ stepText: step ])
	}
	
	def updateAjax = {
		def stepText = StepText.get(params.id)
		stepText.content = params.content
		stepText.save()
		return render(text: stepText.content, contentType: "text/html")
	}

    def create = {
        def stepTextInstance = new StepText()
        stepTextInstance.properties = params
        return [stepTextInstance: stepTextInstance]
    }

    def save = {
        def stepTextInstance = new StepText(params)
        if (!stepTextInstance.hasErrors() && stepTextInstance.save()) {
            flash.message = "stepText.created"
            flash.args = [stepTextInstance.id]
            flash.defaultMessage = "StepText ${stepTextInstance.id} created"
            redirect(action: "show", id: stepTextInstance.id)
        }
        else {
            render(view: "create", model: [stepTextInstance: stepTextInstance])
        }
    }
	
	def saveAjax = {
		def stepTextInstance = new StepText()
		def step = Step.get(params.id)
		def currentUser = getCurrentUser()
		stepTextInstance.step = step
		stepTextInstance.content = params.content.replace('<br>', '<br />')
		stepTextInstance.creator = currentUser
		if (!stepTextInstance.hasErrors() && stepTextInstance.save()) {
			notificationService.notifyStepContentAdded(NotificationType.STEP_ADDED_TEXT, step, currentUser)
			return render(template: "/step/stepText", model: [ stepText: stepTextInstance ])
		}
		else {
			return render(template: "/error/beanErrors", model: [ entity: stepTextInstance ])
		}
	}

    def show = {
        def stepTextInstance = StepText.get(params.id)
        if (!stepTextInstance) {
            flash.message = "stepText.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepText not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [stepTextInstance: stepTextInstance]
        }
    }

    def edit = {
        def stepTextInstance = StepText.get(params.id)
        if (!stepTextInstance) {
            flash.message = "stepText.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepText not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [stepTextInstance: stepTextInstance]
        }
    }

    def update = {
        def stepTextInstance = StepText.get(params.id)
        if (stepTextInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (stepTextInstance.version > version) {
                    
                    stepTextInstance.errors.rejectValue("version", "stepText.optimistic.locking.failure", "Another user has updated this StepText while you were editing")
                    render(view: "edit", model: [stepTextInstance: stepTextInstance])
                    return
                }
            }
            stepTextInstance.properties = params
            if (!stepTextInstance.hasErrors() && stepTextInstance.save()) {
                flash.message = "stepText.updated"
                flash.args = [params.id]
                flash.defaultMessage = "StepText ${params.id} updated"
                redirect(action: "show", id: stepTextInstance.id)
            }
            else {
                render(view: "edit", model: [stepTextInstance: stepTextInstance])
            }
        }
        else {
            flash.message = "stepText.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepText not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }
	
	def deleteAjax = {
		def stepTextInstance = StepText.get(params.id)
		stepTextInstance.delete()
		render(text: "Text gelöscht", contentType: "text/plain")
	}

    def delete = {
        def stepTextInstance = StepText.get(params.id)
        if (stepTextInstance) {
            try {
                stepTextInstance.delete()
                flash.message = "stepText.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "StepText ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "stepText.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "StepText ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "stepText.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepText not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
