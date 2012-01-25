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

import java.util.Date
import java.util.List
import java.util.Set

import org.springframework.core.io.Resource

import owrks.notification.Notification
import owrks.notification.NotificationSettings
import owrks.owo.Owo
import owrks.owo.OwoInvitation
import owrks.owo.OwoPost
import owrks.owo.OwoPostComment
import owrks.owo.OwoRequest
import owrks.owo.Performer
import owrks.owo.StepComment
import owrks.owo.StepLink
import owrks.owo.StepText

class User {
	
	def springSecurityService
	def grailsApplication

	String username
	String password
	String password2
	String email
	String firstName
	String lastName
	String picture

	Profile profile
	NotificationSettings notificationSettings
	int loginCount = 0

	boolean enabled = false
	boolean accountExpired = false
	boolean accountLocked = false
	boolean passwordExpired = false

	Date lastLogin
	Date dateCreated
	Date lastUpdated

	static transients = [
		'springSecurityService',
		'password2',
		'deletePicture',
		'grailsApplication'
	]

	static searchable = {
		except = [
			'password',
			'password2',
			'email',
			'picture',
			'score'
		]
	}
	
	static mappedBy = [ 
		owoRequests: 'requester', 
		affectedNotifications: 'affectedUser',
		actionNotifications: 'actionByUser',
		senderMessages: 'sender',
		receiverMessages: 'receiver'
	]
	
	static hasMany = [
		owoInvitations: OwoInvitation,
		owoRequests: OwoRequest,
		performers: Performer,
		owos: Owo,
		affectedNotifications: Notification,
		actionNotifications: Notification,
		senderMessages: UserMessage,
		receiverMessages: UserMessage,
		owoPosts: OwoPost,
		owoPostComments: OwoPostComment,
		stepLinks: StepLink,
		stepComments: StepComment,
		stepTexts: StepText
	]

	static constraints = {
		username(blank: false, unique: true)
		password(blank: false, nullable: false, validator: { password, obj ->
			def password2 = obj.properties['password2']
			if(password2 == null) return true
			password2 == password ? true : ['default.invalid.passwords']
		})
		email(email: true, unique: true, blank: false)
		profile(nullable: true)
		firstName(nullable: true, size: 3..255)
		lastName(nullable: true, size: 3..255)
		picture(nullable: true)
		lastLogin(nullable: true)
		loginCount(nullable: true)
		notificationSettings(nullable: true)
	}

	static mapping = { 
		password column: '`password`'
		owoRequests cache: true, sort: "dateCreated", order: "desc"
		owoInvitations cache: true, sort: "dateCreated", order: "desc"
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	static List getUsersForRole(String userRole) {
		UserRole.findAllByRole(Role.findByAuthority(userRole)).collect { it.user }
	}

	def getDataPath = {
		Resource resource = grailsApplication.getMainContext().getResource("WEB-INF"+ File.separatorChar +"data" + File.separatorChar +"user" + File.separatorChar + "$id")
		File dataFolder = resource.getFile()
		dataFolder.mkdirs()
		return dataFolder
	}

	String toString() {
		return ( firstName && lastName ? "$firstName $lastName" : "${username}" )
	}

	public Boolean deletePicture() {
		def file = new File( getDataPath().toString() + File.separatorChar + picture)
		picture = ''
		file.delete()
		return save()
	}

	def afterInsert() {
		if(!enabled)
			new RegistrationCode(username: username).save()
		// TODO: handle roles (admin, performer, guest)
		UserRole.create(this, Role.findByAuthority('ROLE_USER'))
		lastLogin = new Date()

		def userInvitations = UserInvitation.findAllByEmail(this.email)
		userInvitations.each { userInvitation ->
			Owo owo = userInvitation.owo
			OwoInvitation owoInvitation = new OwoInvitation(invitee: this, owo: owo, creator: Performer.findByUserAndOwo(owo.owner, owo))
			owo.addToOwoInvitations(owoInvitation)
			if(!owo.save()) {
				owo.errors.each {
					// TODO: log errors
				}
			} else {
				userInvitation.delete()
			}
		}
	}

	def beforeInsert () {
		encodePassword()
		if(!profile)
			profile = new Profile()
		if(!notificationSettings)
			notificationSettings = new NotificationSettings()
	}

	def beforeUpdate () {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	def beforeDelete() {
		UserRole.removeAll(this)
	}

	protected encodePassword() {
		password = springSecurityService.encodePassword(password)
	}


}
