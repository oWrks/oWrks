package owrks.app
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

import java.util.Date

import owrks.notification.MissionNotification
import owrks.notification.Notification
import owrks.notification.NotificationType
import owrks.notification.OwoInvitationNotification
import owrks.notification.OwoNotification
import owrks.notification.OwoRequestNotification
import owrks.notification.StepNotification
import owrks.owo.Owo
import owrks.owo.Step
import owrks.user.User

class NotificationService {

	static transactional = false

	/*
	 * save new notification
	 * @param Notifcation notification
	 * @return Boolean
	 */
	Boolean notify(Notification notification){
		String clazz = notification.class.toString()
		try {
			if (notification.save()){
				return true
			} else {
				log.error(clazz + " NOT saved.")
			}
		} catch (Exception ex){
			log.error(clazz+".save() failed: " + ex)
		}
		return false
	}

	/*
	 * create new step-notification
	 * @param NotificationType type
	 * @param Step step
	 * @param User actionByUser
	 */
	def notifyStepContentAdded(NotificationType type, Step step, User actionByUser) {
		try {
			notify( new StepNotification(action: type, step: step, actionByUser: actionByUser) )
		} catch (Exception ex) {
			log.error("Error creating Notification " + ex)
		}
	}

	/*
	 * create new owo-notification
	 * @param NotificationType type
	 * @param Owo owo
	 * @param User user
	 */
	def notifyOwo(NotificationType type, Owo owo, User actionByUser){
		try {
			notify( new OwoNotification(action: type, owo: owo, actionByUser: actionByUser) )
		} catch (Exception ex) {
			log.error("Error creating Notification " + ex)
		}
	}

	/*
	 * return notification-list
	 * @param int max
	 */
	def getNotifications(int max = 10){
		def notifications = Notification.list(max: max, sort:"dateCreated", order:"desc")
		return notifications
	}

	/*
	 * return all notification for an owo
	 * @param Owo owo
	 * @param int max 
	 */
	def getNotificationsForOwo(Owo owo, int max = 10){

		def notifications = OwoNotification.findAllByOwo(owo)

		owo.missions.each { m ->
			notifications += MissionNotification.findAllByMission(m)
			m.steps.each { s ->
				notifications+=StepNotification.findAllByStep(s)
			}
		}

		notifications += OwoInvitationNotification.withCriteria {
			owoInvitation { eq("owo",owo) }
		}

		notifications += OwoRequestNotification.withCriteria {
			owoRequest { eq("owo",owo) }
		}

		notifications = notifications.unique().sort { it.dateCreated }.reverse()

		if(notifications.size()>max)
			notifications[0..max-1]
		else
			notifications
	}


	/*
	 * return all notification for an user
	 * @param User user
	 * @param int max
	 * @param Date fromDate
	 */
	def getNotificationsForUser(User user, int max = 10, Date fromDate = null) {

		def user_notifications = []
		if(!fromDate)
			fromDate = new Date() -10

		user_notifications += Notification.withCriteria {
			eq("affectedUser", user)
			gt("dateCreated", fromDate)
			ne("actionByUser", user)
		}

		def myowos = Owo.withCriteria {
			performers { eq("user",user) }
		}

		myowos.each {

			// dont know why it is null inside criteria
			def tempowo = it

			user_notifications += OwoInvitationNotification.withCriteria {
				owoInvitation { eq("owo",tempowo) }
				ne("actionByUser",user)
				gt("dateCreated",fromDate)
			}

			user_notifications += OwoRequestNotification.withCriteria {
				owoRequest { eq("owo",tempowo) }
				ne("actionByUser",user)
				gt("dateCreated",fromDate)
			}

			// all requests
			user_notifications += OwoRequestNotification.withCriteria {
				owoRequest { eq("owo",tempowo) }
				ne("actionByUser",user)
				gt("dateCreated",fromDate)
			}

			// all owo generals
			user_notifications += OwoNotification.withCriteria {
				eq("owo",tempowo)
				ne("actionByUser",user)
				gt("dateCreated",fromDate)
			}
		}

		user_notifications += OwoInvitationNotification.withCriteria {
			owoInvitation {
				invitee {
					eq("username",user.username)
				}
			}
			ne("actionByUser",user)
			gt("dateCreated",fromDate)
		}


		// oder beteiligt in owo step
		user_notifications += StepNotification.withCriteria {
			step {
				mission {
					owo {
						performers { eq("user",user) }
					}
				}
			}
			ne("actionByUser",user)
			gt("dateCreated",fromDate)
		}
		// oder beteiligt in owo mission
		user_notifications += MissionNotification.withCriteria {

			mission {
				owo {
					performers { eq("user",user) }
				}
			}
			ne("actionByUser",user)
			gt("dateCreated",fromDate)

		}

		user_notifications = user_notifications.unique().sort{ it.dateCreated }.reverse()

		if (user_notifications.size()<max)
			max=user_notifications.size()

		(max==0)?[]:user_notifications[0..max-1]
	}


}
