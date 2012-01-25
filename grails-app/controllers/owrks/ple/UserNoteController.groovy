package owrks.ple
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

import owrks.ple.UserNote;
import grails.plugins.springsecurity.Secured
import owrks.controller.AController;

@Secured(['ROLE_USER'])	
class UserNoteController extends AController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def show = {
        def user = getCurrentUser()
		def userNoteInstance = UserNote.findByUser(user) ?: new UserNote(user: user)
		[userNoteInstance: userNoteInstance]
    }

    def update = {
        def user = getCurrentUser()
		def userNoteInstance = UserNote.findByUser(user) ?: new UserNote(user: user)
        if (userNoteInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (userNoteInstance.version > version) {
                    userNoteInstance.errors.rejectValue("version", "userNote.optimistic.locking.failure", "Another user has updated this UserNote while you were editing")
                    render(view: "edit", model: [userNoteInstance: userNoteInstance])
                    return
                }
            }
			userNoteInstance.text = params.text
            if (!userNoteInstance.hasErrors() && userNoteInstance.save()) {
                flash.message = g.message(code: 'usernote.updated')
                //flash.defaultMessage = "Notizen gespeichert"
                redirect(action: "show", id: userNoteInstance.id)
            }
        }
        render(view: "show", model: [userNoteInstance: userNoteInstance])
    }
}
