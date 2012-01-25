package owrks.app
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

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser

import owrks.owo.Owo
import owrks.owo.Performer
import owrks.user.User

class owrksService {

    static transactional = false

	def springSecurityService
	def grailsApplication
	

    User getCurrentUser(){
		return springSecurityService.principal instanceof GrailsUser ? User.get(springSecurityService.principal.id) : null
	}
	
	Boolean isPerformer(User user, Owo owo) {
		def isPerformer = Performer.createCriteria().count {
			eq("user", user)
			eq("owo", owo)
		}
		return isPerformer
	}
	
	def parseTags(String tagString){
		def tags = []
		tagString.split (',').each {
			def t = it.trim()
			if(!tags.contains(t))
				tags.add t;  
		}
		return tags				
	}
}
