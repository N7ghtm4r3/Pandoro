package com.tecknobit.pandoro.controllers;

import com.tecknobit.apimanager.annotations.RequestPath;
import com.tecknobit.pandoro.services.ChangelogsHelper;
import com.tecknobit.pandorocore.records.Changelog.ChangelogEvent;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*;
import static com.tecknobit.pandorocore.Endpoints.*;
import static com.tecknobit.pandorocore.helpers.Requester.WRONG_PROCEDURE_MESSAGE;
import static com.tecknobit.pandorocore.records.Changelog.CHANGELOGS_KEY;
import static com.tecknobit.pandorocore.records.Changelog.CHANGELOG_IDENTIFIER_KEY;
import static com.tecknobit.pandorocore.records.structures.PandoroItem.IDENTIFIER_KEY;
import static com.tecknobit.pandorocore.records.users.PublicUser.TOKEN_KEY;

/**
 * The {@code ChangelogsController} class is useful to manage all the changelog operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroController
 */
@RestController
@RequestMapping(path = BASE_ENDPOINT + CHANGELOGS_KEY)
public class ChangelogsController extends PandoroController {

    /**
     * {@code changelogsHelper} instance to manage the changelogs database operations
     */
    private final ChangelogsHelper changelogsHelper;

    /**
     * Constructor to init a {@link ChangelogsController} controller
     *
     * @param changelogsHelper: instance to manage the changelogs database operations
     */
    @Autowired
    public ChangelogsController(ChangelogsHelper changelogsHelper) {
        this.changelogsHelper = changelogsHelper;
    }

    /**
     * Method to get a changelogs list
     *
     * @param id:    the identifier of the user
     * @param token: the token of the user
     * @return the result of the request as {@link String} if fails or {@link JSONArray} if is successfully
     */
    @GetMapping(
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/changelogs", method = GET)
    public <T> T getChangelogs(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isAuthenticatedUser(id, token))
            return (T) changelogsHelper.getChangelogs(id);
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to read a changelog
     *
     * @param id:          the identifier of the user
     * @param token:       the token of the user
     * @param changelogId: the changelog identifier
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = "{" + CHANGELOG_IDENTIFIER_KEY + "}" + READ_CHANGELOG_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/changelogs/{changelog_id}/readChangelog", method = PATCH)
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

    /**
     * Method to delete a changelog
     *
     * @param id: the identifier of the user
     * @param token: the token of the user
     * @param changelogId: the changelog identifier
     * @param groupId: the group identifier where leave if is a {@link ChangelogEvent#INVITED_GROUP}
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "{" + CHANGELOG_IDENTIFIER_KEY + "}" + DELETE_CHANGELOG_ENDPOINT,
            headers = {
                    IDENTIFIER_KEY,
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/changelogs/{changelog_id}/deleteChangelog", method = DELETE)
    public String deleteChangelog(
            @RequestHeader(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(CHANGELOG_IDENTIFIER_KEY) String changelogId,
            @RequestBody(required = false) String groupId
    ) {
        if (isAuthenticatedUser(id, token) && changelogsHelper.changelogExists(changelogId)) {
            try {
                changelogsHelper.deleteChangelog(changelogId, id, groupId);
                return successResponse();
            } catch (IllegalAccessException e) {
                return failedResponse(WRONG_PROCEDURE_MESSAGE);
            }
        } else
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

}
