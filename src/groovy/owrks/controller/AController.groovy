package owrks.controller


import grails.plugins.springsecurity.Secured
import owrks.user.User

/**
 * this abstract controller is used to inject the current
 * user-object to the requested view. the latest (user) 
 * messages will also be loaded and passed to the view.
 * @author		Timo,Raoul
 * @version		1.0
 */
@Secured(['IS_AUTHENTICATED_FULLY'])
abstract class AController {
	
	def owrksService
	
	def beforeInterceptor = [action:this.&beforeInterceptor]
	def afterInterceptor = [action:this.&afterInterceptor]
	
	void injectUser(){
		if(!params.user && getCurrentUser() instanceof User ){
			params.user = getCurrentUser()		
		}
	}

	def beforeInterceptor(){
		if(['save'].contains(params.action))
			injectUser()		
	}

	def afterInterceptor(model){
		model.currentUser = getCurrentUser()
		//model.flashMessages = messageService.getFlashMessages()
	}

	User getCurrentUser (){
		return owrksService.getCurrentUser()
	}
	
}
