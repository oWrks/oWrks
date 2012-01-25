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

import owrks.notification.MissionNotification
import owrks.notification.Notification
import owrks.notification.NotificationType
import owrks.notification.OwoInvitationNotification
import owrks.notification.OwoNotification
import owrks.notification.OwoRequestNotification
import owrks.notification.StepNotification

class NotifyTagLib {

	static namespace = "notify"

	/*
	 * render notification
	 */
	def notification = { attrs, body ->
		Notification n = attrs.notification
		out << g.formatDate(date:n.dateCreated,format:"dd. MMMM yyyy hh:mm")+ " Uhr: "
		handleNotification(n)
	}

	/*
	 * General Notification
	 * this notification should not be used.
	 * instead sub-classes are rendered
	 */
	private handleNotification(Notification n){
		switch(n.action){
			case NotificationType.OWO_DELETED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat ein owo gelöscht."
				break
			default:
				out << 'handleGeneralNotification: unhandled action: '+n.action
		}
	}

	/*
	 * Owo Requests
	 */
	private handleNotification(OwoRequestNotification n){
		switch(n.action){

			case NotificationType.OWO_REQUEST:

				//def requester = n.owoRequest.requester
				out << g.link(mapping: "profile", id: n.owoRequest.requester.id) { n.owoRequest.requester }
				out << " möchte am oWo "
				out << g.link(controller: "owo", action:"show", id: n.owoRequest.owo.id) { n.owoRequest.owo }
				out << " teilnehmen. "
				break

			case NotificationType.OWO_REQUEST_CONFIRMED:
				out << g.link(mapping: "profile", id: n.owoRequest.requester.id) { n.owoRequest.requester }
				out << " wurde von "
				out << g.link(mapping: "profile", id: n.owoRequest.acceptor.id) { n.owoRequest.acceptor}
				out << " als <span title=\"aktiver Teilnehmer\" class=\"tooltip\">Performer</span> bestätigt. oWo "
				out << g.link(controller: "owo", action:"show", id: n.owoRequest.owo.id) { n.owoRequest.owo }

				break

			default:
				out << 'Unhandled notification: '+n.action
		}
	}

	/*
	 * Owo Notifications such as created, deleted 
	 * 	but also useful when requests are rejected (deleted from db) etc.
	 */
	private handleNotification(OwoNotification n){
		switch(n.action){

			case NotificationType.OWO_CREATED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat ein neues oWo erstellt: "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }
				break

			case NotificationType.OWO_REQUEST_DECLINED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " wurde als Performer bei "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }
				out << " abgelehnt. "
				break

			case NotificationType.OWO_REQUEST_REJECTED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat seine Anfrage für "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }
				out << " wieder zurückgezogen. "
				break

			case NotificationType.OWO_PERFORMER_UNSUBSCRIBE:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat das oWo "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }
				out << " verlassen."
				break

			case NotificationType.OWO_PERFORMER_DELETED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat "
				out << g.link(mapping: "profile", id: n.affectedUser.id) { n.affectedUser }
				out << " als Performer im oWo "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }
				out << " gelöscht."
				break

			case NotificationType.INVITE_REJECTED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat eine Einladung zu "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }
				out << " abgelehnt. "
				break

