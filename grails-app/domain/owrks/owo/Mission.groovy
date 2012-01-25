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

import owrks.notification.MissionNotification;
import owrks.user.User

class Mission implements Taggable {
	
	User creator
	String title
	String description

	Date startDate
	Date endDate
	
	Date dateCreated
	Date lastUpdated
	 
	static belongsTo = [ owo: Owo ]
	
	static hasMany = [ 
		steps: Step,
		missionNotifications: MissionNotification
	]

    static constraints = {
		creator(nullable: true)
		title(blank: false, size: 3..255)
		description(nullable: true)
		startDate(nullable: true)
		endDate(nullable: true)
    }
	static mapping = {
		steps cache: false, sort:'sequenceOrder', order:'asc'
		missionNotifications cache: true, sort:"dateCreated", order:"desc"
	}

	String toString(){
		"$title"
	}
}
