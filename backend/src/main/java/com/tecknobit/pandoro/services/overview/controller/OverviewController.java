package com.tecknobit.pandoro.services.overview.controller;

import com.tecknobit.pandoro.services.DefaultPandoroController;
import com.tecknobit.pandoro.services.overview.service.OverviewHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.equinoxbackend.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.TOKEN_KEY;
import static com.tecknobit.equinoxbackend.environment.models.EquinoxUser.USERS_KEY;
import static com.tecknobit.pandorocore.helpers.PandoroEndpoints.OVERVIEW_ENDPOINT;

// TODO: 28/12/2024 TO DOCU
@RestController
@RequestMapping(path = BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + OVERVIEW_ENDPOINT)
public class OverviewController extends DefaultPandoroController {

    @Autowired
    private OverviewHelper overviewHelper;

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
        return (T) successResponse(overviewHelper.getOverview(userId));
    }

}
