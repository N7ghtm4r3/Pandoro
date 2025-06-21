package com.tecknobit.pandoro.services.overview.controller;

import com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController;
import com.tecknobit.pandoro.services.DefaultPandoroController;
import com.tecknobit.pandoro.services.overview.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.pandorocore.helpers.PandoroEndpoints.OVERVIEW_ENDPOINT;

/**
 * The {@code OverviewController} class is useful to manage all the overview operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxController
 * @see DefaultPandoroController
 */
@RestController
@RequestMapping(path = BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + OVERVIEW_ENDPOINT)
public class OverviewController extends DefaultPandoroController {

    /**
     * {@code overviewService} instance to manage the overview database operations
     */
    private final OverviewService overviewService;

    /**
     * Constructor used to init the controller
     *
     * @param overviewService The instance to manage the overview database operations
     */
    @Autowired
    public OverviewController(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    /**
     * Method to get the overview analysis for the requested user
     *
     * @param userId The identifier of the user
     * @param token  The token of the user
     * @return the result of the request as {@link String}
     */
    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    public <T> T getOverview(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token
    ) {
        if (!isMe(userId, token))
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        return (T) successResponse(overviewService.getOverview(userId));
    }

}
