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

import org.springframework.core.io.Resource

import owrks.owo.Owo

class OwoTagLib {

	static namespace = "owo"
	def owoService
	def grailsApplication
	
	def freeSlots = { attrs ->
		def owo = attrs.owo
		out << (owoService.getFreeSlots(owo) ?: "-")
	}

	def renderAllFiles = { attrs ->
		def owo = attrs.owo
		def postsWithAttachment = owo.posts.findAll { it.attachment }.sort { it.dateCreated }.reverse()
		if(postsWithAttachment) {
			postsWithAttachment.each { post ->
				// TODO: neue Funktion für folgende code, zusammenfassen mit siehe renderAttachmentLink
				def attachment = post.attachment
				//def href = g.resource(dir: owo.getDataPath().toString(), file: attachment.filename)
				def href = g.createLink(controller: "data", action: "owoDownload", params: [ owoId: owo.id, filename: attachment.filename])
				def filesize = (attachment.filesize/1000) as int
				out << "<div class=\"attachment\" id=\"attachment_${post?.id}\">"
				out << "<p class=\"link\">"
				out << "<a href=\"$href\">$attachment.filename</a> <span class=\"filesize\">($filesize KB)</span>"
				out << "</p>"
				out << "</div>"
			}
		} else {
			out << "<p>Es wurden bisher keine Dateien hochgeladen.</p>"
		}
	}

	def renderPostAttachment = { attrs ->
		def attachment = attrs.attachment ?: null
		if(attachment) {
			//def href = g.resource(dir: entity.owo.getDataPath().toString(), file: attachment.filename)
			def href = g.createLink(controller: "data", action: "owoPost", id: attachment.id)
			def filesize = (attachment.filesize/1000) as int
			out << "<p class=\"link\">"
			out << "<a href=\"$href\">$attachment.filename</a> <span class=\"filesize\">($attachment.filesize KB)</span>"
			out << "</p>"
		}
	}
	
	/*
	 * DEPRECATED:
	 */
	def renderAttachmentLink = { attrs ->
		def entity = attrs.bean
		def attachment = entity.attachment ?: null
		if(attachment) {
			//def href = g.resource(dir: entity.owo.getDataPath().toString(), file: attachment.filename)
			def href = g.createLink(controller: "data", action: "owoDownload", params: [ owoId: entity.id, filename: attachment.filename])
			def filesize = (attachment.filesize/1000) as int
			out << "<p class=\"link\">"
			out << "<a href=\"$href\">$attachment.filename</a> <span class=\"filesize\">($attachment.filesize KB)</span>"
			out << "</p>"
		}
	}

	def renderWorkflowState = { attrs ->
		def states = Owo.constraints.workflowState.inList
		def owo = attrs.owo
		if(owo) {
			def currentStateId = states.lastIndexOf(owo.workflowState)
			states.eachWithIndex { workflowState, i ->
				def cssClass = currentStateId > i ? "finished" : "notfinished"
				def description =  g.message(code: 'workflowState.'+workflowState+'.label')
				if(i > 0)
					out << "<span class=\"stateSpacer\">&raquo;</span>"
				if(workflowState == owo?.workflowState){
					out << "<span class=\"currentState tooltip\" title=\"$description \">${workflowState }</span>"
				} else{ 
					out << "<span class=\"otherState $cssClass  tooltip\" title=\"$description\">${workflowState}</span>"
				}
			}
		}
	}
	
	def renderOwoIcons = { attrs -> 
		String folderPath = "images/icons/owo-icons"
		Resource resource = grailsApplication.getMainContext().getResource(folderPath)
		def folder = resource.getFile()
		out << "<p>Bild auswählen</p>"
		out << "<ul id=\"owoPictureList\">"
		folder.eachFile { 
			if(it.toString().endsWith(".png")){
				out << 	"<li><img class=\"picture\" src=\""+g.resource(dir: folderPath, file: it.getName())+"\" alt=\"${it.getName()}\" /></li>"
			}			
		}
		out << "</ul>"
	}
}