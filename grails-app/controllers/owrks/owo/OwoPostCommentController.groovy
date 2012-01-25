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
import owrks.owo.OwoPostComment;

@Secured(['ROLE_USER'])	
class OwoPostCommentController extends AController {

	def emailService
	
    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [owoPostCommentInstanceList: OwoPostComment.list(params), owoPostCommentInstanceTotal: OwoPostComment.count()]
    }

    def create = {
        def owoPostCommentInstance = new OwoPostComment()
        owoPostCommentInstance.properties = params
        return [owoPostCommentInstance: owoPostCommentInstance]
    }

    def save = {
        def owoPostCommentInstance = new OwoPostComment(params)
		owoPostCommentInstance.creator = getCurrentUser()
        if (!owoPostCommentInstance.hasErrors() && owoPostCommentInstance.save()) {
            flash.message = "Dein Kommentar wurde gespeichert"
            redirect(controller: "owo", action: "wall", id: owoPostCommentInstance.post.owo.id)
        }
        else {
            render(view: "create", model: [owoPostCommentInstance: owoPostCommentInstance])
        }
    }
	
	def saveAjax = {
		def owoPostInstance = new OwoPostComment(params)
		owoPostInstance.creator = getCurrentUser()
		if (!owoPostInstance.hasErrors() && owoPostInstance.save(flush: true)) {
			
			emailService.sendNewOwoPostComment(owoPostInstance)
			
			return render(template: "/owo/owoComment", model: [ comment: owoPostInstance ],contentType:"text/html",encoding:"UTF-8")
		} else {
			return render(template: "/error/beanErrors", model: [ entity: owoPostInstance ])
		}
	}


    def show = {
        def owoPostCommentInstance = OwoPostComment.get(params.id)
        if (!owoPostCommentInstance) {
            flash.message = "owoPostComment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "OwoPostComment not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [owoPostCommentInstance: owoPostCommentInstance]
        }
    }

    def edit = {
        def owoPostCommentInstance = OwoPostComment.get(params.id)
        if (!owoPostCommentInstance) {
            flash.message = "owoPostComment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "OwoPostComment not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [owoPostCommentInstance: owoPostCommentInstance]
        }
    }

    def update = {
        def owoPostCommentInstance = OwoPostComment.get(params.id)
        if (owoPostCommentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (owoPostCommentInstance.version > version) {
                    
                    owoPostCommentInstance.errors.rejectValue("version", "owoPostComment.optimistic.locking.failure", "Another user has updated this OwoPostComment while you were editing")
                    render(view: "edit", model: [owoPostCommentInstance: owoPostCommentInstance])
                    return
                }
            }
            owoPostCommentInstance.properties = params
            if (!owoPostCommentInstance.hasErrors() && owoPostCommentInstance.save()) {
                flash.message = "owoPostComment.updated"
                flash.args = [params.id]
                flash.defaultMessage = "OwoPostComment ${params.id} updated"
                redirect(action: "show", id: owoPostCommentInstance.id)
            }
            else {
                render(view: "edit", model: [owoPostCommentInstance: owoPostCommentInstance])
            }
        }
        else {
            flash.message = "owoPostComment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "OwoPostComment not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

	
	def ajaxDelete = {
		def owoPostCommentInstance = OwoPostComment.get(params.id)
		owoPostCommentInstance.delete()
		render(text: "Kommentar gelöscht", contentType: "text/html")
	}
	
    def delete = {
        def owoPostCommentInstance = OwoPostComment.get(params.id)
        if (owoPostCommentInstance) {
            try {
                owoPostCommentInstance.delete()
                flash.message = "owoPostComment.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "OwoPostComment ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "owoPostComment.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "OwoPostComment ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "owoPostComment.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "OwoPostComment not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
