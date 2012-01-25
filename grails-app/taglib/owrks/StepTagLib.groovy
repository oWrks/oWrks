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

import grails.util.GrailsNameUtils
import owrks.user.User
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
class StepTagLib {

	static namespace = "step"

	def owrksService


	def stepInfo = { attrs ->
		def mission = attrs.mission
		def stepCount = mission?.steps?.size() ?: 0
		if(stepCount) {
			def completedSteps = mission.steps?.findAll{ it.finished }.size() ?: 0
			def openSteps = mission.steps?.findAll{ !it.finished }.size() ?: 0
			def txt = stepCount == 1 ? "1 Step" : "$stepCount Steps"
			out << "$txt  ($completedSteps abgeschlossen / $openSteps offen)"
		} else {
			out << "Keine Steps (- abgeschlossen / - offen)"
		}
	}

	def assignedTo = { attrs ->
		User currentUser = owrksService.getCurrentUser()
		def assignedToUser = attrs.assignedTo
		if(currentUser?.id == assignedToUser?.id)
			out << "<span class=\"assignedToYou\">Dich</span>"
		else
			out << assignedToUser.toString()
	}

	/*
	 * copied from owo-taglib (renderAttachmentLink)
	 */
	def renderAttachmentLink = { attrs ->
		def entity = attrs.bean
		def attachment = entity.attachment ?: null
		if(attachment) {
			def filesize = (attachment.filesize/1000) as int
			out << "<p class=\"link\">"
			out << "g.link(controller: 'data', action: 'step', id: attachment.id){ $attachment.filename <span class=\"filesize\">($attachment.filesize KB)</span> }"
			out << "</p>"
		}
	}

	def renderWallPost = { attrs ->
		def post = attrs.post
		String className = GrailsNameUtils.getShortName(post.class)
		switch(className) {
			case 'StepText':
				out << g.render(template: "/step/stepText", model: [ stepText: post ])
				break;
			case 'StepLink':
				out << g.render(template: "/step/stepLink", model: [ link: post ])
				break;
			case 'StepComment':
				out << g.render(template: "/step/stepComment", model: [ comment: post ])
				break;
			case 'StepAttachment':
				out << g.render(template: "/step/stepAttachment", model: [ stepAttachment: post ])
				break;
		}
	}
	
	def renderAttachmentImage = { attrs ->
		def attachment = attrs.attachment
		def validImageTypes = CH.config.grails.mime.types.image
		if(validImageTypes.contains(attachment.contentType)) {
			def imageTitle = attachment.title ?: attachment.filename
			out << g.link(controller: "data", action:"step", id:"${attachment.id}"){ 
				"<img src=\"${g.createLink(controller: 'data', action:'stepImage', id: attachment.id)}\" alt=\"${imageTitle}\" title=\"$imageTitle\" class=\"stepImage\" />" 
			}
		} else {
			out << g.link(controller: "data", action:"step", id:"${attachment.id}"){ attachment.filename }
			out << "<span class=\"filesize\">(${attachment.filesize} KB)</span>"
		}
	}
	
	def renderYoutubeVideoId = { attrs ->
		def url = attrs.url
		def m = url =~ /[\?\&]v=([^\?\&]+)/
		if(m.size() == 1 && m[0][1].size() == 11)
			out << m[0][1]
	}
}
