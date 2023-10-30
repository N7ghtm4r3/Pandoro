package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.ChangelogsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/changelogs" // TODO: 29/10/2023 INSERT THE CORRECT PATH
)
public class ChangelogsController {

    private final ChangelogsHelper changelogsHelper;

    @Autowired
    public ChangelogsController(ChangelogsHelper changelogsHelper) {
        this.changelogsHelper = changelogsHelper;
    }

}
