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

import javax.activation.MimetypesFileTypeMap

import owrks.controller.AController
import owrks.notification.NotificationType;
import owrks.notification.OwoNotification;
import owrks.owo.Owo;
import owrks.owo.OwoPost;
import owrks.owo.OwoPostAttachment;

@Secured(['ROLE_USER'])	
class OwoPostController extends AController {

	def uploadService
	def notificationService
	def emailService
	
    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [owoPostInstanceList: OwoPost.list(params), owoPostInstanceTotal: OwoPost.count()]
    }

    def create = {
        def owoPostInstance = new OwoPost()
        owoPostInstance.properties = params
        return [owoPostInstance: owoPostInstance]
    }
	
	def wall = {
		def owo = Owo.get(params.id)
		// check if user is performer
		if(!owrksService.isPerformer(getCurrentUser(), owo)) {
			flash.message = "Du musst erst dem oWo beitreten, um die angeforderte Seiten aufrufen zu können."
			return forward(controller: "owo", action: "show", id: owo.id)
		}
		def owoPost = params.postId ? OwoPost.get(params.postId) : null
		[ owoInstance: owo, owoPostInstance: owoPost ]
	}

	/*
	 * TODO: wird für normalen upload verwendet -> siehe auch owo/savePost
	 */
    def save = {
        def owoPostInstance = new OwoPost(params)
		owoPostInstance.creator = getCurrentUser()
		
		def owo = owoPostInstance.owo
		def file = request.getFile('filename')
		if(!file.empty && owo) {
			def validatedFile = uploadService.validateFile(file)
			if(validatedFile instanceof Boolean && validatedFile) {
				def filename = file.getOriginalFilename().replaceAll(" ", "")
				def newFilename = new File("${owo.getDataPath()}" + File.separatorChar + filename)
				file.transferTo( newFilename )
				owoPostInstance.attachment = new OwoPostAttachment(filename: filename, filesize: file.getSize(), contentType: file.getContentType())
			} else {
				owoPostInstance.errors.reject('owoPost.fileupload.error', validatedFile)
			}
		}
		
		if (!owoPostInstance.hasErrors() && owoPostInstance.save()) {

			notificationService.notifyOwo(NotificationType.PINNWAND_POST_ADDED, owoPostInstance.owo, owoPostInstance.creator)
			// mail
			emailService.sendNewOwoPost(owoPostInstance.owo, owoPostInstance.creator)
			
			return redirect(action: "wall", id: owoPostInstance.owo.id)
		}
		else {
			return render( view: 'wall', model: [ owoInstance: owo, owoPostInstance: owoPostInstance ])
		}
    }
	
	def saveAjax = {
		def owoPostInstance = new OwoPost(params)
		owoPostInstance.creator = getCurrentUser()
		if (!owoPostInstance.hasErrors() && owoPostInstance.save()) {
			
			notificationService.notifyOwo(NotificationType.PINNWAND_POST_ADDED, owoPostInstance.owo, owoPostInstance.creator)
			
			// mail
			emailService.sendNewOwoPost(owoPostInstance.owo, owoPostInstance.creator)
			
			return render(template: "/owo/owoPost", model: [ post: owoPostInstance, currentUser: getCurrentUser() ], encoding:"UTF-8")
		} else {
			return render(template: "/error/beanErrors", model: [ entity: owoPostInstance ])
		}
	}

    def show = {
        def owoPostInstance = OwoPost.get(params.id)
        if (!owoPostInstance) {
            flash.message = "owoPost.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "OwoPost not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [owoPostInstance: owoPostInstance]
        }
    }

    def edit = {
        def owoPostInstance = OwoPost.get(params.id)
        if (!owoPostInstance) {
            flash.message = "owoPost.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "OwoPost not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [owoPostInstance: owoPostInstance]
        }
    }

    def update = {
        def owoPostInstance = OwoPost.get(params.id)
        if (owoPostInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (owoPostInstance.version > version) {
                    
                    owoPostInstance.errors.rejectValue("version", "owoPost.optimistic.locking.failure", "Another user has updated this OwoPost while you were editing")
                    render(view: "edit", model: [owoPostInstance: owoPostInstance])
                    return
                }
            }
            owoPostInstance.properties = params
            if (!owoPostInstance.hasErrors() && owoPostInstance.save()) {
                flash.message = "owoPost.updated"
                flash.args = [params.id]
                flash.defaultMessage = "OwoPost ${params.id} updated"
                redirect(action: "show", id: owoPostInstance.id)
            }
            else {
                render(view: "edit", model: [owoPostInstance: owoPostInstance])
            }
        }
        else {
            flash.message = "owoPost.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "OwoPost not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

	def ajaxDelete = {
		def owoPostInstance = OwoPost.get(params.id)
		owoPostInstance.delete()
		render(text: "Nachricht gelöscht", contentType: "text/html")
	}
	
    def delete = {
        def owoPostInstance = OwoPost.get(params.id)
        if (owoPostInstance) {
            try {
                owoPostInstance.delete()
                flash.message = "owoPost.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "OwoPost ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "owoPost.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "OwoPost ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "owoPost.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "OwoPost not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
