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
import static com.tecknobit.equinoxcore.pagination.PaginatedResponse.*;
import static com.tecknobit.pandorocore.ConstantsKt.*;
import static com.tecknobit.pandorocore.helpers.PandoroEndpoints.UNREAD_CHANGELOGS_ENDPOINT;

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
    @Autowired
    private ChangelogsHelper changelogsHelper;

    /**
     * Method to get an unread changelogs list
     *
     * @param id    The identifier of the user
     * @param token The token of the user
     * @return the result of the request as {@link T}
     */
    @GetMapping(
            path = UNREAD_CHANGELOGS_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/changelogs/unread", method = GET)
    public <T> T getUnreadChangelogsCount(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (isMe(id, token))
            return (T) successResponse(changelogsHelper.getUnreadChangelogsCount(id));
        else
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
    }

    /**
     * Method to get a changelogs list
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @return the result of the request as {@link T}
     */
    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/changelogs", method = GET)
    public <T> T getChangelogs(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(name = PAGE_KEY, defaultValue = DEFAULT_PAGE_HEADER_VALUE, required = false) int page,
            @RequestParam(name = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE_HEADER_VALUE, required = false) int pageSize
    ) {
        if (isMe(id, token))
            return (T) successResponse(changelogsHelper.getChangelogs(id, page, pageSize));
        else
            return (T) failedResponse(WRONG_PROCEDURE_MESSAGE);
    }

    /**
     * Method to read a changelog
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param changelogId The changelog identifier
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
        if (!isMe(id, token) || !changelogsHelper.changelogExists(changelogId))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        changelogsHelper.markAsRead(changelogId, id);
        return successResponse();
    }

    /**
     * Method to delete a changelog
     *
     * @param id The identifier of the user
     * @param token The token of the user
     * @param changelogId The changelog identifier
     * @param payload The payload with group identifier where leave if is a {@link ChangelogEvent#INVITED_GROUP}
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
        if (!isMe(id, token) || !changelogsHelper.changelogExists(changelogId))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        loadJsonHelper(payload);
        String groupId = jsonHelper.getString(GROUP_IDENTIFIER_KEY);
        try {
            changelogsHelper.deleteChangelog(changelogId, id, groupId);
            return successResponse();
        } catch (IllegalAccessException e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
    }

}
