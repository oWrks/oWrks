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

import grails.plugins.springsecurity.Secured
import owrks.controller.AController
import owrks.owo.Step

@Secured(['ROLE_ADMIN'])
class UserController extends AController{

	def springSecurityService
	def userService
	
	def index = { redirect(action: "list", params: params) }

	@Secured(['IS_AUTHENTICATED_FULLY'])
	def deleteUser = {
		if(params.delete) {
			def user = getCurrentUser()
			if(userService.deleteUser(user))
				flash.message = g.message(code: 'user.deleted', default: 'User deleted')
			else
				flash.message = g.message(code: 'user.deletion.failed', default: 'Oops! Could not delete user.')
			// TODO: send confirmation email?
			return redirect(controller: 'logout')
		} else if(params.never) {
			return redirect(controller: 'profile', action: 'index')
		}
	}
	
	def delete = {
		def user = User.get(params.id)
		if(user) {
			if(userService.deleteUser(user))
				flash.message = g.message(code: 'user.deleted', default: 'User deleted')
			else
				flash.message = g.message(code: 'user.deletion.failed', default: 'Oops! Could not delete user.')
		}
		redirect(action: "list")
	}
	
	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
		[userInstanceList: User.list(params), userInstanceTotal: User.count()]
	}

	@Secured(['IS_AUTHENTICATED_FULLY'])
	def changePassword = {
		if(request.method.toUpperCase() == 'POST') {
			def user = getCurrentUser()
			if(user.password == springSecurityService.encodePassword(params.old)) {
				if(params.password && params.password2 && params.password == params.password2) {
					user.password = params.password
					if(user.save()) {
						flash.message = g.message(code: 'user.password.changed')
					} else {
						flash.message = g.message(code: 'user.password.save.error')
					}
				} else {
					flash.message = g.message(code: 'user.invalid.passwordconfirmation')
					return
				}
			} else {
				flash.message = g.message(code: 'user.invalid.password')
				return
			}
			// TODO: send confirmation email?
		}
	}
}
