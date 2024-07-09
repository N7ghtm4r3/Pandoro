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
import com.tecknobit.pandorocore.records.Changelog.*
import com.tecknobit.pandorocore.records.Group.*
import com.tecknobit.pandorocore.records.Note.CONTENT_NOTE_KEY
import com.tecknobit.pandorocore.records.Note.NOTES_KEY
import com.tecknobit.pandorocore.records.Project.*
import com.tecknobit.pandorocore.records.structures.PandoroItem.IDENTIFIER_KEY
import com.tecknobit.pandorocore.records.users.GroupMember.Role
import org.json.JSONArray
import org.json.JSONObject

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
    userToken: String? = null,
    debugMode: Boolean = false,
) : EquinoxRequester(
    host = host,
    userId = userId,
    userToken = userToken,
    enableCertificatesValidation = true,
    connectionErrorMessage = "Server is temporarily unavailable",
    debugMode = debugMode
) {

    /**
     * Function to execute the request to get the projects list of the user
     *
     * No-any params required
     *
     * @return the result of the request as [String]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects", method = GET)
    fun getProjectsList(): JSONObject {
        return execGet(
            endpoint = createProjectEndpoint("")
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
    @RequestPath(path = "/api/v1/users/{id}/projects", method = POST)
    fun addProject(
        name: String,
        projectDescription: String,
        projectShortDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): JSONObject {
        return execPost(
            endpoint = createProjectEndpoint(),
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
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = PATCH)
    fun editProject(
        projectId: String,
        name: String,
        projectDescription: String,
        projectShortDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): JSONObject {
        return execPatch(
            endpoint = createProjectEndpoint(
                id = projectId
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
     * Function to create the payload to execute the [addProject] or the [editProject] requests
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
        payload.addParam(GROUPS_KEY, JSONArray(groups))
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
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = GET)
    fun getProject(
        projectId: String
    ): JSONObject {
        return execGet(
            endpoint = createProjectEndpoint(
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
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = DELETE)
    fun deleteProject(
        projectId: String
    ): JSONObject {
        return execDelete(
            endpoint = createProjectEndpoint(
                id = projectId
            )
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
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/schedule", method = POST)
    fun scheduleUpdate(
        projectId: String,
        targetVersion: String,
        updateChangeNotes: List<String>
    ): JSONObject {
        val payload = Params()
        payload.addParam(UPDATE_TARGET_VERSION_KEY, targetVersion)
        payload.addParam(UPDATE_CHANGE_NOTES_KEY, JSONArray(updateChangeNotes))
        return execPost(
            endpoint = createUpdatesEndpoint(
                subEndpoint = SCHEDULE_UPDATE_ENDPOINT,
                projectId = projectId
            ),
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
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/start", method = PATCH)
    fun startUpdate(
        projectId: String,
        updateId: String
    ): JSONObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = START_UPDATE_ENDPOINT,
                projectId = projectId,
                updateId = updateId
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
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/publish", method = PATCH)
    fun publishUpdate(
        projectId: String,
        updateId: String
    ): JSONObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = PUBLISH_UPDATE_ENDPOINT,
                projectId = projectId,
                updateId = updateId
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
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/addChangeNote", method = PUT)
    fun addChangeNote(
        projectId: String,
        updateId: String,
        changeNote: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(CONTENT_NOTE_KEY, changeNote)
        return execPut(
            endpoint = createUpdatesEndpoint(
                subEndpoint = ADD_CHANGE_NOTE_ENDPOINT,
                projectId = projectId,
                updateId = updateId
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
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsDone",
        method = PATCH
    )
    fun markChangeNoteAsDone(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JSONObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = "/${NOTES_KEY}/$changeNoteId${MARK_CHANGE_NOTE_AS_DONE_ENDPOINT}",
                projectId = projectId,
                updateId = updateId
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
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsToDo",
        method = PATCH
    )
    fun markChangeNoteAsToDo(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JSONObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = "/${NOTES_KEY}/$changeNoteId${MARK_CHANGE_NOTE_AS_TODO_ENDPOINT}",
                projectId = projectId,
                updateId = updateId
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
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}",
        method = DELETE
    )
    fun deleteChangeNote(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JSONObject {
        return execDelete(
            endpoint = createUpdatesEndpoint(
                subEndpoint = "/${NOTES_KEY}/$changeNoteId",
                projectId = projectId,
                updateId = updateId
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
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}", method = DELETE)
    fun deleteUpdate(
        projectId: String,
        updateId: String,
    ): JSONObject {
        return execDelete(
            endpoint = createUpdatesEndpoint(
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the projects/updates controller
     *
     * @param subEndpoint: the path sub-endpoint of the url
     * @param projectId: the project identifier
     * @param updateId: the update identifier
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    private fun createUpdatesEndpoint(
        subEndpoint: String = "",
        projectId: String,
        updateId: String? = null,
    ): String {
        var updatesEndpointPart = UPDATES_PATH
        updatesEndpointPart += if (updateId != null)
            updateId + subEndpoint
        else
            subEndpoint
        return createProjectEndpoint(
            subEndpoint = updatesEndpointPart,
            id = projectId
        )
    }

    /**
     * Method to an subEndpoint to make the request to the projects controller
     *
     * @param subEndpoint: the path of the sub-endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an subEndpoint to make the request as [String]
     */
    @Wrapper
    private fun createProjectEndpoint(
        subEndpoint: String? = null,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = PROJECTS_KEY,
            subEndpoint = subEndpoint,
            id = id
        )
    }

    /**
     * Function to execute the request to get the groups list of the user
     *
     * No-any params required
     *
     * @return the result of the request as [String]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups", method = GET)
    fun getGroupsList(): JSONObject {
        return execGet(
            endpoint = createGroupsEndpoint()
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
    @RequestPath(path = "/api/v1/users/{id}/groups", method = POST)
    fun createGroup(
        name: String,
        groupDescription: String,
        members: List<String>
    ): JSONObject {
        val payload = Params()
        payload.addParam(NAME_KEY, name)
        payload.addParam(GROUP_DESCRIPTION_KEY, groupDescription)
        payload.addParam(GROUP_MEMBERS_KEY, JSONArray(members))
        return execPost(
            endpoint = createGroupsEndpoint(),
            payload = payload
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = GET)
    fun getGroup(groupId: String): JSONObject {
        return execGet(
            endpoint = createGroupsEndpoint(
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/addMembers", method = PUT)
    fun addMembers(
        groupId: String,
        members: List<String>
    ): JSONObject {
        val payload = Params()
        payload.addParam(GROUP_MEMBERS_KEY, JSONArray(members))
        return execPut(
            endpoint = createGroupsEndpoint(
                subEndpoint = ADD_MEMBERS_ENDPOINT,
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/acceptGroupInvitation", method = PATCH)
    fun acceptInvitation(
        groupId: String,
        changelogId: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(CHANGELOG_IDENTIFIER_KEY, changelogId)
        return execPatch(
            endpoint = createGroupsEndpoint(
                subEndpoint = ACCEPT_GROUP_INVITATION_ENDPOINT,
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/declineGroupInvitation", method = DELETE)
    fun declineInvitation(
        groupId: String,
        changelogId: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(CHANGELOG_IDENTIFIER_KEY, changelogId)
        return execDelete(
            endpoint = createGroupsEndpoint(
                subEndpoint = DECLINE_GROUP_INVITATION_ENDPOINT,
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/changeMemberRole", method = PATCH)
    fun changeMemberRole(
        groupId: String,
        memberId: String,
        role: Role
    ): JSONObject {
        val payload = Params()
        payload.addParam(IDENTIFIER_KEY, memberId)
        payload.addParam(MEMBER_ROLE_KEY, role)
        return execPatch(
            endpoint = createGroupsEndpoint(
                subEndpoint = CHANGE_MEMBER_ROLE_ENDPOINT,
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/removeMember", method = DELETE)
    fun removeMember(
        groupId: String,
        memberId: String,
    ): JSONObject {
        val payload = Params()
        payload.addParam(IDENTIFIER_KEY, memberId)
        return execDelete(
            endpoint = createGroupsEndpoint(
                subEndpoint = REMOVE_MEMBER_ENDPOINT,
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/editProjects", method = PATCH)
    fun editProjects(
        groupId: String,
        projects: List<String>
    ): JSONObject {
        val payload = Params()
        payload.addParam(PROJECTS_KEY, JSONArray(projects))
        return execPatch(
            endpoint = createGroupsEndpoint(
                subEndpoint = EDIT_PROJECTS_ENDPOINT,
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/leaveGroup", method = DELETE)
    fun leaveGroup(
        groupId: String,
        nextAdminId: String? = null,
    ): JSONObject {
        val payload = Params()
        if (nextAdminId != null)
            payload.addParam(IDENTIFIER_KEY, nextAdminId)
        return execDelete(
            endpoint = createGroupsEndpoint(
                subEndpoint = LEAVE_GROUP_ENDPOINT,
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
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = DELETE)
    fun deleteGroup(groupId: String): JSONObject {
        return execDelete(
            endpoint = createGroupsEndpoint(
                id = groupId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the groups controller
     *
     * @param subEndpoint: the path of the sub-endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    private fun createGroupsEndpoint(
        subEndpoint: String? = null,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = GROUPS_KEY,
            subEndpoint = subEndpoint,
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
    @RequestPath(path = "/api/v1/users/{id}/notes", method = GET)
    fun getNotesList(): JSONObject {
        return execGet(
            endpoint = createNotesEndpoint()
        )
    }

    /**
     * Function to execute the request to add a new note of the user
     * @param contentNote: the content of the new note to add
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes", method = POST)
    fun addNote(
        contentNote: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(CONTENT_NOTE_KEY, contentNote)
        return execPost(
            endpoint = createNotesEndpoint(),
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
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}/markAsDone", method = PATCH)
    fun markNoteAsDone(
        noteId: String
    ): JSONObject {
        return execPatch(
            endpoint = createNotesEndpoint(
                subEndpoint = MARK_AS_DONE_ENDPOINT,
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
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}/markAsToDo", method = PATCH)
    fun markNoteAsToDo(
        noteId: String
    ): JSONObject {
        return execPatch(
            endpoint = createNotesEndpoint(
                subEndpoint = MARK_AS_TO_DO_ENDPOINT,
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
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}", method = DELETE)
    fun deleteNote(
        noteId: String
    ): JSONObject {
        return execDelete(
            endpoint = createNotesEndpoint(
                id = noteId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the notes controller
     *
     * @param subEndpoint: the path of the sub-endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    private fun createNotesEndpoint(
        subEndpoint: String? = null,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = NOTES_KEY,
            subEndpoint = subEndpoint,
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
    @RequestPath(path = "/api/v1/users/{id}/changelogs", method = GET)
    fun getChangelogsList(): JSONObject {
        return execGet(
            endpoint = createChangelogsEndpoint()
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
    @RequestPath(path = "/api/v1/users/{id}/changelogs/{changelog_id}", method = PATCH)
    fun readChangelog(
        changelogId: String
    ): JSONObject {
        return execPatch(
            endpoint = createChangelogsEndpoint(
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
    @RequestPath(path = "/api/v1/users/{id}/changelogs/{changelog_id}", method = DELETE)
    fun deleteChangelog(
        changelogId: String,
        groupId: String? = null
    ): JSONObject {
        if (groupId != null) {
            val payload = Params()
            payload.addParam(GROUP_IDENTIFIER_KEY, groupId)
            return execDelete(
                endpoint = createChangelogsEndpoint(
                    id = changelogId
                ),
                payload = payload
            )
        }
        return execDelete(
            endpoint = createChangelogsEndpoint(
                id = changelogId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the changelogs controller
     *
     * @param subEndpoint: the path of the sub-endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    private fun createChangelogsEndpoint(
        subEndpoint: String? = null,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = CHANGELOGS_KEY,
            subEndpoint = subEndpoint,
            id = id
        )
    }

    /**
     * Method to an endpoint to make the request
     *
     * @param baseEndpoint: the base endpoint of the url
     * @param subEndpoint: the path of the sub-endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [String]
     */
    private fun createEndpoint(
        baseEndpoint: String,
        subEndpoint: String? = null,
        id: String? = null
    ): String {
        var endpoint = assembleUsersEndpointPath() + "/$baseEndpoint"
        if (id != null) {
            endpoint += "/$id"
            if (subEndpoint != null)
                endpoint += subEndpoint
        }
        return endpoint
    }

}