package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.UsersHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/users" // TODO: 29/10/2023 INSERT THE CORRECT PATH
)
public class UsersController {

    private final UsersHelper usersHelper;

    @Autowired
    public UsersController(UsersHelper usersHelper) {
        this.usersHelper = usersHelper;
    }


}
