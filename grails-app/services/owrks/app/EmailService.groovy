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

import org.springframework.beans.factory.InitializingBean

import owrks.owo.Mission
import owrks.owo.Owo
import owrks.owo.OwoPostComment
import owrks.owo.Step
import owrks.user.RegistrationCode
import owrks.user.User


class EmailService implements InitializingBean {

	static transactional = false

	def mailService
	def owoService
	def gspTagLibraryLookup  // being automatically injected by spring
	def g

	public void afterPropertiesSet() {
		g = gspTagLibraryLookup.lookupNamespaceDispatcher("g")
	}

	/*
	 * Info: mailService.sendMail returns the MailMessage it sent
	 */
	def sendRegistrationConfirmation(User user, RegistrationCode code) {
		try {
			mailService.sendMail {
				multipart true
				to user.email
				subject g.message(code: 'mail.title.confirm.account', default: 'oWrks - Zugang bestätigen')
				text( g.render( template: "/mail/textAccountConfirmation", model:[ username: user.username, token: code.token ]))
				html( g.render( template: "/mail/htmlAccountConfirmation", model:[ username: user.username, token: code.token ]))
				//dualBody(template: "/mail/htmlAccountConfirmation", model: [ username: user.username, token: code.token ])
			}
		} catch (Exception e) {
			log.error("Problem sending email $e.message", e)
		}
	}

	def sendNewPassword(User user, String newPassword) {
		try {
			mailService.sendMail {
				to user.email
				subject g.message(code: 'mail.title.request.password')
				html g.render( template: "/mail/requestPassword", model:[ username: user.username, password: newPassword ])
			}
		} catch (Exception e) {
			log.error("Problem sending email $e.message", e)
		}
	}

	//	def sendAssignedStep(User user, String newStep) {
	//		mailService.sendMail {
	//			to user.email
	//			subject g.message(code: 'mail.title.assigned.step')
	//			html g.render( template: "/mail/assignedStep", model:[ username: user.username, title: newStep, createdBy : user.username ])
	//		}
	//	}

	def sendWelcome(User user) {
		try {
			mailService.sendMail {
				to user.email
				subject g.message(code: 'mail.title.register')
				html g.render( template: "/mail/register", model:[ username: user.username ])
			}
		} catch (Exception e) {
			log.error("Problem sending email $e.message", e)
		}
	}

	def sendConfirm(User owner, User performer, Owo owo) {
		try {
			mailService.sendMail {
				to performer.email
				// TODO: Subject aus message.properties auslesen
				subject "Einladung zu " + performer.owo.title
				html g.render(template: (performer.user ? "/mail/invitation" : "/mail/invitationNewUser"), model: [owo: performer.owo, performer: performer])
			}
		} catch (Exception e) {
			log.error("Problem sending email $e.message", e)
		}
	}

	def sendInvitationByOwoInvitations(def owoInvitations) {
		Owo owo = owoInvitations[0].owo
		owoInvitations.each { owoInvitation ->
			log.info "Sendig Email to " + owoInvitation.invitee.email
			try {
				mailService.sendMail {
					to owoInvitation.invitee.email
					// TODO: Subject aus message.properties auslesen
					subject "Einladung zu " + owo.title
					body(view: "/mail/owoInvitationMail", model: [owo: owo, owoInvitation: owoInvitation])
				}
			} catch (Exception e) {
				log.error("Problem sending email $e.message", e)
			}
		}
	}

	def sendInvitationByUserInvitations(def userInvitations) {
		userInvitations.each { userInvitation ->
			log.info "Sendig Email to " + userInvitation.email
			Owo owo = userInvitation.owo
			try {
				def o = mailService.sendMail {
					to userInvitation.email
					// TODO: Subject aus message.properties auslesen
					subject "Einladung zu " + owo.title
					body(view: "/mail/userInvitationMail", model: [owo: owo, email: userInvitation.email])
				}
			} catch (Exception e) {
				log.error("Problem sending email $e.message", e)
			}
		}
	}

	def sendRequestByOwoRequests(def owoRequests) {
		owoRequests.each { owoRequest ->
			String owoOwnerEmail = owoRequest.owo.owner.email
			log.info "Sendig Email to " + owoOwnerEmail
			try {
				def o = mailService.sendMail {
					to owoOwnerEmail
					// TODO: Subject aus message.properties auslesen
					subject "Einladung zu " + owoRequest.owo.title
					body(view: "/mail/owoRequestMail", model: [owo: owoRequest.owo, username: owoRequest.requester.username])
				}
			} catch (Exception e) {
				log.error("Problem sending email $e.message", e)
			}
		}
	}

