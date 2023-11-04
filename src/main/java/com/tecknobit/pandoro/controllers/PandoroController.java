package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.repositories.UsersRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.FAILED;
import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.SUCCESSFUL;

public abstract class PandoroController {

    public static final String BASE_ENDPOINT = "/api/v1/";

    public static final String SUCCESS_KEY = "success";

    public static final String DATA_KEY = "data";

    public static final String STATUS_CODE_KEY = "statusCode";

    public static final String ERROR_KEY = "error";

    public static final String IDENTIFIER_KEY = "id";

    public static final String AUTHOR_KEY = "author";

    public static final String WRONG_PROCEDURE_MESSAGE = "Wrong procedure";

    @Autowired
    protected UsersRepository usersRepository;

    /**
     * - SUCCESSFUL
     * {
     *   "success" : true,
     *   "data": {
     *       //values
     *   }
     * }
     * - FAILED
     * {
     *   "success" : false,
     *   "error" : "error_message"
     * }
     *
     *
     *
     * FOR DOCU README
     * - https://www.apachefriends.org/it/index.html
     *
     */

    protected String generateIdentifier() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    protected boolean isAuthenticatedUser(String userId, String token) {
        return usersRepository.getAuthorizedUser(userId, token) != null;
    }

    protected String successResponse() {
        return successResponse(null);
    }

    protected String successResponse(JSONObject data) {
        return assembleStatusResponse(true)
                .put(DATA_KEY, data)
                .toString();
    }

    protected String failedResponse(String error) {
        return assembleStatusResponse(false)
                .put(ERROR_KEY, error).toString();
    }

    private JSONObject assembleStatusResponse(boolean success) {
        int statusCode = SUCCESSFUL.getCode();
        if (!success)
            statusCode = FAILED.getCode();
        return new JSONObject()
                .put(SUCCESS_KEY, success)
                .put(STATUS_CODE_KEY, statusCode);
    }

}
