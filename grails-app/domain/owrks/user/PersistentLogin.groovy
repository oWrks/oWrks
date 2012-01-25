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

class PersistentLogin {

	String id
	String username
	String token
	Date lastUsed

	static constraints = {
		username maxSize: 64
		token maxSize: 64
		id maxSize: 64
	}
	
	static transients = ['series']

	void setSeries(String series) { id = series }
	String getSeries() { id }

	static mapping = {
		table 'persistent_logins'
		id column: 'series', generator: 'assigned'
		version false
	}
}
