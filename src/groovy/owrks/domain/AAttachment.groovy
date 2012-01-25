package owrks.domain

import java.util.Date;

abstract class AAttachment {

	String title
	String filename
	String contentType
	long filesize
	
	Date dateCreated
	Date lastUpdated
	
	static constraints = {
		title(nullable: true)
		filename(blank: false)
		contentType(nullable: true)
		filesize(nullable: true)
	}
	
	String toString() {
		"${title ?: filename}"
	}
	
}
