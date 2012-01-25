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

import grails.plugins.springsecurity.Secured
import owrks.controller.AController
import owrks.user.AlphaUser;
@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class PreRegisterController extends AController {

    def index = { }
	
	def save = {
		def user = new AlphaUser()
		user.email = params.email
		if(user.save()) {
			params.success = true
		}
		return render(view: 'index', model: [ success: params.success, entity: user])
	}
	
	@Secured(['ROLE_ADMIN'])
	def listall = {
		def users = AlphaUser.list()
		
		[ users: users ]
	}
}
