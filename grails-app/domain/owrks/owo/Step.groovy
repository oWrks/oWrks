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

import org.grails.taggable.Taggable

import owrks.notification.StepNotification;
import owrks.user.User

class Step implements Taggable {

	def owrksService
	
	User createdBy
	User assignedTo
	String title
	String description
	Date dueDate
	
	int sequenceOrder = 1
	
	Boolean published = false
	Boolean finished = false
	
	User lastEditor
	Date dateCreated
	Date lastUpdated
			
	static belongsTo = [ mission: Mission ]
	
	static hasMany = [ 
		comments: StepComment,
		links: StepLink,
		texts: StepText,
		attachments: StepAttachment,
		stepNotifications: StepNotification
	 ]

	static transients = [ 'owrksService' ]
	
    static constraints = {
		createdBy(nullable: true)
		assignedTo(nullable: true)
		title(blank: false, size: 3..255)
		description(nullable: true)
		dueDate(nullable: true)
		sequenceOrder(nullable: true)
		lastEditor(nullable: true)
    }
	
	static mapping = {
		comments cache: true, sort:"dateCreated", order:"desc"
		links cache: true, sort:"dateCreated", order:"desc"
		attachments cache: true, sort:"dateCreated", order:"desc"
		stepNotifications cache: true, sort:"dateCreated", order:"desc"
		texts cache: true, sort:"dateCreated", order:"desc"
	}
	
	
	def beforeUpdate() {
		lastEditor = owrksService.getCurrentUser()
		if(!finished && published)
			published = false
	}
	
	def getDataPath = {
		File stepDataFolder = new File( mission.owo.getDataPath().toString() + File.separatorChar + "step" + File.separatorChar + "$id")
		stepDataFolder.mkdirs() // TODO: if(dataFolder.exists()) ??
		return stepDataFolder
	}
	
	String toString() {
		"$title"
	}
}
