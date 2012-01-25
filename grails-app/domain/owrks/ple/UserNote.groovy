package owrks.ple
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

import owrks.user.User

class UserNote {

	String text
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [ user: User ]
	
    static constraints = {
		text(nullable: true)
    }
	
	static mapping = {
		text type:'text'
	}
}
