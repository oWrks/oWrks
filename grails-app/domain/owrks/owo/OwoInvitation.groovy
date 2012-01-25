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

import java.util.Date

import owrks.notification.OwoInvitationNotification
import owrks.user.User

/*
 * Ein Benutzer wird zu einem owo eingeladen
 */
class OwoInvitation {

	Performer creator
	Boolean emailSent = false
	Boolean accepted = false
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [ invitee: User, owo: Owo ] 
	
	static hasMany = [ owoInvitationNotifications: OwoInvitationNotification ]
	
	static constraints = {
		invitee(unique: 'owo')
    }
	
	def afterUpdate () {
		// if accepted, set all pending OwoRequests to true;
		if (accepted){
			OwoRequest pendingReq = OwoRequest.createCriteria().get {
				eq("requester",invitee)
				eq("owo",owo)
				eq("accepted",false)
			}
			
			try {
				if(pendingReq)
					pendingReq.delete();
			} catch(Exception ex) {
				log.error "Error afterUpdate OwoInvitation:" + ex
			}
		} 
	}
	
	String toString() {
		"Einladung - $owo - accepted:$accepted - von $creator für $invitee"
	}
}
