package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.ChangelogsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tecknobit.pandoro.controllers.ChangelogsController.CHANGELOGS_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;

@RestController
@RequestMapping(path = BASE_ENDPOINT + CHANGELOGS_KEY)
public class ChangelogsController extends PandoroController {

    public static final String CHANGELOGS_KEY = "changelogs";

    public static final String DELETE_CHANGELOG_ENDPOINT = "/deleteChangelog";

    public static final String READ_CHANGELOG_ENDPOINT = "/redChangelog";

    private final ChangelogsHelper changelogsHelper;

    @Autowired
    public ChangelogsController(ChangelogsHelper changelogsHelper) {
        this.changelogsHelper = changelogsHelper;
    }

}
