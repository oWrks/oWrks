package owrks.user
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import owrks.user.User
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
class CustomUserDetailsService implements GrailsUserDetailsService {
 
	static transactional = false
	
	static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]
 
	UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {
		return loadUserByUsername(username)
	}
 
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
 
		User.withTransaction { status ->
			User user = User.findByUsernameOrEmail(username, username)
			if (!user)
				throw new UsernameNotFoundException('User not found', username)
 
			def authorities = user.authorities.collect {new GrantedAuthorityImpl(it.authority)}
 
			return new GrailsUser(user.username, user.password, user.enabled, !user.accountExpired,
				!user.passwordExpired, !user.accountLocked,
				authorities ?: NO_ROLES, user.id)
		}
	}
}