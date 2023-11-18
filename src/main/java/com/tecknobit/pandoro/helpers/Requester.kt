package com.tecknobit.pandoro.helpers

import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.apimanager.apis.APIRequest
import com.tecknobit.apimanager.apis.APIRequest.*
import com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandoro.controllers.PandoroController.*
import com.tecknobit.pandoro.controllers.UsersController.SIGN_UP_ENDPOINT
import com.tecknobit.pandoro.controllers.UsersController.USERS_ENDPOINT
import com.tecknobit.pandoro.services.UsersHelper.*
import org.json.JSONObject

class Requester(
    private val host: String,
    var userId: String? = null,
    var userToken: String? = null,
) {

    private var lastResponse: JsonHelper? = null

    private val apiRequest: APIRequest = APIRequest()

    private val headers: Headers = Headers()

    init {
        if (userId != null && userToken != null) {
            headers.addHeader(IDENTIFIER_KEY, userId)
            headers.addHeader(TOKEN_KEY, userToken)
        }
        headers.addHeader("Content-Type", "application/json")
    }

    fun execSignUp(
        name: String,
        surname: String,
        email: String,
        password: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(NAME_KEY, name)
        payload.addParam(SURNAME_KEY, surname)
        payload.addParam(EMAIL_KEY, email)
        payload.addParam(PASSWORD_KEY, password)
        val response = execPost(createUsersEndpoint(SIGN_UP_ENDPOINT), payload, true)
        val hResponse = JsonHelper(response)
        userId = hResponse.getString(IDENTIFIER_KEY)
        userToken = hResponse.getString(TOKEN_KEY)
        return response
    }

    private fun createUsersEndpoint(endpoint: String): String {
        return USERS_ENDPOINT + endpoint
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
        payload: Params? = null,
        jsonPayload: Boolean = false
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
        payload: Params? = null,
        jsonPayload: Boolean = false
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
        payload: Params? = null,
        jsonPayload: Boolean = false
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
        payload: Params? = null,
        jsonPayload: Boolean = false
    ): JSONObject {
        val requestUrl = host + BASE_ENDPOINT + endpoint
        if (payload != null) {
            if (jsonPayload)
                apiRequest.sendJSONPayloadedAPIRequest(requestUrl, requestMethod, headers, payload)
            else
                apiRequest.sendPayloadedAPIRequest(requestUrl, requestMethod, headers, payload)
        } else
            apiRequest.sendAPIRequest(requestUrl, requestMethod, headers)
        val response = apiRequest.getJSONResponse<JSONObject>()
        lastResponse = JsonHelper(response)
        return response
    }

}