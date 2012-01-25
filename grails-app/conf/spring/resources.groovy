import org.springframework.web.servlet.i18n.SessionLocaleResolver

import owrks.user.CustomUserDetailsService
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


// Place your Spring DSL code here
beans = {
	/*emailAuthenticationProvider(EmailAuthenticationProvider) {
		userDetailsService = ref("userDetailsService")
		springSecurityService = ref("springSecurityService")
	}*/
	
	customPropertyEditorRegistrar(owrks.util.CustomPropertyEditorRegistrar)
	
	// set default language
	localeResolver(SessionLocaleResolver) {
		defaultLocale = Locale.GERMAN
		Locale.setDefault(Locale.GERMAN)
	 }
	
	userDetailsService(CustomUserDetailsService)
}




