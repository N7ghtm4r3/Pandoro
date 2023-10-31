package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.GroupsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tecknobit.pandoro.controllers.GroupsController.GROUPS_ENDPOINT;
import static com.tecknobit.pandoro.controllers.PandoroController.BASE_ENDPOINT;

@RestController
@RequestMapping(path = BASE_ENDPOINT + GROUPS_ENDPOINT)
public class GroupsController extends PandoroController {

    public static final String GROUPS_ENDPOINT = "groups";

    private final GroupsHelper groupsHelper;

    @Autowired
    public GroupsController(GroupsHelper groupsHelper) {
        this.groupsHelper = groupsHelper;
    }

}
