package owrks.owo
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

import owrks.domain.AAttachment;

class OwoPostAttachment extends AAttachment {

	static belongsTo = [ post: OwoPost ]
	
	def afterDelete = {
		def file = new File(post.owo.getDataPath().toString() + File.separatorChar + filename)
		file.delete()
	}
	
	def getFile = {
		return new File( post.owo.getDataPath().toString() + File.separatorChar + filename)
	}
}
