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

import org.codehaus.groovy.grails.commons.GrailsClassUtils


/*
 * a helper taglib to render form fields
 * @author Raoul Jaeckel
 * @version 0.1
 * @see <a href="http://grails.org/doc/latest/guide/single.html#6.3%20Tag%20Libraries">Grails Tag Libraries</a>
 */
class FormTagLib {

	static namespace = 'form'

	/*
	 * renders a label for an form-field
	 * @param string	for: the id of the field to which the label is rendered
	 * @param string	label: text of the label
	 */
	def label = {attrs, body ->
		def labelText = ""

		if(!attrs.label){
			def labelCodeDefault = GrailsClassUtils.getNaturalName(attrs.for)
			labelText = message( code: attrs.for, default:labelCodeDefault )
		}else{
			labelText = message( code: attrs.label, default: attrs.label)
		}

		out << "<div class=\"fieldLabel\"><label for=\"${attrs.for}\">${labelText}</label></div>"
	}


	/*
	 * render (div) field wrapper for input fields
	 * @param string	description: useful for help texts, will be shown below the input field
	 * @param string	class: add additional css-class
	 * @param string	id: used in label-for tag; id of the input field	
	 */
	def fieldWrapper = { attrs, body ->
		def description = attrs.remove("description");
		def cssClass = attrs.remove("class") ?: ''
		def cssId = attrs.remove("id") ?: ''
		
		if(description){
			def code = "${attrs.name}.description"
			description = g.message([code: code, default: description])
		}

		def descriptionMarkup = ""
		if(description){
			descriptionMarkup = "<div class=\"description\">$description</div>"
		}

		attrs.for = attrs.for ?: attrs.name

		// output
		out << "<div "
		if(cssId)
			out <<	"id=\"$cssId\""
		out <<	" class=\"fieldRow${g.hasErrors([bean: attrs.bean, field: attrs.name], ' errors')} $cssClass \">" +
				label(attrs) +
					"<div class=\"field\">" + body() + "</div>" +
				descriptionMarkup +
				"<div class=\"clear\"></div></div>"
	}
}