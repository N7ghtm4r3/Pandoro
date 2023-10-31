package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.UsersHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.controllers.UsersController.USERS_ENDPOINT;

@RestController
@RequestMapping(path = BASE_ENDPOINT + USERS_ENDPOINT)
public class UsersController extends PandoroController {

    public static final String USERS_ENDPOINT = "users";

    public static final String PUBLIC_KEYS_ENDPOINT = "/publicKeys";

    public static final String SIGN_IN_ENDPOINT = "/signIn";

    public static final String SIGN_UP_ENDPOINT = "/signUp";

    public static final String PROFILE_DETAILS_ENDPOINT = "/profileDetails";

    public static final String CHANGE_PROFILE_PIC_ENDPOINT = "/changeProfilePic";

    public static final String CHANGE_EMAIL_ENDPOINT = "/changeEmail";

    public static final String CHANGE_PASSWORD_ENDPOINT = "/changePassword";

    public static final String LOGOUT_ENDPOINT = "/logout";

    public static final String DELETE_ACCOUNT_ENDPOINT = "/deleteAccount";

    public static final String PUBLIC_KEY = "publicKey";

    public static final String PRIVATE_KEY = "privateKey";

    public static final String SERVER_ADDRESS_KEY = "serverAddress";

    public static final String EMAIL_KEY = "email";

    public static final String PASSWORD_KEY = "password";

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
            path = SIGN_IN_ENDPOINT,
            params = {
                    EMAIL_KEY,
                    PASSWORD_KEY
            }
    )
    public String signIn(@RequestParam(EMAIL_KEY) String email, @RequestParam(PASSWORD_KEY) String password) {
        return "ture";
    }


}
