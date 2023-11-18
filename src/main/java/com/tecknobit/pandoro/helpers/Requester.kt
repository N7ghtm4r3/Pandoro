package com.tecknobit.pandoro.helpers

import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.apimanager.apis.APIRequest
import com.tecknobit.apimanager.apis.APIRequest.*
import com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*
import com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.FAILED
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandoro.controllers.UsersController.*
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
            .put(ERROR_KEY, WRONG_PROCEDURE_MESSAGE)

    }

    private var lastResponse: JsonHelper? = null

    private val apiRequest: APIRequest = APIRequest()

    private val headers: Headers = Headers()

    init {
        setAuthHeaders()
        headers.addHeader("Content-Type", "application/json")
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

    private fun setAuthCredentials(response: JSONObject) {
        val hResponse = JsonHelper(response)
        userId = hResponse.getString(IDENTIFIER_KEY)
        userToken = hResponse.getString(TOKEN_KEY)
        setAuthHeaders()
    }

    private fun createUsersEndpoint(
        endpoint: String,
        id: String? = null
    ): String {
        val pId = if (id != null)
            "/$id"
        else
            ""
        return USERS_ENDPOINT + pId + endpoint
    }

    fun successResponse(): Boolean {
        return lastResponse!!.getBoolean(SUCCESS_KEY, true)
    }

    fun errorMessage(): String {
        return lastResponse!!.getString(ERROR_KEY)
    }

    @Wrapper
    private fun execGet(
        endpoint: String
    ): JSONObject {
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
        return execRequest(
            endpoint = endpoint,
            requestMethod = POST,
            payload = payload,
            jsonPayload = jsonPayload
        )
    }

    @Wrapper
    private fun execPatch(
        endpoint: String,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true
    ): JSONObject {
        return execRequest(
            endpoint = endpoint,
            requestMethod = PATCH,
            payload = payload,
            jsonPayload = jsonPayload
        )
    }

    @Wrapper
    private fun execDelete(
        endpoint: String,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true
    ): JSONObject {
        return execRequest(
            endpoint = endpoint,
            requestMethod = DELETE,
            payload = payload,
            jsonPayload = jsonPayload
        )
    }

    private fun execRequest(
        endpoint: String,
        requestMethod: RequestMethod,
        payload: PandoroPayload? = null,
        jsonPayload: Boolean = true
    ): JSONObject {
        val response: JSONObject?
        return try {
            val requestUrl = host + BASE_ENDPOINT + endpoint
            if (payload != null) {
                if (jsonPayload)
                    apiRequest.sendJSONPayloadedAPIRequest(requestUrl, requestMethod, headers, payload)
                else
                    apiRequest.sendPayloadedAPIRequest(requestUrl, requestMethod, headers, payload)
            } else
                apiRequest.sendAPIRequest(requestUrl, requestMethod, headers)
            response = apiRequest.getJSONResponse<JSONObject>()
            lastResponse = JsonHelper(response)
            response
        } catch (e: Exception) {
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