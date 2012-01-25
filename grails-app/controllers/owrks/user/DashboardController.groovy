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

import grails.plugins.springsecurity.Secured
import owrks.controller.AController
import owrks.owo.Owo

@Secured(['ROLE_USER'])
class DashboardController extends AController {

	def notificationService 
	
    def index = { 
		def currentUser = getCurrentUser()
		def myOwos = Owo.findAllByOwner(currentUser) 
		
		def newestOwos = Owo.list(max:5, sort:"dateCreated", order:"desc")
		
		
		def user_notifications = notificationService.getNotificationsForUser(currentUser)
			
				
		def platformNotifications = notificationService.getNotifications(5)
		
		
		[myOwos : myOwos,newestOwos : newestOwos,
			currentUser:currentUser,
			platformNotifications: platformNotifications,
			user_notifications:user_notifications
			]
	}
}