	/*
	 * send email to all performers, when new user
	 * joined an owo.
	 * NotificationSettings-Check: owoNotifier
	 * @param Owo owo
	 * @param User newUser
	 */
	def sendOwoJoined(Owo owo, User newUser) {
		def performers = owoService.getConfirmedPerformers(owo)
		performers.each {
			def performer = it.user
			if(performer.id != newUser.id && performer.notificationSettings.owoNotifier) {
				try {
					mailService.sendMail {
						to (performer.email)
						subject ("oWrks - Neuer Benutzer beigetreten")
						body( view: "/mail/owoJoined", model: [ username: performer.username, owoId: owo.id, owoTitle: owo.title, newUser: newUser.username ])
					}
					log.info("Mail: owoJoined sent to ${performer.email}, owo: " + owo.title)
				} catch (Exception e) {
					log.error("Problem sending email $e.message", e)
				}
			}
		}
	}

	/*
	 * send email to all performers, when new user
	 * posted a new message on owo-wall
	 * NotificationSettings-Check: owoNotifier
	 * @param Owo owo
	 * @param User poster
	 */
	def sendNewOwoPost(Owo owo, User poster) {
		def performers = owoService.getConfirmedPerformers(owo)
		performers.each {
			def performer = it.user
			if(performer.id != poster.id && performer.notificationSettings.owoNotifier) {
				try {
					mailService.sendMail {
						to (performer.email)
						subject ("oWrks - Neue Owo-Nachricht")
						body( view: "/mail/newOwoPost", model: [ recipientName: performer.username, owoId: owo.id, owoTitle: owo.title, owoPoster: poster.username ])
					}
					log.info("Mail: newOwoPost sent to ${performer.email}, owo: " + owo.title)
				} catch (Exception e) {
					log.error("Problem sending email $e.message", e)
				}
			}
		}
	}


	/*
	 * send email to all performers, when new comment
	 * is posted. notify orig. poster and commentators
	 * NotificationSettings-Check: owoNotifier
	 * @param OwoPostComment owoPostComment
	 */
	def sendNewOwoPostComment(OwoPostComment owoPostComment) {
		def owo = owoPostComment.post.owo

		// get users to send an email, creator + other commentators
		def users = []
		users += owoPostComment.creator
		owoPostComment.post.comments.each {
			if(!users.contains(it.creator))
				users += it.creator
		}

		users.each { owoPostUser ->
			if(owoPostUser.id != owoPostComment.creator.id && owoPostUser.notificationSettings.owoNotifier) {
				try {
					mailService.sendMail {
						to (owoPostUser.email)
						subject ("oWrks - Neuer Kommentar")
						body( view: "/mail/newOwoPostComment", model: [ recipientName: owoPostUser.username, owoId: owo.id, owoTitle: owo.title, commentPoster: owoPostComment.creator.username ])
					}
					log.info("Mail: newOwoPostComment sent to ${owoPostUser.email}, owo: " + owo.title)
				} catch (Exception e) {
					log.error("Problem sending email $e.message", e)
				}
			}
		}
	}

	/*
	 * send email to all performers, when new mission
	 * is crreated
	 * NotificationSettings-Check: owoNotifier
	 * @param Mission mission
	 */
	def sendNewMission(Mission mission) {
		def performers = owoService.getConfirmedPerformers(mission.owo)
		performers.each {
			def performer = it.user
			if(performer.id != mission.creator.id && performer.notificationSettings.owoNotifier) {
				try {
					mailService.sendMail {
						to (performer.email)
						subject ("oWrks - Neue Mission")
						body( view: "/mail/newMission", model: [ recipientName: performer.username, mission: mission ])
					}
					log.info("Mail: newMission sent to ${performer.email}, mission: " + mission.title)
				} catch (Exception e) {
					log.error("Problem sending email $e.message", e)
				}
			}
		}
	}

	/*
	 * send email to all performers, when a new step
	 * is created. extra message if step is assigned to receiver
	 * NotificationSettings-Check: missionNotifier
	 * @param Step step
	 */
	def sendNewStep(Step step) {
		def performers = owoService.getConfirmedPerformers(step.mission.owo)
		performers.each {
			def performer = it.user
			if(performer.id != step.createdBy.id && performer.notificationSettings.missionNotifier) {
				Boolean assignedToMe = false
				String mailSubject = "oWrks - Neuer Step"
				if(step.assignedTo?.id == performer.id) {
					assignedToMe = true
					mailSubject += " dir zugewiesen"
				}
				try {
					mailService.sendMail {
						to (performer.email)
						subject ( mailSubject )
						body( view: "/mail/newStep", model: [ recipientName: performer.username, step: step, assignedToMe: assignedToMe ])
					}
					log.info("Mail: newStep sent to ${performer.email}, step: " + step.title)
				} catch (Exception e) {
					log.error("Problem sending email $e.message", e)
				}
			}
		}
	}

}