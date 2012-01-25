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
import org.springframework.security.access.prepost.PostAuthorize;

import owrks.controller.AController
import owrks.owo.Owo
import owrks.owo.OwoPostAttachment;
import owrks.owo.Step
import owrks.owo.StepAttachment

@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class DataController {

	def owrksService
	
	def index = { }

	/*
	 * Eine hochgeladene Datei wird geladen und
	 * als bin-stream an den Browser gesendet
	 */
	def owo = {
		def owo = params.owoId ? Owo.get(params.owoId) : null
		def filename = params.filename
		File file = filename ? new File( owo.getDataPath().toString() + File.separatorChar + filename) : null
		if(!file || !file.exists()){
			renderFileNotFound()
			// TODO: need some logging here
			// TODO: sinnvoll Fehlerbild zu laden?
		}else{
			renderFile(file)
		}
	}
	
	def owoDownload = {
		def owo = params.owoId ? Owo.get(params.owoId) : null
		def filename = params.filename
		File file = filename ? new File( owo.getDataPath().toString() + File.separatorChar + filename) : null
		if(!file || !file.exists()){
			renderFileNotFound()
			// TODO: need some logging here
			// TODO: sinnvoll Fehlerbild zu laden?
		}else{
			renderFileDownloadDialog(file, filename)
		}
	}
	
	def owoPost = {
		def owoPostAttachment = params.id ? OwoPostAttachment.get(params.id) : null
		def file = owoPostAttachment.getFile()
		//def filename = params.filename
		//File file = filename ? new File( owo.getDataPath().toString() + File.separatorChar + filename) : null
		if(!file || !file.exists()){
			renderFileNotFound()
			// TODO: need some logging here
			// TODO: sinnvoll Fehlerbild zu laden?
		}else{
			renderFileDownloadDialog(file, owoPostAttachment.filename)
		}
	}
	
	def stepImage = {
		def stepAttachment = params.id ? StepAttachment.get(params.id) : null
		def attachmentFile = stepAttachment.getFile()
		if(stepAttachment && attachmentFile.exists()) {
			renderFile(attachmentFile)
		} else {
			renderFileNotFound()
		}
	}

	/*
	 * Eine hochgeladene Datei wird geladen und
	 * als bin-stream an den Browser gesendet
	 */
	def step = {
		def stepAttachment = params.id ? StepAttachment.get(params.id) : null
		def file = stepAttachment.getFile()
		if(!file || !file.exists()){
			renderFileNotFound()
			// TODO: need some logging here
			// TODO: sinnvoll Fehlerbild zu laden?
		}else{
			renderDownloadDialog(stepAttachment)
		}
	}

	/*
	 * Eine hochgeladene Datei wird geladen und
	 * als bin-stream an den Browser gesendet
	 */
	def stepByFilename = {
		def step = params.stepId ? Step.get(params.stepId) : null
		def filename = params.filename
		def owo = params.owoId ? Owo.get(params.owoId) : null
		def currentUser = owrksService.getCurrentUser()
		
		// TODO: bessere Fehlermeldung bzw. Fehlerbild
		if(!currentUser || !owrksService.isPerformer(currentUser, owo))
			return render(text: "Keine Berechtigung!")
			
		def stepAttachment = StepAttachment.findByStepAndFilename(step, filename)
		
		if(!step || !filename || !stepAttachment)
			return render(text: "Keine Datei gefunden")

		def file = stepAttachment.getFile()
		if(!file || !file.exists()){
			return render(text: "Keine Datei gefunden")
			// TODO: need some logging here
			// TODO: sinnvoll Fehlerbild zu laden?
		}else{
			renderDownloadDialog(stepAttachment)
		}
	}

	/*
	 * Datei-Speichern Dialog öffnen
	 */
	private void renderDownloadDialog(StepAttachment attachment) {
		try{
			response.setContentType(attachment.contentType)
			response.setHeader("Content-disposition", "attachment; filename=" + attachment.filename)
			response.setContentLength(attachment.filesize as int)
			response.getOutputStream() << attachment.getFile().getBytes()
			response.outputStream.flush()
		}catch(IOException e){
			// TODO: need some logging here
		}
	}

	private void renderFileDownloadDialog(File file, String filename){
		try{
			response.setHeader("Content-disposition", "attachment; filename=" + filename)
			response.setContentType(new MimetypesFileTypeMap().getContentType(file))
			response.setContentLength(file.size() as int)
			response.outputStream << file.getBytes()
			response.outputStream.flush()
			//response.outputStream.close()
		}catch(IOException e){
			// TODO: need some logging here
		}
		return
	}
	
	private void renderFile(File file){
		try{
			response.setContentType(new MimetypesFileTypeMap().getContentType(file))
			response.outputStream << file.getBytes()
			response.outputStream.flush()
			//response.outputStream.close()
		}catch(IOException e){
			// TODO: need some logging here
		}
		return
	}

	private void renderFileNotFound() {
		def errorImage = new File(ConfigurationHolder.config.grails.owrks.errorUserImagePath)
		renderFile(errorImage)
	}
}
