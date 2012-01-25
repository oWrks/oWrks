package owrks.notification
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
import owrks.user.User

@Secured(['ROLE_USER'])
class NotificationController extends AController {

	def notificationService
	
    def index = { }
	
	/*
	 * get last 5 notifications
	 */
	def lastUserNotification = {
		def currentUser = getCurrentUser()
		def notifications = notificationService.getNotificationsForUser(currentUser,5)
		render(view:"/notification/list",model:[notifications:notifications])
	}
	
	/*
	* note when this method is called, a timestamp for the user is updated in order to
	* know which notifications are new
	*/
    def newUserNotifications = {
	   def currentUser = getCurrentUser()
	   def notifications = notificationService.getNotificationsForUser(currentUser, 5, currentUser.lastLogin)
	   if (notifications.size()){
	   	   render(view:"/notification/list",model:[notifications:notifications])
	   }
	   else {
	   	   response.status = 304 // not modified
		   render("Keine Benachrichtigungen")
	   }
	}
   
	/*
	 * Method with the purpose to check if a user has new notifications
	 */
	def checkForNotifications = {
		def currentUser = getCurrentUser()
		int nrNotifys = notificationService.getNotificationsForUser(currentUser, 10, currentUser.lastLogin).size()
		if (nrNotifys){
			render(nrNotifys)
		} else {
			response.status = 304 // not modified
			render("0")
		}
	}
	
}
