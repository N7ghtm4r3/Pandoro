package com.tecknobit.pandoro.controllers;

import com.tecknobit.pandoro.services.GroupsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/groups" // TODO: 29/10/2023 INSERT THE CORRECT PATH
)
public class GroupsController {

    private final GroupsHelper groupsHelper;

    @Autowired
    public GroupsController(GroupsHelper groupsHelper) {
        this.groupsHelper = groupsHelper;
    }

}
