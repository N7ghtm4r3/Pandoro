package com.tecknobit.pandoro.helpers

import com.tecknobit.apimanager.annotations.RequestPath
import com.tecknobit.apimanager.annotations.Returner
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.apimanager.apis.APIRequest
import com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*
import com.tecknobit.apimanager.apis.sockets.SocketManager
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandoro.controllers.*
import com.tecknobit.pandoro.records.users.GroupMember.Role
import com.tecknobit.pandoro.services.GroupsHelper
import com.tecknobit.pandoro.services.ProjectsHelper
import com.tecknobit.pandoro.services.UsersHelper
import org.json.JSONObject
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.io.File

/**
 * The **BaseRequester** class is useful to communicate with the Pandoro's backend
 *
 * @param host: the host where is running the Pandoro's backend
 * @param userId: the user identifier
 * @param userToken: the user token
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Structure
abstract class BaseRequester(
    private val host: String,
    open var userId: String? = null,
    open var userToken: String? = null
) {

    /**
     * **errorResponse** -> the default error response to send when the request throws an [Exception]
     */
    protected val errorResponse = JSONObject()
        .put(PandoroController.SUCCESS_KEY, false)
        .put(PandoroController.STATUS_CODE_KEY, SocketManager.StandardResponseCode.FAILED)
        .put(PandoroController.ERROR_KEY, PandoroController.WRONG_PROCEDURE_MESSAGE).toString()

    /**
     * **lastResponse** -> the last response received from the backend
     */
    protected var lastResponse: JsonHelper? = null

    /**
     * Function to set the headers for the authentication of the user
     *
     * No-any params required
     */
    protected abstract fun setAuthHeaders()

    /**
     * Function to execute the request to sign up in the Pandoro's system
     *
     * @param name: the name of the user
     * @param surname: the surname of the user
     * @param email: the email of the user
     * @param password: the password of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/signUp", method = POST)
    fun execSignUp(
        name: String,
        surname: String,
        email: String,
        password: String
    ): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(UsersHelper.NAME_KEY, name)
        payload.addParam(UsersHelper.SURNAME_KEY, surname)
        payload.addParam(UsersHelper.EMAIL_KEY, email)
        payload.addParam(UsersHelper.PASSWORD_KEY, password)
        val response = execPost(createUsersEndpoint(UsersController.SIGN_UP_ENDPOINT), payload)
        setAuthCredentials(response)
        return response
    }

    /**
     * Function to execute the request to sign in the Pandoro's system
     *
     * @param email: the email of the user
     * @param password: the password of the user
     *
     * @return the result of the request as [JSONObject]
     */
    @RequestPath(path = "/api/v1/users/signIn", method = POST)
    fun execSignIn(
        email: String,
        password: String
    ): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(UsersHelper.EMAIL_KEY, email)
        payload.addParam(UsersHelper.PASSWORD_KEY, password)
        val response = execPost(createUsersEndpoint(UsersController.SIGN_IN_ENDPOINT), payload)
        setAuthCredentials(response)
        return response
    }

    /**
     * Function to set the user auth credentials data and the headers
     *
     * @param response: the response from fetch the data
     */
    private fun setAuthCredentials(response: JSONObject) {
        val hResponse = JsonHelper(response)
        userId = hResponse.getString(PandoroController.IDENTIFIER_KEY)
        userToken = hResponse.getString(UsersHelper.TOKEN_KEY)
        setAuthHeaders()
    }

    /**
     * Function to execute the request to change the profile pic of the user
     *
     * @param profilePic: the profile pic of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changeProfilePic", method = POST)
    open fun execChangeProfilePic(profilePic: File): JSONObject {
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        headers.add(UsersHelper.TOKEN_KEY, userToken)
        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
        body.add(UsersHelper.PROFILE_PIC_KEY, FileSystemResource(profilePic))
        val requestEntity: HttpEntity<Any?> = HttpEntity<Any?>(body, headers)
        val restTemplate = RestTemplate()
        val response = restTemplate.postForEntity(
            host + PandoroController.BASE_ENDPOINT + UsersController.USERS_ENDPOINT + "/$userId/" +
                    UsersController.CHANGE_PROFILE_PIC_ENDPOINT, requestEntity, String::class.java
        ).body
        lastResponse = JsonHelper(response)
        return JSONObject(response)
    }

    /**
     * Function to execute the request to change the email of the user
     *
     * @param newEmail: the new email of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changeEmail", method = PATCH)
    fun execChangeEmail(newEmail: String): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(newEmail, "")
        return execPatch(createUsersEndpoint(UsersController.CHANGE_EMAIL_ENDPOINT, userId), payload, false)
    }

    /**
     * Function to execute the request to change the password of the user
     *
     * @param newPassword: the new password of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changePassword", method = PATCH)
    fun execChangePassword(newPassword: String): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(newPassword, "")
        return execPatch(createUsersEndpoint(UsersController.CHANGE_PASSWORD_ENDPOINT, userId), payload, false)
    }

    /**
     * Function to execute the request to delete the account of the user
     *
     * No-any params required
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/deleteAccount", method = DELETE)
    fun execDeleteAccount(): JSONObject {
        return execDelete(createUsersEndpoint(UsersController.DELETE_ACCOUNT_ENDPOINT, userId))
    }

    /**
     * Method to an endpoint to make the request to the users controller
     *
     * @param endpoint: the path endpoint of the url
     * @param id: the eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    private fun createUsersEndpoint(
        endpoint: String,
        id: String? = null
    ): String {
        return createEndpoint(UsersController.USERS_ENDPOINT, endpoint, id)
    }

    /**
     * Function to execute the request to get the projects list of the user
     *
     * No-any params required
     *
     * @return the result of the request as [String]
     *
     */
    @RequestPath(path = "/api/v1/projects", method = GET)
    fun execProjectsList(): String {
        return execGet(createProjectsEndpoint(""))
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
            createProjectsEndpoint(ProjectsController.ADD_PROJECT_ENDPOINT),
            createProjectPayload(
                name, projectDescription, projectShortDescription, projectVersion, groups,
                projectRepository
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
            createProjectsEndpoint("/${projectId}" + ProjectsController.EDIT_PROJECT_ENDPOINT),
            createProjectPayload(
                name, projectDescription, projectShortDescription, projectVersion, groups,
                projectRepository
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
     * @return the payload as [PandoroPayload]
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
    ): PandoroPayload {
        val payload = PandoroPayload()
        payload.addParam(UsersHelper.NAME_KEY, name)
        payload.addParam(ProjectsHelper.PROJECT_DESCRIPTION_KEY, projectDescription)
        payload.addParam(ProjectsHelper.PROJECT_SHORT_DESCRIPTION_KEY, projectShortDescription)
        payload.addParam(ProjectsHelper.PROJECT_VERSION_KEY, projectVersion)
        payload.addParam(GroupsController.GROUPS_KEY, groups)
        payload.addParam(ProjectsHelper.PROJECT_REPOSITORY_KEY, projectRepository)
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
        return JSONObject(execGet(createProjectsEndpoint("", projectId)))
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
        return execDelete(createProjectsEndpoint(ProjectsController.DELETE_PROJECT_ENDPOINT, projectId))
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
        return createEndpoint(ProjectsHelper.PROJECTS_KEY, endpoint, id)
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
        val payload = PandoroPayload()
        payload.addParam(ProjectsHelper.UPDATE_TARGET_VERSION_KEY, targetVersion)
        payload.addParam(ProjectsHelper.UPDATE_CHANGE_NOTES_KEY, updateChangeNotes)
        return execPost(createUpdatesEndpoint(ProjectsController.SCHEDULE_UPDATE_ENDPOINT, projectId), payload)
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
        return execPatch(createUpdatesEndpoint(ProjectsController.START_UPDATE_ENDPOINT, projectId, updateId, false))
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
        return execPatch(createUpdatesEndpoint(ProjectsController.PUBLISH_UPDATE_ENDPOINT, projectId, updateId, false))
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
        val payload = PandoroPayload()
        payload.addParam(changeNote, "")
        return execPut(
            createUpdatesEndpoint(ProjectsController.ADD_CHANGE_NOTE_ENDPOINT, projectId, updateId, false),
            payload,
            false
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
            createUpdatesEndpoint(
                "/${NotesController.NOTES_KEY}/$changeNoteId${ProjectsController.MARK_CHANGE_NOTE_AS_DONE_ENDPOINT}",
                projectId,
                updateId,
                false
            )
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
            createUpdatesEndpoint(
                "/${NotesController.NOTES_KEY}/$changeNoteId${ProjectsController.MARK_CHANGE_NOTE_AS_TODO_ENDPOINT}",
                projectId,
                updateId, false
            ),
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
            createUpdatesEndpoint(
                "/${NotesController.NOTES_KEY}/$changeNoteId${ProjectsController.DELETE_CHANGE_NOTE_ENDPOINT}",
                projectId,
                updateId, false
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
        return execDelete(createUpdatesEndpoint(ProjectsController.DELETE_UPDATE_ENDPOINT, projectId, updateId, false))
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
        return createProjectsEndpoint("", projectId) + ProjectsController.UPDATES_PATH + createPathId(
            updateId,
            insertSlash
        ) +
                endpoint
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
    fun execGroupsList(): String {
        return execGet(createGroupsEndpoint(""))
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
        val payload = PandoroPayload()
        payload.addParam(UsersHelper.NAME_KEY, name)
        payload.addParam(GroupsHelper.GROUP_DESCRIPTION_KEY, groupDescription)
        payload.addParam(GroupsHelper.GROUP_MEMBERS_KEY, members)
        return execPost(
            createGroupsEndpoint(GroupsController.CREATE_GROUP_ENDPOINT),
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
        return JSONObject(execGet(createGroupsEndpoint("", groupId)))
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
        val payload = PandoroPayload()
        payload.addParam(members.toString(), "")
        return execPut(
            createGroupsEndpoint(GroupsController.ADD_MEMBERS_ENDPOINT, groupId),
            payload,
            false
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
        val payload = PandoroPayload()
        payload.addParam(changelogId, "")
        return execPatch(
            createGroupsEndpoint(GroupsController.ACCEPT_GROUP_INVITATION_ENDPOINT, groupId),
            payload,
            false
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
        val payload = PandoroPayload()
        payload.addParam(changelogId, "")
        return execDelete(
            createGroupsEndpoint(GroupsController.DECLINE_GROUP_INVITATION_ENDPOINT, groupId),
            payload,
            false
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
        val payload = PandoroPayload()
        payload.addParam(PandoroController.IDENTIFIER_KEY, memberId)
        payload.addParam(GroupsHelper.MEMBER_ROLE_KEY, role)
        return execPatch(
            createGroupsEndpoint(GroupsController.CHANGE_MEMBER_ROLE_ENDPOINT, groupId),
            payload
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
        val payload = PandoroPayload()
        payload.addParam(memberId, "")
        return execDelete(
            createGroupsEndpoint(GroupsController.REMOVE_MEMBER_ENDPOINT, groupId),
            payload,
            false
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
        val payload = PandoroPayload()
        payload.addParam(projects.toString(), "")
        return execPatch(
            createGroupsEndpoint(GroupsController.EDIT_PROJECTS_ENDPOINT, groupId),
            payload,
            false
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
        val payload = PandoroPayload()
        if (nextAdminId != null)
            payload.addParam(nextAdminId, "")
        return execDelete(
            createGroupsEndpoint(GroupsController.LEAVE_GROUP_ENDPOINT, groupId),
            payload,
            false
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
        return execDelete(createGroupsEndpoint(GroupsController.DELETE_GROUP_ENDPOINT, groupId))
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
        return createEndpoint(GroupsController.GROUPS_KEY, endpoint, id)
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
    fun execNotesList(): String {
        return execGet(createNotesEndpoint(""))
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
        val payload = PandoroPayload()
        payload.addParam(contentNote, "")
        return execPost(
            createNotesEndpoint(NotesController.CREATE_NOTE_ENDPOINT),
            payload,
            false
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
        return execPatch(createNotesEndpoint(NotesController.MARK_AS_DONE_ENDPOINT, noteId))
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
        return execPatch(createNotesEndpoint(NotesController.MARK_AS_TO_DO_ENDPOINT, noteId))
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
        return execDelete(createNotesEndpoint(NotesController.DELETE_NOTE_ENDPOINT, noteId))
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
        return createEndpoint(NotesController.NOTES_KEY, endpoint, id)
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
    fun execChangelogsList(): String {
        return execGet(createChangelogsEndpoint(""))
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
        return execPatch(createChangelogsEndpoint(ChangelogsController.READ_CHANGELOG_ENDPOINT, changelogId))
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
            val payload = PandoroPayload()
            payload.addParam(groupId, "")
            return execDelete(
                createChangelogsEndpoint(ChangelogsController.DELETE_CHANGELOG_ENDPOINT, changelogId),
                payload,
                false
            )
        }
        return execDelete(createChangelogsEndpoint(ChangelogsController.DELETE_CHANGELOG_ENDPOINT, changelogId))
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
        return createEndpoint(ChangelogsController.CHANGELOGS_KEY, endpoint, id)
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

    /**
     * Method to check whether the request has been successful
     *
     * No-any params required
     *
     * @return check whether the request has been successful as [Boolean]
     */
    fun successResponse(): Boolean {
        return lastResponse!!.jsonObjectSource == null || lastResponse!!.getBoolean(PandoroController.SUCCESS_KEY, true)
    }

    /**
     * Method to get the error message of the request, if request failed
     *
     * No-any params required
     *
     * @return error message of the request as [String]
     */
    fun errorMessage(): String {
        return lastResponse!!.getString(PandoroController.ERROR_KEY)
    }

    /**
     * Function to execute a [GET] request to the backend
     *
     * @param endpoint: the endpoint which make the request
     * @param contentType: the content type of the request, default "application/json"
     */
    @Wrapper
    private fun execGet(
        endpoint: String,
        contentType: String = "application/json"
    ): String {
        return execRequest(
            contentType = contentType,
            endpoint = endpoint,
            requestMethod = GET
        )
    }

    /**
     * Function to execute a [POST] request to the backend
     *
     * @param endpoint: the endpoint which make the request
     * @param payload: the payload of the request, default null
     * @param jsonPayload: whether the payload must be formatted as JSON, default true
     * @param contentType: the content type of the request, default "application/json"
     */
    @Wrapper
    private fun execPost(
        endpoint: String,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true,
        contentType: String = "application/json"
    ): JSONObject {
        return JSONObject(
            execRequest(
                contentType = contentType,
                endpoint = endpoint,
                requestMethod = POST,
                payload = payload,
                jsonPayload = jsonPayload
            )
        )
    }

    /**
     * Function to execute a [PATCH] request to the backend
     *
     * @param endpoint: the endpoint which make the request
     * @param payload: the payload of the request, default null
     * @param jsonPayload: whether the payload must be formatted as JSON, default true
     * @param contentType: the content type of the request, default "application/json"
     */
    @Wrapper
    private fun execPatch(
        endpoint: String,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true,
        contentType: String = "application/json"
    ): JSONObject {
        return JSONObject(
            execRequest(
                contentType = contentType,
                endpoint = endpoint,
                requestMethod = PATCH,
                payload = payload,
                jsonPayload = jsonPayload
            )
        )
    }

    /**
     * Function to execute a [PUT] request to the backend
     *
     * @param endpoint: the endpoint which make the request
     * @param payload: the payload of the request, default null
     * @param jsonPayload: whether the payload must be formatted as JSON, default true
     * @param contentType: the content type of the request, default "application/json"
     */
    @Wrapper
    private fun execPut(
        endpoint: String,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true,
        contentType: String = "application/json"
    ): JSONObject {
        return JSONObject(
            execRequest(
                contentType = contentType,
                endpoint = endpoint,
                requestMethod = PUT,
                payload = payload,
                jsonPayload = jsonPayload
            )
        )
    }

    /**
     * Function to execute a [DELETE] request to the backend
     *
     * @param endpoint: the endpoint which make the request
     * @param payload: the payload of the request, default null
     * @param jsonPayload: whether the payload must be formatted as JSON, default true
     * @param contentType: the content type of the request, default "application/json"
     */
    @Wrapper
    private fun execDelete(
        endpoint: String,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true,
        contentType: String = "application/json"
    ): JSONObject {
        return JSONObject(
            execRequest(
                contentType = contentType,
                endpoint = endpoint,
                requestMethod = DELETE,
                payload = payload,
                jsonPayload = jsonPayload
            )
        )
    }

    /**
     * Function to execute a request to the backend
     *
     * @param contentType: the content type of the request
     * @param endpoint: the endpoint which make the request
     * @param requestMethod: the method of the request to execute
     * @param payload: the payload of the request, default null
     * @param jsonPayload: whether the payload must be formatted as JSON, default true
     */
    protected abstract fun execRequest(
        contentType: String,
        endpoint: String,
        requestMethod: APIRequest.RequestMethod,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true
    ): String

    /**
     * The **PandoroPayload** class is useful to create the payload for the requests to the Pandoro's backend
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    open class PandoroPayload {

        /**
         * **payload** -> the payload to asseble for the requests
         */
        private val payload = mutableMapOf<String, Any>()

        /**
         * Function to add a new param value
         *
         * @param keyParam:   key of the param
         * @param valueParam: value of the param
         */
        fun <T : Any> addParam(keyParam: String, valueParam: T) {
            payload[keyParam] = valueParam
        }

        /**
         * Function to get the assembled payload for the requests
         *
         * No-any params required
         * @return the payload as [Map] of <[String], [Any]>
         */
        fun getPayload(): Map<String, Any> {
            return payload
        }

    }

}