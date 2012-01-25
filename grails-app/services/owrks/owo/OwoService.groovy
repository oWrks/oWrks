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
import owrks.owo.OwoInvitation;
import owrks.owo.OwoRequest;
import owrks.owo.Performer
import owrks.user.User;


class OwoService {

	static transactional = false

	def getLatestOwos(params) {
		def owos = Owo.createCriteria().list(max: params.max, offset: params.offset ?: 0) {
			ne("type", "private")
			order("dateCreated", "desc")
		}
		return owos
	}

	/*
	 * Alle owos laden an denen der User nicht performt
	 * und auch nicht Ersteller des owos ist
	 */
	def getLatestOwosByUser(params, user) {
		def userOwos = Owo.findAllByOwner(user)
		def performedOwos = Performer.createCriteria().list() {
			eq("user", user)
		}
		
		// owo bei denen der user performt nicht anzeigen
		performedOwos.each { performer ->
			def userIsPerformer = userOwos.find { userOwo -> userOwo.id == performer.owo.id }
			if(!userIsPerformer)
				userOwos.add(performer.owo)
		}
		
		def owos = Owo.createCriteria().list(max: params.max, offset: params.offset ?: 0) {
			ne("type", "private")
			userOwos.each {
				ne("id", it.id)
			}
			order("dateCreated", "desc")
		}
		return owos
	}

	def getFreeSlots(Owo owo) {
		if(owo.maxPerson) {
			def performerCount = Performer.createCriteria().count {
				eq("owo", owo)
			}
			def freeSlots = owo.maxPerson - (performerCount ?: 0)
			return freeSlots
		} else {
			//return "&infin;"
			return 0
		}
	}
	
	def getAllOwos(params) {
		def owos = Owo.createCriteria().list(max: params.max, offset: params.offset ?: 0, sort: (params.sort ?: 'dateCreated'), order: (params.order ?: 'desc')) {
			ne("type", "private")
			//order("dateCreated", "desc")
		}
		return owos
	}
	
	def getAllPublicOwos(params) {
		def owos = Owo.createCriteria().list(max: params.max, offset: params.offset ?: 0) {
			eq("type", "public")
			order("dateCreated", "desc")
		}
		return owos
	}
	
	def getAllCommunityOwos(params) {
		def owos = Owo.createCriteria().list(max: params.max, offset: params.offset ?: 0) {
			eq("type", "community")
			order("dateCreated", "desc")
		}
		return owos
	}

	def getOwosCreatedBy(params,user) {
		def owos = Owo.createCriteria().list(max: params.max, offset: params.offset ?: 0) {
			eq("owner", user)
			order("dateCreated", "desc")
		}
		return owos
	}

	def getOwosByPerformer(params,user) {
		

		def owos = Owo.withCriteria() {
			performers {
				eq("user", user)
				order("dateCreated", "desc")
			}
			ne("owner", user)
			
		}

		return owos
	}
	
	def getOwoRequestsByUser(User user) {
		return OwoRequest.findAllByRequester(user, [ sort: "dateCreated", order: "desc"])
	}
	
	def getOwoInvitationsByUser(User user) {
		return OwoInvitation.createCriteria().list() {
			eq("invitee", user)
			eq("accepted", false)
			order("dateCreated", "desc")
		}
	}
	
	def getOwoInvitationsByOwo(Owo owo) {
		return OwoInvitation.findAllByOwo(owo, [ sort: "dateCreated", order: "desc"])
	}

	def getConfirmedPerformers(Owo owo) {
		def performers = Performer.createCriteria().list() {
			eq("owo", owo)
			order("dateCreated", "desc")
		}
		return performers
	}
}
