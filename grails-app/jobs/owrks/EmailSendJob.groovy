package owrks
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

import owrks.owo.OwoInvitation
import owrks.owo.OwoRequest
import owrks.user.UserInvitation

class EmailSendJob {

	def emailService

	static triggers = {
		simple name: 'emailSendJob', startDelay: 60000, repeatInterval: 120000
	}

	def group = "EmailGroup"

	def execute() {
		def userInvitations = UserInvitation.findAllByEmailSent(false)
		if(userInvitations) {
			emailService.sendInvitationByUserInvitations(userInvitations)
			userInvitations.each { userInvitation ->
				userInvitation.emailSent = true
				if(!userInvitation.save()) {
					log.error("Could not send user-invitation to ${userInvitation.email}")
				} else {
					log.info "New user-invitation send to ${userInvitation.email}"
				}
			}
		}
		userInvitations = null

		def owoInvitations = OwoInvitation.findAllByEmailSent(false)
		if(owoInvitations) {
			emailService.sendInvitationByOwoInvitations(owoInvitations)
			owoInvitations.each { owoInvitation ->
				owoInvitation.emailSent = true
				if(!owoInvitation.save()) {
					log.error("Could not send owo-invitation to ${owoInvitation.invitee.email}")
				} else {
					log.info "New owo-invitation send to ${owoInvitation.invitee.email}"
				}
			}
		}
		owoInvitations = null

		def owoRequests = OwoRequest.findAllByEmailSent(false)
		if(owoRequests) {
			emailService.sendRequestByOwoRequests(owoRequests)
			owoRequests.each { owoRequest ->
				owoRequest.emailSent = true
				if(!owoRequest.save()) {
					log.error("Could not send owo-invitation to ${owoRequest.requester.email}")
				} else {
					log.info "New owo-invitation send to ${owoRequest.requester.email}"
				}
			}
		}
		owoRequests = null
	}
}