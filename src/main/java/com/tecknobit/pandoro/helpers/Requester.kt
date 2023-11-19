package com.tecknobit.pandoro.helpers

import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.apimanager.apis.APIRequest
import com.tecknobit.apimanager.apis.APIRequest.*
import com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*
import com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.FAILED
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandoro.controllers.GroupsController.GROUPS_KEY
import com.tecknobit.pandoro.controllers.NotesController.NOTES_KEY
import com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT
import com.tecknobit.pandoro.controllers.PandoroController.ERROR_KEY
import com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY
import com.tecknobit.pandoro.controllers.PandoroController.STATUS_CODE_KEY
import com.tecknobit.pandoro.controllers.PandoroController.SUCCESS_KEY
import com.tecknobit.pandoro.controllers.PandoroController.WRONG_PROCEDURE_MESSAGE
import com.tecknobit.pandoro.controllers.ProjectsController.*
import com.tecknobit.pandoro.controllers.UsersController.*
import com.tecknobit.pandoro.services.ProjectsHelper.*
import com.tecknobit.pandoro.services.UsersHelper.*
import org.json.JSONObject
import java.io.File
import java.nio.file.Files

class Requester(
    private val host: String,
    var userId: String? = null,
    var userToken: String? = null,
) {

    companion object {

        private val errorResponse = JSONObject()
            .put(SUCCESS_KEY, false)
            .put(STATUS_CODE_KEY, FAILED)
            .put(ERROR_KEY, WRONG_PROCEDURE_MESSAGE).toString()

    }

    private var lastResponse: JsonHelper? = null

    private val apiRequest: APIRequest = APIRequest()

    private val headers: Headers = Headers()

    init {
        setAuthHeaders()
    }

    private fun setAuthHeaders() {
        if (userId != null && userToken != null) {
            headers.addHeader(IDENTIFIER_KEY, userId)
            headers.addHeader(TOKEN_KEY, userToken)
        }
    }

    fun execSignUp(
        name: String,
        surname: String,
        email: String,
        password: String
    ): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(NAME_KEY, name)
        payload.addParam(SURNAME_KEY, surname)
        payload.addParam(EMAIL_KEY, email)
        payload.addParam(PASSWORD_KEY, password)
        val response = execPost(createUsersEndpoint(SIGN_UP_ENDPOINT), payload)
        setAuthCredentials(response)
        return response
    }

    fun execSignIn(
        email: String,
        password: String
    ): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(EMAIL_KEY, email)
        payload.addParam(PASSWORD_KEY, password)
        val response = execPost(createUsersEndpoint(SIGN_IN_ENDPOINT), payload)
        setAuthCredentials(response)
        return response
    }

    private fun setAuthCredentials(response: JSONObject) {
        val hResponse = JsonHelper(response)
        userId = hResponse.getString(IDENTIFIER_KEY)
        userToken = hResponse.getString(TOKEN_KEY)
        setAuthHeaders()
    }

    // TODO: CREATE THE CORRECT REQUEST
    fun execChangeProfilePic(profilePic: File): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(PROFILE_PIC_KEY, Files.readAllBytes(profilePic.toPath()))
        return execPatch(createUsersEndpoint(CHANGE_PROFILE_PIC_ENDPOINT, userId), payload, false)
    }

    fun execChangeEmail(newEmail: String): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(newEmail, "")
        return execPatch(createUsersEndpoint(CHANGE_EMAIL_ENDPOINT, userId), payload, false)
    }

    fun execChangePassword(newPassword: String): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(newPassword, "")
        return execPatch(createUsersEndpoint(CHANGE_PASSWORD_ENDPOINT, userId), payload, false)
    }

    fun execDeleteAccount(): JSONObject {
        return execDelete(createUsersEndpoint(DELETE_ACCOUNT_ENDPOINT, userId))
    }

    @Wrapper
    private fun createUsersEndpoint(
        endpoint: String,
        id: String? = null
    ): String {
        return createEndpoint(USERS_ENDPOINT, endpoint, id)
    }

    fun execProjectsList(): String {
        return execGet(createProjectsEndpoint(""))
    }

    fun execAddProject(
        name: String,
        projectDescription: String,
        projectShortDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): JSONObject {
        return JSONObject(
            execPost(
                createProjectsEndpoint(ADD_PROJECT_ENDPOINT),
                createProjectPayload(
                    name, projectDescription, projectShortDescription, projectVersion, groups,
                    projectRepository
                )
            )
        )
    }

    fun execEditProject(
        projectId: String,
        name: String,
        projectDescription: String,
        projectShortDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): JSONObject {
        return JSONObject(
            execPatch(
                createProjectsEndpoint("/${projectId}" + EDIT_PROJECT_ENDPOINT),
                createProjectPayload(
                    name, projectDescription, projectShortDescription, projectVersion, groups,
                    projectRepository
                )
            )
        )
    }

    private fun createProjectPayload(
        name: String,
        projectDescription: String,
        projectShortDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): PandoroPayload {
        val payload = PandoroPayload()
        payload.addParam(NAME_KEY, name)
        payload.addParam(PROJECT_DESCRIPTION_KEY, projectDescription)
        payload.addParam(PROJECT_SHORT_DESCRIPTION_KEY, projectShortDescription)
        payload.addParam(PROJECT_VERSION_KEY, projectVersion)
        payload.addParam(GROUPS_KEY, groups)
        payload.addParam(PROJECT_REPOSITORY_KEY, projectRepository)
        return payload
    }

    fun execGetSingleProject(projectId: String): JSONObject {
        return JSONObject(execGet(createProjectsEndpoint("", projectId)))
    }

    fun execDeleteProject(projectId: String): JSONObject {
        return JSONObject(execDelete(createProjectsEndpoint(DELETE_PROJECT_ENDPOINT, projectId)))
    }

    @Wrapper
    private fun createProjectsEndpoint(
        endpoint: String,
        id: String? = null
    ): String {
        return createEndpoint(PROJECTS_KEY, endpoint, id)
    }

    fun execScheduleUpdate(
        projectId: String,
        targetVersion: String,
        updateChangeNotes: List<String>
    ): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(UPDATE_TARGET_VERSION_KEY, targetVersion)
        payload.addParam(UPDATE_CHANGE_NOTES_KEY, updateChangeNotes)
        return JSONObject(execPost(createUpdatesEndpoint(SCHEDULE_UPDATE_ENDPOINT, projectId), payload))
    }

    fun execStartUpdate(
        projectId: String,
        updateId: String
    ): JSONObject {
        return JSONObject(execPatch(createUpdatesEndpoint(START_UPDATE_ENDPOINT, projectId, updateId, false)))
    }

    fun execPublishUpdate(
        projectId: String,
        updateId: String
    ): JSONObject {
        return JSONObject(execPatch(createUpdatesEndpoint(PUBLISH_UPDATE_ENDPOINT, projectId, updateId, false)))
    }

    fun execAddChangeNote(
        projectId: String,
        updateId: String,
        changeNote: String
    ): JSONObject {
        val payload = PandoroPayload()
        payload.addParam(changeNote, "")
        return JSONObject(
            execPut(
                createUpdatesEndpoint(ADD_CHANGE_NOTE_ENDPOINT, projectId, updateId, false),
                payload,
                false,
            )
        )
    }

    fun execMarkChangeNoteAsDone(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JSONObject {
        return JSONObject(
            execPatch(
                createUpdatesEndpoint(
                    "/$NOTES_KEY/$changeNoteId$MARK_CHANGE_NOTE_AS_DONE_ENDPOINT", projectId,
                    updateId, false
                ),
            )
        )
    }

    fun execMarkChangeNoteAsToDo(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JSONObject {
        return JSONObject(
            execPatch(
                createUpdatesEndpoint(
                    "/$NOTES_KEY/$changeNoteId$MARK_CHANGE_NOTE_AS_TODO_ENDPOINT", projectId,
                    updateId, false
                ),
            )
        )
    }

    fun execDeleteChangeNote(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JSONObject {
        return JSONObject(
            execDelete(
                createUpdatesEndpoint(
                    "/$NOTES_KEY/$changeNoteId$DELETE_CHANGE_NOTE_ENDPOINT", projectId,
                    updateId, false
                ),
            )
        )
    }

    fun execDeleteUpdate(
        projectId: String,
        updateId: String,
    ): JSONObject {
        return JSONObject(execDelete(createUpdatesEndpoint(DELETE_UPDATE_ENDPOINT, projectId, updateId, false)))
    }

    @Wrapper
    private fun createUpdatesEndpoint(
        endpoint: String,
        projectId: String,
        updateId: String? = null,
        insertSlash: Boolean = true
    ): String {
        return createProjectsEndpoint("", projectId) + UPDATES_PATH + createPathId(updateId, insertSlash) +
                endpoint
    }

    private fun createEndpoint(
        baseEndpoint: String,
        endpoint: String,
        id: String? = null
    ): String {
        return baseEndpoint + createPathId(id) + endpoint
    }

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

    fun successResponse(): Boolean {
        return lastResponse!!.jsonObjectSource == null || lastResponse!!.getBoolean(SUCCESS_KEY, true)
    }

    fun errorMessage(): String {
        return lastResponse!!.getString(ERROR_KEY)
    }

    @Wrapper
    private fun execGet(
        endpoint: String
    ): String {
        return execRequest(
            endpoint = endpoint,
            requestMethod = GET
        )
    }

    @Wrapper
    private fun execPost(
        endpoint: String,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true
    ): JSONObject {
        return JSONObject(
            execRequest(
                endpoint = endpoint,
                requestMethod = POST,
                payload = payload,
                jsonPayload = jsonPayload
            )
        )
    }

    @Wrapper
    private fun execPatch(
        endpoint: String,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true
    ): JSONObject {
        return JSONObject(
            execRequest(
                endpoint = endpoint,
                requestMethod = PATCH,
                payload = payload,
                jsonPayload = jsonPayload
            )
        )
    }

    @Wrapper
    private fun execPut(
        endpoint: String,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true
    ): JSONObject {
        return JSONObject(
            execRequest(
                endpoint = endpoint,
                requestMethod = PUT,
                payload = payload,
                jsonPayload = jsonPayload
            )
        )
    }

    @Wrapper
    private fun execDelete(
        endpoint: String,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true
    ): JSONObject {
        return JSONObject(
            execRequest(
                endpoint = endpoint,
                requestMethod = DELETE,
                payload = payload,
                jsonPayload = jsonPayload
            )
        )
    }

    private fun execRequest(
        contentType: String = "application/json",
        endpoint: String,
        requestMethod: RequestMethod,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true
    ): String {
        headers.addHeader("Content-Type", contentType)
        val response: String?
        return try {
            val requestUrl = host + BASE_ENDPOINT + endpoint
            // TODO: TO REMOVE
            println(requestUrl)
            if (payload != null) {
                if (jsonPayload)
                    apiRequest.sendJSONPayloadedAPIRequest(requestUrl, requestMethod, headers, payload)
                else
                    apiRequest.sendPayloadedAPIRequest(requestUrl, requestMethod, headers, payload)
            } else
                apiRequest.sendAPIRequest(requestUrl, requestMethod, headers)
            response = apiRequest.response
            lastResponse = JsonHelper(response)
            response
        } catch (e: Exception) {
            // TODO: TO REMOVE
            e.printStackTrace()
            apiRequest.printErrorResponse()


            lastResponse = JsonHelper(errorResponse)
            errorResponse
        }
    }

    private class PandoroPayload : Params() {

        override fun createPayload(): String {
            return super.createPayload().replace("=", "")
        }

    }

}