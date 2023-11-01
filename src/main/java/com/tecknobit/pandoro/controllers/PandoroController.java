package com.tecknobit.pandoro.controllers;

import org.json.JSONObject;

import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.FAILED;
import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.SUCCESSFUL;

public abstract class PandoroController {

    public static final String BASE_ENDPOINT = "/api/v1/";

    public static final String SUCCESS_KEY = "success";

    public static final String DATA_KEY = "data";

    public static final String STATUS_CODE_KEY = "statusCode";

    public static final String ERROR_KEY = "error";

    public static final String IDENTIFIER_KEY = "id";

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

    protected String decrypt() {
        return "";
    }

    protected String encrypt() {
        return "";
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
