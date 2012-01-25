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

import groovy.time.TimeCategory

/*
 * This tag-lib is used for general purposes.
 * therefore some helpers are organized in this place
 * instead of separating in a new tag-lib.
 */
class AppTagLib {

	static namespace = "app"

	def grailsApplication

	/*
	 * load css file for the current controller,
	 * if it exists. e.g. IndexController -> load index.css
	 */
	def controllerStyle = {attrs ->
		def file = "css/controller/" + params.controller + ".css";
		if(grailsApplication.mainContext.getResource(file).exists()){
			out << "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + g.resource(file: file) + "\" />";
		}
	}

	def profilePicture = { attrs ->
		//<img src="${ resource(dir: 'data/user/' + entity.id + '/', file: entity.picture) }" alt="${entity.picture}" title="" style="float:right"/>
		out << attrs.entity.getDataPath()
	}

	def renderUserPicture = { attrs ->
		def user = attrs.user
		def cssClass = attrs.class ?: ''
		out << g.link( mapping:"profile", id: user.id, class: cssClass) { "<img src=\"${g.createLink(controller: 'image', action: 'profile', id: user.id)}\" alt=\"${user.username}\" title=\"${user.username}\" />" }
	}

	def languageChooser = { attrs ->
		String targetUri = (request.forwardURI - request.contextPath)
		attrs.languages = attrs.languages ?: grailsApplication.config.languages
		attrs.languages.each{
			out << "<a class=\"language language-${it}\" href=\"${createLink(controller: 'language', action: it, params: [ 'ref': targetUri])}\" >"
			out << flag([language: it, title: message(code: "language.${it}",  default: it)])
			out << "</a>"
		}
	}

	def flag = { attrs ->
		def src = resource(dir: 'images/flags', file: "${attrs.language}.png") ?: ''
		def title = attrs.title
		out << "<img src=\"${src}\" alt=\"${title}\" title=\"${title}\" />"
	}
	
	def niceDateformat = { attrs ->
		if(attrs.date) {
			use(TimeCategory) {
				def date = attrs.future ? (attrs.date - new Date()) : (new Date() - attrs.date)
				out << (!attrs.future ? "vor " : "in ")
				if(date.months)
					out << (date.months > 1 ? "$date.months Monaten" : "einem Monat")
				else if(date.days)
					out << (date.days > 1 ? "$date.days Tagen" : "einem Tag")
				else if(date.hours)
					out << (date.hours > 1 ? "$date.hours Stunden" : "einer Stunde")
				else if(date.minutes)
					out << (date.minutes > 1 ? "$date.minutes Minuten" : "einer Minute")
				else if(date.seconds)
					out << "$date.seconds Sekunden"
				else
					out << "wenigen Sekunden"
			}
		}
	}
	
	def remoteField = { attrs, body ->
		def params = attrs['params'] ?: null
		if(params) {
			String pString = "\'"
			params.each { key, value ->
				pString += "${key}=${value}&"
			}
			pString += "\'"
			attrs['params'] = pString;
		}
			out << g.remoteField(attrs, body)
		}
}
