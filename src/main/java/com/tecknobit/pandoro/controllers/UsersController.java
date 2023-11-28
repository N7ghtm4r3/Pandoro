package com.tecknobit.pandoro.controllers;

import com.tecknobit.apimanager.annotations.RequestPath;
import com.tecknobit.pandoro.records.users.User;
import com.tecknobit.pandoro.services.UsersHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*;
import static com.tecknobit.pandoro.Launcher.protector;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.controllers.UsersController.USERS_ENDPOINT;
import static com.tecknobit.pandoro.helpers.InputsValidatorKt.*;
import static com.tecknobit.pandoro.helpers.ServerProtector.SERVER_SECRET_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.*;

/**
 * The {@code UsersController} class is useful to manage all the users operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroController
 */
@RestController
@RequestMapping(path = BASE_ENDPOINT + USERS_ENDPOINT)
public class UsersController extends PandoroController {

    /**
     * {@code USERS_ENDPOINT} base endpoint for the users
     */
    public static final String USERS_ENDPOINT = "users";

    /**
     * {@code SIGN_UP_ENDPOINT} endpoint to sign up in the <b>Pandoro's system</b>
     */
    public static final String SIGN_UP_ENDPOINT = "/signUp";

    /**
     * {@code SIGN_IN_ENDPOINT} endpoint to sign in the <b>Pandoro's system</b>
     */
    public static final String SIGN_IN_ENDPOINT = "/signIn";

    /**
     * {@code CHANGE_PROFILE_PIC_ENDPOINT} endpoint to change the profile pic of the user
     */
    public static final String CHANGE_PROFILE_PIC_ENDPOINT = "/changeProfilePic";

    /**
     * {@code CHANGE_EMAIL_ENDPOINT} endpoint to change the email of the user
     */
    public static final String CHANGE_EMAIL_ENDPOINT = "/changeEmail";

    /**
     * {@code CHANGE_PASSWORD_ENDPOINT} endpoint to change the password of the user
     */
    public static final String CHANGE_PASSWORD_ENDPOINT = "/changePassword";

    /**
     * {@code DELETE_ACCOUNT_ENDPOINT} endpoint to delete the account of the user
     */
    public static final String DELETE_ACCOUNT_ENDPOINT = "/deleteAccount";

    /**
     * {@code WRONG_EMAIL_MESSAGE} message to use when the email inserted is wrong
     */
    public static final String WRONG_EMAIL_MESSAGE = "Wrong email";

    /**
     * {@code WRONG_PASSWORD_MESSAGE} message to use when the password inserted is wrong
     */
    public static final String WRONG_PASSWORD_MESSAGE = "Wrong password";

    /**
     * {@code usersHelper} instance to manage the users database operations
     */
    private final UsersHelper usersHelper;

    /**
     * Constructor to init the {@link UsersController} controller
     *
     * @param usersHelper:{@code usersHelper} instance to manage the users database operations
     */
    @Autowired
    public UsersController(UsersHelper usersHelper) {
        this.usersHelper = usersHelper;
    }

    /**
     * Method to sign up in the <b>Pandoro's system</b>
     *
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "server_secret" : "the secret of the server" -> [String],
     *                  "name" : "the name of the user" -> [String],
     *                  "surname": "the surname of the user" -> [String],
     *                  "email": "the email of the user" -> [String],
     *                  "password": "the password of the user" -> [String]
     *              }
     *      }
     * </pre>
     *
     * @return the result of the request as {@link String}
     */
    @PostMapping(path = SIGN_UP_ENDPOINT)
    @RequestPath(path = "/api/v1/users/signUp", method = POST)
    public String signUp(@RequestBody Map<String, String> payload) {
        if (protector.serverSecretMatches(payload.get(SERVER_SECRET_KEY))) {
            String name = payload.get(NAME_KEY);
            String surname = payload.get(SURNAME_KEY);
            String email = payload.get(EMAIL_KEY);
            String password = payload.get(PASSWORD_KEY);
            if (isNameValid(name)) {
                if (isSurnameValid(surname)) {
                    if (isEmailValid(email)) {
                        if (isPasswordValid(password)) {
                            String userId = generateIdentifier();
                            String token = generateIdentifier();
                            try {
                                usersHelper.signUp(userId, token, name, surname, email, password);
                                return successResponse(new JSONObject()
                                        .put(IDENTIFIER_KEY, userId)
                                        .put(TOKEN_KEY, token)
                                        .put(PROFILE_PIC_KEY, DEFAULT_PROFILE_PIC)
                                );
                            } catch (Exception e) {
                                return failedResponse(WRONG_PROCEDURE_MESSAGE);
                            }
                        } else
                            return failedResponse(WRONG_PASSWORD_MESSAGE);
                    } else
                        return failedResponse(WRONG_EMAIL_MESSAGE);
                } else
                    return failedResponse("Wrong surname");
            } else
                return failedResponse("Wrong name");
        } else
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
    }

