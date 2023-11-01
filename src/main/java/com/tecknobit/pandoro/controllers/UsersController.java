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

    public static final String PUBLIC_KEYS_ENDPOINT = "/publicKeys";

    public static final String SIGN_UP_ENDPOINT = "/signUp";

    public static final String SIGN_IN_ENDPOINT = "/signIn";

    public static final String PROFILE_DETAILS_ENDPOINT = "/profileDetails";

    public static final String CHANGE_PROFILE_PIC_ENDPOINT = "/changeProfilePic";

    public static final String CHANGE_EMAIL_ENDPOINT = "/changeEmail";

    public static final String CHANGE_PASSWORD_ENDPOINT = "/changePassword";

    public static final String DELETE_ACCOUNT_ENDPOINT = "/deleteAccount";

    public static final String PUBLIC_KEY = "publicKey";

    public static final String PRIVATE_KEY = "privateKey";

    public static final String WRONG_EMAIL_MESSAGE = "Wrong email";

    public static final String WRONG_PASSWORD_MESSAGE = "Wrong password";

    public static final String WRONG_PROCEDURE_MESSAGE = "Wrong procedure";

    private final UsersHelper usersHelper;

    @Autowired
    public UsersController(UsersHelper usersHelper) {
        this.usersHelper = usersHelper;
    }

    @GetMapping(PUBLIC_KEYS_ENDPOINT)
    public String getPublicKeys() {
        // TODO: 31/10/2023 PASS THE REAL KEYS 
        return new JSONObject()
                .put(PUBLIC_KEY, "publicKey")
                .put(PRIVATE_KEY, "privateKey").toString();
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
            params = {
                    TOKEN_KEY
            }
    )
    public String getProfileDetails(
            @PathVariable String userId,
            @RequestParam(TOKEN_KEY) String token
    ) {
        User user = usersHelper.getProfileDetails(userId);
        return successResponse(new JSONObject()
                .put(PROFILE_PIC_KEY, user.getProfilePic())
                .put(NAME_KEY, user.getName())
                .put(SURNAME_KEY, user.getSurname())
                .put(EMAIL_KEY, user.getEmail())
                .put(PASSWORD_KEY, user.getPassword())
                .put(CHANGELOGS_KEY, user.getChangelogs())
                .put(GROUPS_KEY, user.getGroups())
        );
    }

    // TODO: 31/10/2023 CHANGE PROFILE PIC

    @PatchMapping(
            path = "{" + IDENTIFIER_KEY + "}" + CHANGE_EMAIL_ENDPOINT,
            params = {
                    TOKEN_KEY,
                    EMAIL_KEY
            }
    )
    public String changeEmail(
            @PathVariable String userId,
            @RequestParam(TOKEN_KEY) String token,
            @RequestParam(EMAIL_KEY) String newEmail
    ) {
        if (isEmailValid(newEmail)) {
            usersHelper.changeEmail(userId, newEmail);
            return successResponse();
        } else
            return failedResponse(WRONG_EMAIL_MESSAGE);
    }

    @PatchMapping(
            path = "{" + IDENTIFIER_KEY + "}" + CHANGE_PASSWORD_ENDPOINT,
            params = {
                    TOKEN_KEY,
                    PASSWORD_KEY
            }
    )
    public String changePassword(
            @PathVariable String userId,
            @RequestParam(TOKEN_KEY) String token,
            @RequestParam(PASSWORD_KEY) String newPassword
    ) {
        if (isPasswordValid(newPassword)) {
            usersHelper.changePassword(userId, newPassword);
            return successResponse();
        } else
            return failedResponse(WRONG_PASSWORD_MESSAGE);
    }

    @DeleteMapping(
            path = "{" + IDENTIFIER_KEY + "}" + DELETE_ACCOUNT_ENDPOINT,
            params = {
                    TOKEN_KEY
            }
    )
    public String deleteAccount(
            @PathVariable String userId,
            @RequestParam(TOKEN_KEY) String token
    ) {
        usersHelper.delete(userId);
        return successResponse();
    }

}
