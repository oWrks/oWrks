import grails.plugins.springsecurity.SecurityConfigType
import grails.util.Environment

import org.apache.log4j.DailyRollingFileAppender
import org.springframework.dao.OptimisticLockingFailureException

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

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
			html: [
				'text/html',
				'application/xhtml+xml'
			],
			xml: [
				'text/xml',
				'application/xml'
			],
			text: 'text/plain',
			js: 'text/javascript',
			rss: 'application/rss+xml',
			atom: 'application/atom+xml',
			css: 'text/css',
			csv: 'text/csv',
			all: '*/*',
			json: [
				'application/json',
				'text/json'
			],
			form: 'application/x-www-form-urlencoded',
			multipartForm: 'multipart/form-data',
			image: [
				'image/jpg',
				'image/jpeg',
				'image/png',
				'image/gif'
			]
		]

grails.owrks.accepted.mime.types = [
	'image/jpg',
	'image/jpeg',
	'image/png',
	'image/gif',
	'video/mpeg',
	'application/pdf',
	'application/x-download',
	'audio/mpeg',
	'audio/x-mpeg',
	'audio/mp3',
	'audio/x-mp3',
	'audio/mpeg3',
	'audio/x-mpeg3',
	'audio/mpg',
	'audio/x-mpg',
	'audio/mp4'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

//change default js library
grails.views.javascript.library='jquery'

// The default codec used to encode data with ${}
grails.views.default.codec = "html" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// set per-environment serverURL stem for creating absolute links
environments {
	production { grails.serverURL = "http://demo.owrks.com" }
	development { grails.serverURL = "http://localhost:8080/${appName}" }
	test { grails.serverURL = "http://localhost:8080" }
	standalone { grails.serverURL = "http://localhost:8080" }
}


// standalone logging
if(Environment.current == Environment.CUSTOM){
	println "LOG4J for ${Environment.current} Environment"
	def logDirectory = "./logs/owrks"
	log4j = {

		error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
				'org.codehaus.groovy.grails.web.pages' //  GSP
		warn 'org.mortbay.log', 'grails.app'

		appenders {
			console name:'stdout'

			// stacktrace
			rollingFile name:'stacktrace', file:"${logDirectory}/stacktrace.log"
			rollingFile name:'infoLog', file: "${logDirectory}/owrks-info.log", threshold: org.apache.log4j.Level.INFO, maxFileSize:1024, layout: pattern(conversionPattern: "%d{dd. MMM. yyyy HH:mm:ss} [%t] %c %x%n %-5p %m%n")
			rollingFile name:'warnLog', file: "${logDirectory}/owrks-warn.log", threshold: org.apache.log4j.Level.WARN, maxFileSize:1024, layout: pattern(conversionPattern: "%d{dd. MMM. yyyy HH:mm:ss} [%t] %c %x%n %-5p %m%n")
			rollingFile name:'errorLog', file: "${logDirectory}/owrks-error.log", threshold: org.apache.log4j.Level.ERROR, maxFileSize:1024, layout: pattern(conversionPattern: "%d{dd. MMM. yyyy HH:mm:ss} [%t] %c %x%n %-5p %m%n")
		}
		
		root {
			info 'infoLog', 'warnLog', 'errorLog', 'stdout'
			error()
			additivity = true
		}
	}
} else if(Environment.current == Environment.PRODUCTION) {
	println "LOG4J for ${Environment.current} Environment"
	def catalinaBase = System.properties.getProperty('catalina.base')
	if (!catalinaBase) catalinaBase = '.'
	def logDirectory = "${catalinaBase}/logs/owrks"
	def logfileName = "${logDirectory}/owrks.log"
	log4j = {
		appenders {
			console name:'stdout'
			//console name: "stdout", layout: pattern(conversionPattern: "%d{yyyy-MMM-dd HH:mm:ss,SSS} [%t] %c %x%n %-5p %m%n")

			/* log file appender config & layout */
			appender new DailyRollingFileAppender(
					name:"stdout",
					file: logfileName,
					layout:pattern(conversionPattern: "%d{dd.MM.yy HH:mm:ss} [%t] %-5p %c %x - %m%n"),
					datePattern:"'.'dd-MM-yyyy"
					)
			// threshold: org.apache.log4j.Level.ERROR

			// stacktrace
			rollingFile name:'stacktrace', file:"${logDirectory}/owrks-stack.log"

			// disable stacktrace
			//'null' name: "stacktrace"

			// errors
			file name: "errors", file:"${logDirectory}/owrks-errors.log", layout: pattern(conversionPattern: "%d{dd.MMM.yyyy HH:mm:ss,SSS} [%t] %c %x%n %-5p %m%n")

		}

		/* type of errors to log */
		info  'org.codehaus.groovy.grails.scaffolding.view.ScaffoldingViewResolver',
				'org.codehaus.groovy.grails.web.servlet',  //  controllers
				'org.codehaus.groovy.grails.web.pages', //  GSP
				'org.codehaus.groovy.grails.web.sitemesh', //  layouts
				'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
				'org.codehaus.groovy.grails.web.mapping', // URL mapping
				'org.codehaus.groovy.grails.commons', // class
				'org.codehaus.groovy.grails.plugins', // plugins
				'org.codehaus.groovy.grails.orm.hibernate', // hibernate
				'org.springframework', // spring
				'org.hibernate' // hibernate

		warn 'grails.app'

		root {
			// change the root logger to my tomcatLog file // kein log nach stdout!
			error 'stdout'
			info 'errors'
			additivity = true
		}
	}
} else {
	println "LOG4J for ${Environment.current} Environment"
	def logDirectory = "./logs/owrks"
	log4j = {

		error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
				'org.codehaus.groovy.grails.web',
				'org.codehaus.groovy.grails.web.pages', //  GSP
				'org.codehaus.groovy.grails.plugins',
				'grails.spring',
				'org.springframework',
				'org.hibernate'
		warn 'org.mortbay.log'
		info 'grails.app'

		appenders {
			console name:'stdout', threshold: org.apache.log4j.Level.INFO

			// stacktrace
			rollingFile name:'stacktrace', file:"${logDirectory}/stacktrace.log"
			rollingFile name:'infoLog', file: "${logDirectory}/owrks-info.log", threshold: org.apache.log4j.Level.INFO, maxFileSize:1024, layout: pattern(conversionPattern: "%d{dd. MMM. yyyy HH:mm:ss} [%t] %c %x%n %-5p %m%n")
			rollingFile name:'warnLog', file: "${logDirectory}/owrks-warn.log", threshold: org.apache.log4j.Level.WARN, maxFileSize:1024, layout: pattern(conversionPattern: "%d{dd. MMM. yyyy HH:mm:ss} [%t] %c %x%n %-5p %m%n")
			rollingFile name:'errorLog', file: "${logDirectory}/owrks-error.log", threshold: org.apache.log4j.Level.ERROR, maxFileSize:1024, layout: pattern(conversionPattern: "%d{dd. MMM. yyyy HH:mm:ss} [%t] %c %x%n %-5p %m%n")
		}
		
		root {
			info 'infoLog', 'warnLog', 'errorLog', 'stdout'
			error()
			additivity = true
		}
	}
}

// deployment-config
//tomcat.deploy.username="admin"
//tomcat.deploy.password="sucker"
//tomcat.deploy.url="http://localhost:8080/manager/text"

/** Spring Security Config **/
// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'owrks.user.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'owrks.user.UserRole'
grails.plugins.springsecurity.authority.className = 'owrks.user.Role'
grails.plugins.springsecurity.requestMap.className = 'owrks.user.Requestmap'
grails.plugins.springsecurity.securityConfigType = SecurityConfigType.Annotation

// lock down all
grails.plugins.springsecurity.rejectIfNoRule = true

environments {
	development {
		grails.plugins.springsecurity.controllerAnnotations.staticRules = [
					'/' : [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/index/**' : [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/register/**' : [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/js/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/error/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/css/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/images/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/login/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/logout/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/data/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/plugins/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/atmosphere/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/monitoring/**': ['ROLE_ADMIN'],
					]
	}
	production {
		grails.plugins.springsecurity.controllerAnnotations.staticRules = [
					'/**' : [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/preRegister/**' : [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/index/**' : ['IS_AUTHENTICATED_FULLY'],
					'/register/**' : [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/js/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/error/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/css/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/images/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/login/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/logout/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/data/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/plugins/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/atmosphere/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/monitoring/**': ['ROLE_ADMIN'],
					]
	}
	standalone {
		grails.plugins.springsecurity.controllerAnnotations.staticRules = [
					'/**' : [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/preRegister/**' : [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/index/**' : ['IS_AUTHENTICATED_FULLY'],
					'/register/**' : [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/js/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/error/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/css/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/images/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/login/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/logout/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/data/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/plugins/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/atmosphere/**': [
						'IS_AUTHENTICATED_ANONYMOUSLY'
					],
					'/monitoring/**': ['ROLE_ADMIN'],
					]
	}
}

// url handler
grails.plugins.springsecurity.logout.afterLogoutUrl='/'

grails.plugins.springsecurity.successHandler.alwaysUseDefault = false
grails.plugins.springsecurity.successHandler.defaultTargetUrl='/dashboard'
grails.plugins.springsecurity.successHandler.ajaxSuccessUrl='/dashboard'
grails.plugins.springsecurity.successHandler.useReferer=false

grails.plugins.springsecurity.failureHandler.defaultFailureUrl='/login/authfail'

grails.plugins.springsecurity.auth.loginFormUrl='/login/auth'
grails.plugins.springsecurity.auth.ajaxLoginFormUrl='/login/auth'
grails.plugins.springsecurity.auth.useForward=false
grails.plugins.springsecurity.adh.errorPage='/login/auth'
grails.plugins.springsecurity.adh.ajaxErrorPage='/error/ajaxDenied'

grails.plugins.springsecurity.providerNames = [
	'daoAuthenticationProvider',
	'anonymousAuthenticationProvider',
	'rememberMeAuthenticationProvider'
]

// account exception mapping
/*grails.plugins.springsecurity.failureHandler.exceptionMappings = [
 'org.springframework.security.authentication.LockedException':             '/user/accountLocked',
 'org.springframework.security.authentication.DisabledException':           '/user/accountDisabled',
 'org.springframework.security.authentication.AccountExpiredException':     '/user/accountExpired',
 'org.springframework.security.authentication.CredentialsExpiredException': '/user/passwordExpired'
 ]*/

/** grails.plugins.springsecurity.rememberMeServices */
grails.plugins.springsecurity.rememberMe.cookieName = 'owrksUserToken'
grails.plugins.springsecurity.rememberMe.alwaysRemember = false
grails.plugins.springsecurity.rememberMe.tokenValiditySeconds = 1209600 //14 days
grails.plugins.springsecurity.rememberMe.parameter = 'rememberLogin'
grails.plugins.springsecurity.rememberMe.key = 'owrksLogin'
grails.plugins.springsecurity.rememberMe.persistentToken.seriesLength = 16
grails.plugins.springsecurity.rememberMe.persistentToken.tokenLength = 16
grails.plugins.springsecurity.rememberMe.useSecureCookie = false
grails.plugins.springsecurity.rememberMe.persistent = true
grails.plugins.springsecurity.rememberMe.persistentToken.domainClassName = 'owrks.user.PersistentLogin'

/** error messages */
grails.plugins.springsecurity.errors.login.disabled = "Sorry, dein Zugang ist deaktiviert."
grails.plugins.springsecurity.errors.login.expired = "Sorry, dein Zugang ist abgelaufen."
grails.plugins.springsecurity.errors.login.passwordExpired = "Sorry, dein Passwort ist nicht mehr gültig."
grails.plugins.springsecurity.errors.login.locked = "Sorry, dein Zugang ist gesperrt."
grails.plugins.springsecurity.errors.login.fail = "Sorry, es ist niemand mit diesem Benutzernamen und Passwort bei uns registriert."


// register callback events (closures)
grails.plugins.springsecurity.useSecurityEventListener = true

// callback event, after a successful login
grails.plugins.springsecurity.onInteractiveAuthenticationSuccessEvent = { event, appCtx ->
	User.withTransaction {
		def user = User.get(appCtx.springSecurityService.principal.id)
		try {
			if(!user.isAttached())
				user.attach()
			user.lastLogin = new Date()
			user.loginCount += 1
			user.save(flush: true)
		} catch(OptimisticLockingFailureException e) {
			user = User.merge(user)
			log.error("Problem with lastLogin: " + e)
		}
	}
}


/** Mail Config **/
grails {
	mail {
		host = "smtp.gmail.com"
		port = 465
		username = "your-email-address"
		password = "your-password"
		props = ["mail.smtp.auth":"true",
					"mail.smtp.socketFactory.port":"465",
					"mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
					"mail.smtp.socketFactory.fallback":"false",
					"mail.debug": "false"]
	}
}
grails.mail.default.from="info@owrks.com"

environments {
	development {
		grails.mail.disabled=false
	}
	production {
		grails.mail.disabled=false
	}
	standalone {
		grails.mail.disabled=true
	}
}



/** Taggable Plugin **/
//grails.taggable.tag.table="MY_TAGS"
//grails.taggable.tagLink.table="MY_TAG_LINKS"
// By default the plugin will force all tags to lower case
//grails.taggable.preserve.case = true


/** oWrks Defaults **/
// error image path
grails.owrks.imageUserPath="data/user/"
grails.owrks.owoDataPath="data/owo/"
grails.owrks.errorImagePath="images/nopic.png"
grails.owrks.errorUserImagePath="images/noUserPic.png"
grails.owrks.errorOwoImagePath="images/icons/owo-icons/noOwoPic.png"

// max image size in KB
grails.owrks.maxImageUploadSize=500
grails.owrks.maxFileUploadSize=4000

// selectable languages in the frontend
languages = ['de', 'en']

/*
 * oWo-Navigation
 * Target als "controller: x, action: y" angeben.
 * Angabe von Controller ist optional, default "owo".
 * Target kann auch als Mapping angegeben werden. Hierfür
 * muss ein dazugehöriger Eintrag in der UrlMappings.groovy sein
 */
grails.owrks.navigation.owo = [
			"Info": [ action: "show" ],
			"LivePad": [ action: "workspace" ],
			"Missions": [ action: "missions" ],
			"Performer": [ action: "performer" ],
			"Pinnwand": [ mapping: "owoWall" ],
		]

//PLE-Navigation
grails.owrks.navigation.ple = [
			"Dashboard": [ controller: "dashboard" ],
			"Meine oWos": [ mapping: "myOwos" ],
			"Profil": [ controller: "profile" ],
			"Notizen": [ mapping: "userNotes" ],
			"Mailbox": [ mapping: "inbox" ],
			"Settings": [ controller: "notificationSettings" ]
		]

