package owrks.app
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

import java.io.File
import java.io.InputStream

import javax.servlet.http.HttpServletRequest

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.springframework.beans.factory.InitializingBean
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import owrks.domain.FileUploadException

class UploadService implements InitializingBean {

    static transactional = false
	
	def gspTagLibraryLookup  // being automatically injected by spring
	def g

	public void afterPropertiesSet() {
		g = gspTagLibraryLookup.lookupNamespaceDispatcher("g")
	}

    def validateUserImage(file) {
		def imageMimeTypes = CH.config.grails.mime.types.image
		def maxUploadSize = CH.config.grails.owrks.maxImageUploadSize
		if(!imageMimeTypes.contains(file.contentType)) {
			return g.message(code: 'default.invalid.imageContentType', args: [ file.contentType, imageMimeTypes ])
		} else if(((file.size/1000) as int) > maxUploadSize) {
			String filesize = ((file.size/1000) as int) + ' KB'
			return g.message(code: 'default.invalid.imageSize', args:[ filesize, maxUploadSize + ' KB'])
		} else {
			return true
		}
    }
	
	def validateFile(file) {
		def mimeTypes = CH.config.grails.owrks.accepted.mime.types
		def maxUploadSize = CH.config.grails.owrks.maxFileUploadSize
		if(!mimeTypes.contains(file.contentType)) {
			return g.message(code: 'default.invalid.imageContentType', args: [ file.contentType, mimeTypes ])
		} else if(((file.size/1000) as int) > maxUploadSize) {
			String filesize = ((file.size/1000) as int) + ' KB'
			return g.message(code: 'default.invalid.fileSize', args:[ file.getOriginalFilename(), filesize, maxUploadSize + ' KB' ])
		} else {
			return true
		}
	}
	
	/*
	 * AJAX-Upload
	 */
	def upload(File targetFile, HttpServletRequest request) {
		InputStream inStream = selectInputStream(request)
		try {
			targetFile << inStream
		} catch (Exception e) {
			throw new FileUploadException(e)
		}
		return targetFile
	}
	
	/*
	 * used by AJAX-Upload
	 */
	private InputStream selectInputStream(HttpServletRequest request) {
		if (request instanceof MultipartHttpServletRequest) {
			MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('qqfile')
			return uploadedFile.inputStream
		}
		return request.inputStream
	}
}
