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
package owrks.notification


import grails.plugins.springsecurity.Secured
import owrks.controller.AController;

@Secured(['ROLE_USER'])	
class NotificationSettingsController extends AController {

    def index = { 
		def currentUser = getCurrentUser()
		//def notificationSettings = NotificationSettings.findByUser(currentUser)
        [ notificationSettings: currentUser.notificationSettings]
	}

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]


    def update = {
		def currentUser = getCurrentUser()
        def notificationSettings = NotificationSettings.get(params.id)
        if (notificationSettings && currentUser.notificationSettings == notificationSettings) {
            if (params.version) {
                def version = params.version.toLong()
                if (notificationSettings.version > version) {
                    notificationSettings.errors.rejectValue("version", "notificationSettings.optimistic.locking.failure", "Another user has updated this NotificationSettings while you were editing")
					return render(view: "index", model: [notificationSettings: notificationSettings])
                }
            }
            notificationSettings.properties = params
            if (!notificationSettings.hasErrors() && notificationSettings.save()) {
                flash.message = "notificationSettings.updated"
                flash.args = [params.id]
                flash.defaultMessage = "NotificationSettings ${params.id} updated"
                return redirect(action: "index")
            }
            else {
                return render(view: "index", model: [ notificationSettings: notificationSettings ])
            }
        }
		redirect(controller: "dashboard")
    }

}
