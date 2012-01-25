package owrks
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

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import owrks.owo.Performer;

class NavigationTagLib {

	static namespace = 'nav'

	def owrksService

	def renderOwoNav = { attrs ->
		def invalidActions = [ "save", "create", "wizzardInviteAgiler", "wizzardInviteEmail", "tag", "index" ]
		if(invalidActions.contains(actionName))
			return
		
		def owo = attrs.owo
		def owoNav = CH.config.grails.owrks.navigation.owo
		def currentUser = owrksService.getCurrentUser()
		Performer performer = Performer.findByUserAndOwo(currentUser, owo)
		if(owo && owoNav) {
			out << "<ul id=\"nav\">"
			owoNav.each { key, value ->
				def cssClass = (actionName == value.action) ? "active" : ""
				if(performer) {
					if((value.action == "missions" && controllerName == "mission") || (value.action == "performer" && controllerName == "performer"))
						cssClass = "active"
					if(value.mapping) {
						if((value.mapping == "owoWall" && controllerName =="owoPost"))
							cssClass = "active"
						out << "<li class=\"$cssClass\">" << g.link(mapping: value.mapping,  id: "${owo?.id}"){ "$key" } << "</li>"
					} else {
						def targetController = value.controller ?: "owo"
						out << "<li class=\"$cssClass\">" << g.link(controller: targetController, action: value.action,  id: "${owo?.id}"){ "$key" } << "</li>"
					}
				} else {
					if(key == 'Info') {
						out << "<li class=\"$cssClass\">" << g.link(controller: "owo", action: value.action,  id: "${owo?.id}"){ "$key" } << "</li>"
						
					} else {
						out << "<li class=\"$cssClass\"><span class=\"inactive\"><abbr title=\"Du nimmst noch nicht an dem oWo teil\">$key</abbr></span></li>"
					}
				}
			}

			out << "</ul>"
		}
	}
	def renderPleNav = { attrs ->
		def pleNav = CH.config.grails.owrks.navigation.ple
		def msgCount = attrs.msgCount
		if(pleNav) {
			out << "<ul id=\"nav\">"
			pleNav.each { key, value ->
				def cssClass = (controllerName == value.controller) ? "active" : ""
				if(value.mapping) {
					if((value.mapping == "inbox" && controllerName =="userMessage") || 
						(value.mapping == "myOwos" && controllerName == "owo") || 
						(value.mapping == "userNotes" && controllerName == "userNote")) {
						cssClass = "active"
					}
					if(value.mapping == "inbox")
						out << "<li class=\"$cssClass\">" << g.link(mapping: value.mapping){ "$key <span>(${msgCount})</span>" } << "</li>"
					else
						out << "<li class=\"$cssClass\">" << g.link(mapping: value.mapping){ "$key" } << "</li>"
				} else{
					out << "<li class=\"$cssClass\">" << g.link(controller: value.controller){ "$key" } << "</li>"
				}
			}
			out << "</ul>"
		}
	}
}
