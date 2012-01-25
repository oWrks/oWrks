package owrks.notification;
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

public enum NotificationType {

	OWO_REQUEST("A user requests to join a (public) oWo"),
	OWO_REQUEST_CONFIRMED("A user request was confirmed."),
	OWO_REQUEST_REJECTED("A user rejects his request."),
	OWO_REQUEST_DECLINED("A user request was declined (by an admin)."),
	
	OWO_PERFORMER_UNSUBSCRIBE("A performer left the oWo."),
	OWO_PERFORMER_JOINED("A performer joins the oWo (for public right after the request is confirmed)"),
	OWO_PERFORMER_DELETED("A performer was deleted."),
	
	INVITE_CREATED("A invitation was created."),
	INVITE_REJECTED("A invitation was rejected."),
	INVITE_CONFIRMED("A invitation was confirmed."),
	INVITE_UNINVITED("A invitation was cancelled/deleted by an admin"),
	
	OWO_CREATED("A owo was created."),
	OWO_UPDATED("A owo was updated."),
	OWO_DELETED("A owo was deleted."),
	
	OWO_WORKFLOWSTATE_CHANGED("The owo workflow state has changed."),
	
	MISSION_CREATED("A mission was created."),
	MISSION_UPDATED("A mission was updated."),
	MISSION_DELETED("A owo was deleted."),
	MISSION_ACCOMPLISHED("All steps of of this mission are completed."),
		
	STEP_CREATED("A step was created."),
	STEP_DELETE("A owo was deleted."),
	STEP_UPDATED_DESCRIPTION("Step updated."),
	STEP_ASSIGNED_TO("Step assigned to."),
	STEP_COMPLETED("Step completed."),
	
	// STEP CONTENT
	STEP_ADDED_TEXT("Text added to step."),
	STEP_ADDED_LINK("Link added to step."),
	STEP_ADDED_VIDEO("Video added to step."),
	STEP_ADDED_FILE("File added to step."),
	STEP_ADDED_IMAGE("Image added to step."),
	STEP_ADDED_COMMENT("Comment added to step."),
	
	PINNWAND_POST_ADDED("A new entry on the pinnwand."),
	
	String action
			
	NotificationType(String action) {
		this.action = action
	}
	
	
}
