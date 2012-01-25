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

import grails.plugins.springsecurity.Secured
import owrks.controller.AController
import owrks.notification.MissionNotification
import owrks.notification.NotificationType
import owrks.notification.OwoNotification
import owrks.owo.Mission;
import owrks.owo.Owo;
import owrks.owo.Step;
import owrks.owo.StepAttachment;
import owrks.owo.StepComment;
import owrks.owo.StepLink;
import owrks.user.User

@Secured(['ROLE_USER'])	
class MissionController extends AController {

	def notificationService
	def emailService
	
    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        //params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
		def owo = Owo.get(params.id)
		if(!owo) {
			return redirect(uri: "/")
		}
		
		// check if user is performer
		if(!owrksService.isPerformer(getCurrentUser(), owo)) {
			flash.message = "Du musst erst dem oWo beitreten, um die angeforderte Seiten aufrufen zu können."
			return forward(controller: "owo", action: "show", id: owo.id)
		}
		
		def selectedMission = params.missionId ? Mission.get(params.missionId) : null
		def selectedStep = params.stepId ? Step.get(params.stepId) : null
		def missionList = selectedMission ? [ selectedMission ] : owo.missions.sort { it.title }
		
		// display counts in the nav
		def navCount = [ msgCount: 0, attachmentCount: 0, linkCount: 0 ]
		if(selectedStep) {
			navCount.msgCount = StepComment.countByStep(selectedStep)
			navCount.attachmentCount = StepAttachment.countByStep(selectedStep)
			navCount.linkCount = StepLink.countByStep(selectedStep)
		}
		
