package owrks
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

import owrks.controller.AController
import owrks.owo.Owo


class IndexController extends AController{

	def owoService
	def stepService
	def userService
	
    def index = {
		params.max = Math.min(params.max ? params.max.toInteger() : 5,  100)
		def newestOwos = owoService.getLatestOwos(params)
		def newestSteps = stepService.getLatestSteps(params)
		def newestUsers = userService.getLatestUsers(params)
		[newestOwos : newestOwos, totalOwos: newestOwos.totalCount, newestSteps : newestSteps, newestUsers : newestUsers ]
	}

	def sitemap = {
	}
	
}