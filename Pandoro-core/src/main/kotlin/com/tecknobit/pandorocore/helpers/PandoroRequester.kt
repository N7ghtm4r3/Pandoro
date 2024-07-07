package com.tecknobit.pandorocore.helpers

import com.tecknobit.apimanager.annotations.RequestPath
import com.tecknobit.apimanager.annotations.Returner
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.apimanager.apis.APIRequest.Params
import com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*
import com.tecknobit.equinox.environment.helpers.EquinoxRequester
import com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY
import com.tecknobit.pandorocore.Endpoints.*
import com.tecknobit.pandorocore.records.Changelog.CHANGELOGS_KEY
import com.tecknobit.pandorocore.records.Group.*
import com.tecknobit.pandorocore.records.Note.NOTES_KEY
import com.tecknobit.pandorocore.records.Project.*
import com.tecknobit.pandorocore.records.structures.PandoroItem.IDENTIFIER_KEY
import com.tecknobit.pandorocore.records.users.GroupMember.Role
import org.json.JSONObject

//TODO REMAP REQUESTS ENDPOINTS

/**
 * The **PandoroRequester** class is useful to communicate with the Pandoro's backend
 *
 * @param host: the host where is running the Pandoro's backend
 * @param userId: the user identifier
 * @param userToken: the user token
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Structure
open class PandoroRequester(
    host: String,
    userId: String? = null,
    userToken: String? = null
) : EquinoxRequester(
    host = host,
    userId = userId,
    userToken = userToken,
    enableCertificatesValidation = true,
    connectionErrorMessage = ""
) {

    /**
     * Function to execute the request to get the projects list of the user
     *
     * No-any params required
     *
     * @return the result of the request as [String]
     *
     */
    @RequestPath(path = "/api/v1/projects", method = GET)
    fun execProjectsList(): JSONObject {
        return execGet(
            endpoint = createProjectsEndpoint("")
        )
    }

    /**
     * Function to execute the request to add a new project of the user
     *
     * @param name: the name of the project
     * @param projectDescription: the description of the project
     * @param projectShortDescription: the short description of the project
     * @param projectVersion: the current version of the project
     * @param groups: the list of groups where the project can be visible
     * @param projectRepository: the url of the repository of the project
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/projects/addProject", method = POST)
    fun execAddProject(
        name: String,
        projectDescription: String,
        projectShortDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): JSONObject {
        return execPost(
            endpoint = createProjectsEndpoint(ADD_PROJECT_ENDPOINT),
            payload = createProjectPayload(
                name = name,
                projectDescription = projectDescription,
                projectShortDescription = projectShortDescription,
                projectVersion = projectVersion,
                groups = groups,
                projectRepository = projectRepository
            )
        )
    }

    /**
     * Function to execute the request to edit an existing project of the user
     *
     * @param projectId: the project identifier
     * @param name: the name of the project
     * @param projectDescription: the description of the project
     * @param projectShortDescription: the short description of the project
     * @param projectVersion: the current version of the project
     * @param groups: the list of groups where the project can be visible
     * @param projectRepository: the url of the repository of the project
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/projects/{project_id}/editProject", method = PATCH)
    fun execEditProject(
        projectId: String,
        name: String,
        projectDescription: String,
        projectShortDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): JSONObject {
        return execPatch(
            endpoint = createProjectsEndpoint(
                endpoint = "/${projectId}"
            ),
            payload = createProjectPayload(
                name = name,
                projectDescription = projectDescription,
                projectShortDescription = projectShortDescription,
                projectVersion = projectVersion,
                groups = groups,
                projectRepository = projectRepository
            )
        )
    }

    /**
     * Function to create the payload to execute the [execAddProject] or the [execEditProject] requests
     *
     * @param name: the name of the project
     * @param projectDescription: the description of the project
     * @param projectShortDescription: the short description of the project
     * @param projectVersion: the current version of the project
     * @param groups: the list of groups where the project can be visible
     * @param projectRepository: the url of the repository of the project
     *
     * @return the payload as [Params]
     *
     */
    @Returner
    private fun createProjectPayload(
        name: String,
        projectDescription: String,
        projectShortDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): Params {
        val payload = Params()
        payload.addParam(NAME_KEY, name)
        payload.addParam(PROJECT_DESCRIPTION_KEY, projectDescription)
        payload.addParam(PROJECT_SHORT_DESCRIPTION_KEY, projectShortDescription)
        payload.addParam(PROJECT_VERSION_KEY, projectVersion)
        payload.addParam(GROUPS_KEY, groups)
        payload.addParam(PROJECT_REPOSITORY_KEY, projectRepository)
        return payload
    }

    /**
     * Function to execute the request to get a project of the user
     *
     * @param projectId: the project identifier of the project to fetch
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/projects/{project_id}", method = GET)
    fun execGetSingleProject(projectId: String): JSONObject {
        return execGet(
            endpoint = createProjectsEndpoint(
                endpoint = "",
                id = projectId
            )
        )
    }

    /**
     * Function to execute the request to delete a project of the user
     *
     * @param projectId: the project identifier of the project to delete
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/projects/{project_id}", method = DELETE)
    fun execDeleteProject(projectId: String): JSONObject {
        return execDelete(
            endpoint = createProjectsEndpoint(
                endpoint = "",//TODO REMAP REQUESTS,
                id = projectId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the projects controller
     *
     * @param endpoint: the path endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    private fun createProjectsEndpoint(
        endpoint: String,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = PROJECTS_KEY,
            endpoint = endpoint,
            id = id
        )
    }

    /**
     * Function to execute the request to schedule a new update for a project
     *
     * @param projectId: the project identifier where schedule the new update
     * @param targetVersion: the target version of the update
     * @param updateChangeNotes: the change notes of the update
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/projects/{project_id}/updates/schedule", method = POST)
    fun execScheduleUpdate(
        projectId: String,
        targetVersion: String,
        updateChangeNotes: List<String>
    ): JSONObject {
        val payload = Params()
        payload.addParam(UPDATE_TARGET_VERSION_KEY, targetVersion)
        payload.addParam(UPDATE_CHANGE_NOTES_KEY, updateChangeNotes)
        return execPost(
            endpoint = createUpdatesEndpoint(SCHEDULE_UPDATE_ENDPOINT, projectId),
            payload = payload
        )
    }

    /**
     * Function to execute the request to start an existing update of a project
     *
     * @param projectId: the project identifier where start an update
     * @param updateId: the update identifier of the update to start
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/projects/{project_id}/updates/{update_id}/start", method = PATCH)
    fun execStartUpdate(
        projectId: String,
        updateId: String
    ): JSONObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                endpoint = START_UPDATE_ENDPOINT,
                projectId = projectId,
                updateId = updateId,
                insertSlash = false
            ),
            payload = Params()
        )
    }

    /**
     * Function to execute the request to publish an existing update of a project
     *
     * @param projectId: the project identifier where publish an update
     * @param updateId: the update identifier of the update to publish
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/projects/{project_id}/updates/{update_id}/publish", method = PATCH)
    fun execPublishUpdate(
        projectId: String,
        updateId: String
    ): JSONObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                endpoint = PUBLISH_UPDATE_ENDPOINT,
                projectId = projectId,
                updateId = updateId,
                insertSlash = false
            ),
            payload = Params()
        )
    }

    /**
     * Function to execute the request to add a new change note to an update
     *
     * @param projectId: the project identifier
     * @param updateId: the update identifier where add the change note
     * @param changeNote: the content of the change note to add
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/projects/{project_id}/updates/{update_id}/addChangeNote", method = PUT)
    fun execAddChangeNote(
        projectId: String,
        updateId: String,
        changeNote: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(changeNote, "")
        return execPut(
            endpoint = createUpdatesEndpoint(
                endpoint = ADD_CHANGE_NOTE_ENDPOINT,
                projectId = projectId,
                updateId = updateId,
                insertSlash = false
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to mark a change note as done
     *
     * @param projectId: the project identifier
     * @param updateId: the update identifier
     * @param changeNoteId: the note identifier to mark as done
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(
        path = "/api/v1/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsDone",
        method = PATCH
    )
    fun execMarkChangeNoteAsDone(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JSONObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                endpoint = "/${NOTES_KEY}/$changeNoteId${MARK_CHANGE_NOTE_AS_DONE_ENDPOINT}",
                projectId = projectId,
                updateId = updateId,
                insertSlash = false
            ),
            payload = Params()
        )
    }

    /**
     * Function to execute the request to mark a change note as todo
     *
     * @param projectId: the project identifier
     * @param updateId: the update identifier
     * @param changeNoteId: the note identifier to mark as todo
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(
        path = "/api/v1/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsToDo",
        method = PATCH
    )
    fun execMarkChangeNoteAsToDo(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JSONObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                endpoint = "/${NOTES_KEY}/$changeNoteId${MARK_CHANGE_NOTE_AS_TODO_ENDPOINT}",
                projectId = projectId,
                updateId = updateId,
                insertSlash = false
            ),
            payload = Params()
        )
    }

    /**
     * Function to execute the request to delete change note of an update
     *
     * @param projectId: the project identifier
     * @param updateId: the update identifier
     * @param changeNoteId: the note identifier to delete
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(
        path = "/api/v1/projects/{project_id}/updates/{update_id}/notes/{note_id}/deleteChangeNote",
        method = DELETE
    )
    fun execDeleteChangeNote(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JSONObject {
        return execDelete(
            endpoint = createUpdatesEndpoint(
                endpoint = "/${NOTES_KEY}/$changeNoteId${DELETE_CHANGE_NOTE_ENDPOINT}",
                projectId = projectId,
                updateId = updateId, insertSlash = false
            ),
        )
    }

    /**
     * Function to execute the request to delete an update
     *
     * @param projectId: the project identifier
     * @param updateId: the update identifier to delete
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/projects/{project_id}/updates/{update_id}/delete", method = DELETE)
    fun execDeleteUpdate(
        projectId: String,
        updateId: String,
    ): JSONObject {
        return execDelete(
            endpoint = createUpdatesEndpoint(
                endpoint = DELETE_UPDATE_ENDPOINT,
                projectId = projectId,
                updateId = updateId,
                insertSlash = false
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the projects/updates controller
     *
     * @param endpoint: the path endpoint of the url
     * @param projectId: the project identifier
     * @param updateId: the update identifier
     * @param insertSlash: whether insert the slash before the identifier, default true
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    private fun createUpdatesEndpoint(
        endpoint: String,
        projectId: String,
        updateId: String? = null,
        insertSlash: Boolean = true
    ): String {
        return createProjectsEndpoint(
            endpoint = "",
            id = projectId
        ) + UPDATES_PATH + createPathId(
            updateId,
            insertSlash
        ) + endpoint
    }

    /**
     * Function to execute the request to get the groups list of the user
     *
     * No-any params required
     *
     * @return the result of the request as [String]
     *
     */
    @RequestPath(path = "/api/v1/groups", method = GET)
    fun execGroupsList(): JSONObject {
        return execGet(
            endpoint = createGroupsEndpoint("")
        )
    }

    /**
     * Function to execute the request to create a new group for the user
     *
     * @param name: the name of the group
     * @param groupDescription: the description of the group
     * @param members: the list of members of the group
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/groups/createGroup", method = POST)
    fun execCreateGroup(
        name: String,
        groupDescription: String,
        members: List<String>
    ): JSONObject {
        val payload = Params()
        payload.addParam(NAME_KEY, name)
        payload.addParam(GROUP_DESCRIPTION_KEY, groupDescription)
        payload.addParam(GROUP_MEMBERS_KEY, members)
        return execPost(
            createGroupsEndpoint(CREATE_GROUP_ENDPOINT),
            payload
        )
    }

    /**
     * Function to execute the request to get a group of the user
     *
     * @param groupId: the group identifier of the group to fetch
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/groups/{group_id}", method = GET)
    fun execGetSingleGroup(groupId: String): JSONObject {
        return execGet(
            endpoint = createGroupsEndpoint(
                endpoint = "",
                id = groupId
            )
        )
    }

    /**
     * Function to execute the request to add members to a group
     *
     * @param groupId: the group identifier where add the members
     * @param members: the list of the members to add
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/groups/{group_id}/addMembers", method = PUT)
    fun execAddMembers(
        groupId: String,
        members: List<String>
    ): JSONObject {
        val payload = Params()
        payload.addParam(members.toString(), "")
        return execPut(
            endpoint = createGroupsEndpoint(
                endpoint = ADD_MEMBERS_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to accept a group invitation
     *
     * @param groupId: the group identifier of the group to accept the invitation
     * @param changelogId: the changelog identifier to delete
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/groups/{group_id}/acceptGroupInvitation", method = PATCH)
    fun execAcceptInvitation(
        groupId: String,
        changelogId: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(changelogId, "")
        return execPatch(
            endpoint = createGroupsEndpoint(
                endpoint = ACCEPT_GROUP_INVITATION_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to decline a group invitation
     *
     * @param groupId: the group identifier of the group to decline the invitation
     * @param changelogId: the changelog identifier to delete
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/groups/{group_id}/declineGroupInvitation", method = DELETE)
    fun execDeclineInvitation(
        groupId: String,
        changelogId: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(changelogId, "")
        return execDelete(
            endpoint = createGroupsEndpoint(
                endpoint = DECLINE_GROUP_INVITATION_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to change a role of a member of a group
     *
     * @param groupId: the group identifier of the group where change the role
     * @param memberId: the identifier of the member to change the role
     * @param role: the new role of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/groups/{group_id}/changeMemberRole", method = PATCH)
    fun execChangeMemberRole(
        groupId: String,
        memberId: String,
        role: Role
    ): JSONObject {
        val payload = Params()
        payload.addParam(IDENTIFIER_KEY, memberId)
        payload.addParam(MEMBER_ROLE_KEY, role)
        return execPatch(
            endpoint = createGroupsEndpoint(
                endpoint = CHANGE_MEMBER_ROLE_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to remove a member from a group
     *
     * @param groupId: the group identifier of the group where change the role
     * @param memberId: the identifier of the member to remove
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/groups/{group_id}/removeMember", method = DELETE)
    fun execRemoveMember(
        groupId: String,
        memberId: String,
    ): JSONObject {
        val payload = Params()
        payload.addParam(memberId, "")
        return execDelete(
            endpoint = createGroupsEndpoint(
                endpoint = REMOVE_MEMBER_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to edit a projects list of a group
     *
     * @param groupId: the group identifier of the group where edit the projects
     * @param projects: the list of the projects for the group
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/groups/{group_id}/editProjects", method = PATCH)
    fun execEditProjects(
        groupId: String,
        projects: List<String>
    ): JSONObject {
        val payload = Params()
        payload.addParam(projects.toString(), "")
        return execPatch(
            endpoint = createGroupsEndpoint(
                endpoint = EDIT_PROJECTS_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to leave from a group
     *
     * @param groupId: the group identifier of the group from leave
     * @param nextAdminId: the identifier of the next admin, required when the user is leaving is an [Role.ADMIN]
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/groups/{group_id}/leaveGroup", method = DELETE)
    fun execLeaveGroup(
        groupId: String,
        nextAdminId: String? = null,
    ): JSONObject {
        val payload = Params()
        if (nextAdminId != null)
            payload.addParam(nextAdminId, "")
        return execDelete(
            endpoint = createGroupsEndpoint(
                endpoint = LEAVE_GROUP_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to delete a group
     *
     * @param groupId: the group identifier of the group to delete
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/groups/{group_id}/deleteGroup", method = DELETE)
    fun execDeleteGroup(groupId: String): JSONObject {
        return execDelete(
            endpoint = createGroupsEndpoint(
                endpoint = DELETE_GROUP_ENDPOINT,
                id = groupId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the groups controller
     *
     * @param endpoint: the path endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    private fun createGroupsEndpoint(
        endpoint: String,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = GROUPS_KEY,
            endpoint = endpoint,
            id = id
        )
    }

    /**
     * Function to execute the request to get the notes list of the user
     *
     * No-any params required
     *
     * @return the result of the request as [String]
     *
     */
    @RequestPath(path = "/api/v1/notes", method = GET)
    fun execNotesList(): JSONObject {
        return execGet(
            endpoint = createNotesEndpoint("")
        )
    }

    /**
     * Function to execute the request to add a new note of the user
     * @param contentNote: the content of the new note to add
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/notes/create", method = POST)
    fun execAddNote(contentNote: String): JSONObject {
        val payload = Params()
        payload.addParam(contentNote, "")
        return execPost(
            endpoint = createNotesEndpoint(
                endpoint = CREATE_NOTE_ENDPOINT
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to mark a user's note as done
     * @param noteId: the note identifier to mark as done
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/notes/{note_id}/markAsDone", method = PATCH)
    fun execMarkNoteAsDone(noteId: String): JSONObject {
        return execPatch(
            endpoint = createNotesEndpoint(
                endpoint = MARK_AS_DONE_ENDPOINT,
                id = noteId
            ),
            payload = Params()
        )
    }

    /**
     * Function to execute the request to mark a user's note as todo
     * @param noteId: the note identifier to mark as todo
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/notes/{note_id}/markAsToDo", method = PATCH)
    fun execMarkNoteAsToDo(noteId: String): JSONObject {
        return execPatch(
            endpoint = createNotesEndpoint(
                endpoint = MARK_AS_TO_DO_ENDPOINT,
                id = noteId
            ),
            payload = Params()
        )
    }

    /**
     * Function to execute the request to delete a user's note
     * @param noteId: the note identifier to delete
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/notes/{note_id}/deleteNote", method = DELETE)
    fun execDeleteNote(noteId: String): JSONObject {
        return execDelete(
            endpoint = createNotesEndpoint(
                endpoint = DELETE_NOTE_ENDPOINT,
                id = noteId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the notes controller
     *
     * @param endpoint: the path endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    private fun createNotesEndpoint(
        endpoint: String,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = NOTES_KEY,
            endpoint = endpoint,
            id = id
        )
    }

    /**
     * Function to execute the request to get the changelogs list of the user
     *
     * No-any params required
     *
     * @return the result of the request as [String]
     *
     */
    @RequestPath(path = "/api/v1/changelogs", method = GET)
    fun execChangelogsList(): JSONObject {
        return execGet(
            endpoint = createChangelogsEndpoint("")
        )
    }

    /**
     * Function to execute the request to read a changelog
     *
     * @param changelogId: the changelog identifier to read
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/changelogs/{changelog_id}/readChangelog", method = PATCH)
    fun execReadChangelog(changelogId: String): JSONObject {
        return execPatch(
            endpoint = createChangelogsEndpoint(
                endpoint = READ_CHANGELOG_ENDPOINT,
                id = changelogId
            ),
            payload = Params()
        )
    }

    /**
     * Function to execute the request to delete a changelog
     *
     * @param changelogId: the changelog identifier to delete
     * @param groupId: the group identifier where leave if is a [ChangelogEvent.INVITED_GROUP]
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/changelogs/{changelog_id}/deleteChangelog", method = DELETE)
    fun execDeleteChangelog(
        changelogId: String,
        groupId: String? = null
    ): JSONObject {
        if (groupId != null) {
            val payload = Params()
            payload.addParam(groupId, "")
            return execDelete(
                endpoint = createChangelogsEndpoint(
                    endpoint = DELETE_CHANGELOG_ENDPOINT,
                    id = changelogId
                ),
                payload = payload
            )
        }
        return execDelete(
            endpoint = createChangelogsEndpoint(
                endpoint = DELETE_CHANGELOG_ENDPOINT,
                id = changelogId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the changelogs controller
     *
     * @param endpoint: the path endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    private fun createChangelogsEndpoint(
        endpoint: String,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = CHANGELOGS_KEY,
            endpoint = endpoint,
            id = id
        )
    }

    /**
     * Method to an endpoint to make the request
     *
     * @param baseEndpoint: the base endpoint of the url
     * @param endpoint: the path endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [String]
     */
    private fun createEndpoint(
        baseEndpoint: String,
        endpoint: String,
        id: String? = null
    ): String {
        return baseEndpoint + createPathId(id) + endpoint
    }

    /**
     * Method to create a path variable for the url with an identifier
     *
     * @param id: the identifier to create the path variable
     * @param insertSlash: whether insert the slash before the identifier, default true
     *
     * @return path variable for the url with an identifier as [String]
     */
    private fun createPathId(
        id: String?,
        insertSlash: Boolean = true
    ): String {
        return if (id != null) {
            if (insertSlash)
                "/$id"
            else
                id
        } else
            ""
    }

}