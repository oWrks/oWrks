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

import owrks.notification.OwoRequestNotification
import owrks.user.User

class OwoRequest {

	User acceptor
	Boolean emailSent = false
	Boolean accepted = false
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [ owo: Owo, requester: User]
	
	static hasMany = [ owoRequestNotifications: OwoRequestNotification ]
	
	static constraints = {
		owo(nullable:false)
		requester(nullable:false, unique: 'owo')
		acceptor(nullable:true)
	}
	
	static mapping = {
		owoRequestNotifications cache: true, sort: "dateCreated", order: "desc"
	}
	
	def afterUpdate = {
		// if accepted, set all pending OwoInvitations to true;
		if (accepted){
			OwoInvitation pendingInvite = OwoInvitation.createCriteria().get {
					eq("invitee",requester)
					eq("owo",owo)
					eq("accepted",false)
			}
			if(pendingInvite){
				pendingInvite.accepted = true
				try {
					pendingInvite.save()
				} catch(Exception ex) {
					log.error("Error afterUpdate OwoRequest:" + ex)
				}
			}	
		} 
	}
	
	String toString() {
		"Anfrage für $owo von ${requester?.username} - bestätigt: $accepted (von ${acceptor?.username})"
	}
	
}
