package com.tecknobit.pandoro.controllers;

import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.pandoro.services.repositories.UsersRepository;
import com.tecknobit.pandorocore.records.users.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.FAILED;
import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.SUCCESSFUL;
import static com.tecknobit.pandorocore.helpers.Requester.*;

/**
 * The {@code PandoroController} class is useful to give the base behavior of the <b>Pandoro's controllers</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Structure
public abstract class PandoroController {

    /**
     * {@code DATA_KEY} data key
     */
    public static final String DATA_KEY = "data";

    /**
     * {@code NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE} message to use when the request is by a not authorized user or
     * tried to fetch wrong details
     */
    public static final String NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE = "Not authorized or wrong details";

    /**
     * {@code CANNOT_EXECUTE_ACTION_ON_OWN_ACCOUNT_MESSAGE} message to use when the user tried to execute an action on its
     * account wrong
     */
    public static final String CANNOT_EXECUTE_ACTION_ON_OWN_ACCOUNT_MESSAGE = "You cannot execute this action on your account";

    /**
     * {@code usersRepository} instance for the user repository
     */
    @Autowired
    protected UsersRepository usersRepository;

    /**
     * Method to create an identifier for an item <br>
     * No any-params required
     *
     * @return a new identifier as {@link String}
     */
    public static String generateIdentifier() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * Method to check whether the user is an authenticated user or not
     *
     * @param userId: the identifier of the user
     * @param token:  the token of the user
     * @return whether the user is an authenticated user or not as boolean
     */
    protected boolean isAuthenticatedUser(String userId, String token) {
        return getMe(userId, token) != null;
    }

    /**
     * Method to get the user by its details
     *
     * @param userId: the identifier of the user
     * @param token: the token of the user
     *
     * @return user as {@link User}
     */
    protected User getMe(String userId, String token) {
        return usersRepository.getAuthorizedUser(userId, token);
    }

    /**
     * Method to send a success response <br>
     * No any-params required
     *
     * @return the success response as {@link String}
     */
    protected String successResponse() {
        return successResponse(null);
    }

    /**
     * Method to send a success response
     * @param data: custom data to send with the success response
     *
     * @return the success response as {@link String}
     */
    protected String successResponse(JSONObject data) {
        return assembleStatusResponse(true)
                .put(DATA_KEY, data)
                .toString();
    }

    /**
     * Method to send a failed response
     * @param error: the error message to send as response
     *
     * @return the failed response as {@link String}
     */
    protected String failedResponse(String error) {
        return assembleStatusResponse(false)
                .put(ERROR_KEY, error).toString();
    }

    /**
     * Method to assemble a response
     * @param success: whether the response has been successful or not
     *
     * @return the response as {@link JSONObject}
     */
    private JSONObject assembleStatusResponse(boolean success) {
        int statusCode = SUCCESSFUL.getCode();
        if (!success)
            statusCode = FAILED.getCode();
        return new JSONObject()
                .put(SUCCESS_KEY, success)
                .put(STATUS_CODE_KEY, statusCode);
    }

}
