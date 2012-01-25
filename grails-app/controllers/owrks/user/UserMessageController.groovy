package owrks.user
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
import owrks.controller.AController
import owrks.user.User;
import owrks.user.UserMessage;

@Secured(['ROLE_USER', 'ROLE_ADMIN'])	
class UserMessageController extends AController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	/*
	 * list method only for admins
	 */
	@Secured(['ROLE_ADMIN'])
    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [userMessageInstanceList: UserMessage.list(params), userMessageInstanceTotal: UserMessage.count()]
    }
	
	def inbox = {
		params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
		def user = getCurrentUser()
		def messages = UserMessage.findAllByReceiverAndReceiverDeleted(user, false, [max: params.max, sort:(params.sort ?: "dateCreated"), order:(params.order ?: "desc"), offset:params.offset])
		def msgCount = UserMessage.countByReceiverAndReceiverDeleted(user, false)
		[ userMessageInstanceList: messages, userMessageInstanceTotal: msgCount ]
	}
	
	def outbox = {
		params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
		def user = getCurrentUser()
		def messages = UserMessage.findAllBySenderAndSenderDeleted(user, false, [max: params.max, sort:(params.sort ?: "dateCreated"), order:(params.order ?: "desc"), offset:params.offset])
		def msgCount = UserMessage.countBySenderAndSenderDeleted(user, false)
		[ userMessageInstanceList: messages, userMessageInstanceTotal: msgCount ]
	}

    def create = {
        def userMessageInstance = new UserMessage()
        userMessageInstance.properties = params
        return [userMessageInstance: userMessageInstance, receiverName: params.id ]
    }

    def save = {
        def userMessageInstance = new UserMessage()
		userMessageInstance.sender = getCurrentUser()
		userMessageInstance.properties = params
		userMessageInstance.receiver = params.receiverName ? User.findByUsername(params.receiverName) : null
		if(userMessageInstance.sender?.username == userMessageInstance.receiver?.username) {
			flash.message = g.message(code: "userMessage.username.equals.receiver")
			return render(view: "create", model: [userMessageInstance: userMessageInstance])
		} else {
	        if (!userMessageInstance.hasErrors() && userMessageInstance.save()) {
	            flash.message = "userMessage.created"
	            flash.args = [userMessageInstance.id]
	            flash.defaultMessage = "UserMessage ${userMessageInstance.id} created"
	            redirect(action: "inbox")
				//redirect(url: request.getHeader('referer'))
	        }
		}
		return render(view: "create", model: [userMessageInstance: userMessageInstance])
    }

    def show = {
        def userMessageInstance = UserMessage.get(params.id)
        if (!userMessageInstance) {
            flash.message = "userMessage.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "UserMessage not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
			userMessageInstance.setRead()
			userMessageInstance.save(flush: true, failOnError: true)
            return [userMessageInstance: userMessageInstance]
        }
    }

	/*
	* NOTICE: UserMessages cannot be updated!
	* TODO: remove this code
	*/
    /*def edit = {
        def userMessageInstance = UserMessage.get(params.id)
        if (!userMessageInstance) {
            flash.message = "userMessage.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "UserMessage not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [userMessageInstance: userMessageInstance]
        }
    }*/

	/*
	 * NOTICE: UserMessages cannot be updated!
	 * TODO: remove this code
	 */
    /*def update = {
        def userMessageInstance = UserMessage.get(params.id)
        if (userMessageInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (userMessageInstance.version > version) {
                    
                    userMessageInstance.errors.rejectValue("version", "userMessage.optimistic.locking.failure", "Another user has updated this UserMessage while you were editing")
                    render(view: "edit", model: [userMessageInstance: userMessageInstance])
                    return
                }
            }
            userMessageInstance.properties = params
            if (!userMessageInstance.hasErrors() && userMessageInstance.save()) {
                flash.message = "userMessage.updated"
                flash.args = [params.id]
                flash.defaultMessage = "UserMessage ${params.id} updated"
                redirect(action: "show", id: userMessageInstance.id)
            }
            else {
                render(view: "edit", model: [userMessageInstance: userMessageInstance])
            }
        }
        else {
            flash.message = "userMessage.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "UserMessage not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }*/

    def delete = {
        def userMessageInstance = UserMessage.get(params.id)
        if (userMessageInstance) {
            try {
				userMessageInstance.setDeleted()
                //userMessageInstance.delete()
				userMessageInstance.save()
                flash.message = "userMessage.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "UserMessage ${params.id} deleted"
               	//redirect(action: "list")
				redirect(url: request.getHeader('referer'))
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "userMessage.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "UserMessage ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "userMessage.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "UserMessage not found with id ${params.id}"
            //redirect(action: "list")
			redirect(url: request.getHeader('referer'))
        }
    }
	def loadUsers = {
		def currentUser = getCurrentUser()
		def users = User.createCriteria().list() {
			like("username", "%" + params.term + "%")
			ne("username", currentUser.username)
		}
		render users*.username as JSON
	}
}
