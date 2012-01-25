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

import java.util.Date;

import org.grails.taggable.Taggable;

class Profile implements Taggable {
	
	String gender
	Date birthday
	String description
	String city
	String country
	String twitter
	String website
	String skype
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [ user: User ]

    static constraints = {
		gender(nullable: true, inList: ['Männlich', 'Weiblich', 'Keine Angabe'])
		birthday(nullable: true)
		description(nullable: true, size: 0..255)
		city(nullable: true, size: 3..255)
		country(nullable: true, size: 3..3, matches:"[a-zA-Z]+")
		twitter(nullable: true)
		website(nullable: true)
		skype(nullable: true)
    }
}
