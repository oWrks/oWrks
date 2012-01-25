package owrks.security
import org.springframework.security.*
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.context.*
import org.springframework.security.core.Authentication
import org.springframework.security.providers.*
import org.springframework.security.userdetails.*

import owrks.user.User

class EmailAuthenticationProvider implements AuthenticationProvider {

	def userDetailsService
	def springSecurityService

	Authentication authenticate(Authentication customAuth) {
		User.withTransaction { status ->
			User user = User.findByEmail(customAuth.principal)
			if(user) {
				def userDetails = userDetailsService.loadUserByUsername(user.username)
				if (user.password == springSecurityService.encodePassword(customAuth.credentials)) {
					/*def userDetails = new GrailsUserImpl(user.email, user.passwd, true, true, true, true, authorities, user)
					def token = new UsernamePasswordAuthenticationToken(userDetails, user.passwd, userDetails.authorities)
					token.details = customAuth.details
	
					return token*/
					return new UsernamePasswordAuthenticationToken(user.username, user.password, userDetails.authorities)
				} else {
					throw new BadCredentialsException("Log in failed - identity could not be verified");
				}
			}else {
				return null
			}
		}
	}

	boolean supports(Class authentication) {
		return true
	}
}
