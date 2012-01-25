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
import owrks.owo.StepAttachment;

@Secured(['ROLE_USER'])	
class StepAttachmentController extends AController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [stepAttachmentInstanceList: StepAttachment.list(params), stepAttachmentInstanceTotal: StepAttachment.count()]
    }

    def create = {
        def stepAttachmentInstance = new StepAttachment()
        stepAttachmentInstance.properties = params
        return [stepAttachmentInstance: stepAttachmentInstance]
    }

    def save = {
        def stepAttachmentInstance = new StepAttachment(params)
        if (!stepAttachmentInstance.hasErrors() && stepAttachmentInstance.save()) {
            flash.message = "stepAttachment.created"
            flash.args = [stepAttachmentInstance.id]
            flash.defaultMessage = "StepAttachment ${stepAttachmentInstance.id} created"
            redirect(action: "show", id: stepAttachmentInstance.id)
        }
        else {
            render(view: "create", model: [stepAttachmentInstance: stepAttachmentInstance])
        }
    }

    def show = {
        def stepAttachmentInstance = StepAttachment.get(params.id)
        if (!stepAttachmentInstance) {
            flash.message = "stepAttachment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepAttachment not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [stepAttachmentInstance: stepAttachmentInstance]
        }
    }

    def edit = {
        def stepAttachmentInstance = StepAttachment.get(params.id)
        if (!stepAttachmentInstance) {
            flash.message = "stepAttachment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepAttachment not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [stepAttachmentInstance: stepAttachmentInstance]
        }
    }
	
	def renderAttachmentTemplate = {
		def stepAttachment = StepAttachment.get(params.id)
		return render(template: "/step/stepAttachment", model: [ stepAttachment: stepAttachment ])
	}

    def update = {
        def stepAttachmentInstance = StepAttachment.get(params.id)
        if (stepAttachmentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (stepAttachmentInstance.version > version) {
                    
                    stepAttachmentInstance.errors.rejectValue("version", "stepAttachment.optimistic.locking.failure", "Another user has updated this StepAttachment while you were editing")
                    render(view: "edit", model: [stepAttachmentInstance: stepAttachmentInstance])
                    return
                }
            }
            stepAttachmentInstance.properties = params
            if (!stepAttachmentInstance.hasErrors() && stepAttachmentInstance.save()) {
                flash.message = "stepAttachment.updated"
                flash.args = [params.id]
                flash.defaultMessage = "StepAttachment ${params.id} updated"
                redirect(action: "show", id: stepAttachmentInstance.id)
            }
            else {
                render(view: "edit", model: [stepAttachmentInstance: stepAttachmentInstance])
            }
        }
        else {
            flash.message = "stepAttachment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepAttachment not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

	
	def deleteAjax = {
		def stepAttachmentInstance = StepAttachment.get(params.id)
		stepAttachmentInstance.delete()
		render(text: "Attachment gelöscht", contentType: "text/plain")
	}
	
    def delete = {
        def stepAttachmentInstance = StepAttachment.get(params.id)
        if (stepAttachmentInstance) {
            try {
                stepAttachmentInstance.delete()
                flash.message = "stepAttachment.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "StepAttachment ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "stepAttachment.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "StepAttachment ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "stepAttachment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "StepAttachment not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
