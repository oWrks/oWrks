package owrks.admin
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
import owrks.admin.Feedback;
import owrks.controller.AController;

@Secured(['ROLE_ADMIN'])	
class FeedbackController extends AController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [feedbackInstanceList: Feedback.list(params), feedbackInstanceTotal: Feedback.count()]
    }

    def create = {
        def feedbackInstance = new Feedback()
        feedbackInstance.properties = params
        return [feedbackInstance: feedbackInstance]
    }

	@Secured(['IS_AUTHENTICATED_FULLY'])
    def save = {
        def feedbackInstance = new Feedback(params)
		feedbackInstance.user = getCurrentUser()
        if (!feedbackInstance.hasErrors() && feedbackInstance.save()) {
            flash.message = "feedback.created"
            flash.args = [feedbackInstance.id]
            flash.defaultMessage = "Feedback ${feedbackInstance.id} created"
            redirect(action: "show", id: feedbackInstance.id)
        }
        else {
            render(view: "create", model: [feedbackInstance: feedbackInstance])
        }
    }
	
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def saveAjax = {
		def feedbackInstance = new Feedback(params)
		feedbackInstance.user = getCurrentUser()
		if (!feedbackInstance.hasErrors() && feedbackInstance.save()) {
			//flash.message = "Vielen Dank für deine Bemerkung!"
			return render(template: "/feedback/create", model: [ feedbackInstance: feedbackInstance, success: true])
		}
		else {
			return render(template: "/feedback/create", model: [ feedbackInstance: feedbackInstance, success: false])
		}
	}

    def show = {
        def feedbackInstance = Feedback.get(params.id)
        if (!feedbackInstance) {
            flash.message = "feedback.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Feedback not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [feedbackInstance: feedbackInstance]
        }
    }

    def edit = {
        def feedbackInstance = Feedback.get(params.id)
        if (!feedbackInstance) {
            flash.message = "feedback.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Feedback not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [feedbackInstance: feedbackInstance]
        }
    }

    def update = {
        def feedbackInstance = Feedback.get(params.id)
        if (feedbackInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (feedbackInstance.version > version) {
                    
                    feedbackInstance.errors.rejectValue("version", "feedback.optimistic.locking.failure", "Another user has updated this Feedback while you were editing")
                    render(view: "edit", model: [feedbackInstance: feedbackInstance])
                    return
                }
            }
            feedbackInstance.properties = params
            if (!feedbackInstance.hasErrors() && feedbackInstance.save()) {
                flash.message = "feedback.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Feedback ${params.id} updated"
                redirect(action: "show", id: feedbackInstance.id)
            }
            else {
                render(view: "edit", model: [feedbackInstance: feedbackInstance])
            }
        }
        else {
            flash.message = "feedback.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Feedback not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def feedbackInstance = Feedback.get(params.id)
        if (feedbackInstance) {
            try {
                feedbackInstance.delete()
                flash.message = "feedback.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Feedback ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "feedback.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Feedback ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "feedback.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Feedback not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
