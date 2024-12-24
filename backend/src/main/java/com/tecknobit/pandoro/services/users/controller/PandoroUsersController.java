package com.tecknobit.pandoro.services.users.controller;

import com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController;
import com.tecknobit.equinoxbackend.environment.services.users.controller.EquinoxUsersController;
import com.tecknobit.equinoxcore.annotations.RequestPath;
import com.tecknobit.pandoro.services.users.entities.PandoroUser;
import com.tecknobit.pandoro.services.users.repository.PandoroUsersRepository;
import com.tecknobit.pandoro.services.users.service.PandoroUsersHelper;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.TOKEN_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.USERS_KEY;
import static com.tecknobit.equinoxcore.network.RequestMethod.GET;
import static com.tecknobit.equinoxcore.pagination.PaginatedResponse.*;
import static com.tecknobit.pandorocore.helpers.PandoroEndpoints.CANDIDATE_GROUP_MEMBERS_ENDPOINT;
import static com.tecknobit.pandorocore.helpers.PandoroEndpoints.COUNT_CANDIDATE_GROUP_MEMBERS_ENDPOINT;

/**
 * The {@code PandoroUsersController} class is useful to manage all the user operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxController
 * @since 1.0.5
 */
@RestController
public class PandoroUsersController extends EquinoxUsersController<PandoroUser, PandoroUsersRepository,
        PandoroUsersHelper> {

    /**
     * Method to count the candidate members availability
     *
     * @param id    The identifier of the user
     * @param token The token of the user
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            path = USERS_KEY + "/{" + IDENTIFIER_KEY + "}" + COUNT_CANDIDATE_GROUP_MEMBERS_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/candidatesCount", method = GET)
    public <T> T getCandidateMembers(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (!isMe(id, token))
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        else
            return (T) successResponse(usersHelper.countCandidateMembers());
    }

    /**
     * Method to get a list of candidates to be members of a group
     *
     * @param id       The identifier of the user
     * @param token    The token of the user
     * @param page     The page requested
     * @param pageSize The size of the items to insert in the page
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            path = USERS_KEY + "/{" + IDENTIFIER_KEY + "}" + CANDIDATE_GROUP_MEMBERS_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{id}/candidates", method = GET)
    public <T> T getCandidateMembers(
            @PathVariable(IDENTIFIER_KEY) String id,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(name = PAGE_KEY, defaultValue = DEFAULT_PAGE_HEADER_VALUE, required = false) int page,
            @RequestParam(name = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE_HEADER_VALUE, required = false) int pageSize
    ) {
        if (!isMe(id, token))
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        else
            return (T) successResponse(usersHelper.getCandidateMembers(id, page, pageSize));
    }

}
