package owrks.domain

import java.util.Date

import owrks.user.User

/*
 * abstract message class to use for
 * further message-types like system-message,
 * user-message, owo-message...
 * @author Raoul Jaeckel
 * @version 0.1
 */
abstract class AMessage {
	
	String content
	
	Date dateCreated
	Date lastUpdated
	
    static constraints = {
		content(nullable: false, blank: false)
    }
	
	static mapping = {
		content type:"text"
	}
	
	String toString() {
		"$content"
	}
}
