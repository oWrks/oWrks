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

import grails.plugins.springsecurity.Secured

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

import owrks.controller.AController
import owrks.user.RegistrationCode;
import owrks.user.User

@Secured(["isAnonymous()"])
class RegisterController extends AController{

	/** The authentication provider. */
	def daoAuthenticationProvider
	def emailService
	def springSecurityService

	def index = {
	}

	def confirm = {
	}

	def register = {
		def user = new User(params)
		if(user.save()) {
			// TODO: auto-login after confirmation mail
			// note: must use the unhashed password here
			def code = RegistrationCode.findByUsername(user.username)
			emailService.sendRegistrationConfirmation(user, code)
			return redirect(action: confirm)
		}
		render(view: 'index', model: ['entity': user])
	}

	/*
	 * Generate a new 8-chars long password using RandomStringUtils.
	 * Password will be sent to given email address
	 * @see org.apache.commons.lang.RandomStringUtils
	 * @author Raoul Jaeckel
	 */
	def requestPassword = {
		if(request.method.toLowerCase() == "post"){
			if(	params.email && params.username) {
				def email = params.email
				def user = User.findByUsernameAndEmail(params.username, email)
				Boolean success = false
				if(user) {
					String newPassword = RandomStringUtils.randomAlphanumeric(8)
					user.password = newPassword
					if(user.save()) {
						emailService.sendNewPassword(user, newPassword)
						success = true
						flash.message = g.message(code: "user.new.password.sent")
					}
				} else {
					flash.message = g.message(code: "user.invalid.request.password", args: [params.username, email])
				}
				return [ email: email, username: params.username, success: success ]
			} else {
				flash.message = g.message(code: "user.invalid.missing.request.password")
			}
		}
	}

	/*
	 * when a user confirms his account it will be enabled
	 * and automatic login of the given user
	 * @params	String 	token
	 */
	def confirmEmail = {
		def token = params.id
		def code = RegistrationCode.findByToken(token)
		if(code) {
			def user = User.findByUsername(code.username)
			user.enabled = true
			// TODO use transaction for process?
			if(user.save()) {

				// delete existing reg-code
				code.delete()

				// send welcome mail
				emailService.sendWelcome(user)

				// automatically login
				springSecurityService.reauthenticate(user.username)

				flash.message = g.message(code: 'user.welcome.onboard')

				// TODO: Weiterleitung auf Einführungsseite (getting-started)
				return redirect(controller: "dashboard")
			}
		}
		return redirect(controller: 'login')
	}
}