    /**
     * Method to sign in the <b>Pandoro's system</b>
     *
     * @param payload: payload of the request
     * <pre>
     *      {@code
     *              {
     *                  "email": "the email of the user", -> [String]
     *                  "password": "the password of the user" -> [String]
     *              }
     *      }
     * </pre>
     *
     * @return the result of the request as {@link String}
     */
    @PostMapping(path = SIGN_IN_ENDPOINT)
    @RequestPath(path = "/api/v1/users/signIn", method = POST)
    public String signIn(@RequestBody Map<String, String> payload) {
        String email = payload.get(EMAIL_KEY);
        String password = payload.get(PASSWORD_KEY);
        if (isEmailValid(email)) {
            if (isPasswordValid(password)) {
                try {
                    User user = usersHelper.signIn(email, password);
                    if (user != null) {
                        return successResponse(new JSONObject()
                                .put(IDENTIFIER_KEY, user.getId())
                                .put(TOKEN_KEY, user.getToken())
                                .put(NAME_KEY, user.getName())
                                .put(SURNAME_KEY, user.getSurname())
                                .put(PROFILE_PIC_KEY, user.getProfilePic())
                        );
                    } else
                        return failedResponse(WRONG_PROCEDURE_MESSAGE);
                } catch (NoSuchAlgorithmException e) {
                    return failedResponse(WRONG_PROCEDURE_MESSAGE);
                }
            } else
                return failedResponse(WRONG_PASSWORD_MESSAGE);
        } else
            return failedResponse(WRONG_EMAIL_MESSAGE);
    }

    /**
     * Method to change the profile pic of the user
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param profilePic: the profile pic chosen by the user to set as the new profile pic
     *
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            path = "{" + IDENTIFIER_KEY + "}" + CHANGE_PROFILE_PIC_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/changeProfilePic", method = POST)
    public String changeProfilePic(
            @PathVariable String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(PROFILE_PIC_KEY) MultipartFile profilePic
    ) {
        if (!profilePic.isEmpty()) {
            if (isAuthenticatedUser(id, token)) {
                String profilePicPath;
                try {
                    profilePicPath = usersHelper.changeProfilePic(id, token, profilePic);
                } catch (IOException e) {
                    profilePicPath = DEFAULT_PROFILE_PIC;
                }
                return successResponse(new JSONObject().put(PROFILE_PIC_KEY, profilePicPath));
            } else
                return failedResponse(WRONG_PROCEDURE_MESSAGE);
        } else
            return failedResponse("Wrong profile pic");
    }

    /**
     * Method to change the email of the user
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param newEmail: the new user email to set
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "{" + IDENTIFIER_KEY + "}" + CHANGE_EMAIL_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/changeEmail", method = PATCH)
    public String changeEmail(
            @PathVariable String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody String newEmail
    ) {
        if (isEmailValid(newEmail)) {
            if (isAuthenticatedUser(id, token)) {
                usersHelper.changeEmail(id, token, newEmail);
                return successResponse();
            } else
                return failedResponse(WRONG_PROCEDURE_MESSAGE);
        } else
            return failedResponse(WRONG_EMAIL_MESSAGE);
    }

    /**
     * Method to change the password of the user
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param newPassword: the new user password to set
     *
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "{" + IDENTIFIER_KEY + "}" + CHANGE_PASSWORD_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/changePassword", method = PATCH)
    public String changePassword(
            @PathVariable String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody String newPassword
    ) {
        if (isPasswordValid(newPassword)) {
            if (isAuthenticatedUser(id, token)) {
                try {
                    usersHelper.changePassword(id, token, newPassword);
                    return successResponse();
                } catch (NoSuchAlgorithmException e) {
                    return failedResponse(WRONG_PROCEDURE_MESSAGE);
                }
            } else
                return failedResponse(WRONG_PROCEDURE_MESSAGE);
        } else
            return failedResponse(WRONG_PASSWORD_MESSAGE);
    }

    /**
     * Method to delete the account of the user
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "{" + IDENTIFIER_KEY + "}" + DELETE_ACCOUNT_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/deleteAccount", method = DELETE)
    public String deleteAccount(
            @PathVariable String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isAuthenticatedUser(id, token)) {
            try {
                usersHelper.deleteAccount(id, token);
                return successResponse();
            } catch (IOException e) {
                return failedResponse(WRONG_PROCEDURE_MESSAGE);
            }
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
