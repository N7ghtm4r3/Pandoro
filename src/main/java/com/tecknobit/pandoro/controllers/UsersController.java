package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.records.User;
import com.tecknobit.pandoro.services.UsersHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.controllers.UsersController.USERS_ENDPOINT;
import static com.tecknobit.pandoro.services.UsersHelper.*;
import static helpers.InputsValidatorKt.*;

@RestController
@RequestMapping(path = BASE_ENDPOINT + USERS_ENDPOINT)
public class UsersController extends PandoroController {

    public static final String USERS_ENDPOINT = "users";

    public static final String SIGN_UP_ENDPOINT = "/signUp";

    public static final String SIGN_IN_ENDPOINT = "/signIn";

    public static final String PROFILE_DETAILS_ENDPOINT = "/profileDetails";

    public static final String CHANGE_PROFILE_PIC_ENDPOINT = "/changeProfilePic";

    public static final String CHANGE_EMAIL_ENDPOINT = "/changeEmail";

    public static final String CHANGE_PASSWORD_ENDPOINT = "/changePassword";

    public static final String DELETE_ACCOUNT_ENDPOINT = "/deleteAccount";

    public static final String WRONG_EMAIL_MESSAGE = "Wrong email";

    public static final String WRONG_PASSWORD_MESSAGE = "Wrong password";

    private final UsersHelper usersHelper;

    @Autowired
    public UsersController(UsersHelper usersHelper) {
        this.usersHelper = usersHelper;
    }

    @PostMapping(
            path = SIGN_UP_ENDPOINT,
            params = {
                    NAME_KEY,
                    SURNAME_KEY,
                    EMAIL_KEY,
                    PASSWORD_KEY
            }
    )
    public String signUp(
            @RequestParam(NAME_KEY) String name,
            @RequestParam(SURNAME_KEY) String surname,
            @RequestParam(EMAIL_KEY) String email,
            @RequestParam(PASSWORD_KEY) String password
    ) {
        if (isNameValid(name)) {
            if (isSurnameValid(surname)) {
                if (isEmailValid(email)) {
                    if (isPasswordValid(password)) {
                        String userId = generateIdentifier();
                        String token = generateIdentifier();
                        usersHelper.signUp(userId, token, name, surname, email, password);
                        return successResponse(new JSONObject()
                                .put(IDENTIFIER_KEY, userId)
                                .put(TOKEN_KEY, token)
                                .put(PROFILE_PIC_KEY, DEFAULT_PROFILE_PIC)
                        );
                    } else
                        return failedResponse(WRONG_PASSWORD_MESSAGE);
                } else
                    return failedResponse(WRONG_EMAIL_MESSAGE);
            } else
                return failedResponse("Wrong surname");
        } else
            return failedResponse("Wrong name");
    }

    @PostMapping(
            path = SIGN_IN_ENDPOINT,
            params = {
                    EMAIL_KEY,
                    PASSWORD_KEY
            }
    )
    public String signIn(
            @RequestParam(EMAIL_KEY) String email,
            @RequestParam(PASSWORD_KEY) String password
    ) {
        if (isEmailValid(email)) {
            if (isPasswordValid(password)) {
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
            } else
                return failedResponse(WRONG_PASSWORD_MESSAGE);
        } else
            return failedResponse(WRONG_EMAIL_MESSAGE);
    }

    @GetMapping(
            path = "{" + IDENTIFIER_KEY + "}" + PROFILE_DETAILS_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    public String getProfileDetails(
            @PathVariable String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        User user = usersHelper.getProfileDetails(id, token);
        if (user != null) {
            return successResponse(new JSONObject()
                    .put(PROFILE_PIC_KEY, user.getProfilePic())
                    .put(NAME_KEY, user.getName())
                    .put(SURNAME_KEY, user.getSurname())
                    .put(EMAIL_KEY, user.getEmail())
                    .put(PASSWORD_KEY, user.getPassword())
                    .put(CHANGELOGS_KEY, user.getChangelogs())
                    .put(GROUPS_KEY, user.getGroups())
            );
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    // TODO: 01/11/2023 PASS CORRECT PROFILE PIC 
    @PatchMapping(
            path = "{" + IDENTIFIER_KEY + "}" + CHANGE_PROFILE_PIC_ENDPOINT,
            headers = {
                    TOKEN_KEY
            },
            params = {
                    PROFILE_PIC_KEY
            }
    )
    public String changeProfilePic(
            @PathVariable String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(PROFILE_PIC_KEY) String profilePic
    ) {
        if (!profilePic.isEmpty()) {
            if (isAuthenticatedUser(id, token)) {
                usersHelper.changeProfilePic(id, token, profilePic);
                return successResponse();
            } else
                return failedResponse(WRONG_PROCEDURE_MESSAGE);
        } else
            return failedResponse("Wrong profile pic");
    }

    @PatchMapping(
            path = "{" + IDENTIFIER_KEY + "}" + CHANGE_EMAIL_ENDPOINT,
            headers = {
                    TOKEN_KEY
            },
            params = {
                    EMAIL_KEY
            }
    )
    public String changeEmail(
            @PathVariable String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(EMAIL_KEY) String newEmail
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

    @PatchMapping(
            path = "{" + IDENTIFIER_KEY + "}" + CHANGE_PASSWORD_ENDPOINT,
            headers = {
                    TOKEN_KEY
            },
            params = {
                    PASSWORD_KEY
            }
    )
    public String changePassword(
            @PathVariable String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(PASSWORD_KEY) String newPassword
    ) {
        if (isPasswordValid(newPassword)) {
            if (isAuthenticatedUser(id, token)) {
                usersHelper.changePassword(id, token, newPassword);
                return successResponse();
            } else
                return failedResponse(WRONG_PROCEDURE_MESSAGE);
        } else
            return failedResponse(WRONG_PASSWORD_MESSAGE);
    }

    @DeleteMapping(
            path = "{" + IDENTIFIER_KEY + "}" + DELETE_ACCOUNT_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    public String deleteAccount(
            @PathVariable String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isAuthenticatedUser(id, token)) {
            usersHelper.delete(id, token);
            return successResponse();
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
