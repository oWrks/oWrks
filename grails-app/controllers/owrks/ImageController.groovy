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

import grails.plugins.springsecurity.Secured

import java.io.File

import javax.activation.MimetypesFileTypeMap

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import org.springframework.core.io.Resource

import owrks.controller.AController
import owrks.owo.Owo
import owrks.user.User



@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class ImageController {

    def index = { }
	
	def profile = {
		def user = User.get(params.id)
		File picture = user.picture ? new File( user.getDataPath().toString() + File.separatorChar + user.picture) : null
		if(!user || !picture || !picture.exists()){
			renderImageNotFound("user")
			// TODO: need some logging here
		}else{
			renderImage(picture)
		}
	}
	
	def owo = {
		def owo = Owo.get(params.id)
		Resource resource = grailsApplication.getMainContext().getResource("images"+ File.separatorChar +"icons" + File.separatorChar +"owo-icons"+ File.separatorChar+owo.picture)
		File picture = owo.picture ? resource.getFile() : null
		if(!owo || !picture || !picture.exists()){
			renderImageNotFound("owo")
			// TODO: need some logging here
			
		}else{
			renderImage(picture)
		}
	}
	
	private void renderImage(File file){
		try{
			response.setContentType(new MimetypesFileTypeMap().getContentType(file))
			response.outputStream << file.getBytes()
			response.outputStream.flush()
		}catch(IOException e){
			// TODO: need some logging here
		}
	}
	
	private void renderImageNotFound(String noPic) {
		def filename = ConfigurationHolder.config.grails.owrks.errorImagePath
		if(noPic.equals("user")){
			filename = ConfigurationHolder.config.grails.owrks.errorUserImagePath
		} else if(noPic.equals("owo")){
			filename = ConfigurationHolder.config.grails.owrks.errorOwoImagePath
		}
		def errorImage = grailsApplication.getMainContext().getResource(filename).getFile()
		renderImage(errorImage)
	}
	 
	private void renderOwoImageNotFound() {
		def errorImage = new File(ConfigurationHolder.config.grails.owrks.errorOwoImagePath)
		renderImage(errorImage)
	}
}
