package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.ChangelogsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.pandoro.controllers.ChangelogsController.CHANGELOGS_KEY;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;
import static com.tecknobit.pandoro.services.ChangelogsHelper.CHANGELOG_IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.TOKEN_KEY;

@RestController
@RequestMapping(path = BASE_ENDPOINT + CHANGELOGS_KEY)
public class ChangelogsController extends PandoroController {

    public static final String CHANGELOGS_KEY = "changelogs";

    public static final String READ_CHANGELOG_ENDPOINT = "/readChangelog";

    public static final String DELETE_CHANGELOG_ENDPOINT = "/deleteChangelog";

    private final ChangelogsHelper changelogsHelper;

    @Autowired
    public ChangelogsController(ChangelogsHelper changelogsHelper) {
        this.changelogsHelper = changelogsHelper;
    }

    @GetMapping(
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public <T> T getChangelogs(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isAuthenticatedUser(id, token))
            return (T) changelogsHelper.getChangelogs(id);
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    @PatchMapping(
            path = "{" + CHANGELOG_IDENTIFIER_KEY + "}" + READ_CHANGELOG_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    public String readChangelog(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(CHANGELOG_IDENTIFIER_KEY) String changelogId
    ) {
        if (isAuthenticatedUser(id, token) && changelogsHelper.changelogExists(changelogId)) {
            changelogsHelper.markAsRed(changelogId, id);
            return successResponse();
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
