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

@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class LanguageController {

	/*
	 * switch the language for the given lang-parameter
	 * use referer for redirect to previous site
	 */
    def switchLang = { 
		//String targetUri = params.ref ?: "/"
		// next lines are automatically set by grails
		// String language = params.lang ? params.lang : 'en'
		// LocaleContextHolder.setLocale(new Locale(language))
		// RCU.getLocaleResolver(request).setLocale(request, response, new Locale(language))
		redirect(url: request.getHeader('referer'))
	}
}
