package owrks.user
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

import owrks.controller.AController
import grails.plugins.springsecurity.Secured

import org.grails.taggable.TagException

import owrks.controller.AController
import owrks.owo.Performer
import owrks.user.User;

@Secured(['IS_AUTHENTICATED_FULLY'])
class ProfileController extends AController {

	def uploadService

	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def index = {
		def user = params.id ? User.get(params.id) : getCurrentUser()
		def activeOwos = Performer.withCriteria{
			eq('user', user)
			owo { ne('type', 'private')	 }
		}*.owo

		[ entity: user, activeOwos: activeOwos ]
	}

	def edit = {
		[ entity: getCurrentUser() ]
	}

	def save = {
		def user = getCurrentUser()
		bindData(user, params, [ exclude: ['picture', 'profile.tags']])
		if(params.birthday){
			user.profile.birthday=params.birthday
		}
		try {
			user.profile.setTags(params.profile.tags.tokenize(','))
		} catch(TagException e) {
			// TODO: handle this
			// this exception will be thrown e.g. if tag is empty (tag1, , tag2)
		}
		def f = request.getFile('picture')
		if(!user.hasErrors() && !f.empty) {
			def validatedImage = uploadService.validateUserImage(f)
			if(validatedImage instanceof Boolean && validatedImage) {
				def newFilename = new File( user.getDataPath().toString() + File.separatorChar + f.getOriginalFilename())
				f.transferTo( newFilename )
				if(user.picture)
					user.deletePicture()
				user.picture = f.getOriginalFilename()
				flash.message = "${message(code: 'profile.upload.saved')}"
			} else {
				user.errors.reject('user.imageupload.error', validatedImage)
				return render( view: 'edit', model: [ entity: user ])
			}
		}
		if(!user.hasErrors() && user.save(flush: true)) {
			//render( view: 'index', model: [ entity: user ])
			redirect(action: "index", id: user.id)
		} else {
			render( view: 'edit', model: [ entity: user ])
		}
	}

	def deletePicture = {
		def user = getCurrentUser()
		if(user.id == (params.id as int)) {
			user.deletePicture()
		}
		return redirect(action: index)
	}
}
