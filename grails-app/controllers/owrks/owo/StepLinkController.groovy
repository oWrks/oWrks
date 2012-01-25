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
import owrks.owo.StepLink;

@Secured(['IS_AUTHENTICATED_FULLY'])	
class StepLinkController extends AController {

	def notificationService 
	
    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [stepLinkInstanceList: StepLink.list(params), stepLinkInstanceTotal: StepLink.count()]
    }

    def create = {
        def stepLinkInstance = new StepLink()
        stepLinkInstance.properties = params
        return [stepLinkInstance: stepLinkInstance]
    }

    def save = {
        def stepLinkInstance = new StepLink(params)
        if (!stepLinkInstance.hasErrors() && stepLinkInstance.save()) {
            flash.message = "stepLink.created"
            flash.args = [stepLinkInstance.id]
            flash.defaultMessage = "StepLink ${stepLinkInstance.id} created"
            redirect(action: "show", id: stepLinkInstance.id)
        }
        else {
            render(view: "create", model: [stepLinkInstance: stepLinkInstance])
        }
    }

    def show = {
        def stepLinkInstance = StepLink.get(params.id)
        if (!stepLinkInstance) {
            flash.message = "stepLink.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepLink not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [stepLinkInstance: stepLinkInstance]
        }
    }

    def edit = {
        def stepLinkInstance = StepLink.get(params.id)
        if (!stepLinkInstance) {
            flash.message = "stepLink.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepLink not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [stepLinkInstance: stepLinkInstance]
        }
    }
	
	def saveAjax = {
		def step = Step.get(params.id)
		def currentUser = getCurrentUser()
		def stepLinkInstance = new StepLink(title: params.title, url: params.url, step: step)
		stepLinkInstance.creator = currentUser
		String returnMessage = ""
		if (!stepLinkInstance.hasErrors() && stepLinkInstance.save()) {
			notificationService.notifyStepContentAdded(NotificationType.STEP_ADDED_LINK, step, currentUser)
			returnMessage = "Link wurde erfolgreich gespeichert!"
		}
		else {
			returnMessage = "Link konnte nicht gespeichert werden!"
			// TODO reject error
		}
		return render(template: "/step/stepLink", model: [ link: stepLinkInstance ])
	}

    def update = {
        def stepLinkInstance = StepLink.get(params.id)
        if (stepLinkInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (stepLinkInstance.version > version) {
                    
                    stepLinkInstance.errors.rejectValue("version", "stepLink.optimistic.locking.failure", "Another user has updated this StepLink while you were editing")
                    render(view: "edit", model: [stepLinkInstance: stepLinkInstance])
                    return
                }
            }
            stepLinkInstance.properties = params
            if (!stepLinkInstance.hasErrors() && stepLinkInstance.save()) {
                flash.message = "stepLink.updated"
                flash.args = [params.id]
                flash.defaultMessage = "StepLink ${params.id} updated"
                redirect(action: "show", id: stepLinkInstance.id)
            }
            else {
                render(view: "edit", model: [stepLinkInstance: stepLinkInstance])
            }
        }
        else {
            flash.message = "stepLink.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepLink not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }
	
	
	def deleteAjax = {
		def stepLinkInstance = StepLink.get(params.id)
		stepLinkInstance.delete()
		render(text: "Link gelöscht", contentType: "text/plain")
	}
	
    def delete = {
        def stepLinkInstance = StepLink.get(params.id)
        if (stepLinkInstance) {
            try {
                stepLinkInstance.delete()
                flash.message = "stepLink.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "StepLink ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "stepLink.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "StepLink ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "stepLink.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepLink not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
