package com.tecknobit.pandoro.services.changelogs.controller;

import com.tecknobit.equinoxcore.annotations.RequestPath;
import com.tecknobit.pandoro.services.DefaultPandoroController;
import com.tecknobit.pandoro.services.changelogs.service.ChangelogsHelper;
import com.tecknobit.pandorocore.enums.ChangelogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.tecknobit.equinoxbackend.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.TOKEN_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.USERS_KEY;
import static com.tecknobit.equinoxcore.network.RequestMethod.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;

/**
 * The {@code ChangelogsController} class is useful to manage all the changelog operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController
 * @see DefaultPandoroController
 */
@RestController
@RequestMapping(path = BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + CHANGELOGS_KEY)
public class ChangelogsController extends DefaultPandoroController {

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
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/changelogs", method = GET)
    public <T> T getChangelogs(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isMe(id, token))
            return (T) successResponse(changelogsHelper.getChangelogs(id));
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
            path = "{" + CHANGELOG_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/changelogs/{changelog_id}", method = PATCH)
    public String readChangelog(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(CHANGELOG_IDENTIFIER_KEY) String changelogId
    ) {
        if (isMe(id, token) && changelogsHelper.changelogExists(changelogId)) {
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
     * @param payload: the payload with group identifier where leave if is a {@link ChangelogEvent#INVITED_GROUP}
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = "{" + CHANGELOG_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/changelogs/{changelog_id}", method = DELETE)
    public String deleteChangelog(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(CHANGELOG_IDENTIFIER_KEY) String changelogId,
            @RequestBody(required = false) Map<String, String> payload
    ) {
        if (isMe(id, token) && changelogsHelper.changelogExists(changelogId)) {
            loadJsonHelper(payload);
            String groupId = jsonHelper.getString(GROUP_IDENTIFIER_KEY);
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