			case NotificationType.INVITE_UNINVITED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat die Einladung für "
				out << g.link(mapping: "profile", id: n.affectedUser.id) { n.affectedUser }
				out << " zum oWo  "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }
				out << " zurückgezogen. "
				break

			case NotificationType.OWO_PERFORMER_JOINED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " ist dem community-oWo "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }
				out << " beigetreten. "
				break

			case NotificationType.MISSION_DELETED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat die Mission "
				out << "\""+n.info+"\""
				out << " gelöscht. oWo: "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }

				break

			case NotificationType.OWO_WORKFLOWSTATE_CHANGED:
				out << "oWo-Status von "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }
			// TODO tooltips ausbauen
				out << " geändert auf <span title=\"Am oWo wird gearbeitet...\" class=\"tooltip notify_highlight\">" + n.info +"</span>."
				break

			case NotificationType.PINNWAND_POST_ADDED:
				out << "Neuer "
				out << g.link(controller: "owo", action:"wall",id: n.owo.id) { "Pinnwandeintrag" }
				out << " von "
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " für das oWo "
				out << g.link(controller: "owo", action:"show",id: n.owo.id) { n.owo }
				out << "."
				break

			default:
				out << 'Unhandled notification: '+n.action
		}
	}

	/*
	 * OwoInvitation Notifications
	 */
	private handleNotification(OwoInvitationNotification n){
		switch(n.action){

			case NotificationType.INVITE_CREATED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat "
				def u = n.owoInvitation.invitee
				out << (u ? g.link(mapping: "profile", id: n.owoInvitation.invitee?.id) { n.owoInvitation.invitee } : "gel&ouml;cht")
				out << " zum oWo "
				out << g.link(controller: "owo", action:"show",id: n.owoInvitation.owo.id) { n.owoInvitation.owo }
				out << " eingeladen. "
				break

			case NotificationType.INVITE_CONFIRMED:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " ist jetzt <span title=\"aktiver Teilnehmer\" class=\"tooltip\">Performer</span> bei "
				out << g.link(controller: "owo", action:"show",id: n.owoInvitation.owo.id) { n.owoInvitation.owo }
				out << "."
				break

			default:
				out << 'Unhandled notification: '+n.action
		}
	}

	/*
	 * Mission Notifications
	 */
	private handleNotification(MissionNotification n){

		switch(n.action){

			case NotificationType.MISSION_CREATED:
				out << "Neue Mission "
				out << g.link(controller: "owo", action:"missions", id: n.mission.owo.id) { n.mission }
				out << " für oWo "
				out << g.link(controller: "owo", action:"show",id: n.mission.owo.id) { n.mission.owo }
				out << ". Erstellt von "
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }

				break

			case NotificationType.MISSION_UPDATED:
				out << "Mission "
				out << g.link(controller: "owo", action:"missions", id: n.mission.owo.id) { n.mission }
				out << " für oWo "
				out << g.link(controller: "owo", action:"show",id: n.mission.owo.id) { n.mission.owo }
				out << " wurde geändert von "
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }

				break

			case NotificationType.STEP_DELETED:
				out << "Step "
				out << "\""+n.info+"\""
				out << " für Mission "
				out << g.link(controller: "owo", action:"missions", id: n.mission.owo.id) { n.mission }
				out << " wurde von "
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " gelöscht. oWo: "
				out << g.link(controller: "owo", action:"show",id: n.owoInvitation.owo.id) { n.owoInvitation.owo }

				break

			default:
				out << 'Unhandled MissionNotification: '+n.action
		}
	}

	/*
	 * Step Notifications
	 */
	private handleNotification(StepNotification n){

		switch(n.action){

			case NotificationType.STEP_CREATED:
				out << "Neuer Step "
				out << stepLink(n)
				out << " für Mission "
				out << missionLink(n)
				out << ". oWo: "
				out << owoLink(n)
				out << " Erstellt von "
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				break

			case NotificationType.STEP_UPDATED_DESCRIPTION:
				out << "Step verändert: "
				out << stepLink(n)
				out << " für Mission "
				out << missionLink(n)
				out << ". oWo: "
				out << owoLink(n)
				out << " Erstellt von "
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }

				break

			case NotificationType.STEP_ASSIGNED_TO:
				out << "Step "
				out << stepLink(n)
				out << " zugewiesen an "
				if(n.step.assignedTo)
					out << g.link(mapping: "profile", id: n.step.assignedTo.id) { n.step.assignedTo }
				else
					out << " niemand "
				out << " von "
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }

				break

			case NotificationType.STEP_COMPLETED:
				out << "Step  "
				out << stepLink(n)
				out << " abgeschlossen! Durch "
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " ist ein Step der Mission "
				out << missionLink(n)
				out << " erfüllt. Fürs oWo "
				out << owoLink(n)

				break

			case NotificationType.STEP_ADDED_TEXT:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat einen neuen Text für  "
				out << stepLink(n)
				out << " geschrieben, im oWo "
				out << owoLink(n)
				break
			case NotificationType.STEP_ADDED_LINK:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat einen neuen Link für  "
				out << stepLink(n)
				out << " hinzugefügt. "
				break
			case NotificationType.STEP_ADDED_VIDEO:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat ein neues Video für  "
				out << stepLink(n)
				out << " hinzugefügt. "
				break
			case NotificationType.STEP_ADDED_IMAGE:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat ein neues Bild für  "
				out << stepLink(n)
				out << " hochgeladen. "
				break
			case NotificationType.STEP_ADDED_FILE:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat eine neue Datei für  "
				out << stepLink(n)
				out << " hochgeladen. "
				break
			case NotificationType.STEP_ADDED_COMMENT:
				out << g.link(mapping: "profile", id: n.actionByUser.id) { n.actionByUser }
				out << " hat einen Kommentar zu  "
				out << stepLink(n)
				out << " abgegeben. "
				break

			default:
				out << 'Unhandled MissionNotification: '+n.action
		}
	}

	private stepLink(StepNotification n){
		g.link(uri:"/owo/missions/${n.step.mission.owo.id}#/step/wall/${n.step.id}"){ n.step }
	}

	private missionLink(StepNotification n){
		g.link(controller: "owo", action:"missions", id: n.step.mission.id) { n.step.mission }
	}

	private owoLink(StepNotification n){
		g.link(controller: "owo", action:"show", id: n.step.mission.owo.id) { n.step.mission.owo }
	}

}
