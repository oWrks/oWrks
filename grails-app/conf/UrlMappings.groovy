import grails.util.GrailsUtil
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

class UrlMappings {

	static mappings = {
		
		"/"(controller:"index")
		
		// 500 - internal server error
		"500"(controller: 'error', action: 'index')
		
		// 404 - Not found
		"404"(controller: 'error', action: 'notFound')
		"405"(controller: 'error', action: 'notAllowed')
		
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		
		name index: "/index" {
			controller = "index"
		}
		
		name profile: "/p/$id" {
			controller = "profile"
		}
		
		"/dashboard" {
			controller = "dashboard"
		}
		
		name userNotes: "/notes" {
			controller = "userNote"
			action = "show"
		}
		
		"/confirm/email/$id" {
			controller = "register"
			action = "confirmEmail"
		}
		
		"/owo/tag/$tag" {
			controller = "owo"
			action = "tag"
		}
		
		name impressum: "/impressum" {
			controller = "page"
			action = "impressum"
		}
		
		name about: "/about" {
			controller = "page"
			action = "about"
		}

		// display error
		"/error/$action?"{
			controller = "error"
			constraints {
				// apply constraints here
			}
		}
		
		// language select
		"/language/$lang" {
			controller = "language"
			action = "switchLang"
			id = "$lang"
		}
		
		"/search/$q?" {
			controller = "searchable"
			action = "index"
		}
		
		"/login/$action?"(controller: "login")
		"/logout/$action?"(controller: "logout")
		
		// home
		switch(GrailsUtil.environment) {
			case "development":
				"/"(controller: 'index')
			break
			case "production":
				"/"(controller: 'preRegister')
			break
		}
		
		name inbox: "/inbox" {
			controller = "userMessage"
			action = "inbox"
		}
		
		name outbox: "/outbox" {
			controller = "userMessage"
			action = "outbox"
		}
		
		name changePassword: "/changePassword" {
			controller = "user"
			action = "changePassword"
		}
		
		name owoInvite: "/owo/invite/$id" {
			controller = "performer"
			action = "invite"
				
		}
		
		"/data/owo/$owoId/$filename" {
			controller = "data"
			action = "owo"
		}
		
		"/data/owo/$owoId/step/$stepId/$filename" {
			controller = "data"
			action = "stepByFilename"
		}
		
		
		"/owo/missions/$id" {
			controller = "mission"
			action = "list"
		}
		
		"/owo/performer/$id" {
			controller = "performer"
			action = "list"
		}
		
		name myOwos: "/myOwos" {
			controller = "owo"
			action = "myOwos"
		}
		
		name owoWall: "/owo/wall/$id" {
			controller = "owoPost"
			action = "wall"
		}
		
	}
}
