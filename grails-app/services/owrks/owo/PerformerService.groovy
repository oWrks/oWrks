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

import owrks.owo.Owo
import owrks.owo.OwoInvitation
import owrks.owo.OwoRequest
import owrks.owo.Performer;
import owrks.user.User

class PerformerService {

    static transactional = false
	
	/*
	 * 
	 */
	def getUserListNotYetInvited(Owo owo) {
		return getUserListNotYetInvited(owo,"")
	}
	
	def getUserListNotYetInvited(Owo owo, String filterByUsername) {
		return User.withCriteria{
			owo.owoInvitations.each { p ->
				 ne("username", p.invitee.username) // means no performer nor email
			}
			owo.owoRequests.each { p ->
				ne("username", p.requester.username)
			}
			owo.performers.each { p ->
				ne("username", p.user.username) 
				
			}
			
			like("username", filterByUsername + "%")
			
			maxResults(5)
			order("dateCreated", "desc")
		}
	}
	
	def getUserListPerformers(Owo owo){
		return getUserListPerformers(owo,"")
	}
	
	def getUserListPerformers(Owo owo, String filterByUsername){
		return Performer.withCriteria{
			user {
				like("username", filterByUsername + "%")
			}
			eq("owo",owo)
			
			maxResults(10)
		}*.user
	}
	
	def getUserListOwoRequests(Owo owo){
		return OwoRequest.withCriteria {
			eq("owo",owo)
			eq("accepted", false)
			maxResults(10)
			order("requester") 
		}*.requester
	}
	
	/*
	 * TODO: überprüfen ob ein owoInvite besteht
	 */
	def getUserListInvited(Owo owo) {
		return	OwoInvitation.withCriteria {
			eq("owo",owo)
			eq("accepted", false)
			maxResults(10)
			order("invitee") 
		}*.invitee
	}
}
