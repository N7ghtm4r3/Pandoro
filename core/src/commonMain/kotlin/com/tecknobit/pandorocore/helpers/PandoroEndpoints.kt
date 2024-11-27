package com.tecknobit.pandorocore.helpers

import com.tecknobit.equinoxbackend.environment.helpers.EquinoxBaseEndpointsSet

/**
 * The {@code PandoroEndpoints} class is a container with all the Pandoro's endpoints
 *
 * @author N7ghtm4r3 - Tecknobit
 */
object PandoroEndpoints : EquinoxBaseEndpointsSet() {

    /**
     * `ADD_MEMBERS_ENDPOINT` endpoint to add member to group
     */
    const val ADD_MEMBERS_ENDPOINT: String = "/addMembers"

    /**
     * `ACCEPT_GROUP_INVITATION_ENDPOINT` endpoint to accept a group invitation
     */
    const val ACCEPT_GROUP_INVITATION_ENDPOINT: String = "/acceptGroupInvitation"

    /**
     * `DECLINE_GROUP_INVITATION_ENDPOINT` endpoint to decline a group invitation
     */
    const val DECLINE_GROUP_INVITATION_ENDPOINT: String = "/declineGroupInvitation"

    /**
     * `CHANGE_MEMBER_ROLE_ENDPOINT` endpoint to change the role of a member of a group
     */
    const val CHANGE_MEMBER_ROLE_ENDPOINT: String = "/changeMemberRole"

    /**
     * `REMOVE_MEMBER_ENDPOINT` endpoint to remove a member of a group
     */
    const val REMOVE_MEMBER_ENDPOINT: String = "/removeMember"

    /**
     * `EDIT_PROJECTS_ENDPOINT` endpoint to edit the projects of a group
     */
    const val EDIT_PROJECTS_ENDPOINT: String = "/editProjects"

    /**
     * `LEAVE_GROUP_ENDPOINT` endpoint to leave from a group
     */
    const val LEAVE_GROUP_ENDPOINT: String = "/leaveGroup"

    /**
     * `UPDATES_PATH` path for the updates operations
     */
    const val UPDATES_PATH: String = "/updates/"

    /**
     * `SCHEDULE_UPDATE_ENDPOINT` endpoint to schedule a new update for a project
     */
    const val SCHEDULE_UPDATE_ENDPOINT: String = "schedule"

    /**
     * `START_UPDATE_ENDPOINT` endpoint to start an existing update for a project
     */
    const val START_UPDATE_ENDPOINT: String = "/start"

    /**
     * `PUBLISH_UPDATE_ENDPOINT` endpoint to publish an existing update for a project
     */
    const val PUBLISH_UPDATE_ENDPOINT: String = "/publish"

    /**
     * `ADD_CHANGE_NOTE_ENDPOINT` endpoint to add a new change note for an update
     */
    const val ADD_CHANGE_NOTE_ENDPOINT: String = "/addChangeNote"

    /**
     * `MARK_CHANGE_NOTE_AS_DONE_ENDPOINT` endpoint to mark as done a change note of an update
     */
    const val MARK_CHANGE_NOTE_AS_DONE_ENDPOINT: String = "/markChangeNoteAsDone"

    /**
     * `MARK_CHANGE_NOTE_AS_TODO_ENDPOINT` endpoint to mark as todo a change note of an update
     */
    const val MARK_CHANGE_NOTE_AS_TODO_ENDPOINT: String = "/markChangeNoteAsToDo"

    /**
     * `MARK_AS_DONE_ENDPOINT` endpoint to mark as done an existing note
     */
    const val MARK_AS_DONE_ENDPOINT: String = "/markAsDone"

    /**
     * `MARK_AS_TO_DO_ENDPOINT` endpoint to mark as todo an existing note
     */
    const val MARK_AS_TO_DO_ENDPOINT: String = "/markAsToDo"

}