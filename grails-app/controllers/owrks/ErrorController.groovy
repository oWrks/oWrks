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

import grails.converters.JSON
import grails.plugins.springsecurity.Secured

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import owrks.controller.AController
import owrks.owo.Owo

@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class ErrorController extends AController {


	def authenticationTrustResolver

	def springSecurityService

	/**
	 * internal server errors (500)
	 */
	def index = {
	}

	/**
	 * file not found (404)
	 */
	def notFound = {
		// TODO add the filename to the log message
		log.info 'a 404 occured: notFound (check with firebug)'
	   [ 'owos': getLatestOwos() ]
	}

	/**
	 * The specified HTTP method is not allowed for the requested resource (405)
	 * For example: POST-method is expected and GET was sent
	 */
	def notAllowed = {
		// TODO add the filename to the log message
		/*request.headerNames.each { name ->
			println "Header '${name}' = '${request.getHeader(name)}'"
		 }*/
		log.info 'a 405 occured: HTTP-method not allowed'
		[ 'owos': getLatestOwos() ]
	}
	
	def getLatestOwos = {
		return Owo.withCriteria {
			ne("type", "private")
			order("dateCreated", "desc")
			maxResults(3)
		}
	}

	def denied = {
		if (springSecurityService.isLoggedIn() &&
		authenticationTrustResolver.isRememberMe(SecurityContextHolder.context?.authentication)) {
			// have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
			redirect action: full, params: params
		}
	}

	def ajaxDenied = {
		render([error: 'access denied'] as JSON)
	}

	def authfail = {
		def username = session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY]
		String msg = ''
		def exception = session[AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY]
		if (exception) {
			if (exception instanceof AccountExpiredException) {
				msg = SpringSecurityUtils.securityConfig.errors.login.expired
			}
			else if (exception instanceof CredentialsExpiredException) {
				msg = SpringSecurityUtils.securityConfig.errors.login.passwordExpired
			}
			else if (exception instanceof DisabledException) {
				msg = SpringSecurityUtils.securityConfig.errors.login.disabled
			}
			else if (exception instanceof LockedException) {
				msg = SpringSecurityUtils.securityConfig.errors.login.locked
			}
			else {
				msg = SpringSecurityUtils.securityConfig.errors.login.fail
			}
		}

		if (springSecurityService.isAjax(request)) {
			render([error: msg] as JSON)
		}
		else {
			flash.message = msg
		}
	}
}
