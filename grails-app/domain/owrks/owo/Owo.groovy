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

import org.grails.taggable.Taggable
import org.springframework.core.io.Resource

import owrks.notification.OwoNotification;
import owrks.user.User

class Owo implements Taggable {

	def grailsApplication
	
	String title
	String goal
	String invitationText
	int maxPerson
	String hashTag
	String type
	Boolean published = false

	Date startDate
	Date endDate
	Date dateCreated
	Date lastUpdated
	
	String workflowState = 'Starting'
	String picture = 'noOwoPic.png'
	
	static belongsTo = [ owner: User ]
	
	static searchable = {
		except = ['lastUpdated']
		title(boost: 1.4)
		spellCheck "include"
	}
	
	static hasMany = [
		performers: Performer,
		posts: OwoPost,
		missions: Mission,
		owoInvitations: OwoInvitation,
		owoRequests: OwoRequest,
		owoNotifications: OwoNotification
	]
	
	static transients = [ 'grailsApplication' ]

	static constraints = {
		title(blank: false, size: 7..80)
		goal(blank: false, size: 10..255)
		invitationText(nullable: true)
		maxPerson(nullable: true, /*validator: { val, obj ->
			if(obj['type'] != 'community' && (val < 2 || val > 9) )	
				return ['owo.invalid.maxperson']
		}*/)
		startDate(nullable: true)
		type(blank: false, inList: [ 'private', 'public', 'community'])
		workflowState(blank:false, inList: ['Starting','Forming','Storming','Performing','Publishing'])
		endDate(nullable: true, validator: { val, obj ->
			if ( val && obj.startDate && val < obj.startDate )
				return ['owo.invalid.enddate']
		})
		hashTag(nullable: true)
		picture(nullable: true)
	}
	
	static mapping = {
		missions cache: true, sort:"title", order:"desc"
		posts cache:false, sort:"dateCreated", order:"desc"
		performers cache: true, sort:"dateCreated", order:"desc"
		owoNotifications cache: true, sort:"dateCreated", order:"desc"
		owoInvitations cache: true, sort:"dateCreated", order:"desc"
	}

	def beforeInsert() {
		// z.B. #oWoRJ3211 (Initialen+Tag im Jahr+Jahr)
		hashTag = "AL" + "${maxPerson}" + (title.trim().substring(0,3).toUpperCase()) + new Date().format("dMyy")
		workflowState = "Forming"
	}
	
	def afterInsert() {
		//addToPerformers(user: owner, isAdmin: true)
		//save()
	}
	
	def getDataPath = {
		Resource resource = grailsApplication.getMainContext().getResource("WEB-INF"+ File.separatorChar + "data" + File.separatorChar +"owo" + File.separatorChar + "$id")
		File dataFolder = resource.getFile()
		if(!dataFolder.exists())
			dataFolder.mkdirs()
		return dataFolder
	}
	
	def getSteps = {
		if(!missions)
			return null
		return Step.withCriteria() {
			'in'("mission", missions)
		}
	}
	
	def getPendingRequest = {
		OwoRequest.createCriteria().list() {
			eq("owo", this)
			eq("accepted", false)
		}
	}
	
	String toString() {
		"${title}"
	}
}