        [ missionInstanceList: missionList,
			missionInstanceTotal: missionList.size(),
			selectedMission: selectedMission,
			selectedStep: selectedStep,
			owoInstance: owo,
			navCount: navCount]
    }

    def create = {
        def missionInstance = new Mission()
        missionInstance.properties = params
		missionInstance.owo = params.id ? Owo.get(params.id) : null
		return [missionInstance: missionInstance]
    }
	
	def createAjax = {
		def missionInstance = new Mission()
		missionInstance.properties = params
		missionInstance.owo = params.id ? Owo.get(params.id) : null
		return render(template: "createAjax", model: [missionInstance: missionInstance])
	}

    def save = {
        def missionInstance = new Mission(params)
		missionInstance.creator = getCurrentUser()
        if (!missionInstance.hasErrors() && missionInstance.save()) {
            flash.message = "mission.created"
            flash.args = [missionInstance.id]
            flash.defaultMessage = "Mission ${missionInstance.id} created"
			
			/* Notification */
			notify(NotificationType.MISSION_CREATED,currentUser,missionInstance)
			/* End Notification */
			
            redirect(controller: "owo", action: "missions", id: missionInstance.owo.id, params: [ missionId: missionInstance.id ])
        }
        else {
            render(view: "create", model: [missionInstance: missionInstance])
        }
    }
	
	def saveAjax = {
		def missionInstance = new Mission(params)
		missionInstance.creator = getCurrentUser()
		if (!missionInstance.hasErrors() && missionInstance.save(flush: true)) {
			flash.message = "Neue Mission wurde erstellt"
			
			notify(NotificationType.MISSION_CREATED, currentUser, missionInstance)

			// mail
			emailService.sendNewMission(missionInstance)
			
			return render(template: "missionList", model: [ missionInstanceList: [missionInstance], owoInstance: missionInstance.owo, selectedMission: missionInstance ])
		}
		else {
			String message = "Mission konnte nicht gespeichert werden"
			return render(template: "createAjax", model: [ missionInstance: missionInstance, message: message ])
		}
	}

    def show = {
        def missionInstance = Mission.get(params.id)
        if (!missionInstance) {
            flash.message = "mission.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Mission not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [missionInstance: missionInstance, owoInstance: missionInstance.owo]
        }
    }

    def edit = {
        def missionInstance = Mission.get(params.id)
        if (!missionInstance) {
            flash.message = "mission.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Mission not found with id ${params.id}"
            //redirect(action: "show", controller: "owo", id: missionInstance.owo.id)
			
			redirect(url: request.getHeader("referer"))
        }
        else {
            return [missionInstance: missionInstance, owoInstance: missionInstance.owo]
        }
    }
	
	def editAjax = {
		def missionInstance = Mission.get(params.id)
		if (!missionInstance) {
			render(text: "Mission $params.id nicht gefunden")
		}
		else {
			return render(template: "editAjax", model: [missionInstance: missionInstance, owoInstance: missionInstance.owo ] )
		}
	}

    def update = {
        def missionInstance = Mission.get(params.id)
        if (missionInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (missionInstance.version > version) {
                    missionInstance.errors.rejectValue("version", "mission.optimistic.locking.failure", "Another user has updated this Mission while you were editing")
                    render(view: "edit", model: [missionInstance: missionInstance, owoInstance: missionInstance.owo])
                    return
                }
            }
            missionInstance.properties = params
            if (!missionInstance.hasErrors() && missionInstance.save()) {
                flash.message = "mission.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Mission ${params.id} updated"
				
				/* Notification */
				notify(NotificationType.MISSION_UPDATED,currentUser,missionInstance)
				/* End Notification */
				
                redirect(action: "missions", controller:"owo", id: missionInstance.owo.id)
            }
            else {
                render(view: "edit", model: [missionInstance: missionInstance, owoInstance: missionInstance.owo])
            }
        }
        else {
            flash.message = "mission.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Mission not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }
	
	def updateAjax = {
		def missionInstance = Mission.get(params.id)
		if (missionInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (missionInstance.version > version) {
					missionInstance.errors.rejectValue("version", "mission.optimistic.locking.failure", "Another user has updated this Mission while you were editing")
					return render(template: "editAjax", model: [missionInstance: missionInstance, message: infoMessage ])
				}
			}
			missionInstance.properties = params
			String infoMessage = ""
			if (!missionInstance.hasErrors() && missionInstance.save()){
				infoMessage = "Mission wurde geändert!"
				/* Notification */
				notify(NotificationType.MISSION_UPDATED,currentUser,missionInstance)
				/* End Notification */
				return render(template: "missionList", model: [ missionInstanceList: [missionInstance], owoInstance: missionInstance.owo, selectedMission: missionInstance ])
			}
			else {
				infoMessage = "Mission wurde nicht geändert"
				return render(template: "editAjax", model: [missionInstance: missionInstance, message: infoMessage ])
			}
		}
		render(text: "Mission $params.id nicht gefunden!")
	}
	
	def loadOwoMissionListAjax = {
		def owo = Owo.get(params.id)
		def missionInstanceList = Mission.findAllByOwo(owo)
		return render(template: "missionList", model: [ missionInstanceList: missionInstanceList, owoInstance: owo ])
	}
	
	def loadMissionListAjax = {
		def mission = params.id ? Mission.get(params.id) : null
		def owo
		def missionInstanceList
		if(!mission) {
			owo = Owo.get(params.owoId)
			missionInstanceList = owo.missions
		} else {
			missionInstanceList = [ mission ]
		}
		
		return render(template: "missionList", model: [ missionInstanceList: missionInstanceList, owoInstance: (mission ? mission.owo : owo), selectedMission: mission ])
	}
	
	def loadMissionListItemAjax = {
		def missionInstance = Mission.get(params.id)
		//sleep(1000)
		return render(template: "missionListItem", model: [ missionInstance: missionInstance ])
	}

	
	def deleteAjax = {
		def missionInstance = Mission.get(params.id)
		missionInstance.delete()
		render(text: "Mission gelöscht", contentType: "text/plain")
	}
	
    def delete = {
        def missionInstance = Mission.get(params.id)
		def tempMissionTitle = missionInstance.title
		def owo = missionInstance.owo
        if (missionInstance) {
            try {
                missionInstance.delete()
                flash.message = "Mission ${params.id} wurde gelöscht"
				
				/* Notification */
				notifyDelete(NotificationType.MISSION_DELETED,currentUser,owo,tempMissionTitle)
				/* End Notification */
				
                redirect(action: "list", id: missionInstance.owo.id)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Mission ${params.id} könnte nicht gelöscht werden"
                redirect(action: "list", id: missionInstance.owo.id, params: [ missionId: missionInstance.id ])
            }
        }
        else {
            flash.message = "mission.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Mission not found with id ${params.id}"
            redirect(action: "list")
        }
    }
	
	private void notify(NotificationType notificationAction, User currentUser, Mission mission){
		def notification = new MissionNotification(action:notificationAction, actionByUser:currentUser,mission:mission)
		notificationService.notify(notification)
	}
	
	private void notifyDelete(NotificationType notificationAction, User currentUser, Owo owo, String missionTitle){
		def notification = new OwoNotification(action:notificationAction, actionByUser:currentUser,owo:owo,info:missionTitle)
		notificationService.notify(notification)
	}
	
}
