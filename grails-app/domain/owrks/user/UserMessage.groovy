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


class UserMessage {
	
	String message
	Boolean readFlag = false
	Boolean senderDeleted = false
	Boolean receiverDeleted = false
	
	Date dateCreated
	Date lastUpdated
	
	def owrksService
	
	static transients = [
		'owrksService'	
	]
	
	static belongsTo = [ sender: User, receiver: User ]

    static constraints = {
		message(blank: false, nullable: false)
		sender(nullable: false)
		receiver(nullable: false)
    }
	
	String toString() {
		"${message}"
	}
	
	def setDeleted = {
		def user = owrksService.getCurrentUser()
		if(user.id == sender.id)
			senderDeleted = true
		else if(user.id == receiver.id) {
			receiverDeleted = true
			readFlag = true
		}
	}
	
	def setRead = {
		def user = owrksService.getCurrentUser()
		if(user.id == receiver.id)
			readFlag = true
	}
}
