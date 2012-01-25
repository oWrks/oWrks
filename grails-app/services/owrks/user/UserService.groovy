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

import owrks.owo.Mission
import owrks.owo.Step


class UserService {

	static transactional = false

	def getLatestUsers(params) {
		def users = User.createCriteria().list(max: params.max, offset: params.offset ?: 0) {
			order("dateCreated", "desc")
			eq("enabled", true)
		}
		return users
	}

	def getAllUsers(params) {
		def users = User.createCriteria().list(max: params.max, offset: params.offset ?: 0) {
			order("dateCreated", "desc")
		}
		return users
	}
	
	/*
	 * service method to delete an owrks-user
	 * @params User user
	 * @return Boolean 
	 */
	Boolean deleteUser(User user) {
		try {
			// set step relations to null
			def steps = Step.findAllByAssignedToOrCreatedBy(user, user)
			steps.each {
				if(it.assignedTo == user)
					it.assignedTo = null
				if(it.createdBy == user)
					it.createdBy = null
				if(it.lastEditor == user)
					it.lastEditor = null
				it.save()
			}
			
			// set mission relations to null
			def missions = Mission.findAllByCreator(user)
			missions.each {
				it.creator = null
				it.save()
			}
			
			user.delete(flush: true)
			return true
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			log.error("Error while deleting user", e)
			return false 
		}
	}
}
